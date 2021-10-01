package com.example.mycareshoe.ui.settings;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mycareshoe.R;
import com.example.mycareshoe.data.model.SensorsReading;
import com.example.mycareshoe.helpers.SharedPrefManager;
import com.example.mycareshoe.ui.monitoring.MonitoringFragment;
import com.example.mycareshoe.ui.monitoring.WarningsFragment;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class BluetoothFragment extends Fragment {


    private ArrayAdapter aAdapter;
    private BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
    String leftDevice;
    String rightDevice;
    BluetoothDevice leftFootDevice;
    BluetoothDevice rightFootDevice;
    Set<BluetoothDevice> pairedDevices;
    private boolean leftDeviceSelected=false;
    private boolean rightDeviceSelected=false;
    private int aa=0;
    private int bb=0;
    private int cc=0;
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_OBJECT = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final String DEVICE_OBJECT_LEFT = "device_name_left";
    public static final String DEVICE_OBJECT_RIGHT = "device_name_right";

    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private BluetoothController bluetoothControllerLeft;
    private BluetoothController bluetoothControllerRight;
    public SensorsReading sr = new SensorsReading(SharedPrefManager.getInstance(getContext()).getOverPressureValue());
    private ArrayList<String[]> arrayListLeft = new ArrayList<>();
    private ArrayList<String[]> arrayListRight= new ArrayList<>();
    private WarningsFragment warningsFragment= new WarningsFragment();
    private MonitoringFragment monitoring= new MonitoringFragment();
    private TextView leftFootStatusTextView;
    private TextView rightFootStatusTextView;
    private boolean leftFootConnected;
    private boolean rightFootConnected;

    public BluetoothController getBluetoothControllerLeft() {
        return bluetoothControllerLeft;
    }

    public BluetoothController getBluetoothControllerRight() {
        return bluetoothControllerRight;
    }

    public boolean isLeftFootConnected() {
        return leftFootConnected;
    }

    public void setLeftFootConnected(boolean leftFootConnected) {
        this.leftFootConnected = leftFootConnected;
    }

    public boolean isRightFootConnected() {
        return rightFootConnected;
    }

    public void setRightFootConnected(boolean rightFootConnected) {
        this.rightFootConnected = rightFootConnected;
    }

    public SensorsReading getSr() {
        return sr;
    }

    public void setSr(SensorsReading sr) {
        this.sr = sr;
    }

    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.bluetooth, container, false);


    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);


        getActivity().setTitle(getResources().getString(R.string.bluetooth_en));

        Button cancelBtn= (Button) view.findViewById(R.id.blCancelButton);
        Button saveBtn= (Button) view.findViewById(R.id.blSaveButton);
        leftFootStatusTextView = view.findViewById(R.id.leftFootStatus);
        rightFootStatusTextView = view.findViewById(R.id.rightFootStatus);

        if (savedInstanceState != null) {

            setLeftFootConnected(savedInstanceState.getBoolean("leftFootStatus"));
            setRightFootConnected(savedInstanceState.getBoolean("rightFootStatus"));

        }

        if(savedInstanceState!=null || (leftFootStatusTextView.getText()!=null && rightFootStatusTextView.getText()!=null))
        {
            setConnectionStatus(leftFootConnected, leftFootStatusTextView);
            setConnectionStatus(rightFootConnected, rightFootStatusTextView);
        }


        saveBtn.setEnabled(false);
        //get the spinner from the xml
        Spinner dropdownL = view.findViewById(R.id.spinnerBluetoothL);
        Spinner dropdownR = view.findViewById(R.id.spinnerBluetoothR);
        ArrayList list = new ArrayList();


        if(bAdapter==null){
            Toast.makeText(getActivity(),"Bluetooth Not Supported",Toast.LENGTH_SHORT).show();
        }
        else {
            if (!bAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(bAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            } else {
                pairedDevices = bAdapter.getBondedDevices();

                if (pairedDevices.size() == 0) {
                    Toast.makeText(getActivity(), "No paired devices", Toast.LENGTH_SHORT).show();
                }
                if (pairedDevices.size() > 0) {
                    list.add(0, getResources().getString(R.string.select_item_dopdown_en)); //Add element at 0th index
                    for (BluetoothDevice device : pairedDevices) {

                        String devicename = device.getName();
                        String macAddress = device.getAddress();
                        list.add("Name: " + devicename + "MAC Address: " + macAddress);

                    }

                    //create a list of items for the spinner

                    //create an adapter to describe how the items are displayed, adapters are used in several places in android
                    aAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, list);
                    //set the spinners adapter to the previously created one
                    dropdownL.setAdapter(aAdapter);
                    dropdownR.setAdapter(aAdapter);


                } else {
                    list.add(0, "Select Item"); //Add element at 0th index
                }
            }

            dropdownL.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                   leftDevice=list.get(position).toString();

                    if(!leftDevice.equals(getResources().getString(R.string.select_item_dopdown_en)))
                    {
                        leftDeviceSelected=true;

                    }else{

                        leftDeviceSelected=false;
                    }


                    for (BluetoothDevice device : pairedDevices) {

                        if(leftDevice.contains(device.getAddress())){
                            leftFootDevice=device;
                        }

                    }

                    checkSelectedDevices(leftDeviceSelected, rightDeviceSelected, saveBtn);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {


                }

            });

            dropdownR.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {

                    rightDevice=list.get(position).toString();

                    if(!rightDevice.equals(getResources().getString(R.string.select_item_dopdown_en)))
                    {
                        rightDeviceSelected=true;
                    }else{
                        rightDeviceSelected=false;
                    }

                    for (BluetoothDevice device : pairedDevices) {

                        if(rightDevice.contains(device.getAddress())){
                            rightFootDevice=device;
                        }


                    }

                    checkSelectedDevices(leftDeviceSelected, rightDeviceSelected, saveBtn);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });
        }



        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().popBackStackImmediate();

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bAdapter.cancelDiscovery();


                try {
                    connectToDevice(rightFootDevice,leftFootDevice);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    private void connectToDevice(BluetoothDevice rightFootdevice, BluetoothDevice leftFootdevice) throws IOException {
        bAdapter.cancelDiscovery();

        if(bluetoothControllerRight.getRightSocket()!=null || bluetoothControllerLeft.getLeftSocket()!=null) {
            bluetoothControllerLeft.stop("L");
            bluetoothControllerRight.stop("R");
        }
        bluetoothControllerLeft.connect(leftFootdevice, "L");
        bluetoothControllerRight.connect(rightFootdevice, "R");

        }

    public void checkSelectedDevices(boolean leftDeviceSelected, boolean rightDeviceSelected, Button btn){

        if( leftDeviceSelected && rightDeviceSelected){

            btn.setEnabled(true);

        }
        else{

            btn.setEnabled(false);

        }

    }
private Handler setHandler(String foot) {

        String device;
        Boolean connectionStatus;

        if(foot.equals("L")){
            device=DEVICE_OBJECT_LEFT;
            connectionStatus= isLeftFootConnected();
        }
        else{
            device=DEVICE_OBJECT_RIGHT;
            connectionStatus= isRightFootConnected();
        }
    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothController.STATE_CONNECTED:
    //                        Toast.makeText(getContext(), "Connected to: " + bluetoothDevice[0].getName(),
     //                               Toast.LENGTH_SHORT).show();

                            setConnectionStatus(leftFootStatusTextView, rightFootStatusTextView, device, true);
                            break;
                        case BluetoothController.STATE_CONNECTING:
                            Toast.makeText(getContext(), "Connecting...",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothController.STATE_LISTEN:

                            if(connectionStatus==null || !connectionStatus)
                            {
                                setConnectionStatus(leftFootStatusTextView, rightFootStatusTextView, device, false);
                            }
                        case BluetoothController.STATE_NONE:

                            setConnectionStatus(leftFootStatusTextView, rightFootStatusTextView, device, false);
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;

                    String writeMessage = new String(writeBuf);
                    break;
                case MESSAGE_READ:

                    byte[] readBuf = (byte[]) msg.obj;

                    String readMessage = new String(readBuf, 0, msg.arg1);

                    String[] arrofs = readMessage.split("\\|");


                    if(arrofs.length==15){
                        if(device.equals(DEVICE_OBJECT_LEFT)){

                             arrayListLeft.add(arrofs);}
                        else{

                            arrayListRight.add(arrofs);}
                    }

                    if(arrayListLeft.size()!=0 && arrayListRight.size()!=0){

                        while(arrayListLeft.size()!=0 && arrayListRight.size()!=0) {

                            sr.setLeftFootSensors(arrayListLeft.get(0));
                            sr.setRightFootSensors(arrayListRight.get(0));
                            monitoring.createReading(sr);
                            sr = new SensorsReading(SharedPrefManager.getInstance(getContext()).getOverPressureValue());
                            arrayListLeft.remove(0);
                            arrayListRight.remove(0);
                        }

                    }
                    break;
                case MESSAGE_DEVICE_OBJECT:
                    setConnectionStatus(leftFootStatusTextView, rightFootStatusTextView, device, true);
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getContext(), msg.getData().getString("toast"),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

        return handler;
}


    Thread one = new Thread() {
        public void run() {
            while(arrayListLeft.size()!=0 && arrayListRight.size()!=0) {
                System.out.println(arrayListLeft.size());
                System.out.println(arrayListRight.size());
                sr.setLeftFootSensors(arrayListLeft.get(0));
                sr.setRightFootSensors(arrayListRight.get(0));
                monitoring.createReading(sr);
                sr = new SensorsReading(SharedPrefManager.getInstance(getContext()).getOverPressureValue());
                arrayListLeft.remove(0);
                arrayListRight.remove(0);
            }

        }
    };
    @Override
    public void onStart() {
        super.onStart();
        if (!bAdapter.isEnabled()) {
            Intent enableIntent = new Intent(bAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
        } else {
                if ((bluetoothControllerLeft==null || bluetoothControllerLeft.getLeftSocket() == null) && !isLeftFootConnected()){
                    bluetoothControllerLeft = new BluetoothController(getContext(), setHandler("L"));
                }


                if ((bluetoothControllerRight==null || bluetoothControllerRight.getRightSocket() == null) && !isRightFootConnected()){
                    bluetoothControllerRight = new BluetoothController(getContext(), setHandler("R"));}

            }

    }

    @Override
    public void onResume() {
        super.onResume();

        if (bluetoothControllerLeft != null) {
            if (bluetoothControllerLeft.getState() == BluetoothController.STATE_NONE) {

                    bluetoothControllerLeft.start("L");

            }
        }

        if (bluetoothControllerRight != null) {
            if (bluetoothControllerRight.getState() == BluetoothController.STATE_NONE) {

                    bluetoothControllerRight.start("R");

            }
        }
    }



    private void setConnectionStatus(TextView textViewLeft, TextView textViewRight, String foot, boolean connected){


        TextView textView;
        String status=getResources().getString(R.string.disconnected_status_en);

        if(foot.equals(DEVICE_OBJECT_LEFT)){

            textView=textViewLeft;
        }
        else {

            textView=textViewRight;
        }


        if(foot.equals(DEVICE_OBJECT_LEFT)) {
            setLeftFootConnected(connected);

        }else {
            setRightFootConnected(connected);
        }

        setConnectionStatus(connected, textView);


    }

    private void setConnectionStatus(Boolean connectionStatus, TextView footConnectionTextView){

        if(connectionStatus)
        {
            footConnectionTextView.setText(R.string.connected_status_en);
            footConnectionTextView.setTextColor(Color.GREEN);
        }
        else{
            footConnectionTextView.setText(R.string.disconnected_status_en);
            footConnectionTextView.setTextColor(Color.RED);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putBoolean("leftFootStatus", isLeftFootConnected());
        outState.putBoolean("rightFootStatus", isRightFootConnected());
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            String a=savedInstanceState.getString("leftFootStatus");
            setLeftFootConnected(savedInstanceState.getBoolean("leftFootStatus"));
            setRightFootConnected(savedInstanceState.getBoolean("rightFootStatus"));

        }

    }

}
