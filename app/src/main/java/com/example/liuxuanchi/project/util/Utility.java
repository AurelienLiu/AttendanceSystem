package com.example.liuxuanchi.project.util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.liuxuanchi.project.R;
import com.example.liuxuanchi.project.db.AttendanceInfo;
import com.example.liuxuanchi.project.db.People;
import com.example.liuxuanchi.project.peopleManagement.PeopleInformation;
import com.example.liuxuanchi.project.statistics.statistics.StatisticsActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
            return true;
        }
        return false;
    }

    /**
     * 将获取的人员信息入库
     * @param response
     * @return
     */
    public static boolean handlePeopleInfo(String response) {
        if (!TextUtils.isEmpty(response)) {
            Gson gson = new Gson();
            List<People> peopleList = gson.fromJson(response, new TypeToken<List<People>>(){}.getType());
            for (People people : peopleList) {
                if (people.getStatus() == 0) {
                    people.setStatus(9);
                    people.save();
                } else if (people.getStatus() == 1) {
                    people.setStatus(9);
                    people.update(people.getId());
                } else if (people.getStatus() == -1) {
                    DataSupport.delete(People.class, people.getId());
                } else {
                    Log.e("将获取人员信息入库时：", "姓名:" + people.getName() + "id:" + people.getId() + "status:" + people.getStatus() );
                }
            }
            return true;
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
    public static String stampToDate(long s, String format){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
           * 日期格式字符串转换成时间戳
           * @param date_str 字符串日期
           * @return
           */
     public static long dateToTimeStamp(String date_str){
         String format = "yyyy-MM-dd HH:mm:ss";
         try {
             SimpleDateFormat sdf = new SimpleDateFormat(format);
             return sdf.parse(date_str).getTime();
         } catch (Exception e) {
             e.printStackTrace();
         }
         return -1;
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
//        Gson gson = new Gson();
//        String jsonString = gson.toJson(peopleList);
        return jsonArray.toString();
    }


    /**
     * 判断该人员是否迟到或早退
     */
    public static boolean isLateOrGoEarly(Context context, long s) {
        Date date = new Date(s);
        int hour = date.getHours();
        int minute = date.getMinutes();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        int hour1 = pref.getInt("hour1", -1);
        int minute1 = pref.getInt("minute1", -1);
        int hour2 = pref.getInt("hour2", -1);
        int minute2 = pref.getInt("minute2", -1);
        if ((hour1 < 0 || minute1 < 0) && (hour2 < 0 || minute2 < 0)) {
            Toast.makeText(context, "请在设置页面设定预计上下班时间！", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (hour1 > -1 && minute1 > -1 && hour2 > -1 && minute2 > -1) {
            if (hour < hour1) {
                return false;
            } else if (hour == hour1 && minute < minute1 + 1) {
                return false;
            } else if (hour > hour2) {
                return false;
            } else if (hour == hour2 && minute > minute2 - 1) {
                return false;
            } else {
                return true;
            }
        } else if (hour2 < 0 || minute2 < 0) {
            if (hour < hour1) {
                return false;
            } else if (hour == hour1 && minute < minute1 + 1) {
                return false;
            } else {
                return true;
            }
        } else {
            if (hour > hour2) {
                return false;
            } else if (hour == hour2 && minute > minute2 - 1) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static void showDatePickerDialog(final Activity activity, int themeResId,
                                            final PieChart pieChart, Calendar calendar) {
        // Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来
        // 解释一哈，Activity是context的子类
        new DatePickerDialog(activity, themeResId,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        StatisticsActivity.choseYear = year;
                        StatisticsActivity.choseMonth = month + 1;//月份是从0-11
                        StatisticsActivity.choseDay = dayOfMonth;
                        StatisticsActivity.changeStatisticsByDate(pieChart);
                    }
                }
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public static void showTimePickerDialog(final Activity activity, int themeResId,
                                            final TextView tv, Calendar calendar) {
        // Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来
        // 解释一哈，Activity是context的子类
        new TimePickerDialog( activity,themeResId,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        tv.setText( hourOfDay + "时" + minute  + "分");
                        SharedPreferences.Editor editor =
                                PreferenceManager.getDefaultSharedPreferences(activity).edit();
                        if (tv.getId() == R.id.time1) {
                            editor.putInt("hour1", hourOfDay);
                            editor.putInt("minute1", minute);
                        } else {
                            editor.putInt("hour2", hourOfDay);
                            editor.putInt("minute2", minute);
                        }
                        editor.apply();
                    }
                }
                // 设置初始时间
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                ,true).show();
    }

}
