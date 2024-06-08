package com.example.application.uploadModel.bean;

public class FaBuBean {

    private Integer _id;
    private Integer userId;

    private String title;
    private String keys;
    private String text;

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FaBuBean(Integer _id, Integer userId, String title, String keys, String text, String uri) {
        this._id = _id;
        this.userId = userId;
        this.title = title;
        this.keys = keys;
        this.text = text;
        this.uri = uri;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    private String uri;

}
