package com.neon.vyaan.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.neon.vyaan.activity.PlanExploreActivity;
import com.neon.vyaan.application.VyaanApplication;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.fragments.MyPlans;
import com.neon.vyaan.interfaces.AlertClicked;
import com.neon.vyaan.model.AddSubscripionModel;
import com.neon.vyaan.model.ChangeSubscriptionQuantityModel;
import com.neon.vyaan.model.IncreaseSubscriptiomModel;
import com.neon.vyaan.model.SubscriptionModel;
import com.neon.vyaan.utils.DateTimeUtils;
import com.neon.vyaan.utils.Helper;
import com.neon.vyaan.utils.ImageHelper;
import com.neon.vyaan.utils.SharedPreferenceUtils;
import com.neon.vyaan.utils.Validator;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mayank on 30/08/2016.
 */
public class MyPlanAdapter extends RecyclerView.Adapter<MyPlanAdapter.ViewHolder> implements AlertClicked {

    List<SubscriptionModel> subscriptionModelList;
    Activity currentActivity;

    View customDialogView;

    EditText editQuantity;
    Button buttonChangeQuantity;

    DatePickerDialog datePickerDialog;


    DatePickerDialog.OnDateSetListener dateSetListener;

    int subscriptionStartYear;
    int subscriptionStartMonth;
    int subscriptionStartDay;


    int subscriptionEndYear;
    int subscriptionEndMonth;
    int subscriptioEndDay;


    int positionSelected = 0;

    String newStartDate;
    String newEndDate;

    Bundle bundle;

    public MyPlanAdapter(Activity currentActivity, List<SubscriptionModel> subscriptionModelList)

    {
        this.subscriptionModelList = subscriptionModelList;
        this.currentActivity = currentActivity;

        customDialogView = LayoutInflater.from(currentActivity).inflate(R.layout.dialog_edit_quantity, null, false);

    }

    @Override
    public MyPlanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(currentActivity).inflate(R.layout.items_my_plans, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyPlanAdapter.ViewHolder holder, final int position) {
        final int pos = position;

        Picasso.with(currentActivity).load(AppConstants.BASE_URL_IMAGES + "/" + subscriptionModelList.get(pos).getProductImage()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                holder.imageProduct.setImageBitmap(ImageHelper.getRoundedCornerBitmap(bitmap, 10));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });



        holder.textProductName.setText(subscriptionModelList.get(pos).getProductName());

        holder.textStartDate.setText(DateTimeUtils.dateInParticularFormat("yyyy-MM-dd", "dd-MM-yyyy", subscriptionModelList.get(pos).getStartDate()));
        holder.textEndDate.setText(DateTimeUtils.dateInParticularFormat("yyyy-MM-dd", "dd-MM-yyyy", subscriptionModelList.get(pos).getEndDate()));


        if (subscriptionModelList.get(pos).getIsRegular() == 1) {
            holder.textSubscribedFor.setText(currentActivity.getResources().getString(R.string.labelRegular));
        } else {
            holder.textSubscribedFor.setText(currentActivity.getResources().getString(R.string.labelAlternate));
        }

        holder.textAmount.setText("" + subscriptionModelList.get(pos).getPaymentAmount());
        holder.buttonExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bundle == null) {
                    bundle = new Bundle();
                }
                bundle.putParcelable(AppConstants.SUBSCRIPTION_MODEL, subscriptionModelList.get(pos));
                ((BaseActivity) currentActivity).startActivity(currentActivity, PlanExploreActivity.class, bundle, true, AppConstants.REQUEST_TAG_MY_PLANS, true, AppConstants.ANIMATION_SLIDE_LEFT);

            }
        });

        holder.imageChangeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positionSelected = pos;
                openChooser(pos);
            }
        });


    }

    @Override
    public int getItemCount() {
        return subscriptionModelList.size();
    }

    @Override
    public void onAlertClicked(int alertType) {

        switch (alertType) {
            case AppConstants.ALERT_TYPE_CANCEL_SUBSCRIPTION: {
                cancelSubscriptionRequest(positionSelected);
            }
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageProduct;
        ImageView imageChangeOrder;
        TextView textProductName;
        TextView textStartDate;
        TextView textEndDate;
        TextView textAmount;
        TextView textSubscribedFor;
        Button buttonExplore;

        public ViewHolder(View itemView) {
            super(itemView);

            textProductName = (TextView) itemView.findViewById(R.id.textProductName);
            imageProduct = (ImageView) itemView.findViewById(R.id.imageProduct);
            imageChangeOrder = (ImageView) itemView.findViewById(R.id.imageChangeOrder);
            textStartDate = (TextView) itemView.findViewById(R.id.textStartDate);
            textEndDate = (TextView) itemView.findViewById(R.id.textEndDate);
            textAmount = (TextView) itemView.findViewById(R.id.textAmount);
            textSubscribedFor = (TextView) itemView.findViewById(R.id.textSubscribedFor);
            buttonExplore = (Button) itemView.findViewById(R.id.buttonExplore);

        }
    }


    private void openChooser(final int pos) {
        final CharSequence[] items = currentActivity.getResources().getStringArray(R.array.changeOrder);
        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
        builder.setTitle(currentActivity
                .getResources().getString(R.string.changeSubscription));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(items[0])) {
                    ((BaseActivity) currentActivity).creatingDialog(currentActivity, false, false, customDialogView, 220, 300, false);
                    changeQuantity(pos);


                } else if (items[item].equals(items[1])) {

                } else if (items[item].equals(items[2])) {

                    Calendar calendarEndDate = DateTimeUtils.calendarByDate("yyyy-MM-dd", subscriptionModelList.get(pos).getEndDate());
                    Calendar calendarStartDate = DateTimeUtils.calendarByDate("yyyy-MM-dd", subscriptionModelList.get(pos).getStartDate());


                    if (DateTimeUtils.daysBetween(calendarStartDate, calendarEndDate) == 30) {
                        ((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.labelMaxSub), false);
                    } else {
                        initDateAndTimePicker(calendarEndDate, pos);

                        Log.e("day between", "" + DateTimeUtils.daysBetween(calendarStartDate, calendarEndDate));
                        Calendar calendarNewMaxDate = Calendar.getInstance();
                        calendarNewMaxDate.setTime(calendarEndDate.getTime());
                        calendarNewMaxDate.add(Calendar.DATE, 30 - DateTimeUtils.daysBetween(calendarStartDate, calendarEndDate));
                        Log.e("start", "" + calendarStartDate.getTimeInMillis());
                        Log.e("day between", "" + DateTimeUtils.daysBetween(calendarStartDate, calendarEndDate));

                        Log.e("end", "" + calendarNewMaxDate.getTimeInMillis());
                        datePickerDialog.getDatePicker().setMinDate(calendarEndDate.getTimeInMillis());
                        datePickerDialog.getDatePicker().setMaxDate(calendarNewMaxDate.getTimeInMillis());
                        datePickerDialog.show();

                    }


                } else if (items[item].equals(items[3])) {
                    alert(currentActivity, currentActivity.getString(R.string.alert_message_cancel_subscription), currentActivity.getString(R.string.alert_message_cancel_subscription), currentActivity.getString(R.string.labelOk), currentActivity.getString(R.string.labelCancel), true, false, AppConstants.ALERT_TYPE_CANCEL_SUBSCRIPTION);


                }
            }
        });
        builder.show();
    }


    private void changeQuantity(final int pos) {
        editQuantity = (EditText) customDialogView.findViewById(R.id.editQuantity);
        buttonChangeQuantity = (Button) customDialogView.findViewById(R.id.buttonChangeQuantity);


        buttonChangeQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Validator.isNetworkAvailable(currentActivity)) {
                    ((BaseActivity) currentActivity).cancelCustomDialog();

                    if (!editQuantity.getText().toString().isEmpty()) {

                        changeQuantityRequest(pos);
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


    private void orderQuantity(final int pos) {
        editQuantity = (EditText) customDialogView.findViewById(R.id.editQuantity);
        buttonChangeQuantity = (Button) customDialogView.findViewById(R.id.buttonChangeQuantity);


        buttonChangeQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Validator.isNetworkAvailable(currentActivity)) {
                    ((BaseActivity) currentActivity).cancelCustomDialog();

                    if (!editQuantity.getText().toString().isEmpty()) {
                        increaseSubscription(pos);
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

    public void changeQuantityRequest(int position) {

        ChangeSubscriptionQuantityModel changeSubscriptionQuantityModel = new ChangeSubscriptionQuantityModel();
        changeSubscriptionQuantityModel.setStartDate(subscriptionModelList.get(position).getStartDate());
        changeSubscriptionQuantityModel.setEndDate(subscriptionModelList.get(position).getEndDate());
        changeSubscriptionQuantityModel.setProductId(subscriptionModelList.get(position).getProductId());
        changeSubscriptionQuantityModel.setSubscriptionId(subscriptionModelList.get(position).getMonthlySubscriptionId());
        changeSubscriptionQuantityModel.setTotalPackets(Integer.parseInt(editQuantity.getText().toString()));

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


    private void initDateAndTimePicker(Calendar calendarEndDate, final int pos) {

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {


                subscriptionStartYear = i;
                subscriptionStartMonth = i1;
                subscriptionStartDay = i2;
                DateTimeUtils.currentDate("dd", makeCalendar(subscriptionStartYear, subscriptionStartMonth, subscriptionStartDay));


                subscriptionEndYear = i;
                subscriptionEndMonth = i1;
                subscriptioEndDay = i2;

                ((BaseActivity) currentActivity).creatingDialog(currentActivity, false, false, customDialogView, 220, 300, false);
                orderQuantity(pos);
                newEndDate = DateTimeUtils.currentDate("yyyy-MM-dd", makeCalendar(subscriptionEndYear, subscriptionEndMonth, subscriptioEndDay));
            }
        };

        datePickerDialog = new DatePickerDialog(currentActivity, dateSetListener, calendarEndDate.get(Calendar.YEAR), calendarEndDate.get(Calendar.MONTH), calendarEndDate.get(Calendar.DAY_OF_MONTH));


    }


    private Calendar makeCalendar(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar;
    }


    public void cancelSubscriptionRequest(int position) {


        ((BaseActivity) currentActivity).progressDialog(currentActivity, currentActivity.getString(R.string.pdialog_message_loading), currentActivity.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsons = null;
        String COMPLETE_URL_CANCEL_SUBSCRIPTION = AppConstants.URL_CANCEL_SUBSCRIPTION + "?start_date=" + DateTimeUtils.currentDate("yyyy-MM-dd", Calendar.getInstance()) + "&product_id=" + subscriptionModelList.get(position).getProductId() + "&subscription_id=" + subscriptionModelList.get(position).getMonthlySubscriptionId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, COMPLETE_URL_CANCEL_SUBSCRIPTION, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ((BaseActivity) currentActivity).logTesting("cancel subs responce is", response.toString(), Log.ERROR);

                try {
                    ((BaseActivity) currentActivity).cancelProgressDialog();
                    ((BaseActivity) currentActivity).logTesting("is successfull cancel subscription", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {

                        ((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.messageSubscriptionCancelledSuccessfully), true);

                    } else {
                        ((BaseActivity) currentActivity).cancelProgressDialog();

                        ((BaseActivity) currentActivity).logTesting("cancel subscription quan error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    ((BaseActivity) currentActivity).logTesting("cancel subscription quan json exeption is", e.toString(), Log.ERROR);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) currentActivity).cancelProgressDialog();
                ((BaseActivity) currentActivity).logTesting("error is", error.toString(), Log.ERROR);
                ((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.errorCancelSubscription), true);

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


    public void alert(Context context, String title, String message, String positiveButton, String negativeButton, boolean isNegativeButton, boolean isTitle, final int alertType) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        if (isTitle) {
            builder.setTitle(title);
        }


        builder.setMessage(message);
        builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                onAlertClicked(alertType);
            }
        });
        if (isNegativeButton) {
            builder.setNegativeButton(negativeButton, null);
        }

        builder.show();


    }


    private IncreaseSubscriptiomModel initIncreaseSubModel(final int positionSelected) {


        IncreaseSubscriptiomModel increaseSubscriptiomModel = new IncreaseSubscriptiomModel();
        increaseSubscriptiomModel.setProductId(subscriptionModelList.get(positionSelected).getProductId());
        increaseSubscriptiomModel.setUser_id(SharedPreferenceUtils.getInstance(currentActivity).getInteger(AppConstants.KEY_USER_ID));
        increaseSubscriptiomModel.setStartDate(subscriptionModelList.get(positionSelected).getEndDate());
        increaseSubscriptiomModel.setEndDate(newEndDate);
        increaseSubscriptiomModel.setPaymentAmount(subscriptionModelList.get(positionSelected).getPaymentAmount());
        increaseSubscriptiomModel.setPaymentStatus("Unpaid");
        increaseSubscriptiomModel.setQuantity(Integer.parseInt(editQuantity.getText().toString()));
        increaseSubscriptiomModel.setPrice(subscriptionModelList.get(positionSelected).getProduct_price());
        increaseSubscriptiomModel.setType("sub");
        increaseSubscriptiomModel.setSubscription_id(subscriptionModelList.get(positionSelected).getMonthlySubscriptionId());
        increaseSubscriptiomModel.setStatus("Undelivered");

        return increaseSubscriptiomModel;
    }


    public void increaseSubscription(int positionSelected) {
        ((BaseActivity) currentActivity).progressDialog(currentActivity, currentActivity.getString(R.string.pdialog_message_loading), currentActivity.getString(R.string.pdialog_message_loading), false, false);


        String json = new Gson().toJson(initIncreaseSubModel(positionSelected));
        ((BaseActivity) currentActivity).logTesting("increase Subscription request json", json, Log.ERROR);
        JSONObject jsons = null;
        try {
            jsons = new JSONObject(json);
            new Gson().toJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AppConstants.URL_INCREASE_SUBSCRIPTION, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ((BaseActivity) currentActivity).cancelProgressDialog();
                ((BaseActivity) currentActivity).logTesting("increase Subscription is", response.toString(), Log.ERROR);

                try {
                    ((BaseActivity) currentActivity).logTesting("is successfull increase Subscription", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {
                        ((BaseActivity) currentActivity).toast(((BaseActivity) currentActivity).getString(R.string.message_subscription_increased), true);
                        ((Dashboard) currentActivity).myPlans();
                    } else {

                        ((BaseActivity) currentActivity).logTesting("increase Subscription error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    ((BaseActivity) currentActivity).logTesting("increase Subscription json exeption is", e.toString(), Log.ERROR);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) currentActivity).cancelProgressDialog();
                ((BaseActivity) currentActivity).logTesting("error is", error.toString(), Log.ERROR);
                ((BaseActivity) currentActivity).toast(((BaseActivity) currentActivity).getResources().getString(R.string.errorIncreaseSubscription), true);

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
