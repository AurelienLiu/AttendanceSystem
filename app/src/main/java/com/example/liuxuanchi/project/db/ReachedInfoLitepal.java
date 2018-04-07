package com.example.liuxuanchi.project.db;

import org.litepal.crud.DataSupport;

/**
 * Created by xiaozhu on 2018/3/10.
 */
//statistics活动的recyclerView
public class ReachedInfoLitepal extends DataSupport {
    private String name;
    private String reachedTime;
    private int arriveInTime;
    private String telephone;

    public String getName() {
        return name;
    }

    public String getReachedTime() {
        return reachedTime;
    }

    public int isArriveInTime() {
        return arriveInTime;
    }

    public String getTelephone() {
        return telephone;
    }



    public void setName(String name) {
        this.name = name;
    }

    public void setReachedTime(String reachedTime) {
        this.reachedTime = reachedTime;
    }

    public void setArriveInTime(int arriveInTime) {
        this.arriveInTime = arriveInTime;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
