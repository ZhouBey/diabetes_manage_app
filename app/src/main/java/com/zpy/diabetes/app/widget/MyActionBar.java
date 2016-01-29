package com.zpy.diabetes.app.widget;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zpy.diabetes.app.R;


public class MyActionBar {
    public ImageView imageViewLeft, imageViewRight, imageViewCentre;
    public Context context;
    public LinearLayout getLayout_action_bar_centre() {
        return layout_action_bar_centre;
    }


    public LinearLayout layout_action_bar_centre;
    public ImageView getImageViewLeft() {
        return imageViewLeft;
    }


    public ImageView getImageViewRight() {
        return imageViewRight;
    }

    public TextView tv_action_bar_title;

    public TextView getTv_title_bar_right() {
        return tv_title_bar_right;
    }

    public void setTv_title_bar_right(TextView tv_title_bar_right) {
        this.tv_title_bar_right = tv_title_bar_right;
    }

    public TextView tv_title_bar_right;
    public View rootView;

    public MyActionBar(Activity context) {
        this.context = context;
        rootView = context.getLayoutInflater().inflate(R.layout.my_action_bar, null);
        imageViewLeft = (ImageView) rootView.findViewById(R.id.image_title_bar_left);
        imageViewRight = (ImageView) rootView.findViewById(R.id.image_title_bar_right);
        imageViewCentre = (ImageView) rootView.findViewById(R.id.image_title_bar_centre);
        tv_action_bar_title = (TextView) rootView.findViewById(R.id.tv_action_bar_title);
        layout_action_bar_centre = (LinearLayout) rootView.findViewById(R.id.layout_action_bar_centre);
        tv_title_bar_right = (TextView) rootView.findViewById(R.id.tv_title_bar_right);

    }

    public void setImageViewLeft(int imageID) {
        if (imageID != -1) {
            imageViewLeft.setVisibility(View.VISIBLE);
            imageViewLeft.setImageResource(imageID);
        } else {
            imageViewLeft.setVisibility(View.INVISIBLE);
        }
    }

    public void setImageViewRight(int imageID) {
        if (imageID != -1) {
            tv_title_bar_right.setVisibility(View.GONE);
            imageViewRight.setVisibility(View.VISIBLE);
            imageViewRight.setImageResource(imageID);
        } else {
            imageViewRight.setVisibility(View.INVISIBLE);
        }
    }
    public void setTextViewRight(String rightStr) {
        if (rightStr != null) {
            imageViewRight.setVisibility(View.GONE);
            tv_title_bar_right.setVisibility(View.VISIBLE);
            tv_title_bar_right.setText(rightStr);
        }
    }

    public void setImageViewCenter(int imageID) {
        if (imageID != -1) {
            tv_action_bar_title.setVisibility(View.GONE);
            layout_action_bar_centre.setVisibility(View.GONE);
            imageViewCentre.setVisibility(View.VISIBLE);
            imageViewCentre.setImageResource(imageID);
        }
    }

    public void setActionBarTitle(String title) {
        if (title != null) {
            imageViewCentre.setVisibility(View.GONE);
            layout_action_bar_centre.setVisibility(View.GONE);
            tv_action_bar_title.setVisibility(View.VISIBLE);
            tv_action_bar_title.setText(title);
        }
    }
    public void setActionBarCentreView(View view) {
        if(view!=null) {
            tv_action_bar_title.setVisibility(View.GONE);
            imageViewCentre.setVisibility(View.GONE);
            layout_action_bar_centre.setVisibility(View.VISIBLE);
            layout_action_bar_centre.addView(view);
        }
    }

    public View getRootView() {
        return rootView;
    }


}
