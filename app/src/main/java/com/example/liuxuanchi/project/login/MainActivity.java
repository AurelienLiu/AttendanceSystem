package com.example.liuxuanchi.project.login;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.liuxuanchi.project.R;
import com.example.liuxuanchi.project.db.User;

import org.litepal.tablemanager.Connector;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //创建数据库
        Connector.getDatabase();

        //添加一个新用户方便进行测试
        User user = new User();
        user.setAccount("admin");
        user.setPhone("10010");
        user.setPassword("111111");
        user.save();

        //实现退出登录功能
        Button forceOffLine = (Button) findViewById(R.id.force_offline);
        forceOffLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.Jyy.FORCE_OFFLINE");
                sendBroadcast(intent);
            }
        });
    }
}
