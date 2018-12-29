package com.example.admin.saveourlife;
import android.app.Activity;
import android.icu.text.SimpleDateFormat;

import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityManager {                  //管理所有活动
    private static ActivityManager instance;
    private List<Activity> list;

    public static ActivityManager getInstance(){
        if (instance == null){
            instance = new ActivityManager();
        }
        return instance;
    }

    public void AddActivity(Activity activity){         //添加活动进列表
        if (list == null){
            list = new ArrayList<Activity>();
        }
        if (activity != null){
            list.add(activity);
        }
    }

    public void exitAllprogress(){                  //退出所有程序
        for (int i = 0;i < list.size();i++){
            Activity activity = list.get(i);
            activity.finish();
        }
    }

    public void saveNote(String accountName,String title,String content,String time){
        NotePad note = new NotePad();
        note.setTitle(title);
    }

    public String getTime(){
        Calendar callForDate = Calendar.getInstance();

        java.text.SimpleDateFormat currentDate = new java.text.SimpleDateFormat("dd-MMMM-yyyy");

        final String saveCurrentDate = currentDate.format(callForDate.getTime());

        return saveCurrentDate;
    }


}
