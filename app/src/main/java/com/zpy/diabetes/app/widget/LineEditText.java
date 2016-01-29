package com.zpy.diabetes.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

import com.zpy.diabetes.app.R;

public class LineEditText extends EditText {
    private Paint paint;
    private int lineColor;
    private float lineHeight;
    public LineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        init(context,attrs);
        paint.setColor(lineColor);
        paint.setStrokeWidth(lineHeight);
    }
    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LineEditText, 0, 0);
        lineColor = array.getColor(R.styleable.LineEditText_lineColor,getResources().getColor(R.color.colorWhite));
        lineHeight = array.getDimension(R.styleable.LineEditText_lineHeight, 4);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = this.getHeight();
        int width = this.getWidth();
        int ax = 1;
        int ay = height+1;
        int bx = width;
        int by = height+1;
        canvas.drawLines(new float[]{
                ax,ay,bx,by
        },paint);
    }
}

