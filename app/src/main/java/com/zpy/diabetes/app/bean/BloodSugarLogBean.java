package com.zpy.diabetes.app.bean;

public class BloodSugarLogBean extends AppBean{
    private Integer id;
    private Integer suffererId;
    private Double sugarContent;
    private String createD;

    public String getCreateD() {
        return createD;
    }

    public void setCreateD(String createD) {
        this.createD = createD;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSuffererId() {
        return suffererId;
    }

    public void setSuffererId(Integer suffererId) {
        this.suffererId = suffererId;
    }

    public Double getSugarContent() {
        return sugarContent;
    }

    public void setSugarContent(Double sugarContent) {
        this.sugarContent = sugarContent;
    }

}
