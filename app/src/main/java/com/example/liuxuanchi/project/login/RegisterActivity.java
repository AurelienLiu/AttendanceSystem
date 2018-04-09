package com.example.liuxuanchi.project.login;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.liuxuanchi.project.R;
import com.example.liuxuanchi.project.db.User;

public class RegisterActivity extends AppCompatActivity {

    //填写基本信息：手机号，用户名，密码
    private EditText phoneEdit;
    private String phone;
    private EditText accountEdit;
    private String account;
    private EditText passwordEdit;
    private String password;
    //完成注册按钮
    private Button finishRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //初始化
        phoneEdit = (EditText) findViewById(R.id.phone_number_register);
        accountEdit = (EditText) findViewById(R.id.account_register);
        passwordEdit = (EditText) findViewById(R.id.password_register);
        finishRegister = (Button) findViewById(R.id.finish_register);


        //完成注册按钮对应的事件
        finishRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = phoneEdit.getText().toString();
                account = accountEdit.getText().toString();
                password = passwordEdit.getText().toString();
                //此处注册条件还可以更加详细
                if ("".equals(phone)||"".equals(account)||"".equals(password)) {
                    Toast.makeText(RegisterActivity.this, "请填充注册信息", Toast.LENGTH_SHORT).show();
                } else {
                    //向表中添加刚刚注册的对象
                    User user = new User();
                    user.setAccount(account);
                    user.setPhone(phone);
                    user.setPassword(password);
                    user.save();
                    //跳转至登录页面
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
