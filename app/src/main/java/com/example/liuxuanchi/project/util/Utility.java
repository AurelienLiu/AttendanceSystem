package com.example.liuxuanchi.project.util;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.example.liuxuanchi.project.db.AttendanceInfo;
import com.example.liuxuanchi.project.db.People;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by liuxuanchi on 2018/4/5.
 */

public class Utility {

    /**
     * 将获取的考勤信息入库
     * @param response
     * @return
     */
    public static boolean handleAttendacneInfo(String response) {
        if (!TextUtils.isEmpty(response)) {
//            try {
//                Log.d("111", "" + response);
//                JSONArray infoList = new JSONArray(response);
//                for (int i = 0; i < infoList.length(); i++) {
//                    JSONObject infoObject = infoList.getJSONObject(i);
//                    AttendanceInfo info = new AttendanceInfo();
//                    info.setName(infoObject.getString("name"));
//                    info.setDate(infoObject.getLong("date"));
//                    info.setAbsence(infoObject.getBoolean("absence"));
//                    info.setLateTime(infoObject.getInt("get_late_time"));
//                    info.setLeaveEarlyTime(infoObject.getInt("get_leave_early_time"));
//                    info.setTimeRange(infoObject.getString("time_range"));
//                    info.setTimeStamp(System.currentTimeMillis());
//                    info.save();
//                    }
//                    return true;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            Gson gson = new Gson();
            List<AttendanceInfo> infoList = gson.fromJson(response, new TypeToken<List<AttendanceInfo>>(){}.getType());
            for (AttendanceInfo info : infoList) {
                info.save();
            }
        }
        return false;
    }

    /**
     * 获取考勤信息最新时间戳
     */
    public static long getAttendanceInfoLastUpdateTime() {
        Cursor cursor = DataSupport.findBySQL("select max(timestamp) from attendanceinfo");
        return cursor.getLong(cursor.getColumnIndex("timestamp"));
    }

    /**
     * 将时间戳转换为时间
     * @param s
     * @return
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
    public static String stampToDate(long s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }


    /**
     * 将尚未更新至服务器的人员数据转换为Json格式的String，之后上传给服务器
     */
    public static String peopleListToJsonString(List<People> peopleList) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (People people : peopleList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", people.getId());
            jsonObject.put("name", people.getName());
            jsonObject.put("departement", people.getDepartment());
            jsonObject.put("position", people.getPosition());
            jsonObject.put("phone_number", people.getPhoneNumber());
            jsonObject.put("headshot", people.getHeadshot());
            jsonObject.put("job_number", people.getJobNumber());
            jsonArray.put(jsonObject);
        }
       return jsonArray.toString();
    }

}
