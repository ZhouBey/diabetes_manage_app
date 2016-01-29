package com.zpy.diabetes.app.bean;

import java.util.List;

public class DoctorPageBean extends AppBean {
    private List<DoctorBean> doctorBeans;
    private PageInfo pageInfo;

    public List<DoctorBean> getDoctorBeans() {
        return doctorBeans;
    }

    public void setDoctorBeans(List<DoctorBean> doctorBeans) {
        this.doctorBeans = doctorBeans;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
