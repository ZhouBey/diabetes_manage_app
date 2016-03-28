package com.zpy.diabetes.app.interf;

import com.zpy.diabetes.app.bean.AppBean;

import java.util.List;

public interface IAppUserTokenBeansHolder {
    void asynHold(List<AppBean> appBeans);
    void overDue();
}
