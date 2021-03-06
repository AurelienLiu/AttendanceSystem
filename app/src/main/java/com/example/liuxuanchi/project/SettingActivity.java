package com.example.liuxuanchi.project;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.liuxuanchi.project.db.AttendanceInfo;
import com.example.liuxuanchi.project.db.People;
import com.example.liuxuanchi.project.peopleManagement.PeopleManagement;
import com.example.liuxuanchi.project.statistics.statistics.StatisticsActivity;
import com.example.liuxuanchi.project.util.HttpUtil;
import com.example.liuxuanchi.project.util.Utility;

import org.json.JSONException;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SettingActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //设置DrawerLayout
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_24px);
        }

        //设置navigationView的点击事件
        NavigationView navView = (NavigationView)findViewById(R.id.nav_view);
        navView.setCheckedItem(R.id.setting);
        MyNavigationView.onSelectItem(navView, SettingActivity.this, mDrawerLayout);


        //考勤信息下载及入库
        Button updateAttendanceInfo = (Button)findViewById(R.id.update_attendacne_info);
        updateAttendanceInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = "http://10.0.2.2/fahuichu";
                RequestBody requestBody = new FormBody.Builder()
                        .add("timeStamp", "" + Utility.getAttendanceInfoLastUpdateTime())
                        .build();
                Log.d("22222222", "onClick: ");
                HttpUtil.sendOkHttpRequest(address, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(SettingActivity.this,
//                                        "更新签到信息失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseText = response.body().string();
                        boolean result = Utility.handleAttendacneInfo(responseText);
                        if (result) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SettingActivity.this,
                                            "更新签到信息成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }, requestBody);
            }
        });

        //人员信息下载及入库
        Button updatePeopleInfo = (Button)findViewById(R.id.update_people_info);
        updatePeopleInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = "http://10.0.2.2/fahuichu.json";
                HttpUtil.sendOkHttpRequest(address, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SettingActivity.this,
                                        "更新签到信息失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String responseText = response.body().string();
                        boolean result = Utility.handlePeopleInfo(responseText);
                        if (result) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SettingActivity.this,
                                            "更新签到信息成功", Toast.LENGTH_SHORT).show();
                                    Log.d("2222222222", "run: " + responseText);
                                }
                            });
                        }
                    }
                });
            }
        });

        //人员信息更改上传
        Button postPeopleInfo = (Button)findViewById(R.id.post_people_info);
        postPeopleInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String address = "http://10.0.2.2/fahuichu";
//                String address = "http://172.20.10.3:8080/HelloWorld/HelloForm";
                String address = "http://47.95.228.175:8080/DBApi/addPhotoByByte";
                String json = "";//返回的json字符串
                List<People> peopleList = DataSupport.where("status < ?", "9").find(People.class);
                People people = DataSupport.findLast(People.class);
                try {
//                    json = Utility.peopleListToJsonString(peopleList);
                    json = Utility.peopleListToJsonString(people);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody requestBody = RequestBody.create(JSON, json);
                Log.d("1111", "onClick: "+ json);
                HttpUtil.sendOkHttpRequest(address, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SettingActivity.this,
                                        "人员信息更新上传失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SettingActivity.this,
                                        "人员信息更新上传成功" + response.toString(), Toast.LENGTH_SHORT).show();
                                Log.d("111111111", "run: " + response.toString());
                            }
                        });
//                        //在本地库中删除status为-1的人员
//                        DataSupport.deleteAll(People.class, "status < ", "0");
//                        //将剩余所以人员的更新时间戳设为现在的时间，状态戳设为9（已同步）
//                        People people = new People();
//                        people.setTimeStamp(System.currentTimeMillis());
//                        people.setStatus(9);
//                        people.updateAll("status < ?", "9");


                    }
                }, requestBody);
            }
        });


        //本地添加考勤信息
        Button addAttendanceInfo = (Button)findViewById(R.id.add_attendance_info);
        addAttendanceInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataSupport.deleteAll(AttendanceInfo.class);
//                long time = SystemClock.elapsedRealtime();
                long hour = 60 * 60 * 1000;
                long minute = 60 * 1000;
                long time = System.currentTimeMillis() + (9 - StatisticsActivity.getHourOfToday()) * hour;
                long time2 = time + 8 * hour;
                long oneDay = 24 * 60 * 60 * 1000;
                Random random = new Random();
                List<People> list = DataSupport.where("status>?", "-1").find(People.class);
                for (People people : list) {
                    for (int i = 0; i < 120; i++) {
                        if (random.nextInt(12)  == 1) {
                            int n = random.nextInt(200);
                            AttendanceInfo info = new AttendanceInfo(people.getId(), "" + people.getName(),
                                    (long)time - oneDay * i + (n - 50) * minute, true, System.currentTimeMillis());
                            info.save();
                        } else {
                            int n = random.nextInt(200);
                            AttendanceInfo info = new AttendanceInfo(people.getId(), "" + people.getName(),
                                    (long)time - oneDay * i - (n - 50) * minute, false, System.currentTimeMillis());;
                            info.save();
                        }
                    }
                    for (int i = 0; i < 120; i++) {
                        if (random.nextInt(12)  == 1) {
                            int n = random.nextInt(200);
                            AttendanceInfo info = new AttendanceInfo(people.getId(), "" + people.getName(),
                                    (long)time2 - oneDay * i + (n - 50) * minute, true, System.currentTimeMillis());
                            info.save();
                        } else {
                            int n = random.nextInt(200);
                            AttendanceInfo info = new AttendanceInfo(people.getId(), "" + people.getName(),
                                    (long)time2 - oneDay * i + (n - 50) * minute, false, System.currentTimeMillis());
                            info.save();
                        }
                    }
                }

            }
        });


        /**
         * 选择时间
         */
        Button chooseTime1 = (Button)findViewById(R.id.choose_time1);
        final TextView time1= (TextView)findViewById(R.id.time1);
        chooseTime1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.showTimePickerDialog(SettingActivity.this,2 , time1, Calendar.getInstance());
            }
        });
        Button chooseTime2 = (Button)findViewById(R.id.choose_time2);
        final TextView time2= (TextView)findViewById(R.id.time2);
        chooseTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.showTimePickerDialog(SettingActivity.this,2 , time2, Calendar.getInstance());
            }
        });
        //将之前定好的时间填入TextView
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        int hour1 = pref.getInt("hour1", -1);
        int hour2 = pref.getInt("hour2", -1);
        int minute1 = pref.getInt("minute1", -1);
        int minute2 = pref.getInt("minute2", -1);
        if (hour1 > -1 && minute1 > -1) {
            time1.setText(hour1 + "时" + minute1 + "分");
        }
        if (hour2 > -1 && minute2 > -1) {
            time2.setText(hour2 + "时" + minute2 + "分");
        }

        //测试用
        Button test = (Button)findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long i = Utility.getAttendanceInfoLastUpdateTime();
                Toast.makeText(SettingActivity.this, i + "", Toast.LENGTH_SHORT).show();
            }
        });
//

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
                break;
        }
        return true;
    }


}
