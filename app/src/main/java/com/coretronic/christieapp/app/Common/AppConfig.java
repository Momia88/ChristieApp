package com.coretronic.christieapp.app.Common;

import com.coretronic.christieapp.app.R;

/**
 * Created by Morris on 15/1/22.
 */
public class AppConfig {

    public static final String PREFS_IP_SET = "PREFS_IP_SET";
    public static final String SHARED_PREFS_FILE = "SHARED_PREFS_FILE";
    public static final String PREFS_PRE_IP = "PREFS_PRE_IP";
    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // key number id
    public static final Integer[] ids = new Integer[]{
            R.id.key_0,
            R.id.key_1,
            R.id.key_2,
            R.id.key_3,
            R.id.key_4,
            R.id.key_5,
            R.id.key_6,
            R.id.key_7,
            R.id.key_8,
            R.id.key_9
    };
}
