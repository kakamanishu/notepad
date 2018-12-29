package com.example.admin.saveourlife;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class EditNote extends AppCompatActivity {
    public static final int CHOOSE_PHOTO = 2;

    private EditText title, content;
    private Button btn_save, btn_cancel, btn_picture;
    private ActivityManager activityManager;

    private int noteId;
    private String accountname;
    private String time;
    private  byte []image;

    private NotePad notePad;
    private boolean EDIT = false;                   //用来判断是编辑还是删除

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        activityManager = ActivityManager.getInstance();

        btn_save = (Button) findViewById(R.id.save);
        btn_cancel = (Button) findViewById(R.id.cancel);
        btn_picture = (Button) findViewById(R.id.photo);                     //添加照片button
        title = (EditText) findViewById(R.id.noteTitle1);
        content = (EditText) findViewById(R.id.notecontent1);

        Intent intent = getIntent();                                            //nodeId   main传过来的
        noteId = intent.getIntExtra("id", 0);
        accountname = intent.getStringExtra("mainAccount");

        time = intent.getStringExtra("mainTime");

        if (noteId != 0) {
            EDIT = true;
        } else EDIT = false;

        if (EDIT) {                              //如果是长按编辑打开，则显示原有内容
            NotePad notePad = LitePal.find(NotePad.class, noteId);
            title.setText(notePad.getTitle());
            content.setText(notePad.getContent());

            byte[]images=notePad.getImage();
            if (images != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(images, 0, images.length);
                ImageSpan imageSpan = new ImageSpan(EditNote.this, bitmap);
                //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
                SpannableString spannableString = new SpannableString("[local]" + 1 + "[/local]");
                //  用ImageSpan对象替换face
                spannableString.setSpan(imageSpan, 0, "[local]1[local]".length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                //将选择的图片追加到EditText中光标所在位置
                int index = content.getSelectionStart(); //获取光标所在位置
                Editable edit_text = content.getEditableText();
                if (index < 0 || index >= edit_text.length()) {
                    edit_text.append(spannableString);
                } else {
                    edit_text.insert(index, spannableString);
                }
            }
        }
        btn_save.setOnClickListener(new View.OnClickListener() {            //保存按钮
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {          //取消按钮
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(EditNote.this);
                adb.setTitle("提示");
                adb.setMessage("确定不保存吗");
                adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                adb.setNegativeButton("取消", null);
                adb.show();
            }
        });
        btn_picture.setOnClickListener(new View.OnClickListener() {                 //添加照片
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(EditNote.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(EditNote.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbum();
                }
            }
        });
    }
    public void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }


    @Override
    public void onRequestPermissionsResult (int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(EditNote.this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private byte[]img(Bitmap bitmap){                                               //把图片转化成字节
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //取得数据
            ContentResolver cr = EditNote.this.getContentResolver();
            Uri originalUri = data.getData();
            Bitmap bitmap = null;
            try {
                Bitmap originalBitmap = BitmapFactory.decodeStream(cr.openInputStream(originalUri));
                bitmap = resizeImage(originalBitmap, 200, 200);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if(bitmap != null){
                image = img(bitmap);

                //根据Bitmap对象创建ImageSpan对象
                ImageSpan imageSpan = new ImageSpan(EditNote.this, bitmap);
                //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像
                SpannableString spannableString = new SpannableString("[local]"+1+"[/local]");
                //  用ImageSpan对象替换face
                spannableString.setSpan(imageSpan, 0, "[local]1[local]".length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                //将选择的图片追加到EditText中光标所在位置
                int index = content.getSelectionStart(); //获取光标所在位置
                Editable edit_text = content.getEditableText();
                if(index <0 || index >= edit_text.length()){
                    edit_text.append(spannableString);
                }else{
                    edit_text.insert(index, spannableString);
                }
            }else{
                Toast.makeText(EditNote.this, "获取图片失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private Bitmap resizeImage(Bitmap originalBitmap, int newWidth, int newHeight){     //图片缩放
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        //定义欲转换成的宽、高
//      int newWidth = 200;
//      int newHeight = 200;
        //计算宽、高缩放率
        float scanleWidth = (float)newWidth/width;
        float scanleHeight = (float)newHeight/height;
        //创建操作图片用的matrix对象 Matrix
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(scanleWidth,scanleHeight);
        //旋转图片 动作
        //matrix.postRotate(45);
        // 创建新的图片Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(originalBitmap,0,0,width,height,matrix,true);
        return resizedBitmap;
    }

    public void saveNote() {                                         //保存函数
            String t = title.getText().toString().trim();
            String c = content.getText().toString().trim();

            String im = "[local]1[/local]";
            c = c.replace(im,"");                           //去掉  [local]1[local]
            System.out.println("截取后的content = "+c);

            if (t.equals("") || c.equals("")) {
                Toast.makeText(EditNote.this, "标题内容不能为空", Toast.LENGTH_SHORT).show();
            } else {
                if (EDIT) {                          //update
                    notePad = LitePal.find(NotePad.class, noteId);
                    NotePad notePad = new NotePad();
                    notePad.setTitle(t);
                    notePad.setContent(c);
                    notePad.setImage(image);
                    notePad.update(noteId);
                } else {                         //add
                    NotePad notePad = new NotePad();
                    notePad.setTitle(t);
                    notePad.setContent(c);
                    notePad.setImage(image);
                    notePad.setTime(time);
                    if (accountname != null) {
                        notePad.setAccountName(accountname);
                    }
                    notePad.save();

                }
            }
            Intent intent2 = new Intent(EditNote.this, MainActivity.class);
            startActivity(intent2);
            EditNote.this.finish();
        }
}
