package com.neon.vyaan.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.neon.vyaan.adapter.TransactionAdapter;
import com.neon.vyaan.adapter.ViewBillsAdapter;
import com.neon.vyaan.application.VyaanApplication;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.model.BillsModel;
import com.neon.vyaan.model.TransactionModel;
import com.neon.vyaan.utils.SharedPreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mayank on 20/08/2016.
 */
public class TransactionHistory extends BaseFragment {

    View view;

    RecyclerView recyclerTransactions;

    LinearLayoutManager manager;
    List<TransactionModel> transactionModelList;

    TextView textNoTransactions;


    TransactionAdapter transactionAdapter;

    int noOfItems;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_transaction_history, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerTransactions = (RecyclerView) view.findViewById(R.id.recyclerTransactions);
        textNoTransactions = (TextView) view.findViewById(R.id.textNoTransactions);
        transactionModelList = new ArrayList<>();
        manager = new LinearLayoutManager(currentActivity);
        transactionAdapter = new TransactionAdapter(currentActivity, transactionModelList);
        recyclerTransactions.setAdapter(transactionAdapter);
        recyclerTransactions.setLayoutManager(manager);
        myTransactions();
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
        context = getActivity();
        currentActivity = getActivity();
    }

    @Override
    protected void initListners() {

    }

    @Override
    public void onClick(View view) {

    }

    public void myTransactions() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);


        JSONObject jsons = null;

        String complateUrlMyTransactions = URL_ALL_TRANSACTIONS + "?user_id=" + SharedPreferenceUtils.getInstance(currentActivity).getInteger(AppConstants.KEY_USER_ID);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, complateUrlMyTransactions, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ((BaseActivity) currentActivity).logTesting("transactions responce is", response.toString(), Log.ERROR);

                try {
                    cancelProgressDialog();
                    ((BaseActivity) currentActivity).logTesting("is successfull fetch transactions", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {
                        transactionModelList.clear();
                        Gson gson = new Gson();
                        List<TransactionModel> transactionModelListTemp = Arrays.asList(gson.fromJson(response.getJSONArray(MESSAGE).toString(), TransactionModel[].class));
                        Log.e("bill list size", "" + transactionModelListTemp.size());

                        if (transactionModelListTemp.size() > 0) {
                            recyclerTransactions.setVisibility(View.VISIBLE);
                            textNoTransactions.setVisibility(View.GONE);
                            transactionModelList.addAll(transactionModelListTemp);

                            transactionAdapter.notifyDataSetChanged();
                        } else {
                            recyclerTransactions.setVisibility(View.GONE);
                            textNoTransactions.setVisibility(View.VISIBLE);
                        }

                    } else {
                        recyclerTransactions.setVisibility(View.GONE);
                        textNoTransactions.setVisibility(View.VISIBLE);
                        cancelProgressDialog();
                        ((BaseActivity) currentActivity).logTesting("fetch transactions error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    recyclerTransactions.setVisibility(View.GONE);
                    textNoTransactions.setVisibility(View.VISIBLE);
                    ((BaseActivity) currentActivity).logTesting("fetch transactions json exeption is", e.toString(), Log.ERROR);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                ((BaseActivity) currentActivity).logTesting("error is", error.toString(), Log.ERROR);
                ((BaseActivity) currentActivity).toast(getResources().getString(R.string.errorMyTransactions), true);

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

}
