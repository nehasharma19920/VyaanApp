package com.neon.vyaan.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.neon.vyaan.R;
import com.neon.vyaan.adapter.CityAdapter;
import com.neon.vyaan.adapter.LocalityAdapter;
import com.neon.vyaan.application.VyaanApplication;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.model.CityModel;
import com.neon.vyaan.model.LocalityModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mayank on 31/08/2016.
 */
public class SelectLocalityActivity extends BaseActivity {


    RecyclerView recyclerLocality;
    LinearLayoutManager manager;
    TextView textNoLocality;

    LocalityAdapter localityAdapter;
    List<LocalityModel> localityModelList;

    CityModel cityModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_locality);
    }

    @Override
    protected void initViews() {

        cityModel = getIntent().getExtras().getParcelable(CITY_MODEL);
        recyclerLocality = (RecyclerView) findViewById(R.id.recyclerLocality);
        manager = new LinearLayoutManager(currentActivity);
        textNoLocality = (TextView) findViewById(R.id.textNoLocality);
        localityModelList = new ArrayList<>();

        localityAdapter = new LocalityAdapter(currentActivity, localityModelList);


        recyclerLocality.setAdapter(localityAdapter);
        recyclerLocality.setLayoutManager(manager);

        getSupportActionBar().setTitle(R.string.title_city);

        allLocalities();

    }

    @Override
    protected void initContext() {
        currentActivity = SelectLocalityActivity.this;
        context = SelectLocalityActivity.this;
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
    public void onAlertClicked(int alertType) {

    }

    @Override
    public void onClick(View view) {

    }


    public void allLocalities() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);


        JSONObject jsons = null;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_ALL_LOCALITES + "?city_id=" + cityModel.getId(), jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ((BaseActivity) currentActivity).logTesting("localities responce is", response.toString(), Log.ERROR);

                try {
                    cancelProgressDialog();
                    ((BaseActivity) currentActivity).logTesting("is successfull fetch localities", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {
                        localityModelList.clear();
                        Gson gson = new Gson();
                        List<LocalityModel> localityModelListTemp = Arrays.asList(gson.fromJson(response.getJSONArray(MESSAGE).toString(), LocalityModel[].class));
                        Log.e("localities list size", "" + localityModelListTemp.size());

                        if (localityModelListTemp.size() > 0) {
                            recyclerLocality.setVisibility(View.VISIBLE);
                            textNoLocality.setVisibility(View.GONE);
                            localityModelList.addAll(localityModelListTemp);

                            localityAdapter.notifyDataSetChanged();
                        } else {
                            recyclerLocality.setVisibility(View.GONE);
                            textNoLocality.setVisibility(View.VISIBLE);
                        }

                    } else {
                        cancelProgressDialog();
                        ((BaseActivity) currentActivity).logTesting("fetch localities error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    recyclerLocality.setVisibility(View.GONE);
                    textNoLocality.setVisibility(View.VISIBLE);
                    ((BaseActivity) currentActivity).logTesting("fetch localities json exeption is", e.toString(), Log.ERROR);
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
