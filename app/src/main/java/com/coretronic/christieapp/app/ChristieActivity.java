package com.coretronic.christieapp.app;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.coretronic.christieapp.app.Common.AppConfig;

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
    private static TelnetServices telnetServices;
    //    private String remoteip = "10.1.6.103";
//    private int remoteport = 3002;
    private String mConnectedDeviceName;
    //    private String remoteip = "10.2.24.195";
    //    private int remoteport = 3002;
    private String remoteip = "192.168.1.100";
    private int remoteport = 3002;
    //    private String remoteip = "10.100.1.12";
    //    private int remoteport = 23;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AppConfig.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case TelnetServices.STATE_CONNECTED:
                            try {
                                setStatus("STATE_CONNECTED");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case TelnetServices.STATE_CONNECTING:
                            setStatus("STATE_CONNECTING");
                            break;
                        case TelnetServices.STATE_NONE:
                            setStatus("NOT connected");
                            break;
                    }
                    break;
                case AppConfig.MESSAGE_WRITE:
//                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
//                    String writeMessage = new String(writeBuf);
                    String writeMessage = (String) msg.obj;
                    Log.d(TAG, "write message: " + writeMessage);
                    break;
                case AppConfig.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    Log.d(TAG, "read message: " + readMessage);
                    break;
                case AppConfig.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(AppConfig.DEVICE_NAME);
                    Toast.makeText(mContext, "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case AppConfig.MESSAGE_TOAST:
                    Toast.makeText(mContext, msg.getData().getString(AppConfig.TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mContext = this;
        // telnet connect
        try {
            telnetServices = new TelnetServices(mContext, mHandler);
            telnetServices.connect(remoteip, remoteport);
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

    private void setStatus(int resId) {
        final ActionBar actionBar = getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(resId);
    }

    private void setStatus(CharSequence subTitle) {
        final ActionBar actionBar = getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

    public static void sendCommand(String str) {
        telnetServices.write(str);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.connect_close: {
                // Launch the DeviceListActivity to see devices and do scan
                telnetServices.stop();
                return true;
            }
        }
        return false;
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

}
