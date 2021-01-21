package com.esd.model.data;

import org.joda.time.LocalTime;

import java.util.List;

public class WorkingHours {

    private int id;
    private int employeeDetailsId;
    private List<Integer> workingDays;
    private LocalTime startTime;
    private LocalTime endTime;

    public WorkingHours() {
    }

    public WorkingHours(List<Integer> workingDays, LocalTime startTime, LocalTime endTime) {
        this.workingDays = workingDays;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    public List<Integer> getWorkingDays() {
        return workingDays;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeDetailsId() {
        return employeeDetailsId;
    }

    public void setEmployeeDetailsId(int employeeDetailsId) {
        this.employeeDetailsId = employeeDetailsId;
    }

    public void setWorkingDays(List<Integer> workingDays) {
        this.workingDays = workingDays;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
