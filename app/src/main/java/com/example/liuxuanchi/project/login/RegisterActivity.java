package com.example.liuxuanchi.project.login;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.liuxuanchi.project.R;

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
        //初始化
        phoneEdit = (EditText) findViewById(R.id.phone_number_register);
        phone = phoneEdit.getText().toString();
        accountEdit = (EditText) findViewById(R.id.account_register);
        account = accountEdit.getText().toString();
        passwordEdit = (EditText) findViewById(R.id.password_register);
        password = passwordEdit.getText().toString();
        finishRegister = (Button) findViewById(R.id.finish_register);

        //此处注册条件还可以更加详细
        if (phoneEdit!=null && accountEdit!=null && passwordEdit!=null) {
        } else {
            Toast.makeText(this, "请完成信息输入",
                    Toast.LENGTH_SHORT).show();
        }

        //完成注册按钮对应的事件
        finishRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }
}
