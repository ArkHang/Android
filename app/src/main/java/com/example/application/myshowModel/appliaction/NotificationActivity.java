package com.example.application.myshowModel.appliaction;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;
import com.example.application.showModel.bean.Notification;
import com.example.application.showModel.sqlUtils.PLSQLHelper;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {
    
    private Integer id;
    private List<Notification> list=new ArrayList<>();

    private PLSQLHelper plsqlHelper=new PLSQLHelper();
    private MyAdapter myAdapter;

    private TextView tv_back;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        id=getIntent().getIntExtra("EXTRA_DATA",0);
        initActivity();
    }

    private void initActivity() {
        tv_back=findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);
        myAdapter=new MyAdapter();
        List<Notification> notifications = plsqlHelper.queryNotificationByAuthorId(this, id);
        list.addAll(notifications);
        recyclerView=findViewById(R.id.my_noti_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onClick(View v) {
        int id1 = v.getId();
        if(id1==R.id.tv_back){
            finish();
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(NotificationActivity.this, R.layout.notification_item_list, null);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Notification notification = list.get(position);
            holder.tv_rq.setText(notification.getRq());
            holder.tv_content.setText(notification.getContent());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_rq,tv_content;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_rq=itemView.findViewById(R.id.tv_noti_rq);
            tv_content=itemView.findViewById(R.id.tv_noti_content);
        }
    }
}
