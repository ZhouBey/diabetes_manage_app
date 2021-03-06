package com.zpy.diabetes.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.bean.AppBean;
import com.zpy.diabetes.app.bean.DoctorBean;
import com.zpy.diabetes.app.bean.SuffererBean;
import com.zpy.diabetes.app.config.AppConfig;
import com.zpy.diabetes.app.interf.BaseUIInterf;
import com.zpy.diabetes.app.interf.IAppUserTokenBeanHolder;
import com.zpy.diabetes.app.ui.DoctorInfoActivity;
import com.zpy.diabetes.app.ui.FeedBackActivity;
import com.zpy.diabetes.app.ui.LoginActivity;
import com.zpy.diabetes.app.ui.MainActivity;
import com.zpy.diabetes.app.ui.my.MyAccountActivity;
import com.zpy.diabetes.app.ui.my.MyDoctorAttentionActivity;
import com.zpy.diabetes.app.ui.my.MyParticipateActivity;
import com.zpy.diabetes.app.util.ActivityUtil;
import com.zpy.diabetes.app.util.TextUtil;
import com.zpy.diabetes.app.widget.ConfirmationDialog;
import com.zpy.diabetes.app.widget.acpf.ACProgressFlower;

import org.xutils.image.ImageOptions;
import org.xutils.x;

public class UserFragment extends Fragment implements BaseUIInterf, View.OnClickListener {
    private View rootView;
    private CircularImageView image_user_photo;
    private RelativeLayout layout_user_my_account,
            layout_user_my_question,
            layout_user_my_doctor,
            layout_user_feed_back,
            layout_user_call_us;
    private MainActivity activity;
    private TextView tv_user_phone,
            tv_user_about_question,
            tv_my_doctor_or_sufferer;
    private ACProgressFlower loadingDialog;
    private String token;
    private SuffererBean suffererBean;
    private DoctorBean doctorBean;
    private ImageView image_user_about_question;

    View view_user_fragment;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.user_fragment, container, false);
        } else {
            ViewGroup viewGroup = (ViewGroup) rootView.getParent();
            viewGroup.removeView(rootView);
        }
        init();
        show();
        return rootView;
    }

    @Override
    public void init() {
        activity = (MainActivity) getActivity();
        image_user_photo = (CircularImageView) rootView.findViewById(R.id.image_user_photo);
        image_user_photo.setOnClickListener(this);
        layout_user_my_account = (RelativeLayout) rootView.findViewById(R.id.layout_user_my_account);
        layout_user_my_account.setOnClickListener(this);
        layout_user_my_question = (RelativeLayout) rootView.findViewById(R.id.layout_user_my_question);
        layout_user_my_question.setOnClickListener(this);
        layout_user_my_doctor = (RelativeLayout) rootView.findViewById(R.id.layout_user_my_doctor);
        layout_user_my_doctor.setOnClickListener(this);
        layout_user_feed_back = (RelativeLayout) rootView.findViewById(R.id.layout_user_feed_back);
        layout_user_feed_back.setOnClickListener(this);
        layout_user_call_us = (RelativeLayout) rootView.findViewById(R.id.layout_user_call_us);
        layout_user_call_us.setOnClickListener(this);
        tv_user_phone = (TextView) rootView.findViewById(R.id.tv_user_phone);
        loadingDialog = ActivityUtil.getLoadingDialog(activity);
        tv_user_about_question = (TextView) rootView.findViewById(R.id.tv_user_about_question);
        image_user_about_question = (ImageView) rootView.findViewById(R.id.image_user_about_question);
        tv_my_doctor_or_sufferer = (TextView) rootView.findViewById(R.id.tv_my_doctor_or_sufferer);
        view_user_fragment = rootView.findViewById(R.id.view_user_fragment);
    }

    @Override
    public void show() {
        getUserInfo();
    }

    public void getUserInfo() {
        int roleType = activity.getApp().getShareDataInt(AppConfig.ROLE_TYPE);
        token = activity.getApp().getShareDataStr(AppConfig.TOKEN);
        if (!TextUtil.isEmpty(token)) {
            loadingDialog.show();
            if (roleType == AppConfig.ROLE_TYPE_FOR_SUFFERER) {
                activity.getApp().getHttpApi().getSuffererInfo(token, loadingDialog, new IAppUserTokenBeanHolder() {
                    @Override
                    public void asynHold(AppBean bean) {
                        if (bean != null) {
                            suffererBean = (SuffererBean) bean;
                            if (AppConfig.OK.equals(suffererBean.getCode())) {
                                activity.getApp().setShareData(AppConfig.PHONE, suffererBean.getPhone());
                                tv_user_phone.setText(suffererBean.getPhone());
                                tv_user_about_question.setText("我的提问");
                                image_user_about_question.setImageResource(R.mipmap.icon_my_account_question);
                                tv_my_doctor_or_sufferer.setText("我的关注");
                                layout_user_my_doctor.setVisibility(View.VISIBLE);
                                view_user_fragment.setVisibility(View.VISIBLE);
                                x.image().bind(image_user_photo, AppConfig.QINIU_IMAGE_URL + suffererBean.getPhoto(), new ImageOptions.Builder().setLoadingDrawableId(R.mipmap.img_default_photo_blue).setFailureDrawableId(R.mipmap.img_default_photo_blue).build());
                                activity.getHomeFragment().show();
                            } else {
                                Toast.makeText(activity, suffererBean.getMsg(), Toast.LENGTH_SHORT).show();
                                activity.getApp().setShareData(AppConfig.TOKEN, null);
                            }
                        } else {
                            ActivityUtil.loadError(activity);
                        }
                    }

                    @Override
                    public void overDue() {
                        ActivityUtil.overdue(activity, loadingDialog, false);
                    }
                });
            }
            if (roleType == AppConfig.ROLE_TYPE_FOR_DOCTOR) {
                activity.getApp().getHttpApi().getDoctorInfoByToken(token, loadingDialog, new IAppUserTokenBeanHolder() {
                    @Override
                    public void asynHold(AppBean bean) {
                        if (bean != null) {
                            doctorBean = (DoctorBean) bean;
                            if (AppConfig.OK.equals(doctorBean.getCode())) {
                                activity.getApp().setShareData(AppConfig.PHONE, doctorBean.getPhone());
                                tv_user_phone.setText(doctorBean.getPhone());
                                tv_user_about_question.setText("我的回复");
                                image_user_about_question.setImageResource(R.mipmap.icon_my_account_question);
                                layout_user_my_doctor.setVisibility(View.GONE);
                                view_user_fragment.setVisibility(View.GONE);
                                x.image().bind(image_user_photo, AppConfig.QINIU_IMAGE_URL + doctorBean.getPhoto(), new ImageOptions.Builder().setLoadingDrawableId(R.mipmap.img_default_photo_blue).setFailureDrawableId(R.mipmap.img_default_photo_blue).build());
                                activity.getHomeFragment().show();
                            } else {
                                Toast.makeText(activity, doctorBean.getMsg(), Toast.LENGTH_SHORT).show();
                                activity.getApp().setShareData(AppConfig.TOKEN, null);
                            }
                        } else {
                            ActivityUtil.loadError(activity);
                        }
                    }

                    @Override
                    public void overDue() {
                        ActivityUtil.overdue(activity, loadingDialog, false);
                    }
                });
            }

        } else {
            tv_user_phone.setText("未登录");
            image_user_photo.setImageResource(R.mipmap.img_default_photo_blue);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if (v == layout_user_my_account || v == image_user_photo) {
            token = activity.getApp().getShareDataStr(AppConfig.TOKEN);
            if (TextUtil.isEmpty(token)) {
                intent = new Intent(activity, LoginActivity.class);
            } else {
                if (activity.getApp().getShareDataInt(AppConfig.ROLE_TYPE) == AppConfig.ROLE_TYPE_FOR_SUFFERER) {
                    intent = new Intent(activity, MyAccountActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("sufferer", suffererBean);
                    intent.putExtras(bundle);
                } else {
                    intent = new Intent(activity, DoctorInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("doctor", doctorBean);
                    bundle.putInt("is_mine", AppConfig.IS_MINE);
                    intent.putExtras(bundle);
                }
            }
            startActivity(intent);
        }
        if (v == layout_user_my_question) {
            intent = new Intent(activity, MyParticipateActivity.class);
            startActivity(intent);
        }
        if (v == layout_user_my_doctor) {
            intent = new Intent(activity, MyDoctorAttentionActivity.class);
            startActivity(intent);
        }
        if (v == layout_user_feed_back) {
            intent = new Intent(activity, FeedBackActivity.class);
            startActivity(intent);
        }
        if (v == layout_user_call_us) {
            final ConfirmationDialog dialog = new ConfirmationDialog(activity, R.style.CustomDialog);
            dialog.show();
            TextView tv_dialog_info = (TextView) dialog.findViewById(R.id.tv_dialog_info);
            Button btn_dialog_cancel = (Button) dialog.findViewById(R.id.btn_dialog_cancel);
            Button btn_dialog_sure = (Button) dialog.findViewById(R.id.btn_dialog_sure);
            tv_dialog_info.setText("13837113789");
            btn_dialog_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            btn_dialog_sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    ActivityUtil.call(activity, false, "13837113789");
                }
            });
        }
    }

}
