package com.example.liuxuanchi.project.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.liuxuanchi.project.BaseActivity;
import com.example.liuxuanchi.project.R;
import com.example.liuxuanchi.project.db.User;
import com.example.liuxuanchi.project.statistics.statistics.StatisticsActivity;

import org.litepal.crud.DataSupport;

import java.util.List;

public class LoginActivity extends BaseActivity {

    private EditText accountEdit;
    private EditText passwordEdit;
    private Button login;
    private Button forgetPassword;
    private Button registerNewAccount;

    private CheckBox rememberPass;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    public static String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //添加测试用户
        List<User> userList = DataSupport.findAll(User.class);
        if (userList.size() == 0) {
            User user = new User();
            user.setAccount("admin");
            user.setPhone("10010");
            user.setPassword("111111");
            user.save();
        }

        //初始化
        accountEdit = (EditText) findViewById(R.id.account);
        passwordEdit = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        forgetPassword = (Button) findViewById(R.id.forget_password);
        registerNewAccount = (Button) findViewById(R.id.register_new_account);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);
        final boolean isRemembered = pref.getBoolean("remember_password", false);

        if (isRemembered){
            //若选择记住密码，读取上次填写的账户密码并自动填入
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(isRemembered);
        }

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                String passwordDb;
                //查询数据库进行判断
                List<User> users = DataSupport.where("account = ?", account).find(User.class);
                passwordDb = users.get(0).getPassword();
                //打印出来看一下密码
                Log.d("LoginActivity", passwordDb);
                /*先不考虑用户名重复
                if (users.size() != 1) {
                    Toast.makeText(LoginActivity.this, "用户名重复",
                            Toast.LENGTH_SHORT).show();
                } else {}
                */
                if (account.equals("")||password.equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入用户名和密码",
                            Toast.LENGTH_SHORT).show();
                } else {
                    //判断该账户密码是否正确
                    if (passwordDb.equals(password)){
                        editor = pref.edit();
                        if (rememberPass.isChecked()){
                            editor.putBoolean("remember_password", true);
                            editor.putString("account", account);
                            editor.putString("password", password);
                        } else {
                            editor.clear();
                        }
                        editor.apply();
                        //登录成功，回到主活动
                        userName = account;
                        Intent intent = new Intent(LoginActivity.this, StatisticsActivity.class);
                        //连接处：将用户名该信息传输出去
                        intent.putExtra("user_name", account);
                        startActivity(intent);
                        finish();
                    } else {
                        //登录失败
                        Toast.makeText(LoginActivity.this, "账户或密码错误",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //跳转至忘记密码
        forgetPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPassActivity.class);
                startActivity(intent);
            }
        });

        //跳转至注册新账户
        registerNewAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}

