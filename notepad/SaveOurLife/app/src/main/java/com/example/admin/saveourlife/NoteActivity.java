package com.example.admin.saveourlife;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.sql.SQLSyntaxErrorException;
import java.util.List;

public class NoteActivity extends AppCompatActivity {

    private TextView title,time,accounttext,content;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        title = (TextView)findViewById(R.id.Title1);
        time = (TextView)findViewById(R.id.Time);
        accounttext = (TextView)findViewById(R.id.accountText);
        content = (TextView)findViewById(R.id.Content1);
        image = (ImageView)findViewById(R.id.imageview);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id",0);        //定位Id
        String account = intent.getStringExtra("account");
        NotePad notePad = LitePal.find(NotePad.class,id);
//        notePad.setAccountName(account);


        byte []ima = notePad.getImage();
        if (ima != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(ima, 0, ima.length);
//            System.out.println("有图片的：" + ima);
            image.setImageBitmap(bitmap);
        }
        title.setText("标题 :"+notePad.getTitle());
        time.setText("时间 :"+intent.getStringExtra("mainTime"));

//        List<NotePad> notePadList = LitePal.findAll(NotePad.class);
//        NotePad notePadzero = notePadList.get(0);
//        notePad.setAccountName(notePadzero.getAccountName());


        accounttext.setText("创建者 :" + notePad.getAccountName());

    }
}
