package com.coretronic.christieapp.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class ChristieActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private String TAG = ChristieActivity.class.getSimpleName();
    private Context mContext;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private List<Fragment> fragmentList;
    private LinearLayout linearLayout;
    private List<View> pageList;
    private int pageCount = 0;
    private int preView = 0;
    private static TelnetService telnetService;
    private String remoteip = "10.1.6.103";
    private int remoteport = 3002;
    //    private String remoteip = "10.2.24.195";
    //    private int remoteport = 3002;
    //    private String remoteip = "192.168.1.100";
    //    private int remoteport = 3002;
    //    private String remoteip = "10.100.1.12";
    //    private int remoteport = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mContext = this;
        // telnet connect
        try {
            telnetService = new TelnetService(mContext);
            telnetService.connect(remoteip, remoteport);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // view pager fragment list
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new PowerControlFragment());
        fragmentList.add(new HotKeyFragment());
        fragmentList.add(new LensFragment());

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.viewpager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(this);

        pageCount = fragmentList.size();
        pageList = new ArrayList<View>();
        linearLayout = (LinearLayout) findViewById(R.id.pageGroup);
        for (int i = 0; i < pageCount; i++) {
            View page = getPageView();
            pageList.add(page);
            linearLayout.addView(page);
        }
        pageList.get(preView).setBackgroundResource(R.color.white);
    }

    private View getPageView() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(120, 10);
        layoutParams.setMargins(20, 10, 20, 10);
        View pageView = new View(this);
        pageView.setLayoutParams(layoutParams);
        pageView.setBackgroundResource(R.color.gray);
        return pageView;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "preView: " + preView);
        Log.d(TAG, "position: " + position);
        if (position != preView) {
            pageList.get(preView).setBackgroundResource(R.color.gray);
            pageList.get(position).setBackgroundResource(R.color.white);
            preView = position;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }
    public static void sendCommand(final Context mContext, final String key) {
        telnetService.sendCommand(mContext,key);
    }
}
