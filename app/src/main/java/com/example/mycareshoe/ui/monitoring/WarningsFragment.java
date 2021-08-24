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

import androidx.fragment.app.DialogFragment;

import com.example.mycareshoe.R;
import com.example.mycareshoe.helpers.SharedPrefManager;
import com.example.mycareshoe.helpers.URLs;
import com.example.mycareshoe.ui.login.JSONParser;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WarningsFragment extends DialogFragment
{
    private ImageView closeButton;
    private
    // Initializing a new String Array
    ArrayList<String> warnings = new ArrayList<String>();
    ArrayList<String> warningsDate = new ArrayList<String>();
    private List<String> warnings_list;
    private List<String> warnings_date_list;

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
        loadWarnings(lstView);
        TextView emptyText = view.findViewById(R.id.empty);

        lstView.setEmptyView(emptyText);



        lstView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                dismiss();
                WarningDetailsFragment detailsFragment= new WarningDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("reading_id", getWarnings_list().get(position));
                detailsFragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, detailsFragment).addToBackStack("WarningDetail").commit();
                getActivity().setTitle(lstView.getAdapter().getItem(position).toString());

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

                try {
                    JSONArray array= objs.getJSONArray("readings");
                    for(int i=0; i<array.length();i++){
                        warnings.add(array.getJSONObject(i).getString("reading_id"));
                        warningsDate.add(array.getJSONObject(i).getString("warning_date"));
                    }
                    Collections.sort(warnings, Collections.reverseOrder());
                    Collections.sort(warningsDate, Collections.reverseOrder());


                    // Create a List from String Array elements
                    setWarnings_list(new ArrayList<String>(warnings));
                    setWarnings_date_list(new ArrayList<String>(warningsDate));

                    // Create an ArrayAdapter from List
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                            (getActivity(), android.R.layout.simple_list_item_1, getWarnings_date_list());


                    // DataBind ListView with items from ArrayAdapter
                    listView.setAdapter(arrayAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                super.onPostExecute(objs);

            }


                @Override
            protected JSONObject doInBackground(Void... voids) {
                //creating request handler object
                JSONParser jsonParser = new JSONParser();
                //creating request parameters
                ArrayList params = new ArrayList();
                params.add(new BasicNameValuePair("patient_number", Integer.toString(SharedPrefManager.getInstance(getContext()).getPatient(true).getPatient_number())));

                //returning the response
                return jsonParser.makeHttpRequest(URLs.URL_GET_WARNINGS,"GET", params);
            }
        }
        loadWarnings warnings = new loadWarnings();
        warnings.execute();
    }


    public void createWarning(String reading_id, String date) {
        class createWarning extends AsyncTask<Void, Void, JSONObject> {

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
                params.add(new BasicNameValuePair("patient_number", Integer.toString(SharedPrefManager.getInstance(getContext()).getPatient(true).getPatient_number())));
                params.add(new BasicNameValuePair("reading_id", reading_id));
                params.add(new BasicNameValuePair("warning_date", date));

                //returning the response
                return jsonParser.makeHttpRequest(URLs.URL_CREATE_WARNING,"PUT", params);
            }
        }
        createWarning createWarning = new createWarning();
        createWarning.execute();
    }
}
