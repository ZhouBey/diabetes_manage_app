package com.zpy.diabetes.app.bean;

import java.util.List;

public class BloodSugarLogoPageBean extends AppBean {
    private List<BloodSugarLogBean> bloodSugarLogBeans;

    public List<BloodSugarLogBean> getBloodSugarLogBeans() {
        return bloodSugarLogBeans;
    }

    public void setBloodSugarLogBeans(List<BloodSugarLogBean> bloodSugarLogBeans) {
        this.bloodSugarLogBeans = bloodSugarLogBeans;
    }
}
