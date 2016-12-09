package com.neon.vyaan.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.neon.vyaan.R;
import com.neon.vyaan.adapter.AllAddressesAdapter;
import com.neon.vyaan.application.VyaanApplication;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.model.AddSubscripionModel;
import com.neon.vyaan.model.AddressModel;
import com.neon.vyaan.model.CartModel;
import com.neon.vyaan.model.TransactionModel;
import com.neon.vyaan.utils.SharedPreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllAddresses extends BaseActivity {

    RecyclerView recyclerAddresses;
    LinearLayoutManager linearLayoutManager;
    FloatingActionButton fabAddNewAddress;

    LinearLayout containerAllAddress;
    TextView textNoAddress;

    List<AddressModel> addressModelList = new ArrayList<>();

    AllAddressesAdapter allAddressesAdapter;

    public ArrayList<CartModel> cartModelList;

    public AddSubscripionModel addSubscripionModel;

    public boolean isSubscription;


    Button buttonAddNewAddress;
    @Override
    protected void initViews() {

        if (getIntent().hasExtra(SUBSCRIPTION)) {
            isSubscription = true;
            addSubscripionModel = getIntent().getExtras().getParcelable(SUBSCRIPTION_MODEL);

        } else {
            isSubscription = false;
            cartModelList = getIntent().getExtras().getParcelableArrayList(CART_MODEL_LIST);


        }


        recyclerAddresses = (RecyclerView) findViewById(R.id.recyclerAddresses);
        fabAddNewAddress = (FloatingActionButton) findViewById(R.id.fabAddNewAddress);
        containerAllAddress = (LinearLayout) findViewById(R.id.containerAllAddress);
        textNoAddress = (TextView) findViewById(R.id.textNoAddress);
        buttonAddNewAddress = (Button) findViewById(R.id.buttonAddNewAddress);

        linearLayoutManager = new LinearLayoutManager(currentActivity);

        recyclerAddresses.setLayoutManager(linearLayoutManager);


        allAddressesAdapter = new AllAddressesAdapter(currentActivity, addressModelList);

        recyclerAddresses.setAdapter(allAddressesAdapter);

        getSupportActionBar().setTitle(R.string.title_all_addresses);

        buttonAddNewAddress.setOnClickListener(this);

        myAdresses();
    }

    @Override
    protected void initContext() {
        currentActivity = AllAddresses.this;
        context = AllAddresses.this;
    }

    @Override
    protected void initListners() {
        fabAddNewAddress.setOnClickListener(this);
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
        setContentView(R.layout.activity_all_addresses);
    }

    @Override
    public void onAlertClicked(int alertType) {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fabAddNewAddress: {


                ((BaseActivity) currentActivity).startActivity(currentActivity, NewAddress.class, bundle, true, AppConstants.REQUEST_TAG_NEW_ADDRESS_ACTIVITY, true, AppConstants.ANIMATION_SLIDE_LEFT);

                break;
            }


            case R.id.buttonAddNewAddress:
            {

                ((BaseActivity) currentActivity).startActivity(currentActivity, NewAddress.class, bundle, true, AppConstants.REQUEST_TAG_NEW_ADDRESS_ACTIVITY, true, AppConstants.ANIMATION_SLIDE_LEFT);

                break;
            }

        }

    }


    public void myAdresses() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);


        JSONObject jsons = null;

        String complaeteUrlAllAddresses = URL_ALL_ADDRESSES + "?user_id=" + SharedPreferenceUtils.getInstance(currentActivity).getInteger(AppConstants.KEY_USER_ID);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, complaeteUrlAllAddresses, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ((BaseActivity) currentActivity).logTesting("addresses responce is", response.toString(), Log.ERROR);

                try {
                    cancelProgressDialog();
                    ((BaseActivity) currentActivity).logTesting("is successfull fetch addresses", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {
                        addressModelList.clear();
                        Gson gson = new Gson();
                        List<AddressModel> addressModelListTemp = Arrays.asList(gson.fromJson(response.getJSONArray(MESSAGE).toString(), AddressModel[].class));
                        Log.e("addresses list size", "" + addressModelListTemp.size());

                        if (addressModelListTemp.size() > 0) {
                            containerAllAddress.setVisibility(View.VISIBLE);
                            textNoAddress.setVisibility(View.GONE);
                            addressModelList.addAll(addressModelListTemp);

                            allAddressesAdapter.notifyDataSetChanged();
                        } else {
                            containerAllAddress.setVisibility(View.GONE);
                            textNoAddress.setVisibility(View.VISIBLE);
                        }

                    } else {
                        cancelProgressDialog();
                        ((BaseActivity) currentActivity).logTesting("fetch addresses error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    containerAllAddress.setVisibility(View.GONE);
                    textNoAddress.setVisibility(View.VISIBLE);
                    ((BaseActivity) currentActivity).logTesting("fetch addresses json exeption is", e.toString(), Log.ERROR);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                ((BaseActivity) currentActivity).logTesting("error is", error.toString(), Log.ERROR);
                ((BaseActivity) currentActivity).toast(getResources().getString(R.string.errorMyAddresses), true);

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAG_EDIT_ADDRESS_ACTIVITY) {
                myAdresses();
            }
            if (requestCode == REQUEST_TAG_NEW_ADDRESS_ACTIVITY) {
                myAdresses();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public void placeSubscription(String addressId) {
        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);

        addSubscripionModel.setAddress_id(Integer.parseInt(addressId));

        String json = new Gson().toJson(addSubscripionModel);
        logTesting("place Subscription request json", json, Log.ERROR);
        JSONObject jsons = null;
        try {
            jsons = new JSONObject(json);
            new Gson().toJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_ADD_SUBSCRIPTION, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                logTesting("place Subscription is", response.toString(), Log.ERROR);

                try {
                    logTesting("is successfull place Subscription", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {
                        toast(getString(R.string.message_subscription_placed), true);
                        Intent i = new Intent(currentActivity, Dashboard.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();

                    } else {
                        alert(currentActivity, currentActivity.getString(R.string.titleAlreadySubscribed), currentActivity.getString(R.string.messageSubscriptionExist), currentActivity.getString(R.string.labelOk), currentActivity.getString(R.string.labelCancel), false, true, AppConstants.ALERT_TYPE_NO_NETWORK);

                        logTesting("place Subscription error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    logTesting("place Subscription json exeption is", e.toString(), Log.ERROR);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                logTesting("error is", error.toString(), Log.ERROR);
                toast(getResources().getString(R.string.errorAddSubscription), true);

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/json");
                params.put("type", "M");

                return params;
            }
        };

        VyaanApplication.getInstance().addToRequestQueue(request);
    }


}
