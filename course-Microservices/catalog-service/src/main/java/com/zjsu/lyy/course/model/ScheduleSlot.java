package com.zjsu.lyy.course.model;
import jakarta.persistence.Embeddable;

@Embeddable
public class ScheduleSlot {
    private String dayOfWeek;   // MONDAY/TUESDAY...
    private String startTime;   // HH:mm
    private String endTime;
    private Integer expectedAttendance;

    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public Integer getExpectedAttendance() { return expectedAttendance; }
    public void setExpectedAttendance(Integer expectedAttendance) { this.expectedAttendance = expectedAttendance; }
}