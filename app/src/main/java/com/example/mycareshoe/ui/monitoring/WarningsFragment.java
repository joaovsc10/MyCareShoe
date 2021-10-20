package com.example.mycareshoe.ui.monitoring;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.mycareshoe.R;
import com.example.mycareshoe.helpers.SharedPrefManager;
import com.example.mycareshoe.service.HTTPRequest;
import com.example.mycareshoe.service.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;

public class WarningsFragment extends DialogFragment {
    private ImageView closeButton;
    private
    // Initializing a new String Array
    ArrayList<String> warnings = new ArrayList<String>();
    private List<String> warnings_list;
    private List<String> warnings_date_list;
    private Map<String, String> warningsTreeMap =
            new TreeMap<String, String>(Collections.reverseOrder());

    public List<String> getWarnings_date_list() {
        return warnings_date_list;
    }

    public void setWarnings_date_list(List<String> warnings_date_list) {
        this.warnings_date_list = warnings_date_list;
    }

    public List<String> getWarnings_list() {
        return warnings_list;
    }

    public void setWarnings_list(List<String> warnings_list) {
        this.warnings_list = warnings_list;
    }

    public WarningsFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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


        lstView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                dismiss();
                WarningDetailsFragment detailsFragment = new WarningDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("warning_date", warningsTreeMap.keySet().toArray()[position].toString());
                bundle.putString("sensors", warningsTreeMap.get(warningsTreeMap.keySet().toArray()[position]));
                detailsFragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, detailsFragment).addToBackStack("WarningDetail").commit();
                getActivity().setTitle(lstView.getAdapter().getItem(position).toString());

            }


        });

        TextView emptyText = view.findViewById(R.id.empty);
        emptyText.setVisibility(View.GONE);

        loadWarnings(lstView, emptyText, (ProgressBar) view.findViewById(R.id.progressBar));
        return builder;
    }

    private void loadWarnings(ListView listView, TextView emptyText, ProgressBar progressBar) {
        class loadWarnings extends AsyncTask<Void, Void, JSONObject> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(JSONObject objs) {

                try {
                    if (objs == null) {
                        Toast.makeText(getContext(), "Error in warning loading!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        JSONArray array = objs.optJSONArray("readings");
                        if (array != null) {
                            for (int i = 0; i < array.length(); i++) {
                                if (warningsTreeMap.containsKey(array.getJSONObject(i).getString("warning_date"))) {
                                    warningsTreeMap.replace(array.getJSONObject(i).getString("warning_date"), warningsTreeMap.get(array.getJSONObject(i).getString("warning_date")) + "," + array.getJSONObject(i).getString("sensor"));
                                } else {
                                    warningsTreeMap.put(array.getJSONObject(i).getString("warning_date"), array.getJSONObject(i).getString("sensor"));
                                }
                            }

                            // Create a List from String Array elements
                            setWarnings_date_list(new ArrayList<String>(warningsTreeMap.keySet()));


                            if (warningsTreeMap.size() > 0 && getContext() != null) {
                                // Create an ArrayAdapter from List
                                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                                        (getActivity(), android.R.layout.simple_list_item_1, getWarnings_date_list());


                                // DataBind ListView with items from ArrayAdapter
                                listView.setAdapter(arrayAdapter);
                            }
                        } else {

                            listView.setEmptyView(emptyText);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progressBar.setVisibility(View.GONE);

                super.onPostExecute(objs);

            }


            @Override
            protected JSONObject doInBackground(Void... voids) {

                //creating request handler object
                HTTPRequest httpRequest = new HTTPRequest();
                HttpUrl.Builder urlBuilder
                        = HttpUrl.parse(URLs.URL_GET_WARNINGS).newBuilder();

                urlBuilder.addQueryParameter("patient_number", Integer.toString(SharedPrefManager.getInstance(getContext()).getPatient(true).getPatient_number()));

                //returning the response
                return httpRequest.makeHttpRequest(URLs.URL_GET_WARNINGS, "GET", null, urlBuilder);
            }
        }
        loadWarnings warnings = new loadWarnings();
        warnings.execute();
    }


    public void createWarning(String sensor, String date) {
        class createWarning extends AsyncTask<Void, Void, JSONObject> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(JSONObject objs) {

                if (objs == null && getContext()!=null)
                    Toast.makeText(getContext(), "Error creating a warning!",
                            Toast.LENGTH_SHORT).show();
                super.onPostExecute(objs);

            }


            @Override
            protected JSONObject doInBackground(Void... voids) {

                //creating request handler object
                HTTPRequest httpRequest = new HTTPRequest();
                FormBody.Builder formBuilder = new FormBody.Builder();

                formBuilder.add("patient_number", Integer.toString(SharedPrefManager.getInstance(getContext()).getPatient(true).getPatient_number()));
                formBuilder.add("sensor", sensor);
                formBuilder.add("warning_date", date);

                //returning the response
                return httpRequest.makeHttpRequest(URLs.URL_CREATE_WARNING, "POST", formBuilder, null);
            }
        }
        createWarning createWarning = new createWarning();
        createWarning.execute();
    }
}
