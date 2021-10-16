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

        colorInfo.setText(new StringBuilder().append("In the gradient above, there are presented two color ").append("gradients, that depend on the sensor reading value (in kPa):\n").append("- From point 1 to point 2, represents the gradient from green to yellow, where the lowest value is 0 and the highest is ").append(yellowValue).append(";\n").append("- From point 2 to point 3, represents the gradient from yellow to red, where the lowest value is ").append(yellowValue).append(" and the highest is ").append(overPressureValue).append(";\n").append("- All values above ").append(overPressureValue).append(", have its sensor painted as red.").toString());

        return builder;

    }


}
