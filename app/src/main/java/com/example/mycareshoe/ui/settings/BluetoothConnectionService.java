package com.example.mycareshoe.ui.settings;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class BluetoothConnectionService {

    private static final UUID UUID_INSECURE = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    private static final String TAG = "BluetoothConnectionServ";
    private final BluetoothAdapter bluetoothAdapter;

  //  private ConnectThread connectThread;
    private BluetoothDevice bluetoothDevice;
    private UUID deviceUUID;

    Context context;

    private AcceptThread insecureAcceptThread;

    public BluetoothConnectionService(Context context) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.context = context;
    }

    //thread that runs until a connection is made
    private class AcceptThread extends Thread {

        //local server socket
        private final BluetoothServerSocket serverSocket;

        public AcceptThread(){
            BluetoothServerSocket temp = null;

            //creating a new listening server socket

            try {
                temp = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("My Care Shoe", UUID_INSECURE);
                Log.d(TAG, "AcceptThread: Setting up server using: "+UUID_INSECURE);
            }catch( IOException e){
                Log.d(TAG, "AcceptThread: IOException: "+e.getMessage());
            }
            serverSocket = temp;


        }
        public void run(){
            Log.d(TAG, "run: AcceptThread running");

            BluetoothSocket bluetoothSocket = null;

            try {
                //will only return on a successful call or exception
                Log.d(TAG, "run: RFCOM server socket starting");
                bluetoothSocket = serverSocket.accept();
                Log.d(TAG, "run: RFCOM server socket connection accepted");
            }catch (IOException e){
                Log.d(TAG, "AcceptThread: IOException: "+e.getMessage());
            }

            if(bluetoothSocket!=null){
               // connected(bluetoothSocket, device);
            }
        }

        public void cancel(){
            try{
                Log.d(TAG, "cancel: canceling server socket");
                serverSocket.close();
            }catch (IOException e){
                Log.d(TAG, "cancel: close of AcceptThread ServerSocket failed: "+e.getMessage());
            }
        }
    }
}
