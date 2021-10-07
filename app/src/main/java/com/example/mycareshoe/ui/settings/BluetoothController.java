
package com.example.mycareshoe.ui.settings;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.mycareshoe.helpers.URLs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothController {
    private static final String APP_NAME = "MyCareShoe";
    private static final UUID MY_UUID = URLs.MY_UUID;
    private static final UUID MY_UUID2 = URLs.MY_UUID2;

    private final BluetoothAdapter bluetoothAdapter;
    private final Handler handler;
    private AcceptThread acceptThreadLeft;
    private ConnectThread connectThreadLeft;
    private ConnectedThread connectedThreadLeft;
    private AcceptThread acceptThreadRight;
    private ConnectThread connectThreadRight;
    private ConnectedThread connectedThreadRight;
    private BluetoothSocket leftSocket;
    private BluetoothSocket rightSocket;
    private int state;

    static final int STATE_NONE = 0;
    static final int STATE_LISTEN = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;

    public BluetoothController(Context context, Handler handler) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        state = STATE_NONE;

        this.handler = handler;
    }


    public BluetoothSocket getLeftSocket() {
        return leftSocket;
    }

    public void setLeftSocket(BluetoothSocket leftSocket) {
        this.leftSocket = leftSocket;
    }

    public BluetoothSocket getRightSocket() {
        return rightSocket;
    }

    public void setRightSocket(BluetoothSocket rightSocket) {
        this.rightSocket = rightSocket;
    }

    // Set the current state of the chat connection
    private synchronized void setState(int state) {
        this.state = state;

        handler.obtainMessage(BluetoothFragment.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    // get current connection state
    public synchronized int getState() {
        return state;
    }


    // start service
    public synchronized void start(String foot) {


        ConnectThread connectThread = null;
        ConnectedThread connectedThread = null;
        AcceptThread acceptThread = null;

        if (foot.equals("L")) {

            connectThread = connectThreadLeft;
            connectedThread = connectedThreadLeft;
            acceptThread = acceptThreadLeft;
        } else {

            connectThread = connectThreadRight;
            connectedThread = connectedThreadRight;
            acceptThread = acceptThreadRight;
        }

        // Cancel any thread
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        // Cancel any running thread
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        setState(STATE_LISTEN);
        if (acceptThread == null) {
            acceptThread = new AcceptThread(foot);
            acceptThread.start();
        }
    }

    // initiate connection to remote device
    public synchronized void connect(BluetoothDevice device, String foot) throws IOException {

        ConnectThread connectThread = null;
        ConnectedThread connectedThread = null;
        AcceptThread acceptThread = null;

        if (foot.equals("L")) {
            if (getLeftSocket() != null) {
                getLeftSocket().close();
            }
            connectThread = connectThreadLeft;
            connectedThread = connectedThreadLeft;
            acceptThread = acceptThreadLeft;
        } else {
            if (getRightSocket() != null) {
                getRightSocket().close();
            }
            connectThread = connectThreadRight;
            connectedThread = connectedThreadRight;
            acceptThread = acceptThreadRight;
        }

        // Cancel any thread
        if (state == STATE_CONNECTING) {
            if (connectThread != null) {
                connectThread.cancel();
                connectThread = null;
            }
        }

        // Cancel running thread
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        // Start the thread to connect with the given device
        connectThread = new ConnectThread(device, foot);
        connectThread.start();
        setState(STATE_CONNECTING);
    }

    // manage Bluetooth connection
    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device, String foot) {

        ConnectThread connectThread = null;
        ConnectedThread connectedThread = null;
        AcceptThread acceptThread = null;

        if (foot.equals("L")) {
            connectThread = connectThreadLeft;
            connectedThread = connectedThreadLeft;
            acceptThread = acceptThreadLeft;
        } else {
            connectThread = connectThreadRight;
            connectedThread = connectedThreadRight;
            acceptThread = acceptThreadRight;
        }

        // Cancel the thread
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        // Cancel running thread
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        connectedThread = new ConnectedThread(socket, foot);
        connectedThread.start();

        // Send the name of the connected device back to the UI Activity
        Message msg = handler.obtainMessage(BluetoothFragment.MESSAGE_DEVICE_OBJECT);
        Bundle bundle = new Bundle();
        if (foot.equals("L"))
            bundle.putParcelable(BluetoothFragment.DEVICE_OBJECT_LEFT, device);
        else
            bundle.putParcelable(BluetoothFragment.DEVICE_OBJECT_RIGHT, device);
        msg.setData(bundle);
        handler.sendMessage(msg);

        setState(STATE_CONNECTED);
    }

    // stop all threads
    public synchronized void stop(String foot) throws IOException {

        ConnectThread connectThread = null;
        ConnectedThread connectedThread = null;
        AcceptThread acceptThread = null;

        if (foot.equals("L")) {
            connectThread = connectThreadLeft;
            connectedThread = connectedThreadLeft;
            acceptThread = acceptThreadLeft;
            if (getLeftSocket() != null) {
                getLeftSocket().close();
            }
        } else {
            connectThread = connectThreadRight;
            connectedThread = connectedThreadRight;
            acceptThread = acceptThreadRight;
            if (getRightSocket() != null) {
                getRightSocket().close();
            }
        }

        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }
        setState(STATE_NONE);

    }

    public void write(byte[] out, String foot) {
        ConnectedThread connectedThread = null;

        if (foot.equals("L"))
            connectedThread = connectedThreadLeft;
        else
            connectedThread = connectedThreadRight;
        ConnectedThread r;
        synchronized (this) {
            if (state != STATE_CONNECTED)
                return;
            r = connectedThread;
        }
        r.write(out);
    }

    void connectionFailed(String foot) {
        Message msg = handler.obtainMessage(BluetoothFragment.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString("toast", "Unable to connect device");
        msg.setData(bundle);
        handler.sendMessage(msg);

        // Start the service over to restart listening mode
        BluetoothController.this.start(foot);
    }

    private void connectionLost(String foot) {
        Message msg = handler.obtainMessage(BluetoothFragment.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString("toast", "Device connection was lost in " + foot + " foot");
        msg.setData(bundle);
        handler.sendMessage(msg);

        // Start the service over to restart listening mode
        BluetoothController.this.start(foot);
    }


    private class ConnectThread extends Thread {
        private BluetoothSocket socketL = null;
        private BluetoothDevice deviceL = null;
        private BluetoothSocket socketR = null;
        private BluetoothDevice deviceR = null;
        private String footSide;


        public ConnectThread(BluetoothDevice device, String foot) throws IOException {
            footSide = foot;
            if (foot.equals("L")) {

                if (getLeftSocket() != null) {
                    getLeftSocket().close();
                }
                this.deviceL = device;
            } else {
                if (getRightSocket() != null) {
                    getRightSocket().close();
                }
                this.deviceR = device;
            }
            BluetoothSocket tmp = null;
            try {
                tmp = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (foot.equals("L")) {

                socketL = tmp;
                setLeftSocket(socketL);
            } else
                socketR = tmp;
            setRightSocket(socketR);
        }

        public void run() {
            setName("ConnectThread");


            // Always cancel discovery because it will slow down a connection
            bluetoothAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            if (footSide.equals("L")) {
                try {
                    socketL.connect();
                } catch (IOException e) {
                    try {
                        socketL.close();
                    } catch (IOException e2) {
                    }
                    connectionFailed(footSide);
                    return;
                }
            } else {
                try {
                    socketR.connect();
                } catch (IOException e) {
                    try {
                        socketR.close();
                    } catch (IOException e2) {
                    }
                    connectionFailed(footSide);
                    return;
                }
            }


            // Reset the ConnectThread because we're done
            synchronized (BluetoothController.this) {
                if (footSide.equals("L"))
                    connectThreadLeft = null;
                else
                    connectThreadRight = null;

            }

            // Start the connected thread
            if (footSide.equals("L")) {
                connected(socketL, deviceL, footSide);
            } else
                connected(socketR, deviceR, footSide);

        }

        public void cancel() {
            try {
                socketR.close();
            } catch (IOException e) {
            }
        }
    }

    // runs while listening for incoming connections
    private class AcceptThread extends Thread {
        private BluetoothServerSocket serverSocketL = null;
        private BluetoothServerSocket serverSocketR = null;

        private final String footSide;

        public AcceptThread(String foot) {
            footSide = foot;

            BluetoothServerSocket tmp = null;
            try {
                if (foot.equals("L")) {
                    tmp = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(APP_NAME, MY_UUID);
                    serverSocketL = tmp;
                } else {
                    tmp = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(APP_NAME, MY_UUID2);
                    serverSocketR = tmp;
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }


        }

        public void run() {
            setName("AcceptThread");
            BluetoothSocket socketL = null;
            BluetoothSocket socketR = null;
            BluetoothSocket socket = null;
            while (state != STATE_CONNECTED) {
                try {
                    if (footSide.equals("L")) {
                        socketL = serverSocketL.accept();

                        socket = socketL;
                    } else {
                        socketR = serverSocketR.accept();
                        socket = socketR;
                    }

                } catch (IOException e) {
                    break;
                }

                // If a connection was accepted
                if (socket != null) {
                    synchronized (BluetoothController.this) {
                        switch (state) {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                // start the connected thread.
                                if (footSide.equals("L")) {
                                    connected(socketL, socketL.getRemoteDevice(), footSide);

                                } else {
                                    connected(socketR, socketR.getRemoteDevice(), footSide);
                                }
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                // Either not ready or already connected. Terminate
                                // new socket.
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                }
                                break;
                        }
                    }
                }
            }
        }

        public void cancel() {
            try {
                if (footSide.equals("L"))
                    serverSocketL.close();
                else
                    serverSocketR.close();
            } catch (IOException e) {
            }
        }
    }


    // runs during a connection with a remote device
    private class ConnectedThread extends Thread {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream inputStream;
        private final OutputStream outputStream;
        private String foot;

        public ConnectedThread(BluetoothSocket socket, String foot) {
            this.bluetoothSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            this.foot = foot;
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            inputStream = tmpIn;
            outputStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = inputStream.read(buffer);

                    // Send the obtained bytes to the UI Activity
                    handler.obtainMessage(BluetoothFragment.MESSAGE_READ, bytes, -1,
                            buffer).sendToTarget();
                } catch (IOException e) {
                    connectionLost(foot);
                    // Start the service over to restart listening mode
                    BluetoothController.this.start(foot);
                    break;
                }
            }
        }

        // write to OutputStream
        public void write(byte[] buffer) {
            try {
                outputStream.write(buffer);
                System.out.println(handler.obtainMessage(BluetoothFragment.MESSAGE_WRITE, -1, -1,
                        buffer).toString());
                handler.obtainMessage(BluetoothFragment.MESSAGE_WRITE, -1, -1,
                        buffer).sendToTarget();
            } catch (IOException e) {
            }
        }

        public void cancel() {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
