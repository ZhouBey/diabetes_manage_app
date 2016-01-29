package com.zpy.diabetes.app.bean;

import java.util.List;

public class QuestionPageBean extends AppBean {
    private PageInfo pageInfo;
    private List<QuestionBean> questionBeans;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<QuestionBean> getQuestionBeans() {
        return questionBeans;
    }

    public void setQuestionBeans(List<QuestionBean> questionBeans) {
        this.questionBeans = questionBeans;
    }
}
