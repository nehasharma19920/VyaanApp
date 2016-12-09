package com.neon.vyaan.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import com.neon.vyaan.adapter.OrdersAdapter;
import com.neon.vyaan.adapter.ViewBillsAdapter;
import com.neon.vyaan.application.VyaanApplication;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.model.BillsModel;
import com.neon.vyaan.model.OrdersModel;
import com.neon.vyaan.utils.SharedPreferenceUtils;

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
public class ViewBills extends BaseFragment {


    RecyclerView recyclerBills;
    LinearLayoutManager manager;
    List<BillsModel> billsModelList;

    TextView textNoBills;

    View view;

    ViewBillsAdapter viewBillsAdapter;

    FloatingActionButton fabGoToHome;

    LinearLayout containerViewBills;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_bills, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerBills = (RecyclerView) view.findViewById(R.id.recyclerBills);
        containerViewBills = (LinearLayout) view.findViewById(R.id.containerViewBills);
        textNoBills = (TextView) view.findViewById(R.id.textNoBills);
        billsModelList = new ArrayList<>();
        manager = new LinearLayoutManager(currentActivity);
        viewBillsAdapter = new ViewBillsAdapter(currentActivity, billsModelList);
        recyclerBills.setAdapter(viewBillsAdapter);
        recyclerBills.setLayoutManager(manager);
        initFab();
        myBills();
    }

    @Override
    public void alertOkClicked() {

    }

    @Override
    protected void initViews() {
        itemsInCart();
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

    public void myBills() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);


        JSONObject jsons = null;

        String complateUrlMyOrders = URL_MY_BILLS + "?user_id=" + SharedPreferenceUtils.getInstance(currentActivity).getInteger(AppConstants.KEY_USER_ID);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, complateUrlMyOrders, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ((BaseActivity) currentActivity).logTesting("bills responce is", response.toString(), Log.ERROR);

                try {
                    cancelProgressDialog();
                    ((BaseActivity) currentActivity).logTesting("is successfull fetch bills", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {
                        billsModelList.clear();
                        Gson gson = new Gson();
                        List<BillsModel> billsModelListTemp = Arrays.asList(gson.fromJson(response.getJSONArray(MESSAGE).toString(), BillsModel[].class));
                        Log.e("bill list size", "" + billsModelListTemp.size());

                        if (billsModelListTemp.size() > 0) {
                            containerViewBills.setVisibility(View.VISIBLE);
                            textNoBills.setVisibility(View.GONE);
                            billsModelList.addAll(billsModelListTemp);

                            viewBillsAdapter.notifyDataSetChanged();
                        } else {
                            containerViewBills.setVisibility(View.GONE);
                            textNoBills.setVisibility(View.VISIBLE);
                        }

                    } else {
                        cancelProgressDialog();
                        ((BaseActivity) currentActivity).logTesting("fetch bills error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    containerViewBills.setVisibility(View.GONE);
                    textNoBills.setVisibility(View.VISIBLE);
                    ((BaseActivity) currentActivity).logTesting("fetch bills json exeption is", e.toString(), Log.ERROR);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                ((BaseActivity) currentActivity).logTesting("error is", error.toString(), Log.ERROR);
                ((BaseActivity) currentActivity).toast(getResources().getString(R.string.errorMyBills), true);

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





}
