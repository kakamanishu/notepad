package com.example.admin.saveourlife;

import org.litepal.crud.LitePalSupport;

public class NotePad extends LitePalSupport {
    private int id;                       //笔记id
    private String title;               //标题
    private String content;             //内容
    private String accountName;     //用户名
    private String time;            //时间
    private byte[] image;           //图片

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
