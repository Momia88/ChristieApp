package com.coretronic.christieapp.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

public class DeviceListActivity extends FragmentActivity {
    /**
     * Called when the activity is first created.
     */

    private FragmentTabHost tabHost;
    private TabHost.TabSpec spec;
    private String TAG = DeviceListActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_device);
        // Create FragmentTabHost
        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        if(tabHost == null){
            Log.d(TAG,"tabHost == null");
        }
        tabHost.setup(this, getSupportFragmentManager(), R.id.fragment_content);

        // new TabSpec tag
        // set Indicator text and icon
        spec = tabHost.newTabSpec("First")
                .setIndicator(getTabItemView("Favorite", android.R.drawable.star_off));
        tabHost.addTab(spec, DeviceListFragment.class, null);

        spec = tabHost.newTabSpec("Second")
                .setIndicator(getTabItemView("All Contents", android.R.drawable.ic_menu_agenda));
        tabHost.addTab(spec, DeviceListFragment.class, null);
    }

    // get Tab view from layout
    private View getTabItemView(String title, int imageid) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_layout, null);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(title);
        textView.setCompoundDrawablesWithIntrinsicBounds(0, imageid, 0, 0);
        return view;
    }
}
