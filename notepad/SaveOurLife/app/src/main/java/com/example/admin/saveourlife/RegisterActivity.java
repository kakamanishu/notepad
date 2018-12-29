package com.example.admin.saveourlife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private EditText accountEdit;
    private EditText pass1Edit;
    private EditText pass2Edit;
    private Button register;
    ActivityManager activityManager = ActivityManager.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activityManager.AddActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        accountEdit = (EditText)findViewById(R.id.account1);
        pass1Edit = (EditText)findViewById(R.id.password1);
        pass2Edit = (EditText)findViewById(R.id.password2);
        register = (Button)findViewById(R.id.register2);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEdit.getText().toString();
                String pass1 = pass1Edit.getText().toString();
                String pass2 = pass2Edit.getText().toString();
                if(!pass1.equals(pass2)){
                    Toast.makeText(RegisterActivity.this,"两次密码不一样！",Toast.LENGTH_SHORT).show();
                }
                else {                                                                                  //注册成功，并且存储数据
                    UserList user = new UserList();
                    user.setAccount(account);
                    user.setPassword(pass1);
                    user.save();
                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    //转回登陆界面
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
