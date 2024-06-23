package com.example.application.showModel.bean;

public class Notification {
    private Integer _id;
    private Integer authorid;
    private String content;
    private String rq;

    public Notification(Integer _id, Integer authorid, String content, String rq) {
        this._id = _id;
        this.authorid = authorid;
        this.content = content;
        this.rq = rq;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public Integer getAuthorid() {
        return authorid;
    }

    public void setAuthorid(Integer authorid) {
        this.authorid = authorid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRq() {
        return rq;
    }

    public void setRq(String rq) {
        this.rq = rq;
    }
}
