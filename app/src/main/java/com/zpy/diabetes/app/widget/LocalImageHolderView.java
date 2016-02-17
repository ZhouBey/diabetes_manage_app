package com.zpy.diabetes.app.widget;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.my.MyCommonCallbackForDrawable;

import org.xutils.x;

/**
 * 本地图片Holder
 */
public class LocalImageHolderView implements Holder<String> {
    private ImageView imageView;
    private Context context;
    @Override
    public View createView(Context context) {
        this.context = context;
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        x.image().bind(imageView, data,new MyCommonCallbackForDrawable(context,imageView, R.drawable.empty_photo));
    }
}
