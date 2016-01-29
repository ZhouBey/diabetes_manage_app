package com.zpy.diabetes.app.interf;


import com.zpy.diabetes.app.bean.AppBean;

public interface IAppUserTokenBeanHolder {
    void asynHold(AppBean bean);
    void overDue();
}
