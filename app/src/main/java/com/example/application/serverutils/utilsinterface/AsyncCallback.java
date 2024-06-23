package com.example.application.serverutils.utilsinterface;

import com.example.application.uploadModel.bean.FaBuBean;

import java.util.List;

public interface AsyncCallback {
    void onSuccess(List<FaBuBean> list);

    void onFailure(Throwable throwable);
}
