package com.esd.model.service;

import com.esd.model.dao.AppointmentDao;
import com.esd.model.dao.UserDetailsDao;
import com.esd.model.dao.WorkingHoursDao;
import com.esd.model.data.AppointmentPlaceHolder;
import com.esd.model.data.AppointmentStatus;
import com.esd.model.data.UserGroup;
import com.esd.model.data.WorkingHours;
import com.esd.model.data.persisted.Appointment;
import com.esd.model.data.persisted.User;
import com.esd.model.data.persisted.UserDetails;
import com.esd.model.exceptions.InvalidIdValueException;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.sql.SQLException;
import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Original Author: Trent Meier
 * Use: This class is a singleton, used to access appointments data objects
 */
public class AppointmentsService {

    private static AppointmentsService instance;
    private AppointmentDao appointmentDao = AppointmentDao.getInstance();
    private WorkingHoursDao workingHoursDao = WorkingHoursDao.getInstance();

    private AppointmentsService(AppointmentDao appointmentDao){
        if(appointmentDao == null){
            throw new IllegalArgumentException("appointment dao must not be null");
        }
        this.appointmentDao = appointmentDao;
    }

    public List<AppointmentPlaceHolder> generateAllPossibleAppointmentsOfDefaultLength(LocalDate date){
        return generateAllPossibleAppointments(date, 1);
    }

    public List<AppointmentPlaceHolder> generateAllPossibleAppointments(LocalDate date, int requestedSlotLength){
        try {
            List<Integer> employeeIds = UserDetailsDao.getInstance().getAllUsersOfGroups(UserGroup.DOCTOR, UserGroup.NURSE)
                    .stream()
                    .map(UserDetails::getId)
                    .collect(Collectors.toList());

            List<AppointmentPlaceHolder> possibleAppointments = new ArrayList<>();
            for(Integer id : employeeIds){
                possibleAppointments.addAll(generatePossibleAppointmentsForEmployee(id, requestedSlotLength, date));
            }
            return possibleAppointments;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new ArrayList<>();
    }

    public boolean bookAppointment(AppointmentPlaceHolder placeholder, int patientId){
        if(!validateAppointmentSlotForEmployee(placeholder)){
            return false;
        }
        Appointment appointment = new Appointment();
        appointment.setEmployeeId(placeholder.getEmployeeId());
        appointment.setPatientId(patientId);
        appointment.setAppointmentDate(placeholder.getAppointmentDate());
        appointment.setAppointmentTime(placeholder.getAppointmentTime());
        appointment.setSlots(placeholder.getSlots());
        appointment.setStatus(AppointmentStatus.PENDING);
        try {
            appointmentDao.createAppointment(appointment);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<AppointmentPlaceHolder> generatePossibleAppointmentsForEmployeeOfDefaultLength(int employeeDetailsId, LocalDate date) {
        return generatePossibleAppointmentsForEmployee(employeeDetailsId, 1, date);
    }

    public boolean validateAppointmentSlotForEmployee(AppointmentPlaceHolder placeHolder){
        try {
            List<Appointment> bookedAppointments = this.getAppointmentsInRange(placeHolder.getAppointmentDate(), placeHolder.getAppointmentDate(), Optional.empty());
            int slotTime = SystemSettingService.getInstance().getIntegerSettingValueByKey(SystemSettingService.SYSTEMSETTING_SLOT_TIME);
            if(slotTime == 0){
                return false;
            }

            //loop all booked appointments and slots removing any overlaps
            for(Appointment a : bookedAppointments){
                if(a.isCancled()){
                    continue;
                }

                LocalTime appointmentTime = a.getAppointmentTime();
                LocalTime appointmentEndTime = a.getAppointmentTime().plusMinutes(slotTime * a.getSlots());
                LocalTime slotStartTime = placeHolder.getAppointmentTime();
                LocalTime slotEndTime = placeHolder.getAppointmentTime().plusMinutes(slotTime * placeHolder.getSlots());

                //if the slots were periods the following boolean value would look like so:
                // a.start < a.end && a.start < a.end || p = s
                boolean overlap = (appointmentTime.isBefore(slotEndTime)) && (slotStartTime.isBefore(appointmentEndTime))
                        || (appointmentTime.isEqual(slotStartTime) && appointmentEndTime.isEqual(slotEndTime));

                if(overlap){
                    return false;
                }

            }
        } catch (SQLException | InvalidIdValueException e) {
            e.printStackTrace();
        }
       return false;
    }

    public List<AppointmentPlaceHolder> generatePossibleAppointmentsForEmployee(int employeeDetailsId, int requestedSlotLength, LocalDate date){
        try {
            List<WorkingHours> workingTimes = workingHoursDao.getWorkingHoursForEmployee(employeeDetailsId);

            int day = date.getDayOfWeek();

            //flatten all workingTime days into one list then filter by the current day
            List<WorkingHours> applicableWorkingHours = workingTimes.stream()
                    .filter(x -> x.getWorkingDays().contains(day))
                    .collect(Collectors.toList());

            if(applicableWorkingHours.size() == 0){
                return new ArrayList<>();
            }

            int slotTime = SystemSettingService.getInstance().getIntegerSettingValueByKey(SystemSettingService.SYSTEMSETTING_SLOT_TIME);
            if(slotTime == 0){
                return new ArrayList<>();
            }

            List<LocalTime> allSlots = generateAllPossibleSlots(applicableWorkingHours, slotTime, requestedSlotLength);
            List<Appointment> bookedAppointments = this.getAppointmentsInRange(date, date, Optional.empty());

            //loop all booked appointments and slots removing any overlaps
            for(Appointment a : bookedAppointments){
                if(a.isCancled()){
                    continue;
                }
                for (int i = allSlots.size() - 1; i >= 0; i--) {
                    LocalTime appointmentTime = a.getAppointmentTime();
                    LocalTime appointmentEndTime = a.getAppointmentTime().plusMinutes(slotTime * a.getSlots());
                    LocalTime slotStartTime = allSlots.get(i);
                    LocalTime slotEndTime = allSlots.get(i).plusMinutes(slotTime * requestedSlotLength);

                    //if the slots were periods the following boolean value would look like so:
                    // a.start < a.end && a.start < a.end || p = s
                    boolean overlap = (appointmentTime.isBefore(slotEndTime)) && (slotStartTime.isBefore(appointmentEndTime))
                            || (appointmentTime.isEqual(slotStartTime) && appointmentEndTime.isEqual(slotEndTime));

                    if(overlap){
                        allSlots.remove(i);
                    }
                }
            }

            //turn the slot times into Appointment placeholders and then return
            List<AppointmentPlaceHolder> appointmentPlaceHolders = new ArrayList<>();
            allSlots.forEach(slotTime2 -> {
                appointmentPlaceHolders.add(new AppointmentPlaceHolder(employeeDetailsId, requestedSlotLength, date, slotTime2));
            });

            //return the final slot list
            return appointmentPlaceHolders;
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<LocalTime> generateAllPossibleSlots(List<WorkingHours> applicableWorkingHours, int slotTime, int requestedSlotLength) {
        List<LocalTime> allSlots = new ArrayList<>();
        for(WorkingHours currentWorkingTime : applicableWorkingHours){
            LocalTime currentTime = new LocalTime(currentWorkingTime.getStartTime());

            while(currentTime.isBefore(currentWorkingTime.getEndTime())){
                allSlots.add(new LocalTime(currentTime));
                currentTime = currentTime.plusMinutes(slotTime * requestedSlotLength);
            }
        }
        return allSlots;
    }

    private boolean checkIfAptConflicts(Appointment appointment) throws SQLException {
        //check if there are conflicting appointments
        List<Appointment> conflictingApts = appointmentDao.getAppointmentsInPeriodWithStatus(
                appointment.getAppointmentDate(),
                appointment.getAppointmentDate(),
                Optional.empty());
        if(conflictingApts.size() > 0){
            return false;
        }
        return true;
    }

    private boolean checkIfAptIsWorkingDay(Appointment appointment) {
        //joda time day of week 6 or 7 is weekend (non working day)
        int dayOfWeek = appointment.getAppointmentDate().getDayOfWeek();
        if(dayOfWeek == 6 || dayOfWeek == 7){
            return false;
        }
        return true;
    }

    public synchronized static AppointmentsService getInstance(){
        if(instance == null){
            instance = new AppointmentsService(AppointmentDao.getInstance());
        }
        return instance;
    }

    public Appointment getAppointmentById(int AppointmentId) throws SQLException {
        return appointmentDao.getAppointmentById(AppointmentId);
    }

    public List<Appointment> getAppointmentsInRange(LocalDate fromDate, LocalDate toDate, Optional<Map<String, Object>> args) throws SQLException {
        return appointmentDao.getAppointmentsInPeriodWithArgs(fromDate, toDate, args);
    }

    public void createNewAppointment(Appointment appointment) throws SQLException, InvalidIdValueException {
        if(!checkIfAptConflicts(appointment)){
            throw new InvalidIdValueException("Appointment conflicts with existing appointment");
        }
        if(!checkIfAptIsWorkingDay(appointment)){
            throw new InvalidIdValueException("Appointment cannot be for a non-working day");
        }
        appointmentDao.createAppointment(appointment);
    }

    public void updateAppointment(Appointment appointment) throws SQLException, InvalidIdValueException {
        if(!checkIfAptConflicts(appointment)){
            throw new InvalidIdValueException("Appointment conflicts with existing appointment");
        }
        if(!checkIfAptIsWorkingDay(appointment)){
            throw new InvalidIdValueException("Appointment cannot be for a non-working day");
        }
        appointmentDao.updateAppointment(appointment);
    }
}
