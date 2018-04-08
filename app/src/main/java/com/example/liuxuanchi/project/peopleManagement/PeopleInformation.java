package com.example.liuxuanchi.project.peopleManagement;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liuxuanchi.project.MyNavigationView;
import com.example.liuxuanchi.project.R;
import com.example.liuxuanchi.project.db.AttendanceInfo;
import com.example.liuxuanchi.project.util.HttpUtil;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PeopleInformation extends AppCompatActivity {

    private TextView label;
    public static String phoneNumber;
    private DrawerLayout mDrawerLayout;
    private List<AttendanceInfo> infoList;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_information);

        final Intent intent = getIntent();

//        //设置label

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        String name = intent.getStringExtra("data_name");
        collapsingToolbarLayout.setTitle(name);

        //设置DrawerLayout
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_24px);
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

        peopleId.setText("工号： " + intent.getStringExtra("data_job_number"));
        peoplePosition.setText(intent.getStringExtra("data_position"));
        peopleDepartment.setText(Department.intToHanzi(intent.getIntExtra
                ("data_department", -1)));

        //设置navigationView的点击事件
        NavigationView navView = (NavigationView)findViewById(R.id.nav_view);
        navView.setCheckedItem(R.id.people_management);
        MyNavigationView.onSelectItem(navView, PeopleInformation.this, mDrawerLayout);

//        //自动更新该人员签到历史信息
//        String address = "http://10.0.2.2/fahuichu.json";
//        queryFromServer(address);


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
        infoList = DataSupport.where("name=?", name).find(AttendanceInfo.class);
        initInfoList(name);
        AttendanceInfoAdapter adapter = new AttendanceInfoAdapter(PeopleInformation.this,
                R.layout.attendance_info_item, infoList);
        MyListView attendacneList = (MyListView)findViewById(R.id.attendance_list);
        attendacneList.setAdapter(adapter);
    }

    private void queryFromServer(String address) {
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //通过runOnUiThread()方法回到主线程处理逻辑
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(PeopleInformation.this, "数据更新失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().toString();
                boolean result = false;
                Log.d("PeopleInfo.class", "onResponse: " + responseText);
//                result = Utility.handlerOnePersonAttendanceInfo(responseText);
                closeProgressDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PeopleInformation.this, "数据更新成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(PeopleInformation.this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void initInfoList(String name) {
        AttendanceInfo info1 = new AttendanceInfo();
        info1.setName(name);
        info1.setDate("2018-2-5");
        info1.setAbsence(false);
        info1.setLateTime(-10);
        info1.setLeaveEarlyTime(-5);
        info1.setTimeRange("7:50AM----5:05PM");

        AttendanceInfo info2 = new AttendanceInfo();
        info2.setName(name);
        info2.setDate("2018-2-4");
        info2.setAbsence(true);
        info2.setLateTime(-10);
        info2.setLeaveEarlyTime(-5);
        info2.setTimeRange("");

        AttendanceInfo info3 = new AttendanceInfo();
        info3.setName(name);
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
