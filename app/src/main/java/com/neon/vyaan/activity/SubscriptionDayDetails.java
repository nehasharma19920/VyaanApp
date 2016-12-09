package com.neon.vyaan.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.gson.Gson;
import com.neon.vyaan.R;
import com.neon.vyaan.application.VyaanApplication;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.model.ChangeSubscriptionQuantityModel;
import com.neon.vyaan.model.PlanExploreModel;
import com.neon.vyaan.model.SubscriptionModel;
import com.neon.vyaan.utils.DateTimeUtils;
import com.neon.vyaan.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SubscriptionDayDetails extends BaseActivity {

    TextView editPackQuantity;
    TextView editPriceperPacket;
    EditText editSubscribeQuantity;
    TextView editSubscribeQuantityInLitres;
    TextView editAmount;
    SwitchCompat switchHold;
    Button buttonUpdate;

    PlanExploreModel planExploreModel;
    SubscriptionModel subscriptionModel;


    View customDialogView;

    EditText editQuantity;

    LinearLayout containerHold;

    TextView textEditSubcription;


    @Override
    protected void initViews() {


        planExploreModel = getIntent().getExtras().getParcelable(PLAN_EXPLORE);
        subscriptionModel = getIntent().getExtras().getParcelable(SUBSCRIPTION_MODEL);

        getSupportActionBar().setTitle(getResources().getString(R.string.title_day_details));
        customDialogView = LayoutInflater.from(currentActivity).inflate(R.layout.dialog_edit_quantity, null, false);
        Calendar calendar = DateTimeUtils.calendarByDate("yyyy-MM-dd", planExploreModel.getDeliberyDate());
        Calendar currentDate = Calendar.getInstance();

        textEditSubcription = (TextView) findViewById(R.id.textEditSubcription);
        editPackQuantity = (TextView) findViewById(R.id.editPackQuantity);
        containerHold = (LinearLayout) findViewById(R.id.containerHold);
        editPriceperPacket = (TextView) findViewById(R.id.editPriceperPacket);
        editSubscribeQuantity = (EditText) findViewById(R.id.editSubscribeQuantity);
        editSubscribeQuantityInLitres = (TextView) findViewById(R.id.editSubscribeQuantityInLitres);
        editAmount = (TextView) findViewById(R.id.editAmount);
        switchHold = (SwitchCompat) findViewById(R.id.switchHold);
        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);


        editPackQuantity.setText("" + planExploreModel.getProductPackQuantity());
        editPriceperPacket.setText("" + planExploreModel.getPrice());
        editSubscribeQuantity.setText("" + planExploreModel.getTotalPackets());
        editSubscribeQuantityInLitres.setText("" + (Float) (Float.parseFloat(editSubscribeQuantity.getText().toString()) / 2));

        // editSubscribeQuantityInLitres.setText("" + planExploreModel.getTotalPackets() / 2);
        editAmount.setText("" + (planExploreModel.getPrice() * planExploreModel.getTotalPackets()));
        editPackQuantity.setText("" + planExploreModel.getProductPackQuantity());


        if (planExploreModel.getStatus().equalsIgnoreCase("HOLD")) {
            switchHold.setChecked(true);
        } else {
            switchHold.setChecked(false);
        }


        editSubscribeQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                buttonUpdate.setVisibility(View.VISIBLE);

                if (!editSubscribeQuantity.getText().toString().isEmpty()) {
                    if (Integer.parseInt(editSubscribeQuantity.getText().toString()) == 0 || Integer.parseInt(editSubscribeQuantity.getText().toString()) == 1) {
                        editSubscribeQuantity.setText("2");
                        editSubscribeQuantity.setSelection(editSubscribeQuantity.getText().length());

                        editAmount.setText("" + Integer.parseInt(editSubscribeQuantity.getText().toString()) * planExploreModel.getPrice());

                    } else {
                        editAmount.setText("" + Integer.parseInt(editSubscribeQuantity.getText().toString()) * planExploreModel.getPrice());

                    }

                    editSubscribeQuantityInLitres.setText("" + (Float) (Float.parseFloat(editSubscribeQuantity.getText().toString()) / 2));
                } else {
                    editAmount.setText("" + 0);
                    editSubscribeQuantityInLitres.setText("" + 0);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        switchHold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //  switchHold.setChecked(!isChecked);
                if (isChecked) {
                    holdSubscriptionRequest();
                    //   alert(currentActivity, currentActivity.getString(R.string.alert_message_hold_subscription), currentActivity.getString(R.string.alert_message_hold_subscription), currentActivity.getString(R.string.labelOk), currentActivity.getString(R.string.labelCancel), true, false, AppConstants.ALERT_TYPE_HOLD_SUBSCRIPTION);

                } else {
                    unholdSubscriptionRequest();
                    //   alert(currentActivity, currentActivity.getString(R.string.alert_message_unhold_subscription), currentActivity.getString(R.string.alert_message_unhold_subscription), currentActivity.getString(R.string.labelOk), currentActivity.getString(R.string.labelCancel), true, false, AppConstants.ALERT_TYPE_UNHOLD_SUBSCRIPTION);

                }
            }
        });


        if (calendar.before(currentDate)) {
            editSubscribeQuantity.setEnabled(false);
            containerHold.setVisibility(View.GONE);
        } else {
            editSubscribeQuantity.setEnabled(true);
            containerHold.setVisibility(View.VISIBLE);
        }

        if (!isEditable()) {
            editSubscribeQuantity.setEnabled(false);
            containerHold.setVisibility(View.GONE);
            textEditSubcription.setVisibility(View.VISIBLE);
        } else {
            textEditSubcription.setVisibility(View.GONE);
        }

    }


    @Override
    protected void initContext() {

        currentActivity = SubscriptionDayDetails.this;
        context = SubscriptionDayDetails.this;
    }

    @Override
    protected void initListners() {
        buttonUpdate.setOnClickListener(this);
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
        setContentView(R.layout.activity_subscription_day_details);
    }

    @Override
    public void onAlertClicked(int alertType) {
        switch (alertType) {
            case AppConstants.ALERT_TYPE_HOLD_SUBSCRIPTION: {
                //   switchHold.setChecked(true);
                holdSubscriptionRequest();
            }
            case AppConstants.ALERT_TYPE_UNHOLD_SUBSCRIPTION: {
                //   switchHold.setChecked(false);
                unholdSubscriptionRequest();
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonUpdate: {
                editSubscribeQuantity.setError(null);
                if (editSubscribeQuantity.getText().toString().isEmpty()) {
                    editSubscribeQuantity.requestFocus();
                    editSubscribeQuantity.setError(getResources().getString(R.string.labelEmptySubscribedQuantity));
                } else {
                    changeQuantityRequest();
                }
                break;
            }
        }

    }


    public void holdSubscriptionRequest() {


        ((BaseActivity) currentActivity).progressDialog(currentActivity, currentActivity.getString(R.string.pdialog_message_loading), currentActivity.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsons = null;
        String COMPLETE_URL_HOLD_SUBSCRIPTION = AppConstants.URL_HOLD_SUBSCRIPTION + "?start_date=" + planExploreModel.getDeliberyDate() + "&end_date=" + planExploreModel.getDeliberyDate() + "&product_id=" + subscriptionModel.getProductId() + "&subscription_id=" + subscriptionModel.getMonthlySubscriptionId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, COMPLETE_URL_HOLD_SUBSCRIPTION, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ((BaseActivity) currentActivity).logTesting("hold subs responce is", response.toString(), Log.ERROR);

                try {
                    ((BaseActivity) currentActivity).cancelProgressDialog();
                    ((BaseActivity) currentActivity).logTesting("is successfull hold subscription", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {

                        ((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.messageSubscriptionHoldedSuccessfully), true);

                    } else {
                        ((BaseActivity) currentActivity).cancelProgressDialog();

                        ((BaseActivity) currentActivity).logTesting("hold subscription quan error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    ((BaseActivity) currentActivity).logTesting("hold subscription quan json exeption is", e.toString(), Log.ERROR);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) currentActivity).cancelProgressDialog();
                ((BaseActivity) currentActivity).logTesting("error is", error.toString(), Log.ERROR);
                ((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.errorHoldSubscription), true);

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


    public void unholdSubscriptionRequest() {


        ((BaseActivity) currentActivity).progressDialog(currentActivity, currentActivity.getString(R.string.pdialog_message_loading), currentActivity.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsons = null;
        String COMPLETE_URL_UNHOLD_SUBSCRIPTION = AppConstants.URL_UNHOLD_SUBSCRIPTION + "?start_date=" + planExploreModel.getDeliberyDate() + "&end_date=" + planExploreModel.getDeliberyDate() + "&product_id=" + subscriptionModel.getProductId() + "&subscription_id=" + subscriptionModel.getMonthlySubscriptionId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, COMPLETE_URL_UNHOLD_SUBSCRIPTION, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ((BaseActivity) currentActivity).logTesting("hold subs responce is", response.toString(), Log.ERROR);

                try {
                    ((BaseActivity) currentActivity).cancelProgressDialog();
                    ((BaseActivity) currentActivity).logTesting("is successfull hold subscription", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {

                        ((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.messageSubscriptionUnholdedSuccessfully), true);

                    } else {
                        ((BaseActivity) currentActivity).cancelProgressDialog();

                        ((BaseActivity) currentActivity).logTesting("hold subscription quan error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    ((BaseActivity) currentActivity).logTesting("hold subscription quan json exeption is", e.toString(), Log.ERROR);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) currentActivity).cancelProgressDialog();
                ((BaseActivity) currentActivity).logTesting("error is", error.toString(), Log.ERROR);
                ((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.errorHoldSubscription), true);

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


    private void changeQuantity() {
        editQuantity = (EditText) customDialogView.findViewById(R.id.editQuantity);
        buttonUpdate = (Button) customDialogView.findViewById(R.id.buttonUpdate);


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Validator.isNetworkAvailable(currentActivity)) {
                    ((BaseActivity) currentActivity).cancelCustomDialog();

                    if (!editQuantity.getText().toString().isEmpty()) {

                        changeQuantityRequest();
                    } else {
                        editQuantity.requestFocus();
                        editQuantity.setError(currentActivity.getResources().getString(R.string.errorQuantity));
                    }


                } else {
                    alert(currentActivity, currentActivity.getString(R.string.alert_message_no_network), currentActivity.getString(R.string.alert_message_no_network), currentActivity.getString(R.string.labelOk), currentActivity.getString(R.string.labelCancel), false, false, AppConstants.ALERT_TYPE_NO_NETWORK);

                }


            }
        });


    }


    public void changeQuantityRequest() {

        ChangeSubscriptionQuantityModel changeSubscriptionQuantityModel = new ChangeSubscriptionQuantityModel();
        changeSubscriptionQuantityModel.setStartDate(planExploreModel.getDeliberyDate());
        changeSubscriptionQuantityModel.setEndDate(planExploreModel.getDeliberyDate());
        changeSubscriptionQuantityModel.setProductId(subscriptionModel.getProductId());
        changeSubscriptionQuantityModel.setSubscriptionId(subscriptionModel.getMonthlySubscriptionId());
        changeSubscriptionQuantityModel.setTotalPackets(Integer.parseInt(editSubscribeQuantity.getText().toString()));

        ((BaseActivity) currentActivity).progressDialog(currentActivity, currentActivity.getString(R.string.pdialog_message_loading), currentActivity.getString(R.string.pdialog_message_loading), false, false);

        String json = new Gson().toJson(changeSubscriptionQuantityModel);
        ((BaseActivity) currentActivity).logTesting("login request json", json, Log.ERROR);
        JSONObject jsons = null;
        try {
            jsons = new JSONObject(json);
            new Gson().toJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AppConstants.URL_CHANGE_SUBSCRIPTION_QUANTITY, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ((BaseActivity) currentActivity).logTesting("subs responce is", response.toString(), Log.ERROR);

                try {
                    ((BaseActivity) currentActivity).cancelProgressDialog();
                    ((BaseActivity) currentActivity).logTesting("is successfull chnage subscription", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {

                        ((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.messageSubscriptionQuantityUpdated), true);

                    } else {
                        ((BaseActivity) currentActivity).cancelProgressDialog();

                        ((BaseActivity) currentActivity).logTesting("chnage subscription quan error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    ((BaseActivity) currentActivity).logTesting("chnage subscription quan json exeption is", e.toString(), Log.ERROR);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) currentActivity).cancelProgressDialog();
                ((BaseActivity) currentActivity).logTesting("error is", error.toString(), Log.ERROR);
                ((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.errorChnageSubscriptionQunatity), true);

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


    private boolean isEditable() {

        Calendar calendar = DateTimeUtils.calendarByDate("yyyy-MM-dd", planExploreModel.getDeliberyDate());
        Calendar currentDate = Calendar.getInstance();

        calendar.add(Calendar.DATE, -1);

        Log.e("delivery date", "" + calendar.get(Calendar.DATE));
        Log.e("current sub date", "" + currentDate.get(Calendar.DATE));

        if ((calendar.get(Calendar.DATE)) == currentDate.get(Calendar.DATE)) {
            if (currentDate.get(Calendar.HOUR_OF_DAY) >= 17 && currentDate.get(Calendar.MINUTE) >= 30) {
                return false;
            } else {
                return true;

            }

        } else {
            return true;
        }


    }


}
