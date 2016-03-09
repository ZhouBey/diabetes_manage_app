package com.zpy.diabetes.app.config;

public class AppConfig {
    public static final int BIRTHDAY_SELECT = 0;
    public static final int SUFFER_DATE_SELECT = 1;


    public static final int ROLE_TYPE_FOR_SUFFERER = 0;
    public static final int ROLE_TYPE_FOR_DOCTOR = 1;

    public static final int LOGIN_OK_RESULT = 200;

    public static final int BANNER_COUNT = 2;

    // 短信SDK的APPKEY
    public static String SMS_APPKEY = "1010109384f7b";
    // 短信SDK的APPSECRET
    public static String SMS_APPSECRET = "cbc18a246c2a1de6d6935a33286e141e";
    public static final int READ_MSG = 1;
    public static final int DIRECT_CLOCKWISE = 100;
    public static final int REFRESH_QA_LIST = 119;
    public static final String REFRESH_ACCOUNT_ACTION = "refresh_account_action";

    //主机名
//    public static final String HOST = "http://tangzhushou.wicp.net:20162";
//    public static final String HOST = "http://139.129.34.235:8080/diabetesmanage";
    public static final String HOST = "http://192.168.1.118:8080";
    //    public static final String HOST = "http://10.20.96.10:8080";
//    public static final String HOST = "http://192.168.0.100:8080";
    //患者注册
    public static final String REGISTER_FOR_SUFFERER = HOST + "/suffererApi/suffererRegister";
    //患者登录
    public static final String LOGIN_FOR_SUFFERER = HOST + "/suffererApi/suffererLogin";
    //获取患者信息
    public static final String GET_SUFFERER_INFO = HOST + "/suffererApi/getSuffererInfo";
    //获取健康资讯
    public static final String GET_HEALTH_INFO_LIST = HOST + "/healthInfoApi/getHealthInfoList";
    //首页信息
    public static final String GET_INDEX_INFO = HOST + "/indexApi/getIndexInfo";
    //患者添加当天的血糖记录
    public static final String ADD_TODAY_BLOOD_SUGAR_LOG = HOST + "/suffererApi/addBloodSugarLog";
    //更新患者资料
    public static final String UPDATE_SUFFERER_INFO = HOST + "/suffererApi/updateSuffererInfo";
    //问答列表
    public static final String GET_QUESTION_LIST = HOST + "/questionApi/getQuestionList";
    //获取患者的问题列表
    public static final String GET_MY_QUESTION_LIST = HOST + "/questionApi/getMyQuestions";
    //获取医生的回复问题列表
    public static final String GET_MY_REPLY_LIST = HOST + "/doctorApi/getOneDoctorReply";
    //提问题
    public static final String ASK_QUESTION = HOST + "/questionApi/askQuestion";
    //意见反馈
    public static final String FEED_BACK = HOST + "/opinionApi/addOpinion";
    //上传头像的准备工作
    public static final String PREPARE_FOR_UPLOAD_PHOTO = HOST + "/qiniuApi/prepareForUploadImage";
    //上传头像后的工作
    public static final String AFTER_UPLOAD_PHOTO = HOST + "/qiniuApi/afterUploadImage";
    //医生注册
    public static final String REGISTER_FOR_DOCTOR = HOST + "/doctorApi/doctorRegister";
    //医生登录
    public static final String LOGIN_FOR_DOCTOR = HOST + "/doctorApi/doctorLogin";
    //获取医生信息
    public static final String GET_DOCTOR_INFO = HOST + "/doctorApi/getDoctorInfo";
    //医生列表
    public static final String GET_ALL_DOCTOR = HOST + "/doctorApi/getDoctorsListPage";
    //医生回复患者提问
    public static final String REPLY_QUESTION = HOST + "/doctorApi/replyQuestion";
    //获取问题所对应的答案
    public static final String GET_ANSWERS_FOR_ONE_QUESTION = HOST + "/questionApi/getAnswersForOneQuestion";
    //判断医生和患者是否已关注
    public static final String HAS_ATTENTION = HOST + "/doctorPatientApi/isAttention";
    //关注医生
    public static final String ADD_ATTENTION = HOST + "/doctorPatientApi/addDoctorPatient";
    //获取我关注的医生
    public static final String GET_MY_ATTENTION_DOCTOR = HOST + "/doctorPatientApi/getDoctorAttentionForSuffer";
    //查询医生
    public static final String GET_SEARCH_DOCTORS = HOST + "/doctorApi/getSearchDoctors";

    public static final String EXPIRE_IN = "expire_in";
    public static final String TOKEN = "token";
    public static final String ROLE_TYPE = "role_type";
    public static final String PHONE = "phone";
    public static final String PHOTO = "photo";
    public static final String OK = "OK";
    public static final String CODE = "code";
    public static final String MSG = "msg";
    public static final String DATA = "data";
    public static final String INTERNET_ERR = "网络请求失败";
    public static final String NO_INTERNET = "请检查网络";
    public static final int SHOW_COUNT = 15;
    public static final String QINIU_IMAGE_URL = "http://7xo971.com1.z0.glb.clouddn.com/";
    public static final int CROP = 2;
    public static final int CROP_PICTURE = 3;
    public static final int IS_MINE = 100;
}
