package com.zpy.diabetes.app.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.util.TextUtil;


public class WaterDrop extends View {
    private Context context;
    private Bitmap bitmap;
    private int width;
    private int height;
    private int valueSize;
    private int contentSize;
    private String valueStr;
    private String contentStr;
    private boolean inversion;
    private int i;
    private float[] j = {1.0F, 0.0F, 0.0F, 0.0F, 43.0F,
            0.0F, 1.0F, 0.0F, 0.0F, 136.0F,
            0.0F, 0.0F, 1.0F, 0.0F, 239.0F,
            0.0F, 0.0F, 0.0F, 1.0F, 0.0F};

    public WaterDrop(Context paramContext) {
        this(paramContext, null);
    }

    public WaterDrop(Context paramContext, AttributeSet paramAttributeSet) {
        this(paramContext, paramAttributeSet, 0);
    }

    public WaterDrop(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        this.context = paramContext;
        init();
    }

    private void init() {
        this.bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.water_drop);
        this.width = this.bitmap.getWidth();
        this.height = this.bitmap.getHeight();
        contentSize = TextUtil.dip2px(context, 14);
        valueSize = TextUtil.dip2px(context, 17);
    }

    public int getWaterDropHeight() {
        return this.height;
    }

    public int getWaterDropWidth() {
        return this.width;
    }

    public boolean isInversion() {
        return this.inversion;
    }

    protected void onDraw(Canvas paramCanvas) {
        super.onDraw(paramCanvas);
        Paint localPaint = new Paint(1);
        localPaint.setColorFilter(new ColorMatrixColorFilter(this.j));
        paramCanvas.save();
        if (this.inversion) {
            paramCanvas.rotate(180.0F, this.width / 2, this.height / 2);
        }
        paramCanvas.drawBitmap(this.bitmap, new Matrix(), localPaint);
        paramCanvas.restore();
        localPaint.setColor(-1);
        if (!TextUtil.isEmpty(this.valueStr)) {
            localPaint.setTextSize(this.valueSize);
            Rect localRect2 = new Rect();
            localPaint.getTextBounds(this.valueStr, 0, this.valueStr.length(), localRect2);
            float f1 = localRect2.width();
            paramCanvas.drawText(this.valueStr, (this.width - f1) / 2.0F, -5 + this.height / 2, localPaint);
        }
        if (!TextUtil.isEmpty(this.contentStr)) {
            localPaint.setTextSize(this.contentSize);
            Rect localRect1 = new Rect();
            localPaint.getTextBounds(this.contentStr, 0, this.contentStr.length(), localRect1);
            paramCanvas.drawText(this.contentStr, (this.width - localRect1.width()) / 2, 5 + (this.height / 2 + localRect1.height()), localPaint);
        }
    }

    public void setBgColor(int paramInt) {
        this.i = paramInt;
        if (this.i == R.color.color_sugar_mark_low) {
            this.j = new float[]{1.0F, 0.0F, 0.0F, 0.0F, 255.0F,
                    0.0F, 1.0F, 0.0F, 0.0F, 131.0F,
                    0.0F, 0.0F, 1.0F, 0.0F, 9.0F,
                    0.0F, 0.0F, 0.0F, 1.0F, 0.0F};
        }
        if (this.i == R.color.color_sugar_mark_middle) {
            this.j = new float[]{1.0F, 0.0F, 0.0F, 0.0F, 1.0F,
                    0.0F, 1.0F, 0.0F, 0.0F, 177.0F,
                    0.0F, 0.0F, 1.0F, 0.0F, 138.0F,
                    0.0F, 0.0F, 0.0F, 1.0F, 0.0F};
        }
        if (this.i == R.color.color_sugar_mark_high) {
            this.j = new float[]{1.0F, 0.0F, 0.0F, 0.0F, 240.0F,
                    0.0F, 1.0F, 0.0F, 0.0F, 42.0F,
                    0.0F, 0.0F, 1.0F, 0.0F, 65.0F,
                    0.0F, 0.0F, 0.0F, 1.0F, 0.0F};
        }
        for (; ; ) {
            invalidate();
            return;
        }
    }

    public void setContentSize(int paramInt) {
        this.contentSize = paramInt;
        invalidate();
    }

    public void setContentStr(String paramString) {
        this.contentStr = paramString;
        invalidate();
    }

    public void setInversion(boolean paramBoolean) {
        this.inversion = paramBoolean;
        invalidate();
    }

    public void setLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
        paramLayoutParams.width = this.width;
        paramLayoutParams.height = this.height;
        super.setLayoutParams(paramLayoutParams);
    }

    public void setValueSize(int paramInt) {
        this.valueSize = paramInt;
        invalidate();
    }

    public void setValueStr(String paramString) {
        this.valueStr = paramString;
        invalidate();
    }
}

