package com.example.mycareshoe.ui.settings;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.multidex.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class BluetoothConnectionActivity extends AsyncTask<Void, Void, Void> {
    public static boolean Connected = true;
    public static final int DONT_FINISH_TASK_WITH_ACTIVITY = 0;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String TAG = "Socket_Creation";
    Thread ListenInput = new Thread() {
        public void run() {
            while (BluetoothConnectionActivity.Connected && BluetoothConnectionActivity.this.running) {
                try {
                    BluetoothConnectionActivity.this.sendToReadHandler(BluetoothConnectionActivity.this.read());
                } catch (Exception e) {
                    Log.i(BluetoothConnectionActivity.TAG, "Not able to perform read");
                }
            }
        }

        public void interrupt() {
            currentThread().interrupt();
        }
    };
    private BluetoothSocket btsocket = null;
    private BluetoothAdapter mBluetoothadapter = null;
    private Handler mHandler;
    private ProgressDialog mProgressdialog;
    private Activity mactivity = null;
    private String maddress = null;
    private InputStream minstream;
    /* access modifiers changed from: private */
    public boolean running = true;

    BluetoothConnectionActivity(Activity activity, String address, Handler handler) {
        this.mactivity = activity;
        this.maddress = address;
        this.mHandler = handler;
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
        this.mProgressdialog = ProgressDialog.show(this.mactivity, "Connecting", "Please wait...");
    }

    /* access modifiers changed from: protected */
    public Void doInBackground(Void... devices) {
        try {
            if (isCancelled()) {
                return null;
            }
            if (this.btsocket != null && Connected) {
                return null;
            }
            BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
            this.mBluetoothadapter = defaultAdapter;
            BluetoothSocket createInsecureRfcommSocketToServiceRecord = defaultAdapter.getRemoteDevice(this.maddress).createInsecureRfcommSocketToServiceRecord(MY_UUID);
            this.btsocket = createInsecureRfcommSocketToServiceRecord;
            createInsecureRfcommSocketToServiceRecord.connect();
            this.minstream = this.btsocket.getInputStream();
            return null;
        } catch (Exception e) {
            Connected = false;
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Void result) {
        super.onPostExecute(result);
        if (!Connected) {
            Toast.makeText(this.mactivity.getApplicationContext(), "Connection failed", 0).show();
            this.mactivity.finish();
        } else {
            Toast.makeText(this.mactivity.getApplicationContext(), "Connected", 0).show();
            this.ListenInput.start();
        }
        this.mProgressdialog.dismiss();
    }

    /* access modifiers changed from: private */
    public String read() {
        try {
            byte[] buffer = new byte[1024];
            return new String(buffer, "ASCII").substring(0, this.minstream.read(buffer));
        } catch (IOException e) {
            Log.e(TAG, "Read failed");
            return BuildConfig.FLAVOR;
        }
    }

    public void sendToReadHandler(String s) {
        Message msg = Message.obtain();
        msg.obj = s;
        this.mHandler.sendMessage(msg);
    }

    public void disconnect() {
        BluetoothSocket bluetoothSocket = this.btsocket;
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
                isCancelled();
                this.running = false;
            } catch (IOException e) {
                Toast.makeText(this.mactivity.getApplicationContext(), "error", 0).show();
            }
        }
        Toast.makeText(this.mactivity.getApplicationContext(), "Disconnected", 0).show();
    }
}