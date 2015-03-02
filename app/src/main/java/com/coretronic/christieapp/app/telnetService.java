package com.coretronic.christieapp.app;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class TelnetService {
    private static final String TAG = TelnetService.class.getSimpleName();
    private static Context context;
    private static TelnetClient telnetClient;

    public TelnetService(Context context) {
        this.context = context;
        telnetClient = new TelnetClient();
    }

    public static boolean isConnected() {
        return telnetClient.isConnected();
    }

    public void connect(final String remoteip, final int remoteport) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    telnetClient.connect(remoteip, remoteport);
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
    }

    public void disConnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    telnetClient.disconnect();
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "Telnet is disconnected!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void startReadThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Telnet is connecting");
                try {
                    readThread(telnetClient.getInputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public static void sendCommand(final Context mContext, final String key) {
        try {
            if (telnetClient.isConnected()) {
                writeThread(telnetClient.getOutputStream(), key);
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, key, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Log.d(TAG, "Telnet is not connected");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeThread(final OutputStream remoteOutput, final String key) {
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

    private static void readThread(final InputStream remoteInput) {
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
//                        Message msg = new Message();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("str", builder.toString());
//                        msg.setData(bundle);
//                        handler.sendMessage(msg);
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
