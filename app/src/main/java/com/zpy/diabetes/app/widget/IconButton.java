package com.zpy.diabetes.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zpy.diabetes.app.R;


public class IconButton extends LinearLayout {
    private TextView innerTextView;
    private ImageView innerImageView;
    private int imageHeight;
    private float textSize;
    private int textColor;
    private Drawable drawable;
    private Drawable pressedDrawable;
    private int pressedTextColor = -1;
    private int pressedBackground = -1;
    private int background;
    private boolean btnSelected;
    private IconButtonContainer parent;
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isBtnSelected() {
        return btnSelected;
    }

    public void setBtnSelected(boolean pressed) {
        this.btnSelected = pressed;
        if (pressed) {
            if (pressedTextColor != -1) {
                innerTextView.setTextColor(pressedTextColor);
            }
            if (pressedDrawable != null) {
                innerImageView.setImageDrawable(pressedDrawable);
            }
            if (pressedBackground != -1) {
                this.setBackgroundResource(pressedBackground);
            }
        } else {
            innerTextView.setTextColor(textColor);
            innerImageView.setImageDrawable(drawable);
            if (background != -1) {
                this.setBackgroundResource(background);
            }
        }
    }

    public TextView getInnerTextView() {
        return innerTextView;
    }

    public ImageView getInnerImageView() {
        return innerImageView;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        this.setTextSize(textSize);
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        this.innerTextView.setTextColor(textColor);
    }

    public IconButton(Context context) {
        this(context, null);
    }

    public IconButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        innerTextView = new TextView(context);
        innerTextView.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        innerImageView = new ImageView(context);
        innerImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        this.setOrientation(LinearLayout.VERTICAL);
        this.addView(innerImageView);
        this.addView(innerTextView);
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            if (attrs.getAttributeName(i).equals("background")) {
                background = attrs.getAttributeResourceValue(i, -1);
            }
        }
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.IconButton, 0, 0);
        try {
            drawable = a.getDrawable(R.styleable.IconButton_buttonIcon);
            innerImageView.setImageDrawable(drawable);
            imageHeight = a.getDimensionPixelSize(R.styleable.IconButton_iconSize, 16);
            innerImageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, imageHeight));
            innerTextView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
            CharSequence text = a.getText(R.styleable.IconButton_text);
            innerTextView.setText(text);
            textSize = a.getDimension(R.styleable.IconButton_textSize, -1);
            if (textSize != -1) {
                innerTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
            } else {
                textSize = (int) innerTextView.getTextSize();
            }
            textColor = a.getColor(R.styleable.IconButton_iconButtonTextColor, Color.BLACK);
            innerTextView.setTextColor(textColor);
            pressedDrawable = a.getDrawable(R.styleable.IconButton_pressedIcon);
            pressedTextColor = a.getColor(R.styleable.IconButton_iconButtonPressedTextColor, -1);
            pressedBackground = a.getResourceId(R.styleable.IconButton_pressedBackground, -1);

        } finally {
            a.recycle();
        }
    }
}
