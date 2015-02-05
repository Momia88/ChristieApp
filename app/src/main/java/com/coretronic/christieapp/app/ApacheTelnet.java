package com.coretronic.christieapp.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.*;


public class ApacheTelnet extends ActionBarActivity {

    private static final String TAG = ApacheTelnet.class.getSimpleName();
    private TelnetClient tc;
    private Context context;
    private final IOUtil ioUtil = new IOUtil();
    private Map<Integer, String> keyMap;
    private SharedPreferences sharedPreferences;
    private Set<String> ipSet;
    private String remoteip = "10.2.24.195";
    private int remoteport = 3002;
//    private String remoteip = "192.168.1.100";
//    private int remoteport = 3002;
//    private String remoteip = "10.1.6.103";
//    private int remoteport = 3002;
//    private String remoteip = "10.100.1.12";
//    private int remoteport = 23;

    private EditText ipAddr;
    private EditText port;
    private Button btnConnect;
    private Button btnDisconnect;
    private TextView key1;
    private TextView key2;
    private TextView key3;
    private TextView key4;
    private TextView key5;
    private TextView key6;
    private TextView key7;
    private TextView key8;
    private TextView key9;
    private TextView key0;
    private TextView keyMenu;
    private TextView keyPowOn;
    private TextView keyUp;
    private TextView keyPowOff;
    private TextView keyLeft;
    private TextView keyDown;
    private TextView keyRight;
    private TextView keyEnter;
    private TextView keyExit;
    private TextView keyInput;
    private TextView keyAuto;
    private TextView keyInfo;
    private TextView keyFocusFar;
    private TextView keyFocusNear;
    private TextView keyZoomIn;
    private TextView keyLensShiftUp;
    private TextView keyZoomOut;
    private TextView keyLensShiftLeft;
    private TextView keyLensShiftDown;
    private TextView keyLensShiftRight;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String str = msg.getData().getString("str");
            Log.d(TAG, "response: " + str);
        }
    };
    private PopupWindow popupWindow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apache_telnet);
        context = this;
        sharedPreferences = context.getSharedPreferences(AppConfig.SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        ipSet = sharedPreferences.getStringSet(AppConfig.PREFS_IP_SET, null);
        if (ipSet == null) {
            Log.d(TAG, "ipSet == null");
            ipSet = new HashSet<String>();
        } else {
            for (String ip : ipSet) {
                Log.d(TAG, "ipSet:" + ip);
            }
        }
        keyMap = KeyMap.keyMap;
        initView();
        String str = sharedPreferences.getString(AppConfig.PREFS_PRE_IP, "");
        try {
            tc = new TelnetClient();
            if (!str.isEmpty()) {
                ipAddr.setText(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ipAddr.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (popupWindow == null) {
                    Log.d(TAG, "popupWindow == null");
                    initPopWindow();
                    popupWindow.showAsDropDown(ipAddr, 0, 0);
                } else {
                    if (popupWindow.isShowing()) {
                        dismissPopupWindow();
                    } else {
                        Log.d(TAG, "popupWindow show");

                        popupWindow.showAsDropDown(ipAddr, 0, 0);
                    }
                }
            }
        });
    }

    private void initView() {
        ipAddr = (EditText) findViewById(R.id.ipAddr);
        ipAddr.setText(remoteip);
        port = (EditText) findViewById(R.id.port);
        port.setText(String.valueOf(remoteport));
        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnDisconnect = (Button) findViewById(R.id.btnDisconnect);
        key1 = (TextView) findViewById(R.id.key_1);
        key2 = (TextView) findViewById(R.id.key_2);
        key3 = (TextView) findViewById(R.id.key_3);
        key4 = (TextView) findViewById(R.id.key_4);
        key5 = (TextView) findViewById(R.id.key_5);
        key6 = (TextView) findViewById(R.id.key_6);
        key7 = (TextView) findViewById(R.id.key_7);
        key8 = (TextView) findViewById(R.id.key_8);
        key9 = (TextView) findViewById(R.id.key_9);
        key0 = (TextView) findViewById(R.id.key_0);
        keyMenu = (TextView) findViewById(R.id.key_menu);
        keyPowOn = (TextView) findViewById(R.id.key_pow_on);
        keyUp = (TextView) findViewById(R.id.key_up);
        keyPowOff = (TextView) findViewById(R.id.key_pow_off);
        keyLeft = (TextView) findViewById(R.id.key_left);
        keyDown = (TextView) findViewById(R.id.key_down);
        keyRight = (TextView) findViewById(R.id.key_right);
        keyEnter = (TextView) findViewById(R.id.key_enter);
        keyExit = (TextView) findViewById(R.id.key_exit);
        keyInput = (TextView) findViewById(R.id.key_input);
        keyAuto = (TextView) findViewById(R.id.key_auto);
        keyInfo = (TextView) findViewById(R.id.key_info);
        keyFocusFar = (TextView) findViewById(R.id.key_focus_far);
        keyFocusNear = (TextView) findViewById(R.id.key_focus_near);
        keyZoomIn = (TextView) findViewById(R.id.key_zoom_in);
        keyLensShiftUp = (TextView) findViewById(R.id.key_lens_shift_up);
        keyZoomOut = (TextView) findViewById(R.id.key_zoom_out);
        keyLensShiftLeft = (TextView) findViewById(R.id.key_lens_shift_left);
        keyLensShiftDown = (TextView) findViewById(R.id.key_lens_shift_down);
        keyLensShiftRight = (TextView) findViewById(R.id.key_lens_shift_right);

        btnConnect.setOnClickListener(btnListener);
        btnDisconnect.setOnClickListener(btnListener);
        key1.setOnClickListener(keyListener);
        key2.setOnClickListener(keyListener);
        key3.setOnClickListener(keyListener);
        key4.setOnClickListener(keyListener);
        key5.setOnClickListener(keyListener);
        key6.setOnClickListener(keyListener);
        key7.setOnClickListener(keyListener);
        key8.setOnClickListener(keyListener);
        key9.setOnClickListener(keyListener);
        key0.setOnClickListener(keyListener);
        keyMenu.setOnClickListener(keyListener);
        keyPowOn.setOnClickListener(keyListener);
        keyUp.setOnClickListener(keyListener);
        keyPowOff.setOnClickListener(keyListener);
        keyLeft.setOnClickListener(keyListener);
        keyDown.setOnClickListener(keyListener);
        keyRight.setOnClickListener(keyListener);
        keyEnter.setOnClickListener(keyListener);
        keyExit.setOnClickListener(keyListener);
        keyInput.setOnClickListener(keyListener);
        keyAuto.setOnClickListener(keyListener);
        keyInfo.setOnClickListener(keyListener);
        keyFocusFar.setOnClickListener(keyListener);
        keyFocusNear.setOnClickListener(keyListener);
        keyZoomIn.setOnClickListener(keyListener);
        keyLensShiftUp.setOnClickListener(keyListener);
        keyZoomOut.setOnClickListener(keyListener);
        keyLensShiftLeft.setOnClickListener(keyListener);
        keyLensShiftDown.setOnClickListener(keyListener);
        keyLensShiftRight.setOnClickListener(keyListener);
    }

    View.OnClickListener btnListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnConnect:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "Telnet is connecting");
                            try {
                                remoteip = ipAddr.getText().toString();
                                remoteport = Integer.valueOf(port.getText().toString());
                                tc.connect(remoteip, remoteport);
                                ioUtil.readThread(tc.getInputStream());
                                String str = remoteip;
                                sharedPreferences.edit().putString(AppConfig.PREFS_PRE_IP, str).apply();
                                ipSet.add(str);
                                sharedPreferences.edit().putStringSet(AppConfig.PREFS_IP_SET, ipSet).apply();
                                Log.d(TAG, "Telnet is connected");

                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "Telnet is connected", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (IOException e) {
                                ((Activity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "Telnet didn't connected!!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
                case R.id.btnDisconnect:
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                tc.disconnect();
                                Log.d(TAG, "Telnet is disconnected");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
            }
        }
    };

    View.OnClickListener keyListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            final String keyValue = keyMap.get(v.getId());
            Log.d(TAG, "key: " + keyValue);
            sendCommand(keyValue);
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, keyValue, Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private void sendCommand(String key) {
        try {
            if (tc.isConnected()) {
                ioUtil.writeThread(tc.getOutputStream(), key);
            } else {
                Log.d(TAG, "Telnet is not connected");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class IOUtil {
        private String TAG = IOUtil.class.getSimpleName();

        public void writeThread(final OutputStream remoteOutput, final String key) {
            // 定義讀寫的線程
            Thread writer;
            // 定義讀線程的具體操作
            writer = new Thread() {
                @Override
                public void run() {
                    int ch;
                    try {
                        // 判斷沒有被中斷的時候
                        Log.d(TAG, "send to remote");
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(remoteOutput);
//                        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                        outputStreamWriter.write(key);
                        outputStreamWriter.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            // 設置reader為後台運行
            writer.setDaemon(true);
            // 啟動reader線程
            writer.start();
        }

        public void readThread(final InputStream remoteInput) {
            Thread reader;
            // 定義寫線程的具體操作
            reader = new Thread() {
                @Override
                public void run() {
                    int ch;
                    Log.d(TAG, "writer to screen");

                    StringBuilder builder = new StringBuilder();
                    try {
                        // 把數據從輸入流複製到輸出流
//                        Util.copyStream(remoteInput,
//                                localOutput);
                        // 判斷沒有被中斷的時候
                        while ((ch = remoteInput.read()) != -1) {
                            // 寫字節到遠程輸入裡面
                            builder.append(Character.toString((char) ch));
                            // 刷新，發送
                            Message msg = new Message();
                            Bundle bundle = new Bundle();
                            bundle.putString("str", builder.toString());
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                        Log.d(TAG, "-----out while read-----");
                    } catch (Exception e) {
                        e.printStackTrace();
                        // 發生異常的時候退出該操作
                        System.exit(1);
                    }
                }
            };
            // 設置writer線程
            reader.setPriority(Thread.currentThread().getPriority() + 1);
            // 啟動writer線程
            reader.start();
        }

    }

    private ListView lsvAccount;

    private void initPopWindow() {
        //獲取PopupWindow中的控件
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.popw_view, null);
        popupWindow = new PopupWindow(view, ipAddr.getWidth(), RelativeLayout.LayoutParams.WRAP_CONTENT);
        lsvAccount = (ListView) view.findViewById(R.id.lsvAccount);

        //綁定ListView的數據
        List<String> list = new ArrayList<String>(ipSet);
        IpListAdapter adapter = new IpListAdapter(context, list);
        lsvAccount.setAdapter(adapter);
        //想要讓PopupWindow中的控件能夠使用，就必須設置PopupWindow為focusable
        popupWindow.setFocusable(true);

        //想做到在你點擊PopupWindow以外的區域時候讓PopupWindow消失就做如下兩步操作
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        //設置PopupWindow消失的時候觸發的事件
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            public void onDismiss() {
                Toast.makeText(context, "dismiss", Toast.LENGTH_SHORT).show();
            }
        });

        //設置ListView點擊事件
        lsvAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String data = lsvAccount.getItemAtPosition(arg2).toString();
                ipAddr.setText(data);
                dismissPopupWindow();
            }
        });

        //顯示PopupWindow有3個方法
        //popupWindow.showAsDropDown(anchor)
        //popupWindow.showAsDropDown(anchor, xoff, yoff)
        //popupWindow.showAtLocation(parent, gravity, x, y)
        //需要注意的是以上三個方法必須在觸發事件中使用，比如在點擊某個按鈕的時候
    }

    //讓PopupWindow消失
    private void dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }
}
