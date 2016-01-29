package com.zpy.diabetes.app.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.zpy.diabetes.app.BaseActivity;
import com.zpy.diabetes.app.R;
import com.zpy.diabetes.app.adapter.MainControllerAdapter;
import com.zpy.diabetes.app.fragment.HomeFragment;
import com.zpy.diabetes.app.fragment.SearchFragment;
import com.zpy.diabetes.app.fragment.UserFragment;
import com.zpy.diabetes.app.interf.BaseUIInterf;
import com.zpy.diabetes.app.interf.ChangePage;
import com.zpy.diabetes.app.util.ActivityUtil;
import com.zpy.diabetes.app.util.DoubleClickExitHelper;
import com.zpy.diabetes.app.widget.IconButtonContainer;
import com.zpy.diabetes.app.widget.MyViewPager;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener,
        ChangePage, View.OnClickListener, BaseUIInterf {
    private ActionBar actionBar;
    private MyViewPager viewPagerMain;
    private ArrayList<Fragment> fragments;
    private MainControllerAdapter mainControllerAdapter;
    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private UserFragment userFragment;
    private IconButtonContainer iconButtonContainer;
    private DoubleClickExitHelper doubleClickExitHelper;
    private ImageView imageRigth;


    public HomeFragment getHomeFragment() {
        return homeFragment;
    }

    public void setHomeFragment(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public void init() {
        actionBar = getSupportActionBar();
        ActivityUtil.showActionBar(myActionBar, actionBar, -1, R.mipmap.icon_alarm_clock, "糖助手");
        viewPagerMain = (MyViewPager) findViewById(R.id.viewPagerMain);
        viewPagerMain.setSwip(false);
        iconButtonContainer = (IconButtonContainer) findViewById(R.id.nav_buttons);
        fragments = new ArrayList<>();
        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        userFragment = new UserFragment();
//        moreFragment = new MoreFragment();
        fragments.add(homeFragment);
        fragments.add(searchFragment);
        fragments.add(userFragment);
//        fragments.add(moreFragment);
        mainControllerAdapter = new MainControllerAdapter(this, fragments);
        viewPagerMain.setAdapter(mainControllerAdapter);
        viewPagerMain.setOffscreenPageLimit(4);
        viewPagerMain.setOnPageChangeListener(this);
        doubleClickExitHelper = new DoubleClickExitHelper(this);
        imageRigth = myActionBar.getImageViewRight();
        imageRigth.setOnClickListener(this);
    }

    //广播，登录成功后，刷新个人页面
    private final BroadcastReceiver broadcastAccountRefresh = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            userFragment.getUserInfo();
        }
    };

    @Override
    public void show() {

    }

    @Override
    public void onPageChanged(int i) {
        if (viewPagerMain == null) {
            return;
        }
        if (viewPagerMain.getAdapter().getCount() - 1 < i) {
            return;
        }
        viewPagerMain.setCurrentItem(i, false);

        switch (i) {
            case 0:
                myActionBar.setActionBarTitle("糖助手");
                myActionBar.setImageViewRight(R.mipmap.icon_alarm_clock);
                break;
            case 1:
                myActionBar.setActionBarTitle("问答");
                myActionBar.setImageViewRight(-1);
                break;
            case 2:
                myActionBar.setActionBarTitle("我的");
                myActionBar.setImageViewRight(-1);
                break;
            case 3:
                myActionBar.setActionBarTitle("更多");
                myActionBar.setImageViewRight(-1);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if(v == imageRigth) {

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        iconButtonContainer.setCurrentIndex(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            return doubleClickExitHelper.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

}
