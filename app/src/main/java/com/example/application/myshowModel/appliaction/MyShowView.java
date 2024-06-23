package com.example.application.myshowModel.appliaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;
import com.example.application.serverutils.ServerUtils;
import com.example.application.serverutils.utilsinterface.AsyncCallback;
import com.example.application.showModel.application.DetailActivity;
import com.example.application.uploadModel.bean.FaBuBean;
import com.example.application.uploadModel.sqlUtils.FaBuSQLHelper;
import com.example.utils.UtilsHelper;

import java.util.ArrayList;
import java.util.List;

public class MyShowView extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private FaBuSQLHelper sqlHelper = new FaBuSQLHelper();
    private int id;
    private View mCurrentView;

    private ServerUtils serverUtils=new ServerUtils();
    private LayoutInflater layoutInflater;
    private  static Activity mContext;
    private List<FaBuBean> lists = new ArrayList<>();
    public MyShowView(Activity context){
        mContext=context;
        layoutInflater=LayoutInflater.from(mContext);
        id= UtilsHelper.readUserId(mContext);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getView());
    }
    private void initView() {
        mCurrentView=layoutInflater.inflate(R.layout.activity_my_show,null);
        recyclerView=mCurrentView.findViewById(R.id.my_recyclerview);
        myAdapter=new MyAdapter();

        serverUtils.performAsyncOperation(new AsyncCallback() {
            @Override
            public void onSuccess(List<FaBuBean> list) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lists.clear();
                        lists.addAll(list);
                        myAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        },"myIndex",id+"");
        LinearLayoutManager layoutManager=new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);
        mCurrentView.setVisibility(View.VISIBLE);
    }
    public View getView() {
        if(mCurrentView==null){
            initView();
        }
        return mCurrentView;
    }

   public void showView(){
        if (mCurrentView==null){
            initView();
        }
        else {
            serverUtils.performAsyncOperation(new AsyncCallback() {
                @Override
                public void onSuccess(List<FaBuBean> list) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lists.clear();
                            lists.addAll(list);
                            myAdapter.notifyDataSetChanged();
                        }
                    });
                }

                @Override
                public void onFailure(Throwable throwable) {

                }
            },"myIndex",id+"");
            mCurrentView.setVisibility(View.VISIBLE);
        }
   }

    @Override
    public void onClick(View v) {

    }


    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_item_list, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("EXTRA_DATA",lists.get(adapterPosition).get_id());
                    mContext.startActivity(intent);
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            FaBuBean faBuBean = lists.get(position);
            holder.mTitleTv.setText(faBuBean.getTitle());
            holder.mKeys.setText(faBuBean.getKeys());
            holder.mZZTV.setText(sqlHelper.getUserName(mContext,faBuBean.getUserId()));
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("提示");
                    builder.setMessage("是否确定删除?");
                    Integer id = faBuBean.get_id();
                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            serverUtils.performAsyncOperation(new AsyncCallback() {
                                @Override
                                public void onSuccess(List<FaBuBean> list) {
                                }

                                @Override
                                public void onFailure(Throwable throwable) {

                                }
                            },"delete",id+"");
                            boolean b = sqlHelper.deleteById(mContext, id);
                            if (b){
                                Toast.makeText(mContext,"删除成功",Toast.LENGTH_LONG).show();
                                serverUtils.performAsyncOperation(new AsyncCallback() {
                                    @Override
                                    public void onSuccess(List<FaBuBean> list) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                lists.clear();
                                                lists.addAll(list);
                                                myAdapter.notifyDataSetChanged();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(Throwable throwable) {

                                    }
                                },"myIndex",id+"");
                            }
                            else {
                                Toast.makeText(mContext,"失败",Toast.LENGTH_LONG).show();
                            }


                        }

                    });
                    builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return lists.size();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView mTitleTv,mKeys,mZZTV;
        private ImageView iv_delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleTv=itemView.findViewById(R.id.tv_my_title);
            mKeys=itemView.findViewById(R.id.tv_my_keys);
            mZZTV=itemView.findViewById(R.id.tv_my_zz);
            iv_delete=itemView.findViewById(R.id.iv_my_delete);
        }
    }
}
