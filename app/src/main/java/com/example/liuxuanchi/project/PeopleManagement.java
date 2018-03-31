package com.example.liuxuanchi.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class PeopleManagement extends AppCompatActivity {

    private List<People> myList = new ArrayList<>();
    private TextView label;
    private DrawerLayout mDrawerLayout;
    private static final int ORDER_BY_ID = 0;
    private static final int ORDER_BY_DEP = 1;
    private int orderWay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_management);



        //根据排序模式生成list
        SharedPreferences pref = getSharedPreferences("order_way", MODE_PRIVATE);
        orderWay = pref.getInt("order_way", ORDER_BY_ID);
        if (orderWay == ORDER_BY_DEP) {
            List<People> myList0 = DataSupport.where("department = ?", "0").find(People.class);
            myList.addAll(myList0);
            List<People> myList1 = DataSupport.where("department = ?", "1").find(People.class);
            myList.addAll(myList1);
            List<People> myList2 = DataSupport.where("department = ?", "2").find(People.class);
            myList.addAll(myList2);
            List<People> myList3 = DataSupport.where("department = ?", "3").find(People.class);
            myList.addAll(myList3);
            List<People> myList4 = DataSupport.where("department = ?", "-1").find(People.class);
            myList.addAll(myList4);
        } else {
            myList = DataSupport.findAll(People.class);
        }


        //设置label
//        label = (TextView)findViewById(R.id.label);
//        label.setText("人员管理");
//        ActionBar actionBar = getSupportActionBar();  //隐藏系统label
//        if (actionBar != null){
//            actionBar.hide();
//        }
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //设置DrawerLayout
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }



        //设置Recycler view
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        PeopleAdapter adapter = new PeopleAdapter(myList);
        recyclerView.setAdapter(adapter);

        //添加按钮
//        Button buttonAdd = (Button)findViewById(R.id.button_add);
//        buttonAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PeopleManagement.this, PeopleEdit.class);
//                intent.putExtra("for_add", true);
//                startActivity(intent);
//            }
//        });

        //设置navigationView的点击事件
        NavigationView navView = (NavigationView)findViewById(R.id.nav_view);
        navView.setCheckedItem(R.id.people_management);
        MyNavigationView.onSelectItem(navView, PeopleManagement.this, mDrawerLayout);


    }

    //设置toolbar的菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }
    //设置toolbar菜单中的点击事件

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.toolbar_add:
                Intent intent = new Intent(PeopleManagement.this, PeopleEdit.class);
                intent.putExtra("for_add", true);
                startActivity(intent);
                break;
            case R.id.toolbar_order_by_id:
                if (orderWay == ORDER_BY_DEP) {
                    SharedPreferences.Editor editor = getSharedPreferences("order_way", MODE_PRIVATE).edit();
                    editor.putInt("order_way", ORDER_BY_ID);
                    editor.apply();
                    finish();
                    Intent intent1 = new Intent(PeopleManagement.this, PeopleManagement.class);
                    startActivity(intent1);
                }
                break;
            case R.id.toolbar_order_by_dep:
                if (orderWay == ORDER_BY_ID) {
                    SharedPreferences.Editor editor = getSharedPreferences("order_way", MODE_PRIVATE).edit();
                    editor.putInt("order_way", ORDER_BY_DEP);
                    editor.apply();
                    finish();
                    Intent intent1 = new Intent(PeopleManagement.this, PeopleManagement.class);
                    startActivity(intent1);
                }
                break;
            default:
                break;
        }
        return true;
    }


}
