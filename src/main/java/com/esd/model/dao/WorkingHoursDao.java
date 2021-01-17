package com.esd.model.dao;

import com.esd.model.dao.queryBuilders.SelectQueryBuilder;
import com.esd.model.dao.queryBuilders.joins.Joins;
import com.esd.model.dao.queryBuilders.restrictions.Restriction;
import com.esd.model.dao.queryBuilders.restrictions.Restrictions;
import com.esd.model.data.UserGroup;
import com.esd.model.data.WorkingHours;
import org.joda.time.LocalTime;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkingHoursDao {

    private static WorkingHoursDao instance;

    private WorkingHoursDao() {
    }

    public synchronized static WorkingHoursDao getInstance() {
        if (instance == null) {
            instance = new WorkingHoursDao();
        }
        return instance;
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
}
