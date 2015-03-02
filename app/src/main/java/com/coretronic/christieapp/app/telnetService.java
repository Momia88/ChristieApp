package com.coretronic.christieapp.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class TelnetService {
    private static final String TAG = TelnetService.class.getSimpleName();
    private static Context context;
    private static TelnetClient telnetClient;
    private final Handler mHandler;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    private int mState;

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_CONNECTING = 1; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 2;  // now connected to a remote device

    public TelnetService(Context context, Handler handler) {
        this.context = context;
        telnetClient = new TelnetClient();
        mState = STATE_NONE;
        mHandler = handler;
    }

    private synchronized void setState(int state) {
        Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;
        // Give the new state to the Handler so the UI Activity can update
        mHandler.obtainMessage(AppConfig.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }
    /**
     * Return the current connection state.
     */
    public synchronized int getState() {
        return mState;
    }

    public void write(String str) {
        Log.d(TAG, "write");
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED)
                return;
            r = mConnectedThread;
        }
        // Perform the write unsynchronized
        r.write(str);
    }
    /**
     * Start the ConnectedThread to begin managing a Telnet connection
     *
     * @param remoteip   The Telnet ip address
     * @param remoteport The Telnet port
     */
    public synchronized void connect(String remoteip, int remoteport) {
        Log.d(TAG, "connect to: " + remoteip);
        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread != null) {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }
        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(remoteip, remoteport);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    public synchronized void connected(TelnetClient telnetClient) {
        Log.d(TAG, "connected");
        // Cancel the thread that completed the connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(telnetClient);
        mConnectedThread.start();
        // Send the name of the connected device back to the UI Activity
        Message msg = mHandler.obtainMessage(AppConfig.MESSAGE_DEVICE_NAME);
        Bundle bundle = new Bundle();
        bundle.putString(AppConfig.DEVICE_NAME, telnetClient.getCharsetName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);

        setState(STATE_CONNECTED);
    }

    private class ConnectThread extends Thread {
        private final String ipAddr;
        private final int port;

        public ConnectThread(String remoteip, int remoteport) {
            this.ipAddr = remoteip;
            this.port = remoteport;
        }

        public void run() {
            try {
                telnetClient.connect(ipAddr, port);
            } catch (IOException e) {
                try {
                    telnetClient.disconnect();
                } catch (IOException e2) {
                    Log.e(TAG, "telnet during connection failure", e2);
                }
                connectionFailed();
                return;
            }
            Log.d(TAG, "mConnectThread = null");

            // Reset the ConnectThread because we're done
            synchronized (TelnetService.this) {
                mConnectThread = null;
            }
            Log.d(TAG, "go to connected");

            // Start the connected thread
            connected(telnetClient);
        }

        public void cancel() {
            try {
                telnetClient.disconnect();
            } catch (IOException e) {
                Log.e(TAG, " socket failed", e);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final TelnetClient mTelnetClient;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        public ConnectedThread(TelnetClient telnetClient) {
            this.mTelnetClient = telnetClient;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = mTelnetClient.getInputStream();
                tmpOut = mTelnetClient.getOutputStream();
            } catch (Exception e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.d(TAG, "writer to screen");
            byte[] buffer = new byte[1024];
            int bytes;
            try {
                // 判斷沒有被中斷的時候
                while (true) {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // 刷新，發送
                    mHandler.obtainMessage(AppConfig.MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();
                }
            } catch (Exception e) {
                Log.e(TAG, "disconnected", e);
                connectionLost();
            }
        }

        public void write(String str) {
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(mmOutStream);
                outputStreamWriter.write(str);
                outputStreamWriter.flush();
//                mmOutStream.write(buffer);
                // Share the sent message back to the UI Activity
                mHandler.obtainMessage(AppConfig.MESSAGE_WRITE, -1, -1, str)
                        .sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                telnetClient.disconnect();
            } catch (IOException e) {
                Log.e(TAG, " socket failed", e);
            }
        }
    }

    private void connectionFailed() {
        Log.d(TAG, "connectionFailed");

        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(AppConfig.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(AppConfig.TOAST, "Unable to connect device");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        setState(STATE_NONE);
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        Log.d(TAG, "connectionLost");

        // Send a failure message back to the Activity
        Message msg = mHandler.obtainMessage(AppConfig.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(AppConfig.TOAST, "Device connection was lost");
        msg.setData(bundle);
        mHandler.sendMessage(msg);
        setState(STATE_NONE);
    }

    public void stop() {
        Log.d(TAG, "stop");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
        setState(STATE_NONE);
    }

    //------------------------------------------------------------
    // Example
//
//    public void startReadThread() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.d(TAG, "Telnet is connecting");
//                try {
//                    readThread(telnetClient.getInputStream());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//
//    public static void sendCommand(final Context mContext, final String key) {
//        try {
//            if (telnetClient.isConnected()) {
//                writeThread(telnetClient.getOutputStream(), key);
//                ((Activity) mContext).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(mContext, key, Toast.LENGTH_SHORT).show();
//                    }
//                });
//            } else {
//                Log.d(TAG, "Telnet is not connected");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void writeThread(final OutputStream remoteOutput, final String key) {
//        // 定義讀寫的線程
//        Thread writer;
//        // 定義讀線程的具體操作
//        writer = new Thread() {
//            @Override
//            public void run() {
//                int ch;
//                try {
//                    // 判斷沒有被中斷的時候
//                    Log.d(TAG, "send to remote");
//                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(remoteOutput);
////                        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
//                    outputStreamWriter.write(key);
//                    outputStreamWriter.flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        // 設置reader為後台運行
//        writer.setDaemon(true);
//        // 啟動reader線程
//        writer.start();
//    }
//
//    private static void readThread(final InputStream remoteInput) {
//        Thread reader;
//        // 定義寫線程的具體操作
//        reader = new Thread() {
//            @Override
//            public void run() {
//                int ch;
//                Log.d(TAG, "writer to screen");
//
//                StringBuilder builder = new StringBuilder();
//                try {
//                    // 把數據從輸入流複製到輸出流
////                        Util.copyStream(remoteInput,
////                                localOutput);
//                    // 判斷沒有被中斷的時候
//                    while ((ch = remoteInput.read()) != -1) {
//                        // 寫字節到遠程輸入裡面
//                        builder.append(Character.toString((char) ch));
//                        // 刷新，發送
////                        Message msg = new Message();
////                        Bundle bundle = new Bundle();
////                        bundle.putString("str", builder.toString());
////                        msg.setData(bundle);
////                        handler.sendMessage(msg);
//                    }
//                    Log.d(TAG, "-----out while read-----");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    // 發生異常的時候退出該操作
//                    System.exit(1);
//                }
//            }
//        };
//        // 設置writer線程
//        reader.setPriority(Thread.currentThread().getPriority() + 1);
//        // 啟動writer線程
//        reader.start();
//    }
}
