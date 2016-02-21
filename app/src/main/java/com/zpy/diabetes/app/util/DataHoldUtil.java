package com.zpy.diabetes.app.util;

import com.zpy.diabetes.app.bean.AnswerBean;
import com.zpy.diabetes.app.bean.AnswerPageBean;
import com.zpy.diabetes.app.bean.AppBean;
import com.zpy.diabetes.app.bean.BloodSugarLogBean;
import com.zpy.diabetes.app.bean.BloodSugarLogoPageBean;
import com.zpy.diabetes.app.bean.DoctorBean;
import com.zpy.diabetes.app.bean.DoctorPageBean;
import com.zpy.diabetes.app.bean.HealthInfoBean;
import com.zpy.diabetes.app.bean.HealthInfoPageBean;
import com.zpy.diabetes.app.bean.ImageQiniuBean;
import com.zpy.diabetes.app.bean.PageInfo;
import com.zpy.diabetes.app.bean.QuestionBean;
import com.zpy.diabetes.app.bean.QuestionPageBean;
import com.zpy.diabetes.app.bean.ResultBean;
import com.zpy.diabetes.app.bean.SuffererBean;
import com.zpy.diabetes.app.bean.TokenBean;
import com.zpy.diabetes.app.config.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/20 0020.
 */
public class DataHoldUtil {


    /**
     * 解析是否成功
     *
     * @param result
     * @return
     */
    public static ResultBean getResultBean(String result) {
        if (result == null) return null;
        ResultBean bean = null;
        try {
            JSONObject object = new JSONObject(result);
            String code = object.optString(AppConfig.CODE);
            String msg = object.optString(AppConfig.MSG);
            bean = new ResultBean();
            bean.setCode(code);
            bean.setMsg(msg);
            JSONObject data = object.optJSONObject(AppConfig.DATA);
            String dataCode = data.optString("code", "");
            String orderNum = data.optString("orderNum", "");
            if (!"".equals(dataCode)) {
                bean.setData(dataCode);
            }
            if (!"".equals(orderNum)) {
                bean.setData(orderNum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    /**
     * 获取token信息
     *
     * @param result
     * @return
     */
    public static TokenBean getTokenBean(String result) {
        if (result == null) return null;
        TokenBean bean = null;
        try {
            JSONObject object = new JSONObject(result);
            String code = object.optString(AppConfig.CODE);
            String msg = object.optString(AppConfig.MSG);
            bean = new TokenBean();
            bean.setCode(code);
            bean.setMsg(msg);
            JSONObject data = object.optJSONObject(AppConfig.DATA);
            JSONObject appToken = data.optJSONObject("appToken");
            bean.setToken(appToken.optString("token"));
            bean.setUserId(appToken.optString("userId"));
            String duration = appToken.optString("duration");
            int expires_in = Integer.parseInt(duration);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date nowDate = new Date();
            Date expiresDate = new Date(nowDate.getTime() + expires_in * 1000);
            String expiresIn = sdf.format(expiresDate);
            bean.setExpireIn(expiresIn);
            bean.setRoleType(appToken.optInt("roleType"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    /**
     * 解析患者信息
     *
     * @param result
     * @return
     */
    public static SuffererBean getSuffererBean(String result) {
        if (result == null) return null;
        SuffererBean bean = null;
        try {
            JSONObject object = new JSONObject(result);
            String code = object.optString(AppConfig.CODE);
            String msg = object.optString(AppConfig.MSG);
            bean = new SuffererBean();
            bean.setCode(code);
            bean.setMsg(msg);
            JSONObject data = object.optJSONObject(AppConfig.DATA);
            JSONObject suffererObj = data.optJSONObject("sufferer");
            bean.setName(suffererObj.optString("name"));
            bean.setPhone(suffererObj.optString("phone"));
            bean.setSex(suffererObj.optInt("sex"));
            bean.setPhoto(suffererObj.optString("photo"));
            bean.setBirthday(TextUtil.getTimeStr(suffererObj.optLong("birthday")));
            bean.setSufferedDate(TextUtil.getTimeStr(suffererObj.optLong("sufferedDate")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    public static HealthInfoPageBean getHealthInfoBeanList(String result) {
        if (result == null) return null;
        HealthInfoPageBean healthInfoPageBean = null;
        try {
            JSONObject object = new JSONObject(result);
            String code = object.optString(AppConfig.CODE);
            String msg = object.optString(AppConfig.MSG);
            healthInfoPageBean = new HealthInfoPageBean();
            healthInfoPageBean.setCode(code);
            healthInfoPageBean.setMsg(msg);
            JSONObject data = object.optJSONObject(AppConfig.DATA);
            JSONObject pageInfoObj = data.optJSONObject("pageInfo");
            PageInfo pageInfo = new PageInfo();
            pageInfo.setTotalPage(pageInfoObj.optInt("totalPage"));
            pageInfo.setCurrentPage(pageInfoObj.optInt("currentPage"));
            healthInfoPageBean.setPageInfo(pageInfo);
            JSONArray healthInfos = data.optJSONArray("healthInfos");
            List<HealthInfoBean> healthInfoBeans = new ArrayList<>();
            for (int i = 0; i < healthInfos.length(); i++) {
                HealthInfoBean healthInfoBean = new HealthInfoBean();
                JSONObject item = healthInfos.getJSONObject(i);
                healthInfoBean.setTitle(item.optString("title"));
                healthInfoBean.setMsg(item.optString("msg"));
                healthInfoBean.setInfoImage(item.optString("infoImage"));
                healthInfoBean.setCreateD(TextUtil.getTimeStr(item.optLong("createD")));
                healthInfoBeans.add(healthInfoBean);
            }
            healthInfoPageBean.setHealthInfoBeans(healthInfoBeans);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return healthInfoPageBean;
    }

    public static AppBean getIndexBeans(String result) {
        if (result == null) return null;
        AppBean appBean = null;
        try {
            JSONObject object = new JSONObject(result);
            String code = object.optString(AppConfig.CODE);
            String msg = object.optString(AppConfig.MSG);
            appBean = new AppBean();
            appBean.setCode(code);
            appBean.setMsg(msg);
            Map<String, AppBean> map = new HashMap<>();
            HealthInfoPageBean healthInfoPageBean = new HealthInfoPageBean();
            JSONObject data = object.optJSONObject(AppConfig.DATA);
            JSONArray healthInfos = data.optJSONArray("healthInfos");
            List<HealthInfoBean> healthInfoBeans = new ArrayList<>();
            for (int i = 0; i < healthInfos.length(); i++) {
                HealthInfoBean healthInfoBean = new HealthInfoBean();
                JSONObject item = healthInfos.getJSONObject(i);
                healthInfoBean.setTitle(item.optString("title"));
                healthInfoBean.setMsg(item.optString("msg"));
                healthInfoBean.setInfoImage(item.optString("infoImage"));
                healthInfoBean.setCreateD(TextUtil.getTimeStr(item.optLong("createD")));
                healthInfoBeans.add(healthInfoBean);
            }
            healthInfoPageBean.setHealthInfoBeans(healthInfoBeans);
            map.put("healthInfoPageBean", healthInfoPageBean);

            JSONArray weekOfBloodSugarLogsArr = data.optJSONArray("weekOfBloodSugarLogs");
            if (weekOfBloodSugarLogsArr != null) {
                BloodSugarLogoPageBean bloodSugarLogoPageBean = new BloodSugarLogoPageBean();
                List<BloodSugarLogBean> bloodSugarLogBeans = new ArrayList<>();
                for (int i = 0; i < weekOfBloodSugarLogsArr.length(); i++) {
                    BloodSugarLogBean bloodSugarLogBean = new BloodSugarLogBean();
                    JSONObject item = weekOfBloodSugarLogsArr.getJSONObject(i);
                    bloodSugarLogBean.setSugarContent(item.optDouble("sugarContent"));
                    bloodSugarLogBean.setCreateD(TextUtil.getTimeStr(item.optLong("createD")));
                    bloodSugarLogBeans.add(bloodSugarLogBean);
                }
                bloodSugarLogoPageBean.setBloodSugarLogBeans(bloodSugarLogBeans);
                map.put("bloodSugarLogoPageBean", bloodSugarLogoPageBean);
            }


            BloodSugarLogBean bloodSugarLogBeanToday = new BloodSugarLogBean();
            JSONObject sugarLogObj = data.optJSONObject("sugarLog");
            if (sugarLogObj != null) {
                bloodSugarLogBeanToday.setSugarContent(sugarLogObj.optDouble("sugarContent"));
                bloodSugarLogBeanToday.setCreateD(TextUtil.getTimeStr(sugarLogObj.optLong("createD")));
                //第三个加载今日血糖
                map.put("bloodSugarLogBeanToday", bloodSugarLogBeanToday);
            }
            appBean.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appBean;
    }

    public static QuestionPageBean getQuestionPageBean(String result) {
        if (result == null) return null;
        QuestionPageBean questionPageBean = null;
        try {
            JSONObject object = new JSONObject(result);
            String code = object.optString(AppConfig.CODE);
            String msg = object.optString(AppConfig.MSG);
            questionPageBean = new QuestionPageBean();
            questionPageBean.setCode(code);
            questionPageBean.setMsg(msg);
            JSONObject data = object.optJSONObject(AppConfig.DATA);
            JSONObject pageInfoObj = data.optJSONObject("pageInfo");
            PageInfo pageInfo = new PageInfo();
            pageInfo.setTotalPage(pageInfoObj.optInt("totalPage"));
            pageInfo.setCurrentPage(pageInfoObj.optInt("currentPage"));
            questionPageBean.setPageInfo(pageInfo);
            JSONArray questionArr = data.optJSONArray("questionListPage");
            List<QuestionBean> questionBeans = new ArrayList<>();
            for (int i = 0; i < questionArr.length(); i++) {
                QuestionBean questionBean = new QuestionBean();
                JSONObject item = questionArr.getJSONObject(i);
                JSONObject questionObj = item.optJSONObject("question");
                questionBean.setId(questionObj.optInt("id"));
                questionBean.setContent(questionObj.optString("content"));
                questionBean.setTitle(questionObj.optString("title"));
                questionBean.setCreateD(TextUtil.getTimeStr(questionObj.optLong("createD")));
                questionBean.setReplyCount(item.optInt("reply_count"));
                JSONObject suffererObj = item.optJSONObject("sufferer");
                if (suffererObj != null) {
                    questionBean.setSuffererPhone(suffererObj.optString("phone"));
                    questionBean.setSuffererPhoto(suffererObj.optString("photo"));
                }
                questionBeans.add(questionBean);
            }
            questionPageBean.setQuestionBeans(questionBeans);
        } catch (Exception e) {
            LogUtil.e(e.toString());
            e.printStackTrace();
        }
        return questionPageBean;
    }

    public static ImageQiniuBean getImageQiniuBean(String result) {
        if (result == null) return null;
        ImageQiniuBean bean = null;
        try {
            JSONObject object = new JSONObject(result);
            String code = object.optString(AppConfig.CODE);
            String msg = object.optString(AppConfig.MSG);
            bean = new ImageQiniuBean();
            bean.setCode(code);
            bean.setMsg(msg);
            JSONObject data = object.optJSONObject(AppConfig.DATA);
            bean.setQiniuKey(data.optString("key"));
            bean.setQiniuToken(data.optString("token"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    public static DoctorBean getDoctorBean(String result) {
        if (result == null) return null;
        DoctorBean doctorBean = null;
        try {
            JSONObject object = new JSONObject(result);
            String code = object.optString(AppConfig.CODE);
            String msg = object.optString(AppConfig.MSG);
            doctorBean = new DoctorBean();
            doctorBean.setCode(code);
            doctorBean.setMsg(msg);
            JSONObject data = object.optJSONObject(AppConfig.DATA);
            JSONObject doctorObj = data.optJSONObject("doctor");
            doctorBean.setId(doctorObj.optInt("id"));
            doctorBean.setName(doctorObj.optString("name"));
            doctorBean.setPhone(doctorObj.optString("phone"));
            doctorBean.setSex(doctorObj.optInt("sex"));
            doctorBean.setBirthday(TextUtil.getTimeStr(doctorObj.optLong("birthday")));
            doctorBean.setHospital(doctorObj.optString("hospital"));
            doctorBean.setPhoto(doctorObj.optString("photo"));
            doctorBean.setCertificateImage(doctorObj.optString("certificateImage"));
            doctorBean.setIsActivate(doctorObj.optInt("isActivate"));
            doctorBean.setInfo(doctorObj.optString("info"));
            doctorBean.setPost(doctorObj.optString("post"));
        } catch (Exception e) {
            LogUtil.e(String.valueOf(e));
            e.printStackTrace();
        }
        return doctorBean;
    }

    public static DoctorPageBean getDoctorPageBean(String result) {
        if (TextUtil.isEmpty(result)) return null;
        DoctorPageBean doctorPageBean = null;
        try {
            JSONObject object = new JSONObject(result);
            String code = object.optString(AppConfig.CODE);
            String msg = object.optString(AppConfig.MSG);
            doctorPageBean = new DoctorPageBean();
            doctorPageBean.setCode(code);
            doctorPageBean.setMsg(msg);
            JSONObject data = object.optJSONObject(AppConfig.DATA);
            JSONObject pageInfoObj = data.optJSONObject("pageInfo");
            PageInfo pageInfo = new PageInfo();
            pageInfo.setCurrentPage(pageInfoObj.optInt("currentPage"));
            pageInfo.setShowCount(pageInfoObj.optInt("showCount"));
            pageInfo.setTotalPage(pageInfoObj.optInt("totalPage"));
            pageInfo.setTotalResult(pageInfoObj.optInt("totalResult"));
            doctorPageBean.setPageInfo(pageInfo);
            JSONArray doctorsArr = data.optJSONArray("doctors");
            List<DoctorBean> doctorBeans = new ArrayList<>();
            for (int i = 0; i < doctorsArr.length(); i++) {
                DoctorBean doctorBean = new DoctorBean();
                JSONObject doctorObj = doctorsArr.optJSONObject(i);
                doctorBean.setId(doctorObj.optInt("id"));
                doctorBean.setName(doctorObj.optString("name"));
                doctorBean.setPhone(doctorObj.optString("phone"));
                doctorBean.setSex(doctorObj.optInt("sex"));
                doctorBean.setBirthday(TextUtil.getTimeStr(doctorObj.optLong("birthday")));
                doctorBean.setHospital(doctorObj.optString("hospital"));
                doctorBean.setPhoto(doctorObj.optString("photo"));
                doctorBean.setCertificateImage(doctorObj.optString("certificateImage"));
                doctorBean.setIsActivate(doctorObj.optInt("isActivate"));
                doctorBean.setInfo(doctorObj.optString("info"));
                doctorBean.setPost(doctorObj.optString("post"));
                doctorBeans.add(doctorBean);
            }
            doctorPageBean.setDoctorBeans(doctorBeans);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return doctorPageBean;
    }

    public static AnswerPageBean getAnswerPageBean(String result) {
        if (TextUtil.isEmpty(result)) return null;
        AnswerPageBean pageBean = null;
        try {
            JSONObject object = new JSONObject(result);
            String code = object.optString(AppConfig.CODE);
            String msg = object.optString(AppConfig.MSG);
            pageBean = new AnswerPageBean();
            pageBean.setCode(code);
            pageBean.setMsg(msg);
            JSONObject dataObj = object.optJSONObject(AppConfig.DATA);
            JSONObject pageInfoObj = dataObj.optJSONObject("pageInfo");
            PageInfo pageInfo = new PageInfo();
            pageInfo.setCurrentPage(pageInfoObj.optInt("currentPage"));
            pageInfo.setShowCount(pageInfoObj.optInt("showCount"));
            pageInfo.setTotalPage(pageInfoObj.optInt("totalPage"));
            pageInfo.setTotalResult(pageInfoObj.optInt("totalResult"));
            pageBean.setPageInfo(pageInfo);
            List<AnswerBean> answerBeanList = new ArrayList<>();
            JSONArray array = dataObj.optJSONArray("list");
            for (int i = 0; i < array.length(); i++) {
                JSONObject itemObj = array.optJSONObject(i);
                AnswerBean answerBean = new AnswerBean();
                JSONObject answerObj = itemObj.optJSONObject("answer");
                JSONObject doctorObj = itemObj.optJSONObject("doctor");
                answerBean.setId(answerObj.optInt("id"));
                answerBean.setAnswerPhoto(doctorObj.optString("photo"));
                answerBean.setAnswerContent(answerObj.optString("content"));
                answerBean.setAnswerPhone(doctorObj.optString("phone"));
                answerBean.setAnswerTime(TextUtil.getTimeStr(answerObj.optLong("createD")));
                answerBeanList.add(answerBean);
            }
            pageBean.setAnswerBeanList(answerBeanList);
            Integer replyCount = dataObj.optInt("reply_count");
            pageBean.setReplyCount(replyCount);
            return pageBean;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
