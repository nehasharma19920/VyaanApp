package com.neon.vyaan.fragments;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.neon.vyaan.R;
import com.neon.vyaan.activity.BaseActivity;
import com.neon.vyaan.activity.CartActivity;
import com.neon.vyaan.activity.Dashboard;
import com.neon.vyaan.adapter.MyPlanAdapter;
import com.neon.vyaan.application.VyaanApplication;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.model.ProductsModel;
import com.neon.vyaan.model.SubscriptionModel;
import com.neon.vyaan.utils.SharedPreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPlans extends BaseFragment {


    View view;
    RecyclerView recyclerPlans;
    LinearLayoutManager linearLayoutManager;
    List<SubscriptionModel> subscriptionModelList;


    MyPlanAdapter myPlanAdapter;

    TextView textNoPlans;

    int noOfItems;

    LinearLayout containerMyPlans;


    FloatingActionButton fabGoToHome;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_my_plans, container, false);
        return view;
    }

    @Override
    public void alertOkClicked() {

    }

    @Override
    protected void initViews() {

        itemsInCart();

        recyclerPlans = (RecyclerView) view.findViewById(R.id.recyclerPlans);
        textNoPlans = (TextView) view.findViewById(R.id.textNoPlans);
        containerMyPlans = (LinearLayout) view.findViewById(R.id.containerMyPlans);
        linearLayoutManager = new LinearLayoutManager(currentActivity);
        subscriptionModelList = new ArrayList<>();

        myPlanAdapter = new MyPlanAdapter(currentActivity, subscriptionModelList);

        recyclerPlans.setAdapter(myPlanAdapter);
        recyclerPlans.setLayoutManager(linearLayoutManager);
        initFab();
        myPlans();

    }

    @Override
    protected void initContext() {
        currentActivity = getActivity();
        context = getActivity();
    }

    @Override
    protected void initListners() {

    }

    @Override
    public void onClick(View view) {

    }


    public void myPlans() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);


        JSONObject jsons = null;

        String complateUrlSubscription = URL_CURRENT_SUBSCRIPTION + "?user_id=" + SharedPreferenceUtils.getInstance(currentActivity).getInteger(AppConstants.KEY_USER_ID);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, complateUrlSubscription, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ((BaseActivity) currentActivity).logTesting("subs responce is", response.toString(), Log.ERROR);

                try {
                    cancelProgressDialog();
                    ((BaseActivity) currentActivity).logTesting("is successfull fetch subscription", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {
                        subscriptionModelList.clear();
                        Gson gson = new Gson();
                        List<SubscriptionModel> subscriptionModelListTemp = Arrays.asList(gson.fromJson(response.getJSONArray(MESSAGE).toString(), SubscriptionModel[].class));
                        Log.e("subscription list size", "" + subscriptionModelListTemp.size());

                        if (subscriptionModelListTemp.size() > 0) {
                            containerMyPlans.setVisibility(View.VISIBLE);
                            textNoPlans.setVisibility(View.GONE);
                            subscriptionModelList.addAll(subscriptionModelListTemp);

                            myPlanAdapter.notifyDataSetChanged();
                        } else {
                            containerMyPlans.setVisibility(View.GONE);
                            textNoPlans.setVisibility(View.VISIBLE);
                        }

                    } else {
                        containerMyPlans.setVisibility(View.GONE);
                        textNoPlans.setVisibility(View.VISIBLE);
                        cancelProgressDialog();
                        ((BaseActivity) currentActivity).logTesting("fetch subscription error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    containerMyPlans.setVisibility(View.GONE);
                    textNoPlans.setVisibility(View.VISIBLE);
                    ((BaseActivity) currentActivity).logTesting("fetch subscription json exeption is", e.toString(), Log.ERROR);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                ((BaseActivity) currentActivity).logTesting("error is", error.toString(), Log.ERROR);
                ((BaseActivity) currentActivity).toast(getResources().getString(R.string.errorMyPlans), true);

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        View menuView = menu.findItem(R.id.action_cart).getActionView();
        TextView textNoOfItemsInCart = (TextView) menuView.findViewById(R.id.textNoOfItemsInCart);
        RelativeLayout containetCartItems = (RelativeLayout) menuView.findViewById(R.id.containetCartItems);
        textNoOfItemsInCart.setText("" + String.valueOf(noOfItems));
        if (noOfItems == 0) {
            textNoOfItemsInCart.setText("");
        } else {
            textNoOfItemsInCart.setText("" + String.valueOf(noOfItems));

        }

        containetCartItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity) currentActivity).startActivity(currentActivity, CartActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_LEFT);

            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e("cart click ", "mainboard");
        switch (item.getItemId()) {
            case R.id.action_cart: {
                Log.e("cart click ", "minboard");
                ((BaseActivity) currentActivity).startActivity(currentActivity, CartActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_LEFT);

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public void itemsInCart() {


        JSONObject jsons = null;

        String complateUrlSubscription = URL_NO_OF_ITEMS_IN_CART + "?user_id=" + SharedPreferenceUtils.getInstance(currentActivity).getInteger(AppConstants.KEY_USER_ID);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, complateUrlSubscription, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ((BaseActivity) currentActivity).logTesting("items in cart responce is", response.toString(), Log.ERROR);

                try {

                    ((BaseActivity) currentActivity).logTesting("is successfull fetch items in cart", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {
                        noOfItems = response.getInt(KEY_NO_OF_ITEMS);
                        ((BaseActivity) currentActivity).invalidateOptionsMenu();
                    } else {


                        ((BaseActivity) currentActivity).logTesting("fetch items in cart error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {

                    ((BaseActivity) currentActivity).logTesting("fetch items in cart json exeption is", e.toString(), Log.ERROR);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                ((BaseActivity) currentActivity).logTesting("error is", error.toString(), Log.ERROR);
                ((BaseActivity) currentActivity).toast(getResources().getString(R.string.errorCartItems), true);

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

    private void initFab() {

        fabGoToHome = (FloatingActionButton) view.findViewById(R.id.fabGoToHome);
        fabGoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((Dashboard) currentActivity).setSelection(0);
            }
        });


    }

    @Override
    public void onResume() {
        myPlans();
        super.onResume();
    }
}
