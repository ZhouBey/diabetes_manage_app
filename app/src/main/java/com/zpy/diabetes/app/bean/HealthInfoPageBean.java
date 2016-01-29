package com.zpy.diabetes.app.bean;

import java.util.List;


public class HealthInfoPageBean extends AppBean {
    private PageInfo pageInfo;
    private List<HealthInfoBean> healthInfoBeans;
    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<HealthInfoBean> getHealthInfoBeans() {
        return healthInfoBeans;
    }

    public void setHealthInfoBeans(List<HealthInfoBean> healthInfoBeans) {
        this.healthInfoBeans = healthInfoBeans;
    }
}
