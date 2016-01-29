package com.zpy.diabetes.app.my;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.zpy.diabetes.app.R;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.x;

public class MyCommonCallbackForDrawable implements Callback.CommonCallback<Drawable> {

    private ImageView imageView;
    private Context context;
    private int defaultResImage;

    public MyCommonCallbackForDrawable(Context context, ImageView imageView,int defaultResImage) {
        this.imageView = imageView;
        this.context = context;
        this.defaultResImage = defaultResImage;
    }

    @Override
    public void onSuccess(Drawable result) {
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), defaultResImage);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onCancelled(CancelledException cex) {
    }

    @Override
    public void onFinished() {
    }
}
