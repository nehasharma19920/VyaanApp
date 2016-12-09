package com.neon.vyaan.activity;

import android.content.Intent;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.neon.vyaan.R;
import com.neon.vyaan.adapter.OrderConfirmationAdapter;
import com.neon.vyaan.application.VyaanApplication;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.model.CartModel;
import com.neon.vyaan.model.CompleteOrderModel;
import com.neon.vyaan.utils.DateTimeUtils;
import com.neon.vyaan.utils.SharedPreferenceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderConfirmation extends BaseActivity {
    RecyclerView recyclerOrderItems;
    LinearLayoutManager manager;

    Button buttonCod;
    Button buttonPayOnline;

    ArrayList<CartModel> cartModelList;

    OrderConfirmationAdapter orderConfirmationAdapter;

    TextView textTotalBill;

    List<CompleteOrderModel.PlaceOrderModel> placeOrderModelList;

    String addressId;

    CompleteOrderModel completeOrderModel;

    @Override
    protected void initViews() {
        placeOrderModelList = new ArrayList<>();
        cartModelList = getIntent().getExtras().getParcelableArrayList(CART_MODEL_LIST);
        addressId = getIntent().getExtras().getString(ADDRESS_ID);
        recyclerOrderItems = (RecyclerView) findViewById(R.id.recyclerOrderItems);
        manager = new LinearLayoutManager(currentActivity);
        buttonCod = (Button) findViewById(R.id.buttonCod);
        buttonPayOnline = (Button) findViewById(R.id.buttonPayOnline);
        textTotalBill = (TextView) findViewById(R.id.textTotalBill);

        orderConfirmationAdapter = new OrderConfirmationAdapter(cartModelList, currentActivity);
        recyclerOrderItems.setAdapter(orderConfirmationAdapter);
        recyclerOrderItems.setLayoutManager(manager);

        textTotalBill.setText("Rs. " + calculateBill());

        getSupportActionBar().setTitle(getResources().getString(R.string.title_confirm_order));

    }

    @Override
    protected void initContext() {
        currentActivity = OrderConfirmation.this;
        context = OrderConfirmation.this;
    }

    @Override
    protected void initListners() {
        buttonCod.setOnClickListener(this);
        buttonPayOnline.setOnClickListener(this);
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
        setContentView(R.layout.activity_order_confirmation);
    }

    @Override
    public void onAlertClicked(int alertType) {

        switch (alertType) {
            case ALERT_TYPE_CONFIRM_ORDER: {
                placeOrder();
                break;
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonCod: {
                alert(currentActivity, currentActivity.getString(R.string.alert_message_confirm_order), currentActivity.getString(R.string.alert_message_confirm_order), currentActivity.getString(R.string.labelOk), currentActivity.getString(R.string.labelCancel), true, false, AppConstants.ALERT_TYPE_CONFIRM_ORDER);

                break;
            }
            case R.id.buttonPayOnline: {


                Intent intent = new Intent(context, PayUActivity.class);
                Bundle bundle = new Bundle();
                //     bundle.putString("order_num", "123");
                bundle.putString("order_date", DateTimeUtils.currentDate("dd-MM-yyyy", Calendar.getInstance()));
                bundle.putString("customer_id", "cID");
                bundle.putString("amount", String.valueOf(calculateBill()));
                bundle.putString("mode", "online");
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_TAG_PAYU_PAYMENT_ACTIVITY);
                break;
            }
        }

    }

    private int calculateBill() {
        int total = 0;
        for (int i = 0; i < cartModelList.size(); i++) {


            total = total + (cartModelList.get(i).getProductPrice() * cartModelList.get(i).getProductQuantity());
        }
        return total;
    }


    private CompleteOrderModel initPlaceOrderModelList() {


        for (int i = 0; i < cartModelList.size(); i++) {

            CompleteOrderModel.PlaceOrderModel placeOrderModel = new CompleteOrderModel.PlaceOrderModel();
            placeOrderModel.setProductId(cartModelList.get(i).getProductId());
            placeOrderModel.setStartDate(DateTimeUtils.currentDate("yyyy-MM-dd", Calendar.getInstance()));
            placeOrderModel.setEndDate(DateTimeUtils.currentDate("yyyy-MM-dd", Calendar.getInstance()));
            placeOrderModel.setOrderStatus("Placed");
            placeOrderModel.setPaymentMode("COD");
            placeOrderModel.setPaymentStatus("Unpaid");
            placeOrderModel.setType("buy");
            placeOrderModel.setQuantity(cartModelList.get(i).getProductQuantity());
            placeOrderModel.setPaymentAmount(cartModelList.get(i).getProductPrice() * cartModelList.get(i).getProductQuantity());
            placeOrderModelList.add(placeOrderModel);
        }


        completeOrderModel = new CompleteOrderModel();
        completeOrderModel.setOrder_items(placeOrderModelList);
        completeOrderModel.setUserId(SharedPreferenceUtils.getInstance(currentActivity).getInteger(AppConstants.KEY_USER_ID));
        completeOrderModel.setAddressId(addressId);

        return completeOrderModel;
    }


    public void placeOrder() {


        ((BaseActivity) currentActivity).progressDialog(currentActivity, currentActivity.getString(R.string.pdialog_message_loading), currentActivity.getString(R.string.pdialog_message_loading), false, false);

/*
        initPlaceOrderModelList();
        JSONArray itemsArray = new JSONArray();
        for (int i = 0; i < placeOrderModelList.size(); i++) {

            itemsArray.put(new Gson().toJson(placeOrderModelList.get(i)));

        }


        String json = new Gson().toJson(placeOrderModelList);
        // logTesting("place order request json", json, Log.ERROR);
        JSONObject jsons = null;
        try {
            jsons = new JSONObject();
            jsons.put("order_items", itemsArray.toString());
            jsons.put("user_id", SharedPreferenceUtils.getInstance(currentActivity).getInteger(AppConstants.KEY_USER_ID));
            jsons.put("address_id", addressId);
            Log.e("place order request", jsons.toString());
        } catch (JSONException e) {
            Log.e("josn exe", e.toString());
            e.printStackTrace();
        }
*/


        String jsonsa = new Gson().toJson(initPlaceOrderModelList());

        JSONObject jsonse = null;
        try {
            jsonse = new JSONObject(jsonsa);
            new Gson().toJson(jsonse);
            Log.e("place order request", jsonse.toString());
        } catch (JSONException e) {
            Log.e("josn exe", e.toString());
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_PLACE_ORDER, jsonse, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                logTesting("place order responce is", response.toString(), Log.ERROR);

                try {
                    logTesting("is successfull place order", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {

                        toast(getString(R.string.message_order_placed), true);
                        Intent i = new Intent(currentActivity, Dashboard.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    } else {
                        cancelProgressDialog();
                        logTesting("place order error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    logTesting("place order json exeption is", e.toString(), Log.ERROR);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                logTesting("error is", error.toString(), Log.ERROR);
                toast(getResources().getString(R.string.errorPlaceOrder), true);

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAG_PAYU_PAYMENT_ACTIVITY && resultCode == RESULT_OK) {
            placeOrderOnlinePayment();
        }

    }


    public void placeOrderOnlinePayment() {


        ((BaseActivity) currentActivity).progressDialog(currentActivity, currentActivity.getString(R.string.pdialog_message_loading), currentActivity.getString(R.string.pdialog_message_loading), false, false);

        String jsonsa = new Gson().toJson(initPlaceOrderOnlineModelList());

        JSONObject jsonse = null;
        try {
            jsonse = new JSONObject(jsonsa);
            new Gson().toJson(jsonse);
            Log.e("place order request", jsonse.toString());
        } catch (JSONException e) {
            Log.e("josn exe", e.toString());
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_PLACE_ORDER, jsonse, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                logTesting("place order responce is", response.toString(), Log.ERROR);

                try {
                    logTesting("is successfull place order", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {

                        toast(getString(R.string.message_order_placed), true);
                        Intent i = new Intent(currentActivity, Dashboard.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    } else {
                        cancelProgressDialog();
                        logTesting("place order error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    logTesting("place order json exeption is", e.toString(), Log.ERROR);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                logTesting("error is", error.toString(), Log.ERROR);
                toast(getResources().getString(R.string.errorPlaceOrder), true);

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


    private CompleteOrderModel initPlaceOrderOnlineModelList() {


        for (int i = 0; i < cartModelList.size(); i++) {

            CompleteOrderModel.PlaceOrderModel placeOrderModel = new CompleteOrderModel.PlaceOrderModel();
            placeOrderModel.setProductId(cartModelList.get(i).getProductId());
            placeOrderModel.setStartDate(DateTimeUtils.currentDate("yyyy-MM-dd", Calendar.getInstance()));
            placeOrderModel.setEndDate(DateTimeUtils.currentDate("yyyy-MM-dd", Calendar.getInstance()));
            placeOrderModel.setOrderStatus("Placed");
            placeOrderModel.setPaymentMode("ONLINE");
            placeOrderModel.setPaymentStatus("Unpaid");
            placeOrderModel.setType("buy");
            placeOrderModel.setQuantity(cartModelList.get(i).getProductQuantity());
            placeOrderModel.setPaymentAmount(cartModelList.get(i).getProductPrice() * cartModelList.get(i).getProductQuantity());
            placeOrderModelList.add(placeOrderModel);
        }


        completeOrderModel = new CompleteOrderModel();
        completeOrderModel.setOrder_items(placeOrderModelList);
        completeOrderModel.setUserId(SharedPreferenceUtils.getInstance(currentActivity).getInteger(AppConstants.KEY_USER_ID));
        completeOrderModel.setAddressId(addressId);

        return completeOrderModel;
    }


}
