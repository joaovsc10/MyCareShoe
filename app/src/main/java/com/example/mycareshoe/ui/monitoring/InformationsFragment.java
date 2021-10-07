package com.example.mycareshoe.ui.monitoring;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.mycareshoe.R;
import com.example.mycareshoe.helpers.SharedPrefManager;

public class InformationsFragment extends DialogFragment {


    private ImageView closeButton;
    private TextView colorInfo;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.informations_popup, new LinearLayout(getActivity()), false);


        // Build dialog
        Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setContentView(view);

        colorInfo = view.findViewById(R.id.colorInfo);
        closeButton = view.findViewById(R.id.informations_exit);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        int overPressureValue = SharedPrefManager.getInstance(getContext()).getOverPressureValue();
        int yellowValue = overPressureValue / 3;

        colorInfo.setText("In the gradient above, there are presented two color " +
                "gradients, that depend on the sensor reading value (in kPa):\n" +
                "- From point 1 to point 2, represents the gradient from green to yellow, where the lowest value is 0 and the highest is " + yellowValue + ";\n" +
                "- From point 2 to point 3, represents the gradient from yellow to red, where the lowest value is " + yellowValue + " and the highest is " + overPressureValue + ";\n" +
                "- All values above " + overPressureValue + ", have its sensor painted as red.");

        return builder;

    }


}
