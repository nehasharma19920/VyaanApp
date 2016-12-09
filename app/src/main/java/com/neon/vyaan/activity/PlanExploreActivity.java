package com.neon.vyaan.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.gson.Gson;
import com.neon.vyaan.R;
import com.neon.vyaan.application.VyaanApplication;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.model.DecreaseSubscriptionModel;
import com.neon.vyaan.model.IncreaseSubscriptiomModel;
import com.neon.vyaan.model.PlanExploreModel;
import com.neon.vyaan.model.ReorderSubscriptionModel;
import com.neon.vyaan.model.SubscriptionModel;
import com.neon.vyaan.utils.DateTimeUtils;
import com.neon.vyaan.utils.SharedPreferenceUtils;
import com.neon.vyaan.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanExploreActivity extends BaseActivity {

    CompactCalendarView compactCalendarView;
    Button buttonCancel;
    Button buttonIncrease;
    TextView textSelectedMonth;

    SubscriptionModel subscriptionModel;

    List<PlanExploreModel> planExploreModelList;

    DatePickerDialog datePickerDialog;


    DatePickerDialog.OnDateSetListener dateSetListener;

    int subscriptionStartYear;
    int subscriptionStartMonth;
    int subscriptionStartDay;


    int subscriptionEndYear;
    int subscriptionEndMonth;
    int subscriptioEndDay;

    View customDialogView;

    EditText editQuantity;
    Button buttonChangeQuantity;

    String newEndDate;

    Button buttonDecrease;
    Button buttonReorder;

    boolean isIncreaseSubscription;
    boolean isBackPressed;

    String reorderStartDate;
    String reorderEndDate;


    @Override
    protected void initViews() {

        getSupportActionBar().setTitle(getResources().getString(R.string.title_plan_details));

        customDialogView = LayoutInflater.from(currentActivity).inflate(R.layout.dialog_edit_quantity, null, false);

        planExploreModelList = new ArrayList<>();

        subscriptionModel = getIntent().getParcelableExtra(SUBSCRIPTION_MODEL);

        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactCalendarView);

        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        buttonIncrease = (Button) findViewById(R.id.buttonIncrease);
        textSelectedMonth = (TextView) findViewById(R.id.textSelectedMonth);

        buttonDecrease = (Button) findViewById(R.id.buttonDecrease);
        buttonReorder = (Button) findViewById(R.id.buttonReorder);


        buttonCancel.setOnClickListener(this);
        buttonIncrease.setOnClickListener(this);
        buttonDecrease.setOnClickListener(this);
        buttonReorder.setOnClickListener(this);


        reorderStatus();
        // Add event 1 on Sun, 07 Jun 2015 18:20:51 GMT
        Event ev1 = new Event(Color.GREEN, 1433701251000L, "Some extra data that I want to store.");
        compactCalendarView.addEvent(ev1);

        // Added event 2 GMT: Sun, 07 Jun 2015 19:10:51 GMT
        Event ev2 = new Event(Color.GREEN, 1433704251000L);
        compactCalendarView.addEvent(ev2);

        // Query for events on Sun, 07 Jun 2015 GMT.
        // Time is not relevant when querying for events, since events are returned by day.
        // So you can pass in any arbitary DateTime and you will receive all events for that day.
        List<Event> events = compactCalendarView.getEvents(1433701251000L); // can also take a Date object

        // events has size 2 with the 2 events inserted previously
        Log.d("events", "Events: " + events);

        // define a listener to receive callbacks when certain events happen.
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                Log.e("date", "Day was clicked: " + dateClicked + " with events " + events);

                if (events.size() > 0) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dateClicked);

                    Calendar startCalendar = DateTimeUtils.calendarByDate("yyyy-MM-dd", planExploreModelList.get(0).getDeliberyDate());
                    Calendar endCalendar = DateTimeUtils.calendarByDate("yyyy-MM-dd", planExploreModelList.get(planExploreModelList.size() - 1).getDeliberyDate());

                    if (bundle == null) {
                        bundle = new Bundle();
                    }

                    bundle.putParcelable(PLAN_EXPLORE, (PlanExploreModel) events.get(0).getData());
                    bundle.putParcelable(SUBSCRIPTION_MODEL, subscriptionModel);
                    startActivity(currentActivity, SubscriptionDayDetails.class, bundle, true, REQUEST_TAG_PLAN_EXPLORE_ACTIVITY, true, ANIMATION_SLIDE_UP);


                } else {
                    toast(getResources().getString(R.string.message_no_subscription), true);

                }


            /*    if ((dateClicked.equals(startCalendar) || dateClicked.after(startCalendar.getTime())) && (dateClicked.equals(endCalendar) || dateClicked.before(endCalendar.getTime()))) {
                    compactCalendarView.setCurrentSelectedDayBackgroundColor(Color.parseColor("#E57373"));
                } else {
                    compactCalendarView.setCurrentSelectedDayBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                if (DateTimeUtils.currentDate("MMM-yyyy", calendar).toString().equals(DateTimeUtils.currentDate("MMM-yyyy", Calendar.getInstance()))) {

                }*/

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                compactCalendarView.setCurrentDate(firstDayOfNewMonth);

                Log.e("month", "Month was scrolled to: " + firstDayOfNewMonth);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(firstDayOfNewMonth);


                textSelectedMonth.setText(DateTimeUtils.currentDate("MMM-yyyy", calendar));

                Calendar startCalendar = DateTimeUtils.calendarByDate("yyyy-MM-dd", planExploreModelList.get(0).getDeliberyDate());
                Calendar endCalendar = DateTimeUtils.calendarByDate("yyyy-MM-dd", planExploreModelList.get(planExploreModelList.size() - 1).getDeliberyDate());

                Log.e("under o", DateTimeUtils.currentDate("M-yyyy", startCalendar).toString());
                Log.e("under s end", (firstDayOfNewMonth.getMonth() + 1) + "-" + (firstDayOfNewMonth.getYear() + 1900));

                if (DateTimeUtils.currentDate("M-yyyy", startCalendar).toString().equals((firstDayOfNewMonth.getMonth() + 1) + "-" + (firstDayOfNewMonth.getYear() + 1900)))

                {
                    Log.e("match same", "yes");
               /*     Log.e("under old", DateTimeUtils.currentDate("M-yyyy", startCalendar).toString());
                    Log.e("under strt end", firstDayOfNewMonth.getMonth() + "-" + (firstDayOfNewMonth.getYear() + 1900));*/
                    compactCalendarView.setCurrentSelectedDayBackgroundColor(Color.parseColor("#E57373"));
                    compactCalendarView.setCurrentDate(DateTimeUtils.calendarByDate("yyyy-MM-dd", planExploreModelList.get(0).getDeliberyDate()).getTime());
                    compactCalendarView.setSelected(false);
                } else {
                    Log.e("under new", firstDayOfNewMonth.toString());
                    compactCalendarView.setCurrentDate(firstDayOfNewMonth);

                    if ((firstDayOfNewMonth.equals(startCalendar) || firstDayOfNewMonth.after(startCalendar.getTime())) && (firstDayOfNewMonth.equals(endCalendar) || firstDayOfNewMonth.before(endCalendar.getTime()))) {
                        compactCalendarView.setCurrentSelectedDayBackgroundColor(Color.parseColor("#E57373"));
                        Log.e("under com", firstDayOfNewMonth.toString());
                        //  compactCalendarView.setCurrentDate(DateTimeUtils.calendarByDate("yyyy-MM-dd", planExploreModelList.get(0).getDeliberyDate()).getTime());
                    } else {
                        compactCalendarView.setCurrentSelectedDayBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                }


           /*     if (DateTimeUtils.currentDate("MMM-yyyy", calendar).toString().equals(DateTimeUtils.currentDate("MMM-yyyy", Calendar.getInstance())))

                {
                    compactCalendarView.setCurrentSelectedDayBackgroundColor(Color.parseColor("#E57373"));
                    compactCalendarView.setCurrentDate(DateTimeUtils.calendarByDate("yyyy-MM-dd", planExploreModelList.get(0).getDeliberyDate()).getTime());
                } else {
                    compactCalendarView.setCurrentSelectedDayBackgroundColor(Color.parseColor("#FFFFFF"));
                }*/

            }
        });

        myPlanDetails();

    }

    @Override
    protected void initContext() {
        currentActivity = PlanExploreActivity.this;
        context = PlanExploreActivity.this;

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
        setContentView(R.layout.activity_plan_explore);
    }

    @Override
    public void onAlertClicked(int alertType) {
        switch (alertType) {
            case AppConstants.ALERT_TYPE_CANCEL_SUBSCRIPTION: {
                cancelSubscriptionRequest();
            }
        }
    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.buttonCancel: {
                alert(currentActivity, currentActivity.getString(R.string.alert_message_cancel_subscription), currentActivity.getString(R.string.alert_message_cancel_subscription), currentActivity.getString(R.string.labelOk), currentActivity.getString(R.string.labelCancel), true, false, AppConstants.ALERT_TYPE_CANCEL_SUBSCRIPTION);

                break;
            }

            case R.id.buttonIncrease: {

                isBackPressed = false;
                isIncreaseSubscription = true;

                Calendar calendarEndDate = DateTimeUtils.calendarByDate("yyyy-MM-dd", subscriptionModel.getEndDate());
                Calendar calendarStartDate = DateTimeUtils.calendarByDate("yyyy-MM-dd", subscriptionModel.getStartDate());


                if (DateTimeUtils.daysBetween(calendarStartDate, calendarEndDate) == 30) {
                    ((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.labelMaxSub), false);
                } else {
                    initDateAndTimePicker(calendarEndDate);

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


                break;
            }


            case R.id.buttonDecrease: {
                isBackPressed = false;
                isIncreaseSubscription = false;

                Calendar calendarEndDate = DateTimeUtils.calendarByDate("yyyy-MM-dd", subscriptionModel.getEndDate());
                Calendar calendarStartDate = DateTimeUtils.calendarByDate("yyyy-MM-dd", subscriptionModel.getStartDate());


                if (DateTimeUtils.daysBetween(calendarStartDate, calendarEndDate) <= 5) {
                    ((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.labelMinSub), false);
                } else {
                    initDateAndTimePicker(calendarEndDate);

                    Log.e("day between", "" + DateTimeUtils.daysBetween(calendarStartDate, calendarEndDate));
                    Calendar calendarNewMaxDate = Calendar.getInstance();
                    calendarNewMaxDate.setTime(calendarStartDate.getTime());
                    calendarNewMaxDate.add(Calendar.DATE, 5);
                    Log.e("start", "" + calendarStartDate.getTimeInMillis());
                    Log.e("day between", "" + DateTimeUtils.daysBetween(calendarStartDate, calendarEndDate));

                    Log.e("end", "" + calendarNewMaxDate.getTimeInMillis());
                    datePickerDialog.getDatePicker().setMinDate(calendarNewMaxDate.getTimeInMillis());
                    datePickerDialog.getDatePicker().setMaxDate(calendarEndDate.getTimeInMillis());
                    datePickerDialog.show();

                }


                break;
            }


            case R.id.buttonReorder: {


                reorderSubscription();

            /*    Calendar calendarCurrentDate = Calendar.getInstance();
                Calendar calendarEndDate = DateTimeUtils.calendarByDate("yyyy-MM-dd", subscriptionModel.getEndDate());


                if (DateTimeUtils.daysBetween(calendarCurrentDate, calendarEndDate) <= 5) {
                    buttonReorder.setEnabled(true);
                    ((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.labelMaxSub), false);
                } else {
                    buttonReorder.setEnabled(false);
                }
*/

                break;
            }
        }

    }

    public void myPlanDetails() {

        progressDialog(context, context.getString(R.string.pdialog_message_loading), context.getString(R.string.pdialog_message_loading), false, false);


        JSONObject jsons = null;
        try {
            jsons = new JSONObject();
            jsons.put("product_id", subscriptionModel.getProductId());
            jsons.put("subscription_id", subscriptionModel.getMonthlySubscriptionId());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_SUBSCRIPTION_DETAILS, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cancelProgressDialog();
                logTesting("responce is", response.toString(), Log.ERROR);

                try {
                    logTesting("is successfull plan", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {
                        planExploreModelList.clear();
                        Gson gson = new Gson();
                        List<PlanExploreModel> planExploreModelListTemp = Arrays.asList(gson.fromJson(response.getJSONArray(MESSAGE).toString(), PlanExploreModel[].class));
                        planExploreModelList.addAll(planExploreModelListTemp);
                        Log.e("plan explore model l se", "" + planExploreModelListTemp.size());
                        initEvents();

                    } else {
                        cancelProgressDialog();
                        logTesting("plan explore error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    logTesting("plan details json exeption is", e.toString(), Log.ERROR);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                logTesting("error is", error.toString(), Log.ERROR);
                toast(getResources().getString(R.string.errorPlan), true);

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

    private void initEvents() {

        textSelectedMonth.setText(DateTimeUtils.currentDate("MMM yyyy", Calendar.getInstance()));
        compactCalendarView.removeAllEvents();

        compactCalendarView.setUseThreeLetterAbbreviation(true);

        //   compactCalendarView.goT

        compactCalendarView.setCurrentDate(DateTimeUtils.calendarByDate("yyyy-MM-dd", planExploreModelList.get(0).getDeliberyDate()).getTime());
        // compactCalendarView.setSelected(false);

        //  compactCalendarView.setse(false);
        for (int i = 0; i < planExploreModelList.size(); i++) {
            Calendar calendar = DateTimeUtils.calendarByDate("yyyy-MM-dd", planExploreModelList.get(i).getDeliberyDate());
            Calendar currentDate = Calendar.getInstance();

            if (calendar.before(currentDate)) {
                //   compactCalendarView.flecompactCalendarMultiEventIndicatorColor
                Event ev1 = new Event(Color.GRAY, calendar.getTimeInMillis(), planExploreModelList.get(i));
                compactCalendarView.addEvent(ev1);
            } else {
                if (planExploreModelList.get(i).getStatus().equalsIgnoreCase("HOLD")) {
                    Event ev1 = new Event(Color.RED, calendar.getTimeInMillis(), planExploreModelList.get(i));
                    compactCalendarView.addEvent(ev1);
                } else {
                    Event ev1 = new Event(Color.GREEN, calendar.getTimeInMillis(), planExploreModelList.get(i));
                    compactCalendarView.addEvent(ev1);
                }
            }


        }


    }


    public void cancelSubscriptionRequest() {


        ((BaseActivity) currentActivity).progressDialog(currentActivity, currentActivity.getString(R.string.pdialog_message_loading), currentActivity.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsons = null;
        String COMPLETE_URL_CANCEL_SUBSCRIPTION = AppConstants.URL_CANCEL_SUBSCRIPTION + "?start_date=" + DateTimeUtils.currentDate("yyyy-MM-dd", Calendar.getInstance()) + "&product_id=" + subscriptionModel.getProductId() + "&subscription_id=" + subscriptionModel.getMonthlySubscriptionId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, COMPLETE_URL_CANCEL_SUBSCRIPTION, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ((BaseActivity) currentActivity).logTesting("cancel subs responce is", response.toString(), Log.ERROR);

                try {
                    ((BaseActivity) currentActivity).cancelProgressDialog();
                    ((BaseActivity) currentActivity).logTesting("is successfull cancel subscription", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {

                        ((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.messageSubscriptionCancelledSuccessfully), true);
                        setResult(RESULT_OK);
                        finish();
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


    private void initDateAndTimePicker(Calendar calendarEndDate) {

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

                if (!isBackPressed) {
                    Log.e("is back", "" + "no");
                    if (isIncreaseSubscription) {
                        ((BaseActivity) currentActivity).creatingDialog(currentActivity, false, false, customDialogView, 220, 300, false);

                        newEndDate = DateTimeUtils.currentDate("yyyy-MM-dd", makeCalendar(subscriptionEndYear, subscriptionEndMonth, subscriptioEndDay));
                        orderQuantity();
                    } else {
                        newEndDate = DateTimeUtils.currentDate("yyyy-MM-dd", makeCalendar(subscriptionEndYear, subscriptionEndMonth, subscriptioEndDay));

                        decreaseSubscription();
                    }

                }
            }
        };

        datePickerDialog = new DatePickerDialog(currentActivity, dateSetListener, calendarEndDate.get(Calendar.YEAR), calendarEndDate.get(Calendar.MONTH), calendarEndDate.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                isBackPressed = true;
            }
        });
        datePickerDialog.setCancelable(false);
    }


    private void orderQuantity() {
        editQuantity = (EditText) customDialogView.findViewById(R.id.editQuantity);
        buttonChangeQuantity = (Button) customDialogView.findViewById(R.id.buttonChangeQuantity);


        buttonChangeQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Validator.isNetworkAvailable(currentActivity)) {
                    ((BaseActivity) currentActivity).cancelCustomDialog();

                    if (!editQuantity.getText().toString().isEmpty()) {
                        //  cancelCustomDialog();
                        increaseSubscription();
                   /*     if (isIncreaseSubscription) {

                        } else {

                        }
*/
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


    private Calendar makeCalendar(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar;
    }


    public void increaseSubscription() {
        ((BaseActivity) currentActivity).progressDialog(currentActivity, currentActivity.getString(R.string.pdialog_message_loading), currentActivity.getString(R.string.pdialog_message_loading), false, false);


        String json = new Gson().toJson(initIncreaseSubModel());
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
                        //  myPlanDetails();
                        finish();
                        //  onBackPressed();
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


    private IncreaseSubscriptiomModel initIncreaseSubModel() {


        IncreaseSubscriptiomModel increaseSubscriptiomModel = new IncreaseSubscriptiomModel();
        increaseSubscriptiomModel.setProductId(subscriptionModel.getProductId());
        increaseSubscriptiomModel.setUser_id(SharedPreferenceUtils.getInstance(currentActivity).getInteger(AppConstants.KEY_USER_ID));
        increaseSubscriptiomModel.setStartDate(subscriptionModel.getEndDate());
        increaseSubscriptiomModel.setEndDate(newEndDate);
        increaseSubscriptiomModel.setPaymentAmount(subscriptionModel.getPaymentAmount());
        increaseSubscriptiomModel.setPaymentStatus("Unpaid");
        increaseSubscriptiomModel.setQuantity(Integer.parseInt(editQuantity.getText().toString()));
        increaseSubscriptiomModel.setPrice(subscriptionModel.getProduct_price());
        increaseSubscriptiomModel.setType("sub");
        increaseSubscriptiomModel.setSubscription_id(subscriptionModel.getMonthlySubscriptionId());
        increaseSubscriptiomModel.setStatus("Undelivered");

        return increaseSubscriptiomModel;
    }


    private DecreaseSubscriptionModel initDecreaseSubModel() {


        DecreaseSubscriptionModel decreaseSubscriptiomModel = new DecreaseSubscriptionModel();
        decreaseSubscriptiomModel.setProductId(subscriptionModel.getProductId());
        decreaseSubscriptiomModel.setUser_id(SharedPreferenceUtils.getInstance(currentActivity).getInteger(AppConstants.KEY_USER_ID));
        decreaseSubscriptiomModel.setStartDate(subscriptionModel.getEndDate());
        decreaseSubscriptiomModel.setEndDate(newEndDate);
        decreaseSubscriptiomModel.setPaymentAmount(subscriptionModel.getPaymentAmount());
        decreaseSubscriptiomModel.setPaymentStatus("Unpaid");
        decreaseSubscriptiomModel.setQuantity(0);
        decreaseSubscriptiomModel.setPrice(subscriptionModel.getProduct_price());
        decreaseSubscriptiomModel.setType("sub");
        decreaseSubscriptiomModel.setSubscription_id(subscriptionModel.getMonthlySubscriptionId());
        decreaseSubscriptiomModel.setStatus("Undelivered");

        return decreaseSubscriptiomModel;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        myPlanDetails();
      /*  if (requestCode == REQUEST_TAG_PLAN_EXPLORE_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                myPlanDetails();
            }
        }*/
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void reorderStatus() {
        Calendar calendarCurrentDate = Calendar.getInstance();
        Calendar calendarEndDate = DateTimeUtils.calendarByDate("yyyy-MM-dd", subscriptionModel.getEndDate());


        if (DateTimeUtils.daysBetween(calendarCurrentDate, calendarEndDate) <= 5) {
            buttonReorder.setEnabled(true);
            buttonReorder.setBackgroundColor(getResources().getColor(R.color.button_color));
        } else {
            buttonReorder.setEnabled(false);
            buttonReorder.setBackgroundColor(getResources().getColor(R.color.button_color_opaque));
        }

    }

 /*   @Override
    public void onBackPressed() {
        Log.e("is back", "" + isBackPressed);
        isBackPressed = true;
        super.onBackPressed();
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // write your code here
            Log.e("is back", "" + isBackPressed);
            isBackPressed = true;

        }
        return super.onKeyDown(keyCode, event);
    }


    public void decreaseSubscription() {
        ((BaseActivity) currentActivity).progressDialog(currentActivity, currentActivity.getString(R.string.pdialog_message_loading), currentActivity.getString(R.string.pdialog_message_loading), false, false);


        String json = new Gson().toJson(initDecreaseSubModel());
        ((BaseActivity) currentActivity).logTesting("decrease Subscription request json", json, Log.ERROR);
        JSONObject jsons = null;
        try {
            jsons = new JSONObject(json);
            new Gson().toJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AppConstants.URL_DECREASE_SUBSCRIPTION, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ((BaseActivity) currentActivity).cancelProgressDialog();
                ((BaseActivity) currentActivity).logTesting("decrease Subscription is", response.toString(), Log.ERROR);

                try {
                    ((BaseActivity) currentActivity).logTesting("is successfull increase Subscription", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {
                        ((BaseActivity) currentActivity).toast(((BaseActivity) currentActivity).getString(R.string.message_subscription_decreased), true);
                        // myPlanDetails();
                        finish();
                        // onBackPressed();
                    } else {

                        ((BaseActivity) currentActivity).logTesting("decrease Subscription error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    ((BaseActivity) currentActivity).logTesting("decrease Subscription json exeption is", e.toString(), Log.ERROR);
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


    public void reorderSubscription() {
        ((BaseActivity) currentActivity).progressDialog(currentActivity, currentActivity.getString(R.string.pdialog_message_loading), currentActivity.getString(R.string.pdialog_message_loading), false, false);


        String json = new Gson().toJson(initReorderSubModel());
        ((BaseActivity) currentActivity).logTesting("reorder Subscription request json", json, Log.ERROR);
        JSONObject jsons = null;
        try {
            jsons = new JSONObject(json);
            new Gson().toJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AppConstants.URL_REORDER_SUBSCRIPTION, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ((BaseActivity) currentActivity).cancelProgressDialog();
                ((BaseActivity) currentActivity).logTesting("reorder Subscription is", response.toString(), Log.ERROR);

                try {
                    ((BaseActivity) currentActivity).logTesting("is successfull reorder Subscription", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {
                        ((BaseActivity) currentActivity).toast(((BaseActivity) currentActivity).getString(R.string.message_subscription_resubscribed), true);
                        myPlanDetails();
                    } else {

                        ((BaseActivity) currentActivity).logTesting("reorder Subscription error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    ((BaseActivity) currentActivity).logTesting("reorder Subscription json exeption is", e.toString(), Log.ERROR);
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


    private ReorderSubscriptionModel initReorderSubModel() {

        recalculateReorderDate();
        ReorderSubscriptionModel reorderSubscriptionModel = new ReorderSubscriptionModel();
        reorderSubscriptionModel.setProductId(subscriptionModel.getProductId());
        reorderSubscriptionModel.setUser_id(SharedPreferenceUtils.getInstance(currentActivity).getInteger(AppConstants.KEY_USER_ID));
        reorderSubscriptionModel.setStartDate(reorderStartDate);
        reorderSubscriptionModel.setEndDate(reorderEndDate);
        reorderSubscriptionModel.setPaymentAmount(subscriptionModel.getPaymentAmount());
        reorderSubscriptionModel.setPaymentStatus("Unpaid");
        reorderSubscriptionModel.setQuantity(0);
        reorderSubscriptionModel.setPrice(subscriptionModel.getProduct_price());
        reorderSubscriptionModel.setType("sub");
        reorderSubscriptionModel.setSubscription_id(subscriptionModel.getMonthlySubscriptionId());
        reorderSubscriptionModel.setStatus("Undelivered");
        reorderSubscriptionModel.setIsRegular(0);

        return reorderSubscriptionModel;
    }


    private void recalculateReorderDate() {

        Calendar calendarStartDate = DateTimeUtils.calendarByDate("yyyy-MM-dd", subscriptionModel.getStartDate());
        Calendar calendarEndDate = DateTimeUtils.calendarByDate("yyyy-MM-dd", subscriptionModel.getEndDate());

        int subscriptionDays = DateTimeUtils.daysBetween(calendarStartDate, calendarEndDate);

        Calendar newStartDate = calendarEndDate;
        newStartDate.add(Calendar.DATE, 1);


        Calendar newEndDate = Calendar.getInstance();
        newEndDate.setTime(calendarEndDate.getTime());
        newEndDate.add(Calendar.DATE, subscriptionDays);
        reorderStartDate = DateTimeUtils.currentDate("yyyy-MM-dd", newStartDate);
        reorderEndDate = DateTimeUtils.currentDate("yyyy-MM-dd", newEndDate);

    }


}
