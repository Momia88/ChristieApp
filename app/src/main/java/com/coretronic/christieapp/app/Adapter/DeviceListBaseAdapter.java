package com.coretronic.christieapp.app.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import com.coretronic.christieapp.app.R;

import java.util.List;

/**
 * Created by morris on 2015/3/5.
 */
public class DeviceListBaseAdapter extends BaseAdapter {
    private Context context = null;
    private LayoutInflater layoutInflater;
    private String TAG = DeviceListBaseAdapter.class.getSimpleName();
    private List<String> mDeviceList = null;

    public DeviceListBaseAdapter(Context context, List<String> mDeviceList) {
        this.context = context;
        this.mDeviceList = mDeviceList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDeviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDeviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "Into getView ");
        ViewTag viewTag;

        if (convertView == null) {
            Log.i(TAG, "convertView == null ");
            convertView = layoutInflater.inflate(R.layout.device_item, null);
            viewTag = new ViewTag(
                    (TextView) convertView.findViewById(R.id.device_ip),
                    (Switch) convertView.findViewById(R.id.connect_switch),
                    (ImageButton) convertView.findViewById(R.id.device_info));
            convertView.setTag(viewTag);
        } else {
            Log.i(TAG, "convertView != null ");
            viewTag = (ViewTag) convertView.getTag();
        }

        viewTag.deviceIp.setText(mDeviceList.get(position));
        return convertView;
    }


    class ViewTag {
        TextView deviceIp = null;
        Switch connetSwitch = null;
        ImageButton deviceInfo = null;

        public ViewTag(TextView deviceIp, Switch connetSwitch, ImageButton deviceInfo) {
            this.deviceIp = deviceIp;
            this.connetSwitch = connetSwitch;
            this.deviceInfo = deviceInfo;
        }

    }
}