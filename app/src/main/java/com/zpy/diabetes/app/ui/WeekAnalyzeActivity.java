package com.zpy.diabetes.app.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zpy.diabetes.app.BaseActivity;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.bean.BloodSugarLogBean;
import com.zpy.diabetes.app.interf.BaseUIInterf;
import com.zpy.diabetes.app.util.ActivityUtil;
import com.zpy.diabetes.app.util.TextUtil;
import com.zpy.diabetes.app.widget.MyActionBar;
import com.zpy.diabetes.app.widget.WaterDrop;

import java.util.List;

/**
 * Created by Administrator on 2015/11/26 0026.
 */
public class WeekAnalyzeActivity extends BaseActivity implements BaseUIInterf, View.OnClickListener {

    private ActionBar actionBar;
    private FrameLayout layoutLeft;
    private WaterDrop water_drop;
    private TextView tv_week_analyze_general_comment,
            tv_week_analyze_detail;
    private Double week_blood_average;
    private List<BloodSugarLogBean> bloodSugarLogBeans;
    private double bloodTotal;
    private int bloodNormalCount = 0;
    private String high_low;
    private String advanceStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.week_analyze);
        init();
        show();
    }

    @Override
    public void init() {
        actionBar = getSupportActionBar();
        ActivityUtil.showActionBar(myActionBar, actionBar, R.mipmap.back, -1, "评价与建议");
        layoutLeft = myActionBar.getLayout_my_actionbar_left();
        layoutLeft.setOnClickListener(this);
        water_drop = (WaterDrop) findViewById(R.id.water_drop);
        tv_week_analyze_general_comment = (TextView) findViewById(R.id.tv_week_analyze_general_comment);
        tv_week_analyze_detail = (TextView) findViewById(R.id.tv_week_analyze_detail);
        bloodSugarLogBeans = (List<BloodSugarLogBean>) getIntent().getSerializableExtra("bloodSugarLogBeans");
    }

    @Override
    public void show() {
        for (int i = 0; i < bloodSugarLogBeans.size(); i++) {
            double sugarContent = bloodSugarLogBeans.get(i).getSugarContent();
            bloodTotal += sugarContent;
            if (sugarContent >= 4.4 && sugarContent <= 8.0) {
                bloodNormalCount++;
            }
        }
        week_blood_average = bloodTotal / bloodSugarLogBeans.size();
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) water_drop.getLayoutParams();
        if (week_blood_average > 8.0) {
            params.addRule(RelativeLayout.ALIGN_LEFT, R.id.view_sugar_mark_high);
            high_low = "偏高";
            advanceStr = "注意高血糖风险，只要努力，一定能控制好血糖。";
            water_drop.setBgColor(getResources().getColor(R.color.color_sugar_mark_high));
        } else if (week_blood_average < 4.4) {
            params.addRule(RelativeLayout.ALIGN_LEFT, R.id.view_sugar_mark_low);
            high_low = "偏低";
            advanceStr = "注意饮食营养，避免饥饿。";
            water_drop.setBgColor(getResources().getColor(R.color.color_sugar_mark_low));
        } else {
            params.addRule(RelativeLayout.ALIGN_LEFT, R.id.view_sugar_mark_middle);
            high_low = "正常";
            advanceStr = "做得不做，继续保持！";
            water_drop.setBgColor(getResources().getColor(R.color.color_sugar_mark_middle));
        }
        water_drop.setValueStr(String.valueOf(week_blood_average));
        water_drop.setContentStr("mmol/L");

        String comment = "本周血糖平均值" + high_low + "，为" + String.valueOf(week_blood_average) + "mmol/L。" + advanceStr;
        String info = "你本周共测量血糖" + String.valueOf(bloodSugarLogBeans.size()) + "次，" + String.valueOf(bloodNormalCount) + "次正常。";
        tv_week_analyze_general_comment.setText(comment);
        tv_week_analyze_detail.setText(info);
    }

    @Override
    public void onClick(View v) {
        if (v == layoutLeft) {
            this.finish();
        }
    }
}
