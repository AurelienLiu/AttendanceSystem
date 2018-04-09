package com.example.liuxuanchi.project.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.liuxuanchi.project.db.AttendanceInfo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

/**
 * Created by liuxuanchi on 2018/4/5.
 */

public class Utility {

    /**
     * 处理个人历史签到信息
     */
    public static boolean handlerOnePersonAttendanceInfo(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                Log.d("111", "handlerOnePersonAttendanceInfo : " + response);
                JSONArray infoList = new JSONArray(response);
                for (int i = 0; i < infoList.length(); i++) {
                    JSONObject infoObject = infoList.getJSONObject(i);
                    String date = infoObject.getString("date");
                    String name = infoObject.getString("name");
                    if (DataSupport.where("name=?", name).where("date=", date).find(AttendanceInfo.class).size() == 0) {
                        AttendanceInfo info = new AttendanceInfo();
                        info.setName(name);
                        info.setDate(date);
                        info.setAbsence(infoObject.getBoolean("absence"));
                        info.setLateTime(infoObject.getInt("get_late_time"));
                        info.setLeaveEarlyTime(infoObject.getInt("get_leave_early_time"));
                        info.setTimeRange(infoObject.getString("time_range"));
                        Log.d("111", "handlerOnePersonAttendanceInfo : " + name);
                        info.save();
                    }
                    return true;
                }

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
