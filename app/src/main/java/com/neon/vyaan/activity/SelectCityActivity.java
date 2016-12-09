package com.neon.vyaan.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.neon.vyaan.R;
import com.neon.vyaan.adapter.CartAdapter;
import com.neon.vyaan.adapter.CityAdapter;
import com.neon.vyaan.application.VyaanApplication;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.model.AddressModel;
import com.neon.vyaan.model.CityModel;
import com.neon.vyaan.utils.SharedPreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectCityActivity extends BaseActivity {

    RecyclerView recyclerCity;
    LinearLayoutManager manager;
    TextView textNoCity;

    CityAdapter cityAdapter;
    List<CityModel> cityModelList;


    @Override
    protected void initViews() {
        recyclerCity = (RecyclerView) findViewById(R.id.recyclerCity);
        manager = new LinearLayoutManager(currentActivity);
        textNoCity = (TextView) findViewById(R.id.textNoCity);
        cityModelList = new ArrayList<>();

        cityAdapter = new CityAdapter(currentActivity, cityModelList);

        recyclerCity.setAdapter(cityAdapter);
        recyclerCity.setLayoutManager(manager);

        getSupportActionBar().setTitle(R.string.title_city);

        allCities();

    }

    @Override
    protected void initContext() {
        currentActivity = SelectCityActivity.this;
        context = SelectCityActivity.this;
    }

    @Override
    protected void initListners() {

    }

    @Override
    protected boolean isActionBar() {
        return true;
    }

    @Override
    protected boolean isHomeButton() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
    }

    @Override
    public void onAlertClicked(int alertType) {

    }

    @Override
    public void onClick(View view) {

    }


    public void allCities() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);


        JSONObject jsons = null;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_ALL_CITIES, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ((BaseActivity) currentActivity).logTesting("cities responce is", response.toString(), Log.ERROR);

                try {
                    cancelProgressDialog();
                    ((BaseActivity) currentActivity).logTesting("is successfull fetch cities", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {
                        cityModelList.clear();
                        Gson gson = new Gson();
                        List<CityModel> cityModelListTemp = Arrays.asList(gson.fromJson(response.getJSONArray(MESSAGE).toString(), CityModel[].class));
                        Log.e("cities list size", "" + cityModelListTemp.size());

                        if (cityModelListTemp.size() > 0) {
                            recyclerCity.setVisibility(View.VISIBLE);
                            textNoCity.setVisibility(View.GONE);
                            cityModelList.addAll(cityModelListTemp);

                            cityAdapter.notifyDataSetChanged();
                        } else {
                            recyclerCity.setVisibility(View.GONE);
                            textNoCity.setVisibility(View.VISIBLE);
                        }

                    } else {
                        cancelProgressDialog();
                        ((BaseActivity) currentActivity).logTesting("fetch cities error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    recyclerCity.setVisibility(View.GONE);
                    textNoCity.setVisibility(View.VISIBLE);
                    ((BaseActivity) currentActivity).logTesting("fetch cities json exeption is", e.toString(), Log.ERROR);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                ((BaseActivity) currentActivity).logTesting("error is", error.toString(), Log.ERROR);
                ((BaseActivity) currentActivity).toast(getResources().getString(R.string.errorCities), true);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Content-Type", "application/json");


                return params;
            }
        };

        VyaanApplication.getInstance().addToRequestQueue(request);
    }


}
