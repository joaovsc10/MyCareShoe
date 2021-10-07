package com.example.mycareshoe.ui.monitoring;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.mycareshoe.R;
import com.example.mycareshoe.data.model.SensorsReading;

import org.jetbrains.annotations.NotNull;

public class TemperatureHumidityFragment extends DialogFragment {
    private ImageView closeButton;
    private TextView leftFootTemperature;
    private TextView rightFootTemperature;
    private TextView leftFootHumidity;
    private TextView rightFootHumidity;
    private SensorsReading sr;
    private Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            leftFootTemperature.setText(getString(R.string.temperature_display, sr.getT1()));
            rightFootTemperature.setText(getString(R.string.temperature_display, sr.getT2()));
            leftFootHumidity.setText(getString(R.string.humidity_display, sr.getH1()));
            rightFootHumidity.setText(getString(R.string.humidity_display, sr.getH2()));
            handler.postDelayed(this, 0);
        }
    };

    public void setSr(SensorsReading sr) {
        this.sr = sr;
    }

    @Override
    public void onDismiss(@NonNull @NotNull DialogInterface dialog) {
        handler.removeCallbacks(runnable);
        super.onDismiss(dialog);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.temperature_humidity, new LinearLayout(getActivity()), false);


        // Build dialog
        Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setContentView(view);


        Bundle bundle = getArguments();
        setSr((SensorsReading) bundle.getSerializable("reading"));


        leftFootTemperature = (TextView) view.findViewById(R.id.leftFootTemperature);
        rightFootTemperature = (TextView) view.findViewById(R.id.rightFootTemperature);
        leftFootHumidity = (TextView) view.findViewById(R.id.leftFootHumidity);
        rightFootHumidity = (TextView) view.findViewById(R.id.rightFootHumidity);


        handler.postDelayed(runnable, 0);

        closeButton = view.findViewById(R.id.tempHumidity_exit);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(runnable);
                dismiss(); // <---- closes the dialog
            }
        });

        return builder;
    }


}
