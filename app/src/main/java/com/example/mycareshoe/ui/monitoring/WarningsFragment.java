package com.example.mycareshoe.ui.monitoring;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.mycareshoe.R;
import com.example.mycareshoe.data.Warnings;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.mycareshoe.data.Warnings.getNumOfWarnings;

public class WarningsFragment extends DialogFragment
{
    private ImageView closeButton;

    public WarningsFragment()
    {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View view = getActivity().getLayoutInflater().inflate(R.layout.warning_popup, new LinearLayout(getActivity()), false);


        // Build dialog
        Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setContentView(view);


        closeButton = view.findViewById(R.id.warning_exit);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss(); // <---- closes the dialog
            }
        });

        ListView lstView = (ListView) view.findViewById(R.id.warningListView);
        TextView emptyText = view.findViewById(R.id.empty);

        lstView.setEmptyView(emptyText);

        // Initializing a new String Array
        String[] warnings = new String[] {
                "Warning 1",
                "Warning 2"
        };

        // Create a List from String Array elements
        final List<String> warnings_list = new ArrayList<String>(Arrays.asList(warnings));

        // Create an ArrayAdapter from List
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (getActivity(), android.R.layout.simple_list_item_1, warnings_list);

        // DataBind ListView with items from ArrayAdapter
        lstView.setAdapter(arrayAdapter);
        if(getNumOfWarnings()==0)
            ;

        return builder;
    }
}
