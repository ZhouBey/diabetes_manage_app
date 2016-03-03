package com.zpy.diabetes.app.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zpy.diabetes.app.BaseActivity;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.bean.HealthInfoBean;
import com.zpy.diabetes.app.config.AppConfig;
import com.zpy.diabetes.app.interf.BaseUIInterf;
import com.zpy.diabetes.app.util.ActivityUtil;
import com.zpy.diabetes.app.util.TextUtil;

import org.xutils.common.Callback;
import org.xutils.x;

public class HealthInfoDetailsActivity extends BaseActivity implements BaseUIInterf, View.OnClickListener {

    private ActionBar actionBar;
    private ImageView leftImage,
            image_health_info_details;
    private Bundle bundle;
    private HealthInfoBean healthInfoBean;
    private TextView tv_health_detail_title,
            tv_health_detail_time,
            tv_health_detail_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_info_details);
        init();
        show();
    }

    @Override
    public void init() {
        actionBar = getSupportActionBar();
        ActivityUtil.showActionBar(myActionBar, actionBar, R.mipmap.back, -1, "资讯详情");
        leftImage = myActionBar.getImageViewLeft();
        leftImage.setOnClickListener(this);
        image_health_info_details = (ImageView) findViewById(R.id.image_health_info_details);
        bundle = getIntent().getExtras();
        healthInfoBean = (HealthInfoBean) bundle.getSerializable("healthInfo");
        tv_health_detail_title = (TextView) findViewById(R.id.tv_health_detail_title);
        tv_health_detail_time = (TextView) findViewById(R.id.tv_health_detail_time);
        tv_health_detail_content = (TextView) findViewById(R.id.tv_health_detail_content);

    }

    @Override
    public void show() {
        tv_health_detail_title.setText(healthInfoBean.getTitle());
        tv_health_detail_time.setText(healthInfoBean.getCreateD());
        tv_health_detail_content.setText(Html.fromHtml(healthInfoBean.getMsg()));
        x.image().bind(image_health_info_details, AppConfig.QINIU_IMAGE_URL + healthInfoBean.getInfoImage(),
                new Callback.CommonCallback<Drawable>() {
                    @Override
                    public void onSuccess(Drawable result) {
                        Bitmap bitmap = ((BitmapDrawable) result).getBitmap();
                        ActivityUtil.fitWidth(HealthInfoDetailsActivity.this, bitmap, image_health_info_details);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        image_health_info_details.setScaleType(ImageView.ScaleType.FIT_XY);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TextUtil.dip2px(HealthInfoDetailsActivity.this,160));
                        params.setMargins(TextUtil.dip2px(HealthInfoDetailsActivity.this, 20), TextUtil.dip2px(HealthInfoDetailsActivity.this, 7), TextUtil.dip2px(HealthInfoDetailsActivity.this, 20), 0);
                        image_health_info_details.setLayoutParams(params);
                        image_health_info_details.setImageResource(R.drawable.empty_photo);

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
    }


    @Override
    public void onClick(View v) {
        if (v == leftImage) {
            this.finish();
        }
    }
}
