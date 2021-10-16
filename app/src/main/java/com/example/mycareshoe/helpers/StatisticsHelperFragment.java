package com.example.mycareshoe.helpers;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.example.mycareshoe.service.HTTPRequest;
import com.example.mycareshoe.service.URLs;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.HttpUrl;

public abstract class StatisticsHelperFragment extends Fragment {

    private String startDateString;
    private String endDateString;

    public String getStartDateString() {
        return startDateString;
    }

    public void setStartDateString(String startDateString) {
        this.startDateString = startDateString;
    }

    public String getEndDateString() {
        return endDateString;
    }

    public void setEndDateString(String endDateString) {
        this.endDateString = endDateString;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanseState) {

        View view = provideYourFragmentView(inflater, parent, savedInstanseState);
        return view;
    }

    public abstract View provideYourFragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState);


    public void setDateTextClickListener(EditText dateText, String dateType, ImageButton searchBtn) {


        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                dateText.setText(new StringBuilder().append(year).append("-").append(monthOfYear + 1).append("-").append(dayOfMonth).toString());

                                if (dateType.equals("start")) {

                                    setStartDateString(dateText.getText().toString());
                                } else
                                    setEndDateString(dateText.getText().toString());

                                if (getStartDateString() != null && getEndDateString() != null)
                                    searchBtn.setEnabled(true);
                                else
                                    searchBtn.setEnabled(false);
                            }
                        }, year, month, day);

                if (dateType.equals("start")) {
                    if (getEndDateString() != null) {
                        try {
                            picker.getDatePicker().setMaxDate(new SimpleDateFormat("yyyy-MM-dd").parse(getEndDateString()).getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    if (getStartDateString() != null) {
                        try {
                            picker.getDatePicker().setMinDate(new SimpleDateFormat("yyyy-MM-dd").parse(getStartDateString()).getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                }
                picker.show();


            }
        });
    }

    public JSONObject getReadings(String topic, String startDateStringQuery, String endDateStringQuery) {

        //creating request handler object
        HTTPRequest httpRequest = new HTTPRequest();
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(URLs.URL_SEARCH_DATE).newBuilder();

        urlBuilder.addQueryParameter("patient_number", Integer.toString(SharedPrefManager.getInstance(getContext()).getPatient(true).getPatient_number()));
        urlBuilder.addQueryParameter("start_date", startDateStringQuery);
        urlBuilder.addQueryParameter("end_date", endDateStringQuery);
        urlBuilder.addQueryParameter("topic", topic);


        //returning the response
        return httpRequest.makeHttpRequest(URLs.URL_SEARCH_DATE, "GET", null, urlBuilder);
    }

}