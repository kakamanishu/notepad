package com.example.admin.saveourlife;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

public class LoginActivity extends BaseActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private CheckBox rememberPass;
    private EditText accountEdit;
    private EditText passwordEdit;
    private Button login;
    private Button register;
    ActivityManager activityManager = ActivityManager.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityManager.AddActivity(this);
        LitePal.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        accountEdit  = (EditText)findViewById(R.id.account);
        passwordEdit = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        rememberPass = (CheckBox)findViewById(R.id.remember_pass);
        boolean isremember = pref.getBoolean("remember_password",false);
        if (isremember){
            String account = pref.getString("account","");
            String password = pref.getString("password","");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }

        register = (Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建数据库
//                LitePal.getDatabase();
                UserList user = new UserList();
                String account = accountEdit.getText().toString();
                String pass = passwordEdit.getText().toString();

                List<UserList> users = LitePal.findAll(UserList.class);         //遍历查询数据库

                boolean exist = false;
                for(UserList u : users){
                    if (u.getAccount().equals(account)){                    //如果账号存在
                        exist = true;
                        if (!u.getPassword().equals(pass)){             //密码不正确
                            Toast.makeText(LoginActivity.this,"密码不正确哦",Toast.LENGTH_SHORT).show();
                            accountEdit.setText("");                    //弹出提示信息，并清空
                            passwordEdit.setText("");
                        } else {                                        //密码正确
                            editor = pref.edit();                               //记住密码
                            if (rememberPass.isChecked()){
                                editor.putBoolean("remember_password",true);
                                editor.putString("account",account);
                                editor.putString("password",pass);
                            }else {
                                editor.clear();
                            }
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra("account",account);     //告诉Main 登陆者是谁
                            startActivity(intent);
                        }
                    }
                }
                if (!exist){
                    Toast.makeText(LoginActivity.this,"账号不存在",Toast.LENGTH_SHORT).show();
                    accountEdit.setText("");                    //弹出提示信息，并清空
                    passwordEdit.setText("");
                }
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder adb = new AlertDialog.Builder(LoginActivity.this);
            adb.setTitle("提醒")
                    .setMessage("确定退出？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activityManager.exitAllprogress();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
        return super.onKeyDown(keyCode, event);
    }
}
