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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liuxuanchi.project.BaseActivity;
import com.example.liuxuanchi.project.MyNavigationView;
import com.example.liuxuanchi.project.R;
import com.example.liuxuanchi.project.db.AttendanceInfo;
import com.example.liuxuanchi.project.util.HttpUtil;
import com.example.liuxuanchi.project.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PeopleInformation extends BaseActivity implements View.OnClickListener{

    private TextView label;
    public static String phoneNumber;
    private DrawerLayout mDrawerLayout;
    private List<AttendanceInfo> infoList;
    private ProgressDialog progressDialog;
    private String name;
    private AttendanceInfoAdapter adapter;
    private TextView timeRange;
    private TextView checkTime;
    private TextView lateTime;
    private TextView absenceTime;
    private int id;
    private long nowTimeStamp;
    //考勤信息的列表
    MyListView attendacneList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_information);

        //获取当前时间时间戳，用于筛选考勤数据
        nowTimeStamp = System.currentTimeMillis();

        final Intent intent = getIntent();

//        //设置label

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        name = intent.getStringExtra("data_name");
        collapsingToolbarLayout.setTitle(name);
        id = intent.getIntExtra("data_id", -1);
        Toast.makeText(PeopleInformation.this, "name=" + name + "id=" + id, Toast.LENGTH_SHORT).show();

        //设置DrawerLayout
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.back_24px);
        }

        //设置navigationView的点击事件
        NavigationView navView = (NavigationView)findViewById(R.id.nav_view);
        navView.setCheckedItem(R.id.people_management);
        MyNavigationView.onSelectItem(navView, PeopleInformation.this, mDrawerLayout);

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
//        peopleDepartment.setText(Department.intToHanzi(intent.getIntExtra
//                ("data_department", -1)));
        peopleDepartment.setText(intent.getStringExtra("data_department"));


//        //自动更新该人员签到历史信息
//        String address = "http://10.0.2.2/fahuichu.json";
//        queryFromServer(address);


        /**
         * 周、月、总三个按钮
         */
        timeRange = (TextView)findViewById(R.id.time_range);
        checkTime = (TextView)findViewById(R.id.check_time);
        lateTime = (TextView)findViewById(R.id.late_time);
        absenceTime = (TextView)findViewById(R.id.absence_time);
        Button weekButton = (Button)findViewById(R.id.week_range);
        Button monthButton = (Button)findViewById(R.id.month_range);
        Button allButton = (Button)findViewById(R.id.all_range);
        weekButton.setOnClickListener(this);
        monthButton.setOnClickListener(this);
        allButton.setOnClickListener(this);

        //将考勤信息填入attendance content里面

        infoList = new ArrayList<>();
        long oneWeek = 7 * 24 * 60 * 60 * 1000;  //一周的毫秒数
        //通过AttendanceInfo里的peopleId匹配对应的考勤数据
        infoList = DataSupport
                .where("date>? AND peopleId=?", "" + (nowTimeStamp - oneWeek),  ""+id)
                .order("date desc")
                .find(AttendanceInfo.class);
        adapter = new AttendanceInfoAdapter(PeopleInformation.this,
                R.layout.attendance_info_item, infoList);
        attendacneList = (MyListView)findViewById(R.id.attendance_list);
        attendacneList.setAdapter(adapter);
        checkTime.setText(infoList.size() + "");

        //计算缺勤次数
        int absence = 0;
        int lateOrEarly = 0;
        for (AttendanceInfo info : infoList) {
            if (info.isAbsence()) {
                absence++;
            } else {
                if(Utility.isLateOrGoEarly(PeopleInformation.this, info.getDate())){
                    lateOrEarly++;
                }
            }
        }
        absenceTime.setText(absence + "");
        lateTime.setText(lateOrEarly + "");


    }

//    private void queryFromServer(String address) {
//        showProgressDialog();
//        HttpUtil.sendOkHttpRequest(address, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                //通过runOnUiThread()方法回到主线程处理逻辑
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        closeProgressDialog();
//                        Toast.makeText(PeopleInformation.this, "数据更新失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String responseText = response.body().string();
//                boolean result = false;
//                Log.d("PeopleInfo.class", "onResponse: " + responseText);
//                result = Utility.handlerOnePersonAttendanceInfo(responseText);
//                closeProgressDialog();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        infoList.clear();
//                        infoList = DataSupport.where("name=?", name).find(AttendanceInfo.class);
//                        adapter.notifyDataSetChanged();
//                        Toast.makeText(PeopleInformation.this, "更新成功", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//    }

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
                finish();
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.week_range:
                showProgressDialog();
                long oneWeek = 7 * 24 * 60 * 60 * 1000;  //一周的毫秒数
                timeRange.setText("本周");
                infoList.clear();
                infoList.addAll(DataSupport
                        .where("date>? AND peopleId=?", "" + (nowTimeStamp - oneWeek), "" + id)
                        .order("date desc")
                        .find(AttendanceInfo.class));
                adapter.notifyDataSetChanged();
                checkTime.setText(infoList.size() + "");
                //计算缺勤次数
                int absence1 = 0;
                int lateOrEarly1 = 0;
                for (AttendanceInfo info : infoList) {
                    if (info.isAbsence()) {
                        absence1++;
                    } else {
                        if(Utility.isLateOrGoEarly(PeopleInformation.this, info.getDate())){
                            lateOrEarly1++;
                        }
                    }
                }
                absenceTime.setText(absence1 + "");
                lateTime.setText(lateOrEarly1 + "");
                closeProgressDialog();
                break;
            case R.id.month_range:
                showProgressDialog();
                long oneMonth = (long)30 * 24 * 60 * 60 * 1000;  //一周的毫秒数
                timeRange.setText("本月");
                infoList.clear();
                infoList.addAll(DataSupport
                        .where("date>? AND peopleId=?", "" + (nowTimeStamp - oneMonth), "" + id)
                        .order("date desc")
                        .find(AttendanceInfo.class));
                adapter.notifyDataSetChanged();
                String t = infoList.size() + "";
                checkTime.setText(t);
                //计算缺勤次数
                int absence2 = 0;
                int lateOrEarly2 = 0;
                for (AttendanceInfo info : infoList) {
                    if (info.isAbsence()) {
                        absence2++;
                    } else {
                        if(Utility.isLateOrGoEarly(PeopleInformation.this, info.getDate())){
                            lateOrEarly2++;
                        }
                    }
                }
                absenceTime.setText(absence2 + "");
                lateTime.setText(lateOrEarly2 + "");
                closeProgressDialog();
                break;
            case R.id.all_range:
                showProgressDialog();
                timeRange.setText("总");
                infoList.clear();
                infoList.addAll(DataSupport.where("peopleId=?", "" + id)
                        .order("date desc")
                        .find(AttendanceInfo.class));
                adapter.notifyDataSetChanged();
                checkTime.setText(infoList.size() + "");
                //计算缺勤次数
                int absence3 = 0;
                int lateOrEarly3 = 0;
                for (AttendanceInfo info : infoList) {
                    if (info.isAbsence()) {
                        absence3++;
                    } else {
                        if(Utility.isLateOrGoEarly(PeopleInformation.this, info.getDate())){
                            lateOrEarly3++;
                        }
                    }
                }
                absenceTime.setText(absence3 + "");
                lateTime.setText(lateOrEarly3 + "");
                closeProgressDialog();
                break;
            default:
                break;
        }
    }
}
