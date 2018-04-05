package com.example.liuxuanchi.project.login;




import android.app.Activity;
import java.util.ArrayList;
import java.util.List;

//创建该类用于对所有活动进行管理

public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<>();

    //添加活动
    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    //删除某项活动
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }
    //结束所有活动
    public static void finishAll(){
        for (Activity activity : activities){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}

