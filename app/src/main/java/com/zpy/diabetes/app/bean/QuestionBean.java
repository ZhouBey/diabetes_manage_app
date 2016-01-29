package com.zpy.diabetes.app.bean;

import java.io.Serializable;

public class QuestionBean implements Serializable{
    private Integer id;
    private String title;
    private String content;
    private String suffererPhone;
    private String createD;
    private Integer replyCount;
    private String suffererPhoto;

    public String getSuffererPhoto() {
        return suffererPhoto;
    }

    public void setSuffererPhoto(String suffererPhoto) {
        this.suffererPhoto = suffererPhoto;
    }

    public Integer getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSuffererPhone() {
        return suffererPhone;
    }

    public void setSuffererPhone(String suffererPhone) {
        this.suffererPhone = suffererPhone;
    }

    public String getCreateD() {
        return createD;
    }

    public void setCreateD(String createD) {
        this.createD = createD;
    }
}
