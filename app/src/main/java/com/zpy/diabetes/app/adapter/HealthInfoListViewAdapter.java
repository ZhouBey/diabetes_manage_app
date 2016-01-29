package com.zpy.diabetes.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.zpy.diabetes.app.App;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.bean.BloodSugarLogBean;
import com.zpy.diabetes.app.config.AppConfig;
import com.zpy.diabetes.app.my.MyCommonCallbackForDrawable;
import com.zpy.diabetes.app.ui.HealthInfoCenterActivity;
import com.zpy.diabetes.app.util.ActivityUtil;
import com.zpy.diabetes.app.util.TextUtil;
import com.zpy.diabetes.app.widget.LocalImageHolderView;
import com.zpy.diabetes.app.widget.linechart.FancyChart;
import com.zpy.diabetes.app.widget.linechart.FancyChartPointListener;
import com.zpy.diabetes.app.widget.linechart.data.Point;

import org.xutils.common.util.LogUtil;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HealthInfoListViewAdapter<T> extends ArrayAdapter<T> {
    private Context context;
    private int resource;
    private List list;
    private App app;
    private View.OnClickListener sugarAnalyzeClickListener,
            guageClickListener;
    private OnItemClickListener bannerOnItemClickListener;

    public OnItemClickListener getBannerOnItemClickListener() {
        return bannerOnItemClickListener;
    }

    public void setBannerOnItemClickListener(OnItemClickListener bannerOnItemClickListener) {
        this.bannerOnItemClickListener = bannerOnItemClickListener;
    }

    private FancyChartPointListener fancyChartPointListener;

    public FancyChartPointListener getFancyChartPointListener() {
        return fancyChartPointListener;
    }

    public void setFancyChartPointListener(FancyChartPointListener fancyChartPointListener) {
        this.fancyChartPointListener = fancyChartPointListener;
    }

    public View.OnClickListener getSugarAnalyzeClickListener() {
        return sugarAnalyzeClickListener;
    }

    public void setSugarAnalyzeClickListener(View.OnClickListener sugarAnalyzeClickListener) {
        this.sugarAnalyzeClickListener = sugarAnalyzeClickListener;
    }

    public View.OnClickListener getGuageClickListener() {
        return guageClickListener;
    }

    public void setGuageClickListener(View.OnClickListener guageClickListener) {
        this.guageClickListener = guageClickListener;
    }

    public HealthInfoListViewAdapter(Context context, int resource, List<T> objects) {
        super(context, resource, objects);
        this.context = context;
        this.list = objects;
        this.resource = resource;
        app = (App) context.getApplicationContext();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListView listView = (ListView) parent;
        int id = listView.getId();
        View view = null;
        if (0 == position && id == R.id.listview_home) {
            int type = app.getShareDataInt(AppConfig.ROLE_TYPE);
            ViewHolder viewHolder = new ViewHolder();
            if (AppConfig.ROLE_TYPE_FOR_DOCTOR == type) {
                List<String> bannerList = (List<String>) list.get(position);
                view = View.inflate(context, R.layout.doctor_banner_layout, null);
                viewHolder.banner_doctor_home = (ConvenientBanner) view.findViewById(R.id.banner_doctor_home);
                viewHolder.setDoctorAttrs(bannerList);
            } else {
                Map<String, Object> map = (Map<String, Object>) list.get(position);
                view = View.inflate(context, R.layout.home_sufferer_head_view, null);
                viewHolder.tv_home_current_week_sugar_analyze = (TextView) view.findViewById(R.id.tv_home_current_week_sugar_analyze);
                viewHolder.tv_home_today_blood_sugar = (TextView) view.findViewById(R.id.tv_home_today_blood_sugar);
                viewHolder.tv_home_today_date = (TextView) view.findViewById(R.id.tv_home_today_date);
                viewHolder.linechart_sugar_log = (FancyChart) view.findViewById(R.id.linechart_sugar_log);
                viewHolder.layout_home_guage = (RelativeLayout) view.findViewById(R.id.layout_home_guage);
                viewHolder.tv_home_health_info_more = (TextView) view.findViewById(R.id.tv_home_health_info_more);
                viewHolder.setSuffererAttrs(map);
            }
            return view;

        }
        if (convertView == null) {
            view = View.inflate(context, resource, null);
        } else {
            view = convertView;
        }
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            viewHolder.tv_health_info_time = (TextView) view.findViewById(R.id.tv_health_info_time);
            viewHolder.tv_health_info_title = (TextView) view.findViewById(R.id.tv_health_info_title);
            viewHolder.image_health_info = (RoundedImageView) view.findViewById(R.id.image_health_info);
            viewHolder.image_health_info_tuijian = (ImageView) view.findViewById(R.id.image_health_info_tuijian);
            view.setTag(viewHolder);
        }
        viewHolder.setItemAttrs((Map<String, Object>) list.get(position));
        return view;
    }

    public class ViewHolder {
        public TextView tv_health_info_time,
                tv_health_info_title,
                tv_home_current_week_sugar_analyze,
                tv_home_today_blood_sugar,
                tv_home_today_date,
                tv_home_health_info_more;
        public ImageView image_health_info_tuijian;
        public RoundedImageView image_health_info;
        public ConvenientBanner banner_doctor_home;
        public FancyChart linechart_sugar_log;
        public RelativeLayout layout_home_guage;

        public void setItemAttrs(Map<String, Object> item) {
            tv_health_info_title.setText(String.valueOf(item.get("info_title")));
            tv_health_info_time.setText(String.valueOf(item.get("info_time")));
            x.image().bind(image_health_info, String.valueOf(item.get("info_image")), new MyCommonCallbackForDrawable(context, image_health_info, R.drawable.empty_photo));
            if ("1".equals(String.valueOf(item.get("info_tuijian")))) {
                image_health_info_tuijian.setImageResource(R.mipmap.icon_tuijian);
            }
        }

        public void setSuffererAttrs(Map<String, Object> item) {
            tv_home_today_blood_sugar.setText(String.valueOf(item.get("todayBloodSugarLog")).equals("null") ? "——" : String.valueOf(item.get("todayBloodSugarLog")));
            tv_home_today_date.setText(TextUtil.getTimeStr());
            List<BloodSugarLogBean> bloodSugarLogBeans = (List<BloodSugarLogBean>) item.get("bloodSugarLogBeans");
            double[] yValues = new double[7];
            if (bloodSugarLogBeans != null) {
                for (int i = 0; i < bloodSugarLogBeans.size(); i++) {
                    BloodSugarLogBean bloodSugarLogBean = bloodSugarLogBeans.get(i);
                    Double sugarContent = bloodSugarLogBean.getSugarContent();
                    String date = bloodSugarLogBean.getCreateD();
                    int weekOfDate = TextUtil.getWeekOfDate(TextUtil.str2Date(date));
                    yValues[weekOfDate - 1] = sugarContent;
                }
            }
            ActivityUtil.drawLineChart(linechart_sugar_log, yValues);
            linechart_sugar_log.setOnPointClickListener(fancyChartPointListener);
            tv_home_current_week_sugar_analyze.setOnClickListener(sugarAnalyzeClickListener);
            layout_home_guage.setOnClickListener(guageClickListener);
            tv_home_health_info_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, HealthInfoCenterActivity.class);
                    context.startActivity(intent);
                }
            });
        }

        public void setDoctorAttrs(List<String> list) {
            banner_doctor_home.setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused});
            banner_doctor_home.setPages(
                    new CBViewHolderCreator<LocalImageHolderView>() {
                        @Override
                        public LocalImageHolderView createHolder() {
                            return new LocalImageHolderView();
                        }
                    }, list);
            banner_doctor_home.startTurning(2000);
            banner_doctor_home.setOnItemClickListener(bannerOnItemClickListener);
        }
    }
}
