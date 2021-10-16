package com.example.mycareshoe.ui.settings;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mycareshoe.R;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SettingsFragment extends Fragment {

    public BluetoothFragment bluetoothFragment = new BluetoothFragment();
    private AccountSettingsFragment accountSettingsFragment = new AccountSettingsFragment();
    private Button accountSettings;

    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_settings, container, false);


    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getResources().getString(R.string.settings_en));

        Button bluetooth = (Button) view.findViewById(R.id.bluetooth);

        accountSettings = (Button) view.findViewById(R.id.accountSettingsBtn);
        try {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            Method getUuidsMethod = BluetoothAdapter.class.getDeclaredMethod("getUuids", null);
            ParcelUuid[] uuids = (ParcelUuid[]) getUuidsMethod.invoke(adapter, null);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.contentFragmente);

                frameLayout.removeAllViews();
                getParentFragmentManager().beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), bluetoothFragment).addToBackStack("Bluetooth").commit();

            }
        });

        accountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.contentFragmente);

                frameLayout.removeAllViews();
                getParentFragmentManager().beginTransaction().replace(((ViewGroup) getView().getParent()).getId(), accountSettingsFragment).addToBackStack("AccountSettings").commit();

            }
        });


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);


    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {


        }

    }


}
