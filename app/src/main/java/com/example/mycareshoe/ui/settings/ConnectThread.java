package com.example.mycareshoe.ui.settings;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class ConnectThread extends Thread {
    private BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private final BluetoothAdapter mmBluetoothAdapter;
    private final static UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    public ConnectThread(BluetoothDevice device, BluetoothAdapter BluetoothAdapter) {

        BluetoothSocket tmp = null;
        mmDevice = device;

        try {
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) { }
        mmSocket = tmp;
        mmBluetoothAdapter= BluetoothAdapter;
    }

    public void run() {
        mmBluetoothAdapter.cancelDiscovery();
        try {

            try {
                mmSocket =(BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(mmDevice,1);
                Log.d("String Key", "the value you want to see1");

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            mmSocket.connect();
            Log.d("String Key", "the value you want to see2");

        } catch (IOException connectException) {

            System.out.println(connectException.getMessage());
            try {
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }
        ConnectedThread mConnectedThread = new ConnectedThread(mmSocket);
        Log.d("String Key", "the value you want to see");
        mConnectedThread.start();
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}
