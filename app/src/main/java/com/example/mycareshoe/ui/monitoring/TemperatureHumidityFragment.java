package com.example.mycareshoe.ui.monitoring;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.mycareshoe.R;
import com.example.mycareshoe.data.Warnings;
import com.example.mycareshoe.helpers.SharedPrefManager;
import com.example.mycareshoe.helpers.URLs;
import com.example.mycareshoe.ui.login.JSONParser;
import com.example.mycareshoe.ui.settings.BluetoothFragment;
import com.google.gson.JsonArray;

import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.example.mycareshoe.data.Warnings.getNumOfWarnings;

public class TemperatureHumidityFragment extends DialogFragment
{
    private ImageView closeButton;
    private TextView leftFootTemperature;
    private TextView rightFootTemperature;
    private TextView leftFootHumidity;
    private TextView rightFootHumidity;

    // Initializing a new String Array
    ArrayList<String> warnings = new ArrayList<String>();





    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        View view = getActivity().getLayoutInflater().inflate(R.layout.temperature_humidity, new LinearLayout(getActivity()), false);



        // Build dialog
        Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setContentView(view);


        leftFootTemperature = (TextView) view.findViewById(R.id.leftFootTemperature);
        rightFootTemperature = (TextView) view.findViewById(R.id.rightFootTemperature);
        leftFootHumidity = (TextView) view.findViewById(R.id.leftFootHumidity);
        rightFootHumidity = (TextView) view.findViewById(R.id.rightFootHumidity);

        leftFootTemperature.setText("38ºC");
        rightFootTemperature.setText("38ºC");
        leftFootHumidity.setText("50%");
        rightFootHumidity.setText("60%");
        closeButton = view.findViewById(R.id.tempHumidity_exit);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss(); // <---- closes the dialog
            }
        });





        return builder;
    }

    private void loadWarnings(ListView listView) {
        class loadWarnings extends AsyncTask<Void, Void, JSONObject> {

            ProgressBar progressBar;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(JSONObject objs) {



                super.onPostExecute(objs);

            }


            @Override
            protected JSONObject doInBackground(Void... voids) {
                //creating request handler object
                JSONParser jsonParser = new JSONParser();
                //creating request parameters
                ArrayList params = new ArrayList();
                params.add(new BasicNameValuePair("patient_number", Integer.toString(SharedPrefManager.getInstance(getContext()).getUser(true).getPatient_number())));

                //returning the response
                return jsonParser.makeHttpRequest(URLs.URL_GET_WARNINGS,"GET", params);
            }
        }
        loadWarnings warnings = new loadWarnings();
        warnings.execute();
    }
}
