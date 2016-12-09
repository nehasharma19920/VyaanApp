package com.neon.vyaan.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.neon.vyaan.R;
import com.neon.vyaan.adapter.CartAdapter;
import com.neon.vyaan.application.VyaanApplication;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.model.CartModel;
import com.neon.vyaan.model.SubscriptionModel;
import com.neon.vyaan.utils.SharedPreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends BaseActivity {


    TextView textTotalItems;
    RecyclerView recyclerCartItems;
    LinearLayoutManager linearLayoutManager;
    ArrayList<CartModel> cartModelList;
    Button buttonCheckOut;
    CartAdapter cartAdapter;

    TextView textNoItemsInCart;

    LinearLayout containerCart;

    boolean isCheckOut;

    FloatingActionButton fabGoToHome;

    @Override
    protected void initViews() {


        linearLayoutManager = new LinearLayoutManager(currentActivity);

        cartModelList = new ArrayList<>();

        textTotalItems = (TextView) findViewById(R.id.textTotalItems);
        textNoItemsInCart = (TextView) findViewById(R.id.textNoItemsInCart);
        textTotalItems = (TextView) findViewById(R.id.textTotalItems);
        recyclerCartItems = (RecyclerView) findViewById(R.id.recyclerCartItems);
        containerCart = (LinearLayout) findViewById(R.id.containerCart);


        cartAdapter = new CartAdapter(currentActivity, cartModelList);

        recyclerCartItems.setAdapter(cartAdapter);
        recyclerCartItems.setLayoutManager(linearLayoutManager);

        getSupportActionBar().setTitle(R.string.title_cart);

        getCartItems();

        buttonCheckOut = (Button) findViewById(R.id.buttonCheckOut);
        initFab();

    }

    @Override
    protected void initContext() {
        currentActivity = CartActivity.this;
        context = CartActivity.this;
    }

    @Override
    protected void initListners() {
        buttonCheckOut.setOnClickListener(this);
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
        setContentView(R.layout.activity_cart);
    }

    @Override
    public void onAlertClicked(int alertType) {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonCheckOut: {

                if (cartModelList.size() > 0) {

                    if (bundle == null) {
                        bundle = new Bundle();
                    }
                    bundle.putParcelableArrayList(CART_MODEL_LIST, cartModelList);

                    startActivity(currentActivity, AllAddresses.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_LEFT);

                } else {
                    //   invalidateOptionsMenu();
                    ((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.errorAddAtLeastOneItem), true);
                }


                break;
            }
        }

    }


    public void getCartItems() {


        ((BaseActivity) currentActivity).progressDialog(currentActivity, currentActivity.getString(R.string.pdialog_message_loading), currentActivity.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsons = null;

        String urlCart = AppConstants.URL_GET_CART_ITEMS + "?user_id=" + SharedPreferenceUtils.getInstance(currentActivity).getInteger(AppConstants.KEY_USER_ID);


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlCart, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ((BaseActivity) currentActivity).logTesting("get cart item responce is", response.toString(), Log.ERROR);

                try {
                    ((BaseActivity) currentActivity).cancelProgressDialog();
                    ((BaseActivity) currentActivity).logTesting("is successfull cart item", "hi" + response.getBoolean(AppConstants.KEY_IS_ITEM), Log.ERROR);
                    if (response.getBoolean(AppConstants.KEY_IS_ITEM)) {

                        cartModelList.clear();
                        Gson gson = new Gson();
                        List<CartModel> cartModelListTemp = Arrays.asList(gson.fromJson(response.getJSONArray(KEY_USER_IDS).toString(), CartModel[].class));
                        Log.e("cart list size", "" + cartModelListTemp.size());

                        if (cartModelListTemp.size() > 0) {
                            textTotalItems.setText("" + cartModelListTemp.size());
                            isCheckOut = true;
                            containerCart.setVisibility(View.VISIBLE);
                            textNoItemsInCart.setVisibility(View.GONE);
                            cartModelList.addAll(cartModelListTemp);

                            cartAdapter.notifyDataSetChanged();

                            invalidateOptionsMenu();
                        } else {
                            isCheckOut = false;
                            containerCart.setVisibility(View.GONE);
                            textNoItemsInCart.setVisibility(View.VISIBLE);
                            invalidateOptionsMenu();
                        }
                    } else {
                        isCheckOut = false;
                        ((BaseActivity) currentActivity).cancelProgressDialog();

                        ((BaseActivity) currentActivity).logTesting("no product in cart", "true", Log.ERROR);

                        containerCart.setVisibility(View.GONE);
                        textNoItemsInCart.setVisibility(View.VISIBLE);
                        invalidateOptionsMenu();
                    }


                } catch (JSONException e) {
                    ((BaseActivity) currentActivity).logTesting("get cart item json exeption is", e.toString(), Log.ERROR);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) currentActivity).cancelProgressDialog();
                ((BaseActivity) currentActivity).logTesting("error is", error.toString(), Log.ERROR);
                ((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.errorCartItems), true);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        if (isCheckOut) {
            menu.getItem(0).setVisible(true);
        } else {
            menu.getItem(0).setVisible(false);
        }


        menu.getItem(0).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_check_out: {
                if (cartModelList.size() > 0) {

                    if (bundle == null) {
                        bundle = new Bundle();
                    }
                    bundle.putParcelableArrayList(CART_MODEL_LIST, cartModelList);

                    startActivity(currentActivity, AllAddresses.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_LEFT);

                } else {
                    invalidateOptionsMenu();
                    ((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.errorAddAtLeastOneItem), true);
                }

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void initFab() {

        fabGoToHome = (FloatingActionButton) findViewById(R.id.fabGoToHome);
        fabGoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   ((BaseActivity) currentActivity).startActivity(currentActivity, Dashboard.class, bundle, true, AppConstants.REQUEST_TAG_NO_RESULT, true, AppConstants.ANIMATION_SLIDE_LEFT);
                Intent i = new Intent(CartActivity.this, Dashboard.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });


    }


}
