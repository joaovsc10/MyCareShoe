package com.example.mycareshoe.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mycareshoe.R;
import com.example.mycareshoe.ui.monitoring.MonitoringFragment;

import org.jetbrains.annotations.NotNull;

public class SettingsFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);


    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getResources().getString(R.string.settings_en));

        Button bluetooth= (Button) view.findViewById(R.id.bluetooth);


        bluetooth.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
      //          FragmentAddProduct fragmentAddProduct = FragmentAddProduct.newInstance();
      //          FragmentTransaction fragmentTransaction = ((AppCompatActivity)mcontext).getSupportFragmentManager().beginTransaction();Toast.makeText(getApplicationContext(), "You clicked Option 1", Toast.LENGTH_SHORT).show();
      //          setTitle(R.string.monitorization_en);
            }


    });

}
}
