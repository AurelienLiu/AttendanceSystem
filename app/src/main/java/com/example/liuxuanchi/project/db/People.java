package com.example.liuxuanchi.project.db;

import android.graphics.Bitmap;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;

/**
 * Created by liuxuanchi on 2017/12/23.
 */

public class People extends DataSupport{
    private String name;
    private int id;
    private int department;
    private int level;
    private String position;
    private String phoneNumber;
    private byte[] headshot;
    //缺少时间戳 状态戳


    public People(){

    }

    public People(String name){
        this.name = name;
    }

    public People(String name, int id, int departement, int level){
        this.name = name;
        this.id = id;
        this.department = departement;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public byte[] getHeadshot() {
        return headshot;
    }

    public void setHeadshot(byte[] headshot) {
        this.headshot = headshot;
    }

    public static byte[] bitmapToArrayOfByte(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
