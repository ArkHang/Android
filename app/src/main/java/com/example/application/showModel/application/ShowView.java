package com.example.application.showModel.application;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;
import com.example.application.showModel.callback.MyDiffCallback;
import com.example.application.uploadModel.bean.FaBuBean;
import com.example.application.uploadModel.sqlUtils.FaBuSQLHelper;

import java.util.ArrayList;
import java.util.List;

public class ShowView extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    MyAdapter myAdapter;


    private static Activity mContext;
    private View mCurrentView;

    private EditText tv_title;
    private ImageButton ib_search;
    private LayoutInflater layoutInflater;
    private List<FaBuBean> lists=new ArrayList<FaBuBean>();

    private FaBuSQLHelper sqlHelper=new FaBuSQLHelper();

    public ShowView(Activity context){
        mContext=context;
        layoutInflater=LayoutInflater.from(mContext);
    }

    private void initView(){
        mCurrentView=layoutInflater.inflate(R.layout.activity_show,null);
        recyclerView=mCurrentView.findViewById(R.id.recyclerview);
        ib_search=mCurrentView.findViewById(R.id.search);
        tv_title=mCurrentView.findViewById(R.id.et_search_title);
        List<FaBuBean> faBuBeans = sqlHelper.queryFaBuInfo(mContext, "");
        for (FaBuBean bean: faBuBeans) {
            lists.add(bean);
        }
        myAdapter=new MyAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);

        ib_search.setOnClickListener(this);

        mCurrentView.setVisibility(View.VISIBLE);


    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if (id==R.id.search){
            String title = tv_title.getText().toString();
            selectList(title);
        }
    }

    private void selectList(String title) {
        List<FaBuBean> newDataLists = sqlHelper.queryFaBuInfo(mContext, title);
        myAdapter.updateDataList(newDataLists);

    }

    public View getView(){
        if (mCurrentView==null){
            initView();
        }
        return mCurrentView;
    }

    public void showView(){
        if (mCurrentView==null){
            initView();
        }
        else {
            List<FaBuBean> newDataLists = sqlHelper.queryFaBuInfo(mContext, "");
            myAdapter.updateDataList(newDataLists);
            mCurrentView.setVisibility(View.VISIBLE);
        }

    }


    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=View.inflate(mContext,R.layout.item_list,null);
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
            holder.mAuthorTv.setText(faBuBean.getKeys());
//            holder.mAuthorTv.setText(sqlHelper.getUserName(mContext,faBuBean.getUserId()));
        }


        @Override
        public int getItemCount() {
           return lists.size();
        }

        public void updateDataList(List<FaBuBean> newDataList){
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MyDiffCallback(lists, newDataList));
            lists.clear();
            lists.addAll(newDataList);
            diffResult.dispatchUpdatesTo(this);
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitleTv;
        private TextView mAuthorTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleTv=itemView.findViewById(R.id.tv_title);
            mAuthorTv=itemView.findViewById(R.id.tv_author);
        }
    }
}
