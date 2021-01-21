package com.esd.model.service;

import com.esd.model.dao.WorkingHoursDao;
import com.esd.model.data.WorkingHours;
import com.esd.model.util.DateUtil;
import org.joda.time.LocalTime;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class WorkingHoursService {

    private static WorkingHoursService instance;
    private WorkingHoursDao workingHoursDao = WorkingHoursDao.getInstance();

    private static final WorkingHours DEFAULT_WORKING_HOURS = new WorkingHours(
            Arrays.asList(1,2,3,4,5),
            DateUtil.newLocalTime(9,0),
            DateUtil.newLocalTime(17,0)
    );

    private WorkingHoursService() {
    }

    public List<WorkingHours> getWorkingHoursForEmployee(int employeeDetailsId){
        List<WorkingHours> workingHours = workingHoursDao.getWorkingHoursForEmployee(employeeDetailsId);
        if(workingHours.size() == 0){
            return Arrays.asList(DEFAULT_WORKING_HOURS);
        }
        return workingHours;
    }

    public synchronized static WorkingHoursService getInstance() {
        if (instance == null) {
            instance = new WorkingHoursService();
        }
        return instance;
    }

    public void addWorkingHoursToEmployee(WorkingHours workingHours) throws SQLException {
        workingHoursDao.addWorkingHoursToEmployee(workingHours);
    }
}
