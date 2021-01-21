package com.esd.model.dao;

import com.esd.model.dao.queryBuilders.SelectQueryBuilder;
import com.esd.model.dao.queryBuilders.joins.Joins;
import com.esd.model.dao.queryBuilders.restrictions.Restriction;
import com.esd.model.dao.queryBuilders.restrictions.Restrictions;
import com.esd.model.data.UserGroup;
import com.esd.model.data.WorkingHours;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.net.Inet4Address;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkingHoursDao {

    private static WorkingHoursDao instance;

    private static final String INSERT_WORKING_HOURS = "INSERT INTO WORKINGHOURS (WORKINGDAYS, STARTTIME, ENDTIME) VALUES (?, ?, ?)";
    private static final String DELETE_EMPLOYEE_WORKING_HOURS = "DELETE FROM WORKINGHOURSJT WHERE employeeID=?";
    private static final String GET_ASSIGNED_WORKING_HOURS = "SELECT * FROM WORKINGHOURSJT WHERE employeeID=?";
    private static final String INSERT_WORKING_HOUR_JT = "INSERT INTO WORKINGHOURSJT (EMPLOYEEID, WORKINGHOURSID) VALUES (?, ?)";

    private WorkingHoursDao() {
    }

    public synchronized static WorkingHoursDao getInstance() {
        if (instance == null) {
            instance = new WorkingHoursDao();
        }
        return instance;
    }

    String daysToString(List<Integer> daysList){
        StringBuilder days = new StringBuilder();
        for (Integer day:daysList){
            days.append(day);
            days.append(",");
        }

        days.setLength(days.length() - 1);
        return days.toString();
    }

    String formatTime(LocalTime time){
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm:ss");
        return time.toString(timeFormatter);
    }

    public List<WorkingHours> getWorkingHoursForEmployee(int employeeDetailsId) {
        try {
            PreparedStatement statement = new SelectQueryBuilder(DaoConsts.TABLE_WORKINGHOURS)
                    //join workinghoursJt on wokringhoursid = workinghours.id
                    .withJoin(Joins.innerJoin(DaoConsts.TABLE_WORKINGHOURS_JT,
                            DaoConsts.WORKINGHOURS_ID_FK,
                            DaoConsts.TABLE_WORKINGHOURS_REFERENCE + DaoConsts.ID))
                    .withRestriction(Restrictions.equalsRestriction(DaoConsts.EMPLOYEE_ID_FK, employeeDetailsId))
                    .createStatement();
            return extractWorkingHoursFromResultSet(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private List<WorkingHours> extractWorkingHoursFromResultSet(PreparedStatement statement) throws SQLException {
        List<WorkingHours> returnValue = new ArrayList<>();
        ResultSet result = statement.executeQuery();
        while(result.next()){
            returnValue.add(getWorkingHoursFromResults(result));
        }
        return returnValue;
    }

    public WorkingHours getWorkingHoursFromResults(ResultSet resultSet) throws SQLException {
        WorkingHours workingHours = new WorkingHours();
        workingHours.setId(resultSet.getInt(DaoConsts.ID));
        workingHours.setEmployeeDetailsId(resultSet.getInt(DaoConsts.EMPLOYEE_ID_FK));
        workingHours.setStartTime(LocalTime.parse(resultSet.getString(DaoConsts.WORKINGHOURS_STARTTIME)));
        workingHours.setEndTime(LocalTime.parse(resultSet.getString(DaoConsts.WORKINGHOURS_ENDTIME)));

        String workingDaysString = resultSet.getString(DaoConsts.WORKINGHOURS_WORKINGDAYS);
        String[] workingDays = workingDaysString.split(",");
        Integer[] workingDaysCasted = new Integer[workingDays.length];
        for (int i = 0; i < workingDays.length; i++) {
            workingDaysCasted[i] = Integer.parseInt(workingDays[i]);
        }
        workingHours.setWorkingDays(Arrays.asList(workingDaysCasted));

        return workingHours;
    }

    public int findWorkingHours(WorkingHours workingHours) throws SQLException {

        PreparedStatement statement = workingHoursToSelectStatement(workingHours);

        // List<WorkingHours> results = extractWorkingHoursFromResultSet(statement);
        ResultSet result = statement.executeQuery();

        if (result.next())
            return result.getInt(1);
        else
            return -1;
    }



    public PreparedStatement workingHoursToSelectStatement(WorkingHours workingHours) throws SQLException {
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("HH:mm:ss");

        StringBuilder days = new StringBuilder();
        for (Integer day:workingHours.getWorkingDays()){
            days.append(day);
            days.append(",");
        }

        days.setLength(days.length() - 1);

        String string1 = workingHours.getStartTime().toString(timeFormatter);
        String string2 = workingHours.getEndTime().toString(timeFormatter);

        return new SelectQueryBuilder(DaoConsts.TABLE_WORKINGHOURS)
                .withRestriction(Restrictions.equalsRestriction(
                        DaoConsts.WORKINGHOURS_STARTTIME,
                        string1))
                .withRestriction(Restrictions.equalsRestriction(
                        DaoConsts.WORKINGHOURS_ENDTIME,
                        string2))
                .withRestriction(Restrictions.equalsRestriction(DaoConsts.WORKINGHOURS_WORKINGDAYS, days.toString()))
                .createStatement();
    }

    public void addWorkingHoursToEmployee(WorkingHours workingHours) throws SQLException {
        int existingWorkingHours = findWorkingHours(workingHours);
        if (existingWorkingHours != -1) {
            int assignedHoursID = getAssignedWorkingHoursID(workingHours.getEmployeeDetailsId());
            if (assignedHoursID == existingWorkingHours)
                return;
            deleteWorkingHours(workingHours.getEmployeeDetailsId());
            addEmployeeToWorkingHours(workingHours.getEmployeeDetailsId(), existingWorkingHours);
            return;
        }

        deleteWorkingHours(workingHours.getEmployeeDetailsId());

        int id = addWorkingHours(workingHours);

        addEmployeeToWorkingHours(workingHours.getEmployeeDetailsId(), id);

    }

    public void deleteWorkingHours(int employeeID) throws SQLException {
        Connection con = ConnectionManager.getInstance().getConnection();

        PreparedStatement statement = con.prepareStatement(DELETE_EMPLOYEE_WORKING_HOURS);
        statement.setInt(1, employeeID);

        statement.execute();
    }

    public int getAssignedWorkingHoursID(int employeeID) throws SQLException {
        Connection con = ConnectionManager.getInstance().getConnection();

        PreparedStatement statement = con.prepareStatement(GET_ASSIGNED_WORKING_HOURS);
        statement.setInt(1, employeeID);

        ResultSet result = statement.executeQuery();

        if(result.next())
            return result.getInt(1);
        return -1;
    }

    public int addWorkingHours(WorkingHours hours) throws SQLException {
        Connection con = ConnectionManager.getInstance().getConnection();

        PreparedStatement statement = con.prepareStatement(INSERT_WORKING_HOURS);
        statement.setString(1, daysToString(hours.getWorkingDays()));
        statement.setString(2, formatTime(hours.getStartTime()));
        statement.setString(3, formatTime(hours.getEndTime()));

        statement.execute();

        return findWorkingHours(hours);


    }

    public void addEmployeeToWorkingHours(int employeID, int workingHoursID) throws SQLException {
        Connection con = ConnectionManager.getInstance().getConnection();

        PreparedStatement statement = con.prepareStatement(INSERT_WORKING_HOUR_JT);
        statement.setInt(1, employeID);
        statement.setInt(2, workingHoursID);

        statement.executeUpdate();
    }
}
