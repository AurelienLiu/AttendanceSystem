package com.example.liuxuanchi.project.db;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

/**
 * Created by liuxuanchi on 2018/3/31.
 */

public class AttendanceInfo extends DataSupport {

    private int id;
    //员工id
    private int peopleId;
    //员工姓名
    private String name;
    //签到时间
    private long date;

    private boolean absence;
    //时间戳，表示更新入库时间
    private long timeStamp;


    //    @SerializedName("late_time")
//    private int lateTime;
//    @SerializedName("leave_early_time")
//    private int leaveEarlyTime;
//    @SerializedName("time_range")
//    private String timeRange;
    public AttendanceInfo(){

    }

    public AttendanceInfo(int peopleId, String name, long data, boolean absence, long timeStamp){
        setPeopleId(peopleId);
        setName(name);
        setDate(data);
        setAbsence(absence);
        setTimeStamp(timeStamp);
    }

    public int getPeopleId() {
        return peopleId;
    }

    public void setPeopleId(int peopleId) {
        this.peopleId = peopleId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public boolean isAbsence() {
        return absence;
    }

    public void setAbsence(boolean absence) {
        this.absence = absence;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
//
//    public int getLateTime() {
//        return lateTime;
//    }
//
//    public void setLateTime(int lateTime) {
//        this.lateTime = lateTime;
//    }
//
//    public int getLeaveEarlyTime() {
//        return leaveEarlyTime;
//    }
//
//    public void setLeaveEarlyTime(int leaveEarlyTime) {
//        this.leaveEarlyTime = leaveEarlyTime;
//    }
//
//    public String getTimeRange() {
//        return timeRange;
//    }
//
//    public void setTimeRange(String timeRange) {
//        this.timeRange = timeRange;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
