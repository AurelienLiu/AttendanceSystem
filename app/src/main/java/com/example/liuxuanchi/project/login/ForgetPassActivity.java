package com.example.liuxuanchi.project.login;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.liuxuanchi.project.R;
import com.example.liuxuanchi.project.db.User;

public class ForgetPassActivity extends AppCompatActivity {

    private EditText phoneEdit;
    private EditText verificationCodeEdit;
    private EditText newPasswordEdit;
    private String phone;
    private String verificationCode;
    private String newPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        //初始化
        phoneEdit = (EditText) findViewById(R.id.phone_number_forget);
        phone = phoneEdit.getText().toString();
        verificationCodeEdit = (EditText) findViewById(R.id.verification_code);
        verificationCode = verificationCodeEdit.getText().toString();
        newPasswordEdit = (EditText) findViewById(R.id.new_password);
        newPassword = newPasswordEdit.getText().toString();

        Button getVerificationCode = (Button) findViewById(R.id.get_verification_code);
        Button confirm = (Button) findViewById(R.id.confirm);

        //验证码先简单写一个toast
        getVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Toast.makeText(ForgetPassActivity.this, "您的验证码为0000",
                            Toast.LENGTH_SHORT).show();
                }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //对验证码进行判断
                if (!verificationCode.equals("0000")) {
                    Toast.makeText(ForgetPassActivity.this, "您输入的验证码有误",
                            Toast.LENGTH_SHORT).show();
                } else {
                    //验证码正确，需要更新当前用户的密码
                    User user = new User();
                    user.setPassword(newPassword);
                    user.updateAll("phone = ?", phone);
                    //跳转至登录页面
                    Intent intent = new Intent(ForgetPassActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}
