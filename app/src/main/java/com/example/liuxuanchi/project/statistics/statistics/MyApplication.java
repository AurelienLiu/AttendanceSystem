package com.example.liuxuanchi.project.statistics.statistics;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by xiaozhu on 2017/11/5.
 */
//全局获得context的方式
public class MyApplication extends Application{
    private static Context context;

    @Override
    public void onCreate(){
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
    }

    public static Context getContext(){
        return context;
    }
}
