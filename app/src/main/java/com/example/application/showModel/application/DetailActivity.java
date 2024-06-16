package com.example.application.showModel.application;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.example.application.R;
import com.example.application.uploadModel.bean.FaBuBean;
import com.example.application.uploadModel.sqlUtils.FaBuSQLHelper;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView tv_author,tv_tittle,tv_keys,tv_js,tv_back;
    private ImageView iv_show;

    private Integer _id;

    private FaBuSQLHelper sqlHelper=new FaBuSQLHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        _id=getIntent().getIntExtra("EXTRA_DATA",0);
        init();
        setListener();
    }

    private void init(){
        tv_author=findViewById(R.id.tv_show_author);
        tv_tittle=findViewById(R.id.tv_show_tittle);
        tv_keys=findViewById(R.id.tv_show_keys);
        tv_js=findViewById(R.id.tv_show_js);
        tv_back = findViewById(R.id.tv_back);
        iv_show=findViewById(R.id.iv_show);
        FaBuBean fabuBean = sqlHelper.findFaBuInfoByID(this, _id);
        tv_author.setText(sqlHelper.getUserName(this,fabuBean.getUserId()));
        tv_tittle.setText(fabuBean.getTitle());
        tv_keys.setText(fabuBean.getKeys());
        tv_js.setText(fabuBean.getText());
        Uri uri = Uri.parse(fabuBean.getUri());
//        try {
//            InputStream inputStream=getContentResolver().openInputStream(uri);
//            Drawable drawable = Drawable.createFromStream(inputStream, uri.toString());
//            iv_show.setImageDrawable(drawable);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        Bitmap bitmap = BitmapFactory.decodeFile(fabuBean.getUri());
//        iv_show.setImageBitmap(bitmap);
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            // 如果已经有权限，设置图片
//            iv_show.setImageURI(uri);
//        } else {
//            // 请求权限
//            openDocument();
//        }
        iv_show.setImageURI(uri);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                getContentResolver().takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                );
                // 现在您可以使用这个URI来访问文件了
            }
        }
    }

    private int READ_REQUEST_CODE=1;
    private int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE=2;
    public void openDocument() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 权限被用户同意，可以做你需要做的事情了。
                openDocument();
            } else {
                // 权限被用户拒绝了，可以提示用户，关闭功能等操作。

                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        throw new IllegalStateException("Unexpected value: " + requestCode);
    }

    private void setListener(){
        tv_back.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.tv_back){
            finish();
        }
    }
}
