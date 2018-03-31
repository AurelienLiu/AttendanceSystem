package com.example.liuxuanchi.project;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PeopleInformation extends AppCompatActivity {

    private TextView label;
    public static String phoneNumber;
    private DrawerLayout mDrawerLayout;
    private List<AttendanceInfo> infoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_information);

        final Intent intent = getIntent();

//        //设置label

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(intent.getStringExtra("data_name"));

        //设置DrawerLayout
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }


        //电话呼叫联系人
        phoneNumber = intent.getStringExtra("data_phone_number");

        FloatingActionButton buttonCall = (FloatingActionButton)findViewById(R.id.button_call);
        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PeopleInformation.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PeopleInformation.this,
                            new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    call(phoneNumber);
                }
            }
        }); 


        //设置联系人头像以及姓名等信息
        ImageView peopleImage = (ImageView)findViewById(R.id.info_image);
        TextView peopleId = (TextView)findViewById(R.id.info_id);
        TextView peoplePosition = (TextView)findViewById(R.id.info_position);
        TextView peopleDepartment = (TextView)findViewById(R.id.info_department);

        int dep = intent.getIntExtra("data_department", -1);
        byte[] byteOfHeadshot = intent.getByteArrayExtra("data_headshot");
//        peopleImage.setImageResource(PeopleAdapter.setPeopleImage(Department.intToDepartment(dep)));
        peopleImage.setImageBitmap(BitmapFactory.decodeByteArray(byteOfHeadshot, 0, byteOfHeadshot.length));

        peopleId.setText("id： " + intent.getIntExtra("data_id", -1));
        peoplePosition.setText(intent.getStringExtra("data_position"));
        peopleDepartment.setText(Department.intToHanzi(intent.getIntExtra
                ("data_department", -1)));

        //设置navigationView的点击事件
        NavigationView navView = (NavigationView)findViewById(R.id.nav_view);
        navView.setCheckedItem(R.id.people_management);
        MyNavigationView.onSelectItem(navView, PeopleInformation.this, mDrawerLayout);


        //将考勤信息填入attendance content里面
//        StringBuilder builder = new StringBuilder();
//        builder.append("在这里看到他近期的考勤情况" + "\n");
//        for(int i = 0; i < 500; i++) {
//            builder.append("12345");
//        }
//        String content = builder.toString();
//        TextView attendanceContent = (TextView)findViewById(R.id.attendance_content);
//        attendanceContent.setText(content);

        infoList = new ArrayList<>();
        initInfoList();
        AttendanceInfoAdapter adapter = new AttendanceInfoAdapter(PeopleInformation.this,
                R.layout.attendance_info_item, infoList);
        MyListView attendacneList = (MyListView)findViewById(R.id.attendance_list);
        attendacneList.setAdapter(adapter);


    }

    private void initInfoList() {
        AttendanceInfo info1 = new AttendanceInfo();
        info1.setDate("2018-2-5");
        info1.setAbsence(false);
        info1.setLateTime(-10);
        info1.setLeaveEarlyTime(-5);
        info1.setTimeRange("7:50AM----5:05PM");

        AttendanceInfo info2 = new AttendanceInfo();
        info2.setDate("2018-2-4");
        info2.setAbsence(true);
        info2.setLateTime(-10);
        info2.setLeaveEarlyTime(-5);
        info2.setTimeRange("");

        AttendanceInfo info3 = new AttendanceInfo();
        info3.setDate("2018-2-3");
        info3.setAbsence(false);
        info3.setLateTime(10);
        info3.setLeaveEarlyTime(-5);
        info3.setTimeRange("8:10AM----5:05PM");
        for (int i = 0; i < 10; i++) {
            infoList.add(info2);
            infoList.add(info1);
            infoList.add(info3);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call(phoneNumber);
                } else {
                    Toast.makeText(this, "你拒绝了请求", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private void call(String phoneNumber) {
        try {
            Intent intent1 = new Intent(Intent.ACTION_CALL);
            intent1.setData(Uri.parse("tel:" + phoneNumber));

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(intent1);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
