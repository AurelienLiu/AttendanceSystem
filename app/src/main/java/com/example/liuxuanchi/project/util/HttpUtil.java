package com.example.liuxuanchi.project.util;

import android.app.Activity;
import android.widget.Toast;

import java.io.IOException;

import javax.naming.Context;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liuxuanchi on 2018/3/19.
 */

public class HttpUtil {

    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    /**
     *请求异步get人员信息
     */
    public static void getPeopleInfoFromServer(String address, Activity activity) {
        sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity.getBaseContext(), "更新失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                Utility.handlePeopleInfo(responseText);
            }
        });
    }

    /**
     *请求异步get考勤信息
     */
    public static void getAttendanceInfoFromServer(String address, Activity activity) {
        sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity.getBaseContext(), "更新失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                Utility.handleAttendanceInfo(responseText);
            }
        });
    }


}
