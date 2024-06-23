package com.example.application.uploadModel.bean;

public class InsertParam {
    public FaBuBean getFaBuBean() {
        return faBuBean;
    }

    public void setFaBuBean(FaBuBean faBuBean) {
        this.faBuBean = faBuBean;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public InsertParam(FaBuBean faBuBean, String author) {
        this.faBuBean = faBuBean;
        this.author = author;
    }

    private FaBuBean faBuBean;
    private String author;

}
