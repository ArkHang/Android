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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.application.R;
import com.example.application.showModel.bean.PingLunBean;
import com.example.application.showModel.callback.MyPLDiffCallback;
import com.example.application.showModel.sqlUtils.PLSQLHelper;
import com.example.application.uploadModel.bean.FaBuBean;
import com.example.application.uploadModel.sqlUtils.FaBuSQLHelper;
import com.example.utils.UtilsHelper;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView tv_author,tv_tittle,tv_keys,tv_js,tv_back;
    private ImageView iv_show;

    private Integer _id,userId;

    private Button btn_upload_pl;
    private EditText et_pl;
    private RecyclerView show_recyclerView;

    private List<PingLunBean> pllists=new ArrayList<>();
    private FaBuSQLHelper sqlHelper=new FaBuSQLHelper();

    private PLSQLHelper plsqlHelper=new PLSQLHelper();

    private MyAdapter myAdapter;

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
        show_recyclerView=findViewById(R.id.show_recyclervoew);
        et_pl=findViewById(R.id.et_pl);
        btn_upload_pl=findViewById(R.id.upload_pl);
        FaBuBean fabuBean = sqlHelper.findFaBuInfoByID(this, _id);
        userId= UtilsHelper.readUserId(this);
        tv_author.setText(sqlHelper.getUserName(this,fabuBean.getUserId()));
        tv_tittle.setText(fabuBean.getTitle());
        tv_keys.setText(fabuBean.getKeys());
        tv_js.setText(fabuBean.getText());
        Uri uri = Uri.parse(fabuBean.getUri());
        iv_show.setImageURI(uri);
        myAdapter=new MyAdapter();
        List<PingLunBean> pingLunBeans = plsqlHelper.queryPingLun(this, _id);
        for (PingLunBean bean:pingLunBeans) {
            pllists.add(bean);
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        show_recyclerView.setLayoutManager(layoutManager);
        show_recyclerView.setAdapter(myAdapter);

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
        btn_upload_pl.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id==R.id.tv_back){
            finish();
        }
        else if(id==R.id.upload_pl){
            uploadPinglun();

        }
    }

    private void insertNotification(String rq) {
        FaBuBean faBuBean = sqlHelper.findFaBuInfoByID(this, _id);
        String string="";
        String userName = sqlHelper.getUserName(this, userId);
        string="["+userName+"]"+"评论了["+faBuBean.getTitle()+"]!";
        plsqlHelper.saveNotification(this,faBuBean.getUserId(),string,rq);

    }

    private void uploadPinglun() {
        String pl=et_pl.getText().toString();
        if(pl==null||pl.equals("")){
            Toast.makeText(DetailActivity.this,"请输入内容",Toast.LENGTH_LONG).show();
            return;
        }
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String rq = format.format(date);
        boolean b = plsqlHelper.savePLInfo(this, userId, _id, pl,rq);
        if (b){
            Toast.makeText(DetailActivity.this,"发布成功",Toast.LENGTH_LONG).show();
            et_pl.setText("");
            myAdapter.updateDataList(plsqlHelper.queryPingLun(DetailActivity.this,_id));
            insertNotification(rq);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{


        public void updateDataList(List<PingLunBean> newDataList ){
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MyPLDiffCallback(pllists, newDataList));
            pllists.clear();
            pllists.addAll(newDataList);
            diffResult.dispatchUpdatesTo(this);
        }
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(DetailActivity.this, R.layout.pinglun_list, null);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            PingLunBean pingLunBean = pllists.get(position);
            holder.mAuthorTv.setText(sqlHelper.getUserName(DetailActivity.this,pingLunBean.getUserId()));
            holder.mContnt.setText(pingLunBean.getContent());
            holder.mdate.setText(pingLunBean.getRq());
        }

        @Override
        public int getItemCount() {
            return pllists.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView mAuthorTv;
        private TextView mContnt;
        private TextView mdate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mAuthorTv=itemView.findViewById(R.id.tv_pl_author);
            mContnt=itemView.findViewById(R.id.tv_content);
            mdate=itemView.findViewById(R.id.tv_rq);
        }
    }

}
