package com.example.liuxuanchi.project;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.liuxuanchi.project.R;
import com.example.liuxuanchi.project.peopleManagement.PeopleManagement;
import com.example.liuxuanchi.project.statistics.statistics.StatisticsActivity;

/**
 * Created by liuxuanchi on 2018/3/1.
 */

public class MyNavigationView extends NavigationView {

    public MyNavigationView(Context context) {
        super(context);
    }

    public MyNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //设置navigationView中item的点击事件
    public static void onSelectItem(NavigationView navView, final Context context, final DrawerLayout mDrawerLayout) {
        navView.setNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.attendance_record:
                        Toast.makeText(context, "跳转到考勤记录界面", Toast.LENGTH_SHORT).show();
                        mDrawerLayout.closeDrawers();
                        if (context.getClass() != StatisticsActivity.class) {
                            Intent intent = new Intent(context, StatisticsActivity.class);
                            context.startActivity(intent);
                        }
                        break;
                    case R.id.people_management:
                        mDrawerLayout.closeDrawers();
                        if (context.getClass() != PeopleManagement.class) {
                            Intent intent = new Intent(context, PeopleManagement.class);
                            context.startActivity(intent);
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }
}
