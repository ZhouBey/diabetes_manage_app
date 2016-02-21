package com.zpy.diabetes.app.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/2/21.
 */
public class AnswerPageBean extends AppBean {
    private PageInfo pageInfo;
    private List<AnswerBean> answerBeanList;
    public Integer replyCount;

    public Integer getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<AnswerBean> getAnswerBeanList() {
        return answerBeanList;
    }

    public void setAnswerBeanList(List<AnswerBean> answerBeanList) {
        this.answerBeanList = answerBeanList;
    }
}
