package com.zpy.diabetes.app.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/2/21.
 */
public class AnswerBean implements Serializable {
    public Integer id;
    public String answerTime;
    public String answerContent;
    public String answerPhone;
    public String answerPhoto;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(String answerTime) {
        this.answerTime = answerTime;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public String getAnswerPhone() {
        return answerPhone;
    }

    public void setAnswerPhone(String answerPhone) {
        this.answerPhone = answerPhone;
    }

    public String getAnswerPhoto() {
        return answerPhoto;
    }

    public void setAnswerPhoto(String answerPhoto) {
        this.answerPhoto = answerPhoto;
    }
}
