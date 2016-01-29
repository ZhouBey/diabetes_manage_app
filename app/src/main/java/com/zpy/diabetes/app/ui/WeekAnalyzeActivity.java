package com.zpy.diabetes.app.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;

import com.zpy.diabetes.app.BaseActivity;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.interf.BaseUIInterf;
import com.zpy.diabetes.app.util.ActivityUtil;
import com.zpy.diabetes.app.util.TextUtil;
import com.zpy.diabetes.app.widget.MyActionBar;
import com.zpy.diabetes.app.widget.WaterDrop;

/**
 * Created by Administrator on 2015/11/26 0026.
 */
public class WeekAnalyzeActivity extends BaseActivity implements BaseUIInterf, View.OnClickListener {

    private ActionBar actionBar;
    private ImageView imageLeft;
    private WaterDrop water_drop;

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
        ActivityUtil.showActionBar(myActionBar,actionBar, R.mipmap.back,-1,"评价与建议");
        imageLeft = myActionBar.getImageViewLeft();
        imageLeft.setOnClickListener(this);
        water_drop = (WaterDrop) findViewById(R.id.water_drop);
    }

    @Override
    public void show() {
        water_drop.setValueStr("1.1");
        water_drop.setContentStr("mmol/L");
        water_drop.setBgColor(getResources().getColor(R.color.color_sugar_mark_low));
    }

    @Override
    public void onClick(View v) {
        if(v == imageLeft) {
            this.finish();
        }
    }
}
