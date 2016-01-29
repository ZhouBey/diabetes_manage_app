package com.zpy.diabetes.app.bean;

public class HealthInfoBean extends AppBean {
    private Integer id;
    private String title;
    private String msg;
    private String infoImage;
    private String createD;

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

    @Override
    public String getMsg() {
        return msg;
    }

    @Override
    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getInfoImage() {
        return infoImage;
    }

    public void setInfoImage(String infoImage) {
        this.infoImage = infoImage;
    }

    public String getCreateD() {
        return createD;
    }

    public void setCreateD(String createD) {
        this.createD = createD;
    }
}
