package com.example.liuxuanchi.project.db;

import org.litepal.crud.DataSupport;

/**
 * Created by liuxuanchi on 2018/3/31.
 */

public class AttendanceInfo extends DataSupport {

    private String name;

    private String date;

    private int lateTime;

    private int leaveEarlyTime;

    private String timeRange;

    private boolean absence;

    public boolean isAbsence() {
        return absence;
    }

    public void setAbsence(boolean absence) {
        this.absence = absence;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLateTime() {
        return lateTime;
    }

    public void setLateTime(int lateTime) {
        this.lateTime = lateTime;
    }

    public int getLeaveEarlyTime() {
        return leaveEarlyTime;
    }

    public void setLeaveEarlyTime(int leaveEarlyTime) {
        this.leaveEarlyTime = leaveEarlyTime;
    }

    public String getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(String timeRange) {
        this.timeRange = timeRange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
