package com.zpy.diabetes.app.interf;

import com.zpy.diabetes.app.bean.AppBean;

import java.util.List;

public interface IAppUserTokenBeansHolder {
    public abstract void asynHold(List<AppBean> appBeans);
    public abstract void overDue();
}
