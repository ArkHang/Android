package com.example.application.showModel.bean;

public class PingLunBean {
    private Integer _id;

    private Integer userId;
    private Integer fabuId;
    private String content;

    private String rq;

    public String getRq() {
        return rq;
    }

    public void setRq(String rq) {
        this.rq = rq;
    }

    public PingLunBean(Integer _id, Integer userId, Integer fabuId, String content, String rq) {
        this.userId = userId;
        this.fabuId = fabuId;
        this.content = content;
        this._id=_id;
        this.rq=rq;
    }

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

    public Integer getFabuId() {
        return fabuId;
    }

    public void setFabuId(Integer fabuId) {
        this.fabuId = fabuId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
