package com.zpy.diabetes.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class MainControllerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentList;
    public MainControllerAdapter(FragmentActivity fragmentActivity, List<Fragment> fragmentList) {
        super(fragmentActivity.getSupportFragmentManager());
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        long time = System.currentTimeMillis();
        super.destroyItem(container, position, object);
    }
}
