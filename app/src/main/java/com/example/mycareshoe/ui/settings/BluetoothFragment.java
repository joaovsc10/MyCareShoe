package com.example.mycareshoe.ui.settings;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.mycareshoe.R;

import org.jetbrains.annotations.NotNull;

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
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
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

                getChildFragmentManager().popBackStack("Bluetooth", 0);

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), leftDevice, Toast.LENGTH_SHORT).show();
                ConnectThread mConnectThread = new ConnectThread(leftFootDevice, bAdapter);
                mConnectThread.start();
            }
        });








    }

    public void checkSelectedDevices(boolean leftDeviceSelected, boolean rightDeviceSelected, Button btn){

        if( leftDeviceSelected && rightDeviceSelected){

            btn.setEnabled(true);

        }
        else{

            btn.setEnabled(false);

        }

    }
}
