package com.zpy.diabetes.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.interf.BaseUIInterf;
import com.zpy.diabetes.app.ui.DoctorsActivity;
import com.zpy.diabetes.app.ui.HealthInfoCenterActivity;

/**
 * Created by Administrator on 2015/11/19 0019.
 */
public class MoreFragment extends Fragment implements BaseUIInterf, View.OnClickListener {
    private View rootView;
    private RelativeLayout layout_more_health_info_center,
            layout_more_doctors;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null) {
            rootView = inflater.inflate(R.layout.more_fragment, container, false);
        } else {
            ViewGroup viewGroup = (ViewGroup) rootView.getParent();
            viewGroup.removeView(rootView);
        }
        init();
        return rootView;
    }

    @Override
    public void init() {
        layout_more_health_info_center = (RelativeLayout) rootView.findViewById(R.id.layout_more_health_info_center);
        layout_more_health_info_center.setOnClickListener(this);
        layout_more_doctors = (RelativeLayout) rootView.findViewById(R.id.layout_more_doctors);
        layout_more_doctors.setOnClickListener(this);
    }

    @Override
    public void show() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        if(v == layout_more_health_info_center) {
            intent = new Intent(getActivity(), HealthInfoCenterActivity.class);
            startActivity(intent);
        }
        if(v == layout_more_doctors) {
            intent = new Intent(getActivity(), DoctorsActivity.class);
            startActivity(intent);
        }
    }
}
