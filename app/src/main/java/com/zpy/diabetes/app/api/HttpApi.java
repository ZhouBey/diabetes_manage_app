package com.zpy.diabetes.app.api;

import android.support.v4.widget.SwipeRefreshLayout;

import com.zpy.diabetes.app.App;
import com.zpy.diabetes.app.bean.AnswerPageBean;
import com.zpy.diabetes.app.bean.AppBean;
import com.zpy.diabetes.app.bean.DoctorBean;
import com.zpy.diabetes.app.bean.DoctorPageBean;
import com.zpy.diabetes.app.bean.HealthInfoPageBean;
import com.zpy.diabetes.app.bean.ImageQiniuBean;
import com.zpy.diabetes.app.bean.QuestionPageBean;
import com.zpy.diabetes.app.bean.ResultBean;
import com.zpy.diabetes.app.bean.SuffererBean;
import com.zpy.diabetes.app.bean.TokenBean;
import com.zpy.diabetes.app.config.AppConfig;
import com.zpy.diabetes.app.interf.IAppCommonBeanHolder;
import com.zpy.diabetes.app.interf.IAppUserTokenBeanHolder;
import com.zpy.diabetes.app.util.DataHoldUtil;
import com.zpy.diabetes.app.util.TextUtil;
import com.zpy.diabetes.app.widget.acpf.ACProgressFlower;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HttpApi {
    private App app;

    public HttpApi(App app) {
        this.app = app;
    }

    /**
     * @return true代表没有过期，false代表过期
     */
    public boolean isNotOverDue() {
        String expireIn = app.getShareDataStr(AppConfig.EXPIRE_IN);
        Date nowDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (!TextUtil.isEmpty(expireIn)) {
                Date expiresInDate = sdf.parse(expireIn);
                if (!nowDate.before(expiresInDate)) {
                    return false;
                }
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 患者注册
     *
     * @param phone
     * @param password
     * @param holder
     */
    public void registerForSufferer(String phone, String password, final ACProgressFlower dialog, final IAppCommonBeanHolder holder) {
        RequestParams params = new RequestParams(AppConfig.REGISTER_FOR_SUFFERER);
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("password", TextUtil.md5(password));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ResultBean resultBean = DataHoldUtil.getResultBean(result);
                holder.asynHold(resultBean);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                LogUtil.e(throwable.toString());
                holder.asynHold(null);
            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }


    /**
     * 患者登录
     *
     * @param phone
     * @param password
     * @param holder
     */
    public void loginForSufferer(String phone, String password, final ACProgressFlower dialog, final IAppCommonBeanHolder holder) {
        RequestParams params = new RequestParams(AppConfig.LOGIN_FOR_SUFFERER);
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("password", TextUtil.md5(password));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e(result);
                TokenBean tokenBean = DataHoldUtil.getTokenBean(result);
                holder.asynHold(tokenBean);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e(ex.toString());
                holder.asynHold(null);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }

    /**
     * 获取患者信息
     *
     * @param token
     * @param dialog
     * @param holder
     */
    public void getSuffererInfo(String token, final ACProgressFlower dialog, final IAppUserTokenBeanHolder holder) {
        RequestParams params = new RequestParams(AppConfig.GET_SUFFERER_INFO);
        params.addBodyParameter("token", token);
        if (isNotOverDue()) {
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    LogUtil.e(result);
                    SuffererBean suffererBean = DataHoldUtil.getSuffererBean(result);
                    holder.asynHold(suffererBean);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    holder.asynHold(null);
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
        } else {
            holder.overDue();
        }
    }

    /**
     * 获取健康资讯
     *
     * @param currentPage
     * @param holder
     */
    public void getHealthInfoList(int currentPage, final SwipeRefreshLayout swipeRefreshLayout, final IAppCommonBeanHolder holder) {
        RequestParams params = new RequestParams(AppConfig.GET_HEALTH_INFO_LIST);
        params.addBodyParameter("currentPage", String.valueOf(currentPage));
        params.addBodyParameter("showCount", String.valueOf(AppConfig.SHOW_COUNT));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                HealthInfoPageBean healthInfoBeanList = DataHoldUtil.getHealthInfoBeanList(result);
                holder.asynHold(healthInfoBeanList);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                holder.asynHold(null);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    /**
     * 获取首页信息
     *
     * @param token
     * @param dialog
     * @param holder
     */
    public void getIndexInfo(String token, final ACProgressFlower dialog, final IAppUserTokenBeanHolder holder) {
        RequestParams params = new RequestParams(AppConfig.GET_INDEX_INFO);
        if (!TextUtil.isEmpty(token) && isNotOverDue()) {
            params.addBodyParameter(AppConfig.TOKEN, token);
        }
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                AppBean appBean = DataHoldUtil.getIndexBeans(result);
                holder.asynHold(appBean);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                holder.asynHold(null);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }

    /**
     * 添加当天的血糖含量
     *
     * @param token
     * @param sugar_content
     * @param dialog
     * @param holder
     */
    public void addTodayBloodSugar(String token, Double sugar_content, final ACProgressFlower dialog, final IAppUserTokenBeanHolder holder) {
        if (isNotOverDue()) {
            RequestParams params = new RequestParams(AppConfig.ADD_TODAY_BLOOD_SUGAR_LOG);
            params.addBodyParameter(AppConfig.TOKEN, token);
            params.addBodyParameter("sugar_content", String.valueOf(sugar_content));
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    ResultBean resultBean = DataHoldUtil.getResultBean(result);
                    holder.asynHold(resultBean);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    holder.asynHold(null);
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
        } else {
            holder.overDue();
        }
    }

    /**
     * 更新患者资料
     *
     * @param token
     * @param key
     * @param value
     * @param dialog
     * @param holder
     */
    public void updateSuffererInfo(String token, String key, String value, final ACProgressFlower dialog, final IAppUserTokenBeanHolder holder) {
        if (isNotOverDue()) {
            RequestParams params = new RequestParams(AppConfig.UPDATE_SUFFERER_INFO);
            if (!TextUtil.isEmpty(token)) {
                params.addBodyParameter(AppConfig.TOKEN, token);
            }
            if (!TextUtil.isEmpty(key) && !TextUtil.isEmpty(value)) {
                params.addBodyParameter(key, value);
            }
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    ResultBean resultBean = DataHoldUtil.getResultBean(result);
                    holder.asynHold(resultBean);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    holder.asynHold(null);
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
        } else {
            holder.overDue();
        }
    }

    /**
     * 获取问答列表
     *
     * @param currentPage
     * @param holder
     */
    public void getQuestionList(int currentPage, final SwipeRefreshLayout swipeRefreshLayout, final IAppCommonBeanHolder holder) {
        RequestParams params = new RequestParams(AppConfig.GET_QUESTION_LIST);
        params.addBodyParameter("currentPage", String.valueOf(currentPage));
        params.addBodyParameter("showCount", String.valueOf(AppConfig.SHOW_COUNT));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                QuestionPageBean questionPageBean = DataHoldUtil.getQuestionPageBean(result);
                holder.asynHold(questionPageBean);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                holder.asynHold(null);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    /**
     * 提问
     *
     * @param token
     * @param title
     * @param content
     * @param dialog
     * @param holder
     */
    public void askQuestion(String token, String title, String content, final ACProgressFlower dialog, final IAppUserTokenBeanHolder holder) {
        if (isNotOverDue()) {
            RequestParams params = new RequestParams(AppConfig.ASK_QUESTION);
            params.addBodyParameter(AppConfig.TOKEN, token);
            params.addBodyParameter("title", title);
            params.addBodyParameter("content", content);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    ResultBean resultBean = DataHoldUtil.getResultBean(result);
                    holder.asynHold(resultBean);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    holder.asynHold(null);
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
        } else {
            holder.overDue();
        }
    }

    /**
     * 意见反馈
     *
     * @param token
     * @param content
     * @param dialog
     * @param holder
     */
    public void feedBack(String token, String content, final ACProgressFlower dialog, final IAppCommonBeanHolder holder) {
        RequestParams params = new RequestParams(AppConfig.FEED_BACK);
        params.addBodyParameter("content", content);
        if (isNotOverDue()) {
            params.addBodyParameter(AppConfig.TOKEN, token);
        } else {
            params.addBodyParameter(AppConfig.TOKEN, "");
        }
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ResultBean resultBean = DataHoldUtil.getResultBean(result);
                holder.asynHold(resultBean);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                holder.asynHold(null);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }

    /**
     * 获取我的问题
     *
     * @param requestUrl
     * @param token
     * @param currentPage
     * @param swipeRefreshLayout
     * @param holder
     */
    public void getMyQuestionList(String requestUrl, String token, int currentPage, final SwipeRefreshLayout swipeRefreshLayout, final IAppUserTokenBeanHolder holder) {
        if (isNotOverDue()) {
            RequestParams params = new RequestParams(requestUrl);
            params.addBodyParameter(AppConfig.TOKEN, token);
            params.addBodyParameter("currentPage", String.valueOf(currentPage));
            params.addBodyParameter("showCount", String.valueOf(AppConfig.SHOW_COUNT));
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    QuestionPageBean questionPageBean = DataHoldUtil.getQuestionPageBean(result);
                    holder.asynHold(questionPageBean);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    holder.asynHold(null);
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            });
        } else {
            holder.overDue();
        }
    }

    /**
     * 上传到七牛，得到图片的链接
     *
     * @param holder
     */
    public void prepareForUploadImage(final IAppCommonBeanHolder holder) {
        RequestParams params = new RequestParams(AppConfig.PREPARE_FOR_UPLOAD_PHOTO);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ImageQiniuBean bean = DataHoldUtil.getImageQiniuBean(result);
                holder.asynHold(bean);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                holder.asynHold(null);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 上传头像
     *
     * @param token
     * @param photoUrlQiniu
     * @param dialog
     * @param holder
     */
    public void afterUploadPhoto(String token, String photoUrlQiniu, final ACProgressFlower dialog, final IAppUserTokenBeanHolder holder) {
        if (isNotOverDue()) {
            RequestParams params = new RequestParams(AppConfig.AFTER_UPLOAD_PHOTO);
            params.addBodyParameter(AppConfig.TOKEN, token);
            params.addBodyParameter("photoUrlQiniu", photoUrlQiniu);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    ResultBean resultBean = DataHoldUtil.getResultBean(result);
                    holder.asynHold(resultBean);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    holder.asynHold(null);
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
        } else {
            holder.overDue();
        }
    }

    /**
     * 医生注册
     *
     * @param name
     * @param phone
     * @param password
     * @param hospital
     * @param certificate_image
     * @param info
     * @param post
     * @param sex
     * @param birthday
     * @param dialog
     * @param holder
     */
    public void doctorRegister(String name, String phone, String password, String hospital,
                               String certificate_image, String info, String post,
                               String sex, String birthday,
                               final ACProgressFlower dialog,
                               final IAppCommonBeanHolder holder) {
        RequestParams params = new RequestParams(AppConfig.REGISTER_FOR_DOCTOR);
        params.addBodyParameter("name", name);
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("password", TextUtil.md5(password));
        params.addBodyParameter("hospital", hospital);
        params.addBodyParameter("certificate_image", certificate_image);
        params.addBodyParameter("info", info);
        params.addBodyParameter("post", post);
        params.addBodyParameter("sex", sex);
        params.addBodyParameter("birthday", birthday);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ResultBean resultBean = DataHoldUtil.getResultBean(result);
                holder.asynHold(resultBean);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                holder.asynHold(null);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }

    /**
     * 医生登录
     *
     * @param phone
     * @param password
     * @param dialog
     * @param holder
     */
    public void loginForDoctor(String phone, String password, final ACProgressFlower dialog, final IAppCommonBeanHolder holder) {
        RequestParams params = new RequestParams(AppConfig.LOGIN_FOR_DOCTOR);
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("password", TextUtil.md5(password));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("loginForDoctor=" + result);
                TokenBean tokenBean = DataHoldUtil.getTokenBean(result);
                holder.asynHold(tokenBean);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("ex=" + String.valueOf(ex));
                holder.asynHold(null);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }

    /**
     * 获取医生的信息
     *
     * @param token
     * @param dialog
     * @param holder
     */
    public void getDoctorInfo(String token, final ACProgressFlower dialog, final IAppUserTokenBeanHolder holder) {
        if (isNotOverDue()) {
            RequestParams params = new RequestParams(AppConfig.GET_DOCTOR_INFO);
            params.addBodyParameter(AppConfig.TOKEN, token);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    LogUtil.e("getDoctorInfo=" + result);
                    DoctorBean doctorBean = DataHoldUtil.getDoctorBean(result);
                    holder.asynHold(doctorBean);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    holder.asynHold(null);
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
        } else {
            holder.overDue();
        }
    }

    /**
     * 获取所有的医生
     *
     * @param currentPage
     * @param swipeRefreshLayout
     * @param holder
     */
    public void getAllDoctorList(int currentPage, final SwipeRefreshLayout swipeRefreshLayout, final IAppCommonBeanHolder holder) {
        RequestParams params = new RequestParams(AppConfig.GET_ALL_DOCTOR);
        params.addBodyParameter("currentPage", String.valueOf(currentPage));
        params.addBodyParameter("showCount", String.valueOf(AppConfig.SHOW_COUNT));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                DoctorPageBean doctorPageBean = DataHoldUtil.getDoctorPageBean(result);
                holder.asynHold(doctorPageBean);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                holder.asynHold(null);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    /**
     * 医生回答
     *
     * @param token
     * @param questionId
     * @param content
     * @param dialog
     * @param holder
     */
    public void replyQuestion(String token, Integer questionId, String content, final ACProgressFlower dialog, final IAppUserTokenBeanHolder holder) {
        if (isNotOverDue()) {
            RequestParams params = new RequestParams(AppConfig.REPLY_QUESTION);
            params.addBodyParameter(AppConfig.TOKEN, token);
            params.addBodyParameter("questionId", String.valueOf(questionId));
            params.addBodyParameter("content", content);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    ResultBean resultBean = DataHoldUtil.getResultBean(result);
                    holder.asynHold(resultBean);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    holder.asynHold(null);
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
        } else {
            holder.overDue();
        }

    }

    /**
     * 获取问题的回复
     *
     * @param questionId
     * @param currentPage
     * @param dialog
     * @param holder
     */
    public void getAnswersForOneQuestion(Integer questionId, int currentPage, final ACProgressFlower dialog, final IAppCommonBeanHolder holder) {
        RequestParams params = new RequestParams(AppConfig.GET_ANSWERS_FOR_ONE_QUESTION);
        params.addBodyParameter("questionId", String.valueOf(questionId));
        params.addBodyParameter("currentPage", String.valueOf(currentPage));
        params.addBodyParameter("showCount", String.valueOf(AppConfig.SHOW_COUNT));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("result=" + result);
                AnswerPageBean answerPageBean = DataHoldUtil.getAnswerPageBean(result);
                holder.asynHold(answerPageBean);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                holder.asynHold(null);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }

    /**
     * 判断医生和患者时候已关注
     *
     * @param token
     * @param doctorId
     * @param dialog
     * @param holder
     */
    public void isAttention(String token, Integer doctorId, final ACProgressFlower dialog, final IAppUserTokenBeanHolder holder) {
        if (isNotOverDue()) {
            RequestParams params = new RequestParams(AppConfig.HAS_ATTENTION);
            params.addBodyParameter(AppConfig.TOKEN, token);
            params.addBodyParameter("doctorId", String.valueOf(doctorId));
            x.http().post(params, new Callback.CommonCallback<String>() {

                @Override
                public void onSuccess(String result) {
                    ResultBean bean = DataHoldUtil.getResultBean(result);
                    holder.asynHold(bean);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    holder.asynHold(null);
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
        } else {
            holder.overDue();
        }
    }

    /**
     * 关注医生
     *
     * @param token
     * @param doctorId
     */
    public void addDoctorPatient(String token, Integer doctorId, final ACProgressFlower dialog, final IAppUserTokenBeanHolder holder) {
        if (isNotOverDue()) {
            RequestParams params = new RequestParams(AppConfig.ADD_ATTENTION);
            params.addBodyParameter(AppConfig.TOKEN, token);
            params.addBodyParameter("doctorId", String.valueOf(doctorId));
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    ResultBean resultBean = DataHoldUtil.getResultBean(result);
                    holder.asynHold(resultBean);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    holder.asynHold(null);
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
        } else {
            holder.overDue();
        }
    }

    /**
     * 获取我关注的医生
     *
     * @param token
     * @param currentPage
     * @param refreshLayout
     * @param holder
     */
    public void getDoctorAttentionForSuffer(String token, int currentPage, final SwipeRefreshLayout refreshLayout, final IAppUserTokenBeanHolder holder) {
        if (isNotOverDue()) {
            RequestParams params = new RequestParams(AppConfig.GET_MY_ATTENTION_DOCTOR);
            params.addBodyParameter(AppConfig.TOKEN, token);
            params.addBodyParameter("currentPage", String.valueOf(currentPage));
            params.addBodyParameter("showCount", String.valueOf(AppConfig.SHOW_COUNT));
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    DoctorPageBean doctorPageBean = DataHoldUtil.getDoctorPageBean(result);
                    holder.asynHold(doctorPageBean);
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    LogUtil.e(ex.toString());
                    holder.asynHold(null);
                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {
                    if (refreshLayout != null && refreshLayout.isRefreshing()) {
                        refreshLayout.setRefreshing(false);
                    }
                }
            });
        } else {
            holder.overDue();
        }
    }
}
