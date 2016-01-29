package com.zpy.diabetes.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.adapter.HealthInfoListViewAdapter;
import com.zpy.diabetes.app.bean.AppBean;
import com.zpy.diabetes.app.bean.BloodSugarLogBean;
import com.zpy.diabetes.app.bean.BloodSugarLogoPageBean;
import com.zpy.diabetes.app.bean.HealthInfoBean;
import com.zpy.diabetes.app.bean.HealthInfoPageBean;
import com.zpy.diabetes.app.bean.ResultBean;
import com.zpy.diabetes.app.config.AppConfig;
import com.zpy.diabetes.app.interf.BaseUIInterf;
import com.zpy.diabetes.app.interf.IAppUserTokenBeanHolder;
import com.zpy.diabetes.app.ui.HealthInfoDetailsActivity;
import com.zpy.diabetes.app.ui.LoginActivity;
import com.zpy.diabetes.app.ui.MainActivity;
import com.zpy.diabetes.app.ui.WeekAnalyzeActivity;
import com.zpy.diabetes.app.util.ActivityUtil;
import com.zpy.diabetes.app.util.TextUtil;
import com.zpy.diabetes.app.widget.InputSugarDialog;
import com.zpy.diabetes.app.widget.acpf.ACProgressFlower;
import com.zpy.diabetes.app.widget.linechart.FancyChartPointListener;
import com.zpy.diabetes.app.widget.linechart.data.Point;


import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements BaseUIInterf,
        AdapterView.OnItemClickListener, ViewPager.OnPageChangeListener {
    private View rootView;
    private ListView listview_home;
    private List list;
    private HealthInfoListViewAdapter adapter;
    private MainActivity activity;
    private String token;
    private ACProgressFlower loadingDialog;
    public String[] week = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.home_fragment, container, false);
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
        listview_home = (ListView) rootView.findViewById(R.id.listview_home);
        token = activity.getApp().getShareDataStr(AppConfig.TOKEN);
        loadingDialog = ActivityUtil.getLoadingDialog(activity);
    }

    @Override
    public void show() {
        loadIndex();
    }

    public void loadIndex() {
        token = activity.getApp().getShareDataStr(AppConfig.TOKEN);
        loadingDialog.show();
        activity.getApp().getHttpApi().getIndexInfo(token, loadingDialog, new IAppUserTokenBeanHolder() {
            @Override
            public void asynHold(AppBean appBean) {
                if (appBean != null) {
                    if (AppConfig.OK.equals(appBean.getCode())) {
                        Map<String, AppBean> map = (Map<String, AppBean>) appBean.getData();
                        if (map == null) {
                            Toast.makeText(activity, "无数据", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int roleType = activity.getApp().getShareDataInt(AppConfig.ROLE_TYPE);
                        HealthInfoPageBean healthInfoPageBean = (HealthInfoPageBean) map.get("healthInfoPageBean");
                        if (AppConfig.ROLE_TYPE_FOR_DOCTOR == roleType) {
                            list = new ArrayList();
                            if (healthInfoPageBean != null) {
                                final List<HealthInfoBean> healthInfoBeanList = healthInfoPageBean.getHealthInfoBeans();
                                List<String> bannerList = new ArrayList<String>();
                                for (int i = 0; i < AppConfig.BANNER_COUNT; i++) {
                                    HealthInfoBean healthInfoBean = healthInfoBeanList.get(i);
                                    bannerList.add(AppConfig.QINIU_IMAGE_URL + healthInfoBean.getInfoImage());
                                }
                                list.add(bannerList);
                                for (int i = AppConfig.BANNER_COUNT; i < healthInfoBeanList.size(); i++) {
                                    HealthInfoBean healthInfoBean = healthInfoBeanList.get(i);
                                    Map item = new HashMap();
                                    item.put("info_title", healthInfoBean.getTitle());
                                    item.put("info_time", healthInfoBean.getCreateD());
                                    item.put("info_image", AppConfig.QINIU_IMAGE_URL + healthInfoBean.getInfoImage());
                                    list.add(item);
                                }
                                adapter = new HealthInfoListViewAdapter(activity, R.layout.health_info_listview_item, list);
                                listview_home.setAdapter(adapter);
                                listview_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        HealthInfoBean healthInfoBean = healthInfoBeanList.get(position + 1);
                                        Intent intent = new Intent(activity, HealthInfoDetailsActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("healthInfo", healthInfoBean);
                                        intent.putExtras(bundle);
                                        startActivity(intent);

                                    }
                                });
                                adapter.setBannerOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        Intent intent = new Intent(activity, HealthInfoDetailsActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("healthInfo", healthInfoBeanList.get(position));
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                });
                            }
                        } else {
                            list = new ArrayList();
                            Map<String, Object> firstMap = new HashMap<String, Object>();
                            BloodSugarLogoPageBean bloodSugarLogoPageBean = (BloodSugarLogoPageBean) map.get("bloodSugarLogoPageBean");
                            if (bloodSugarLogoPageBean != null) {
                                List<BloodSugarLogBean> bloodSugarLogBeans = bloodSugarLogoPageBean.getBloodSugarLogBeans();
                                firstMap.put("bloodSugarLogBeans", bloodSugarLogBeans);
                            }
                            BloodSugarLogBean todayBloodSugarLogBean = (BloodSugarLogBean) map.get("bloodSugarLogBeanToday");
                            if (todayBloodSugarLogBean != null) {
                                firstMap.put("todayBloodSugarLog", todayBloodSugarLogBean.getSugarContent());
                            }
                            firstMap.put(AppConfig.ROLE_TYPE, AppConfig.ROLE_TYPE_FOR_SUFFERER);
                            list.add(firstMap);
                            if (healthInfoPageBean != null) {
                                final List<HealthInfoBean> healthInfoBeanList = healthInfoPageBean.getHealthInfoBeans();
                                for (int i = 0; i < 4; i++) {
                                    HealthInfoBean healthInfoBean = healthInfoBeanList.get(i);
                                    Map item = new HashMap();
                                    item.put("info_title", healthInfoBean.getTitle());
                                    item.put("info_time", healthInfoBean.getCreateD());
                                    item.put("info_image", AppConfig.QINIU_IMAGE_URL + healthInfoBean.getInfoImage());
                                    item.put("info_tuijian", "1");
                                    list.add(item);
                                }
                                adapter = new HealthInfoListViewAdapter(activity, R.layout.health_info_listview_item, list);
                                listview_home.setAdapter(adapter);
                                listview_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        if (0 != position) {
                                            HealthInfoBean healthInfoBean = healthInfoBeanList.get(position - 1);
                                            Intent intent = new Intent(activity, HealthInfoDetailsActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("healthInfo", healthInfoBean);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    }
                                });
                                adapter.setFancyChartPointListener(fancyChartPointListener);
                                adapter.setGuageClickListener(guageClickListener);
                                adapter.setSugarAnalyzeClickListener(sugarAnalyzeClickListener);
                            }

                        }
                    } else {
                        Toast.makeText(activity, appBean.getMsg(), Toast.LENGTH_SHORT).show();
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

    View.OnClickListener guageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            token = activity.getApp().getShareDataStr(AppConfig.TOKEN);
            if (!TextUtil.isEmpty(token)) {
                final InputSugarDialog dialog = new InputSugarDialog(activity, R.style.CustomDialog);
                dialog.show();
                final EditText et_input_blood_sugar = (EditText) dialog.findViewById(R.id.et_input_blood_sugar);
                TextView tv_submit_blood_sugar = (TextView) dialog.findViewById(R.id.tv_submit_blood_sugar);
                final TextView tv_input_sugar_error_message = (TextView) dialog.findViewById(R.id.tv_input_sugar_error_message);
                tv_submit_blood_sugar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String bloodSugar = et_input_blood_sugar.getText().toString();
                        if (!TextUtil.isEmpty(bloodSugar)) {
                            Double bloodSugarD = Double.parseDouble(bloodSugar);
                            activity.getApp().getHttpApi().addTodayBloodSugar(token, bloodSugarD, loadingDialog, new IAppUserTokenBeanHolder() {
                                @Override
                                public void asynHold(AppBean bean) {
                                    dialog.dismiss();
                                    if (bean != null) {
                                        ResultBean resultBean = (ResultBean) bean;
                                        if (AppConfig.OK.equals(resultBean.getCode())) {
                                            Toast.makeText(activity, "记录成功！", Toast.LENGTH_SHORT).show();
                                            loadIndex();
                                        } else {
                                            Toast.makeText(activity, resultBean.getMsg(), Toast.LENGTH_SHORT).show();
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
                        } else {
                            tv_input_sugar_error_message.setVisibility(View.VISIBLE);
                            tv_input_sugar_error_message.setText("请输入您的血糖");
                        }
                    }
                });

            } else {
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
            }
        }
    };

    FancyChartPointListener fancyChartPointListener = new FancyChartPointListener() {
        @Override
        public void onClick(Point point) {
            Toast.makeText(activity, "您在" + week[point.x - 1] + "的血糖是" + point.y + "mmol/L", Toast.LENGTH_LONG).show();

        }
    };
    View.OnClickListener sugarAnalyzeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            token = activity.getApp().getShareDataStr(AppConfig.TOKEN);
            Intent intent = null;
            if (!TextUtil.isEmpty(token)) {
                intent = new Intent(activity, WeekAnalyzeActivity.class);
            } else {
                intent = new Intent(activity, LoginActivity.class);
            }
            startActivity(intent);
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
