package com.example.application.showModel.callback;

import androidx.recyclerview.widget.DiffUtil;

import com.example.application.showModel.bean.PingLunBean;
import com.example.application.uploadModel.bean.FaBuBean;

import java.util.List;

public class MyPLDiffCallback extends DiffUtil.Callback{
    private List<PingLunBean> oldDataList;


    private List<PingLunBean> newDataList;
    public MyPLDiffCallback(List<PingLunBean> oldDataList, List<PingLunBean> newDataList) {
        this.oldDataList=oldDataList;
        this.newDataList=newDataList;
    }

    @Override
    public int getOldListSize() {
        return oldDataList.size();
    }

    @Override
    public int getNewListSize() {
        return newDataList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldDataList.get(oldItemPosition).get_id().equals(newDataList.get(newItemPosition).get_id());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldDataList.get(oldItemPosition).equals(newDataList.get(newItemPosition));
    }
}
