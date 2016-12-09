package com.neon.vyaan.activity;

import android.app.DatePickerDialog;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.neon.vyaan.R;
import com.neon.vyaan.adapter.ViewPagerProductImagesAdapter;
import com.neon.vyaan.application.VyaanApplication;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.model.AddCartModel;
import com.neon.vyaan.model.AddSubscripionModel;
import com.neon.vyaan.model.ProductsModel;
import com.neon.vyaan.utils.DateTimeUtils;
import com.neon.vyaan.utils.SharedPreferenceUtils;
import com.neon.vyaan.utils.Validator;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProductDetails extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    ViewPager viewPagerProductImages;
    CirclePageIndicator circleIndicatorImages;
    TextView textAboutProduct;
    TextView textProductQuantity;
    TextView textProductPrice;
    TextView textBuyOnceTabLabel;
    TextView textSubscriptionTabLabel;

    View viewBuyOnceTabSelected;
    View viewSubscriptionTabSelected;

    EditText editBuyOnceQuantity;
    TextView textBuyOnceTotalAmount;

    Button buttonAddToCart;

    TextView textStartDate;
    TextView textStartMonthYear;

    TextView textEndDate;
    TextView textEndMonthYear;

    EditText editSubscriptionQuantity;
    TextView textTotalSubscriptionAmount;

    Button buttonPlaceOrder;

    LinearLayout containerBuyOnceTab;
    LinearLayout containerSubscriptionTab;

    LinearLayout containerBuyOnce;
    LinearLayout containerSubscription;

    ProductsModel productsModel;

    DatePickerDialog datePickerDialog;


    DatePickerDialog.OnDateSetListener dateSetListener;

    int subscriptionStartYear;
    int subscriptionStartMonth;
    int subscriptionStartDay;


    int subscriptionEndYear;
    int subscriptionEndMonth;
    int subscriptioEndDay;

    boolean isStartDate = true;

    ViewPagerProductImagesAdapter viewPagerProductImagesAdapter;

    RadioButton radioRegular;
    RadioButton radioAlternate;

    String startDate;
    String endDate;

    boolean isRegular = true;

    int noOfItems;

    View menuView;

    TextView textNoOfItemsInCart;

    View customDialogView;

    EditText editQuantity;
    Button buttonChangeQuantity;

    boolean isBuyOnce;

    TextView textBuyOnceQuantityInLitre;
    TextView textSubscriptionQuantityInLitre;

    @Override
    protected void initViews() {
        itemsInCart();

        productsModel = (ProductsModel) getIntent().getExtras().getParcelable(PRODUCT_DETAILS);

        getSupportActionBar().setTitle(productsModel.getName());


        viewPagerProductImages = (ViewPager) findViewById(R.id.viewPagerProductImages);
        circleIndicatorImages = (CirclePageIndicator) findViewById(R.id.circleIndicatorImages);
        textAboutProduct = (TextView) findViewById(R.id.textAboutProduct);
        textProductQuantity = (TextView) findViewById(R.id.textProductQuantity);
        textProductPrice = (TextView) findViewById(R.id.textProductPrice);
        textBuyOnceTabLabel = (TextView) findViewById(R.id.textBuyOnceTabLabel);
        textSubscriptionTabLabel = (TextView) findViewById(R.id.textSubscriptionTabLabel);

        viewBuyOnceTabSelected = (View) findViewById(R.id.viewBuyOnceTabSelected);
        viewSubscriptionTabSelected = (View) findViewById(R.id.viewSubscriptionTabSelected);

        editBuyOnceQuantity = (EditText) findViewById(R.id.editBuyOnceQuantity);
        textBuyOnceTotalAmount = (TextView) findViewById(R.id.textBuyOnceTotalAmount);

        buttonAddToCart = (Button) findViewById(R.id.buttonAddToCart);

        textStartDate = (TextView) findViewById(R.id.textStartDate);

        textStartMonthYear = (TextView) findViewById(R.id.textStartMonthYear);
        textEndDate = (TextView) findViewById(R.id.textEndDate);

        textEndMonthYear = (TextView) findViewById(R.id.textEndMonthYear);

        editSubscriptionQuantity = (EditText) findViewById(R.id.editSubscriptionQuantity);
        textTotalSubscriptionAmount = (TextView) findViewById(R.id.textTotalSubscriptionAmount);

        buttonPlaceOrder = (Button) findViewById(R.id.buttonPlaceOrder);

        containerBuyOnceTab = (LinearLayout) findViewById(R.id.containerBuyOnceTab);
        containerSubscriptionTab = (LinearLayout) findViewById(R.id.containerSubscriptionTab);

        containerBuyOnce = (LinearLayout) findViewById(R.id.containerBuyOnce);
        containerSubscription = (LinearLayout) findViewById(R.id.containerSubscription);


        textBuyOnceQuantityInLitre = (TextView) findViewById(R.id.textBuyOnceQuantityInLitre);
        textSubscriptionQuantityInLitre = (TextView) findViewById(R.id.textSubscriptionQuantityInLitre);

        radioRegular = (RadioButton) findViewById(R.id.radioRegular);
        radioAlternate = (RadioButton) findViewById(R.id.radioAlternate);

        radioRegular.setOnCheckedChangeListener(this);
        radioAlternate.setOnCheckedChangeListener(this);

        textStartDate.setOnClickListener(this);
        textEndDate.setOnClickListener(this);

        subscriptionStartYear = Calendar.getInstance().get(Calendar.YEAR);
        subscriptionStartMonth = Calendar.getInstance().get(Calendar.MONTH);
        subscriptionStartDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        subscriptionEndYear = Calendar.getInstance().get(Calendar.YEAR);
        subscriptionEndMonth = Calendar.getInstance().get(Calendar.MONTH);
        subscriptioEndDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        customDialogView = LayoutInflater.from(currentActivity).inflate(R.layout.dialog_edit_quantity, null, false);

        initData();
    }

    private void initData() {

        viewPagerProductImagesAdapter = new ViewPagerProductImagesAdapter(getSupportFragmentManager(), productsModel.getImages());


        viewPagerProductImages.setAdapter(viewPagerProductImagesAdapter);
        circleIndicatorImages.setViewPager(viewPagerProductImages);

        circleIndicatorImages.setRadius(10);

        textAboutProduct.setText(productsModel.getAbout());
        textProductQuantity.setText("" + productsModel.getQuantity() + " ml" + " (per bottle)");
        textProductPrice.setText("Rs. " + productsModel.getPrice() + " (per bottle)");


        editBuyOnceQuantity.setText("" + 2);
        textBuyOnceTotalAmount.setText("Rs. " + (productsModel.getPrice() * 2));

        editBuyOnceQuantity.setSelection(editBuyOnceQuantity.getText().length());


        Calendar startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.DATE, 1);
        startDate = (DateTimeUtils.currentDate("yyyy-MM-dd", startCalendar));


        textStartDate.setText(DateTimeUtils.currentDate("dd", startCalendar));
        textStartMonthYear.setText(DateTimeUtils.currentDate("MMMM yyyy", startCalendar));

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.DATE, 31);

        endDate = (DateTimeUtils.currentDate("yyyy-MM-dd", endCalendar));

        textEndDate.setText(DateTimeUtils.currentDate("dd", endCalendar));
        textEndMonthYear.setText(DateTimeUtils.currentDate("MMMM yyyy", endCalendar));


        editSubscriptionQuantity.setText("" + 2);


        textBuyOnceQuantityInLitre.setText("1");
        textSubscriptionQuantityInLitre.setText("1");

        editSubscriptionQuantity.setSelection(editSubscriptionQuantity.getText().length());
        textTotalSubscriptionAmount.setText("Rs. " + (productsModel.getPrice() * 2));


        editBuyOnceQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBuyOnce = true;
                changeQuantity();
               /* if (!editBuyOnceQuantity.getText().toString().isEmpty()) {
                    Log.e("come under sel", "yes");
                    editBuyOnceQuantity.setSelection(editBuyOnceQuantity.getText().length());
                }*/

            }
        });

        editSubscriptionQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBuyOnce = false;
                changeQuantity();
       /*         if (!editSubscriptionQuantity.getText().toString().isEmpty()) {
                    Log.e("come under sel", "yes");
                    editSubscriptionQuantity.setSelection(editSubscriptionQuantity.getText().length());
                }
*/
            }
        });

 /*       editSubscriptionQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editSubscriptionQuantity.getText().toString().isEmpty()) {
                    textTotalSubscriptionAmount.setText("0");

                } else {
                    if (Integer.parseInt(editSubscriptionQuantity.getText().toString()) == 0 || Integer.parseInt(editSubscriptionQuantity.getText().toString()) == 1) {
                        editSubscriptionQuantity.setText("2");
                        editSubscriptionQuantity.setSelection(editSubscriptionQuantity.getText().length());
                        textTotalSubscriptionAmount.setText("" + Integer.parseInt(editSubscriptionQuantity.getText().toString()) * productsModel.getPrice());

                    } else {
                        //     editBuyOnceQuantity.setText(editBuyOnceQuantity.getText().toString());
                        textTotalSubscriptionAmount.setText("" + Integer.parseInt(editSubscriptionQuantity.getText().toString()) * productsModel.getPrice());
                    }
                }

            }
        });


        editBuyOnceQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editBuyOnceQuantity.getText().toString().isEmpty()) {
                    textBuyOnceTotalAmount.setText("0");

                } else {
                    if (Integer.parseInt(editBuyOnceQuantity.getText().toString()) == 0 || Integer.parseInt(editBuyOnceQuantity.getText().toString()) == 1) {
                        editBuyOnceQuantity.setText("2");
                        editBuyOnceQuantity.setSelection(editBuyOnceQuantity.getText().length());
                        textBuyOnceTotalAmount.setText("" + Integer.parseInt(editBuyOnceQuantity.getText().toString()) * productsModel.getPrice());

                    } else {
                        //     editBuyOnceQuantity.setText(editBuyOnceQuantity.getText().toString());
                        textBuyOnceTotalAmount.setText("" + Integer.parseInt(editBuyOnceQuantity.getText().toString()) * productsModel.getPrice());
                    }
                }

            }
        });*/
        subscriptionStartYear = startCalendar.get(Calendar.YEAR);
        subscriptionStartMonth = startCalendar.get(Calendar.MONTH);
        subscriptionStartDay = startCalendar.get(Calendar.DAY_OF_MONTH);

        subscriptionEndYear = endCalendar.get(Calendar.YEAR);
        subscriptionEndMonth = endCalendar.get(Calendar.MONTH);
        subscriptioEndDay = endCalendar.get(Calendar.DAY_OF_MONTH);


        initDateAndTimePicker();


    }


    @Override
    protected void initContext() {
        currentActivity = ProductDetails.this;
        context = ProductDetails.this;
    }

    @Override
    protected void initListners() {
        buttonAddToCart.setOnClickListener(this);
        buttonPlaceOrder.setOnClickListener(this);
        containerBuyOnceTab.setOnClickListener(this);
        containerSubscriptionTab.setOnClickListener(this);
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
        setContentView(R.layout.activity_product_details);
    }

    @Override
    public void onAlertClicked(int alertType) {

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.containerBuyOnceTab: {

                selectBuyOnce();
                break;
            }

            case R.id.containerSubscriptionTab: {

                selectSubscription();
                break;
            }

            case R.id.buttonAddToCart: {

                if (Validator.isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFieldsOrder()) {
                        addToCart();

                    }
                } else {
                    alert(currentActivity, getString(R.string.alert_message_no_network), getString(R.string.alert_message_no_network), getString(R.string.labelOk), getString(R.string.labelCancel), false, false, ALERT_TYPE_NO_NETWORK);

                }
                break;
            }

            case R.id.buttonPlaceOrder: {

                if (Validator.isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFieldsSubscrition()) {
                        if (bundle == null) {
                            bundle = new Bundle();
                        }
                        bundle.putParcelable(SUBSCRIPTION_MODEL, initAddSubModel());
                        bundle.putBoolean(SUBSCRIPTION, true);

                        startActivity(currentActivity, AllAddresses.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_LEFT);


                    }
                } else {
                    alert(currentActivity, getString(R.string.alert_message_no_network), getString(R.string.alert_message_no_network), getString(R.string.labelOk), getString(R.string.labelCancel), false, false, ALERT_TYPE_NO_NETWORK);

                }


                break;
            }

            case R.id.textStartDate: {
                isStartDate = true;

                //    datePickerDialog.updateDate(subscriptionStartYear, subscriptionStartMonth, subscriptionStartDay);


                //      datePickerDialog.getDatePicker().setMaxDate(99999999999999999l);

                initDateAndTimePicker();
                datePickerDialog.updateDate(subscriptionStartYear, subscriptionStartMonth, subscriptionStartDay);
                //  datePickerDialog.getDatePicker().setMinDate(DateTimeUtils.currentTimeMills() - 1000);


                Calendar calendarStartDate = DateTimeUtils.calendarByDate("yyyy-MM-dd", startDate);
                Calendar calendarMaxDate = Calendar.getInstance();
                calendarMaxDate.setTime(calendarStartDate.getTime());
                calendarMaxDate.add(Calendar.DATE, 1);


                Calendar startCalendar = Calendar.getInstance();
                startCalendar.add(Calendar.DATE, 1);
                //  startDate = (DateTimeUtils.currentDate("yyyy-MM-dd", startCalendar));

                datePickerDialog.getDatePicker().setMinDate(startCalendar.getTimeInMillis() - 10000);


                datePickerDialog.show();
                break;
            }

            case R.id.textEndDate: {
                isStartDate = false;


                initDateAndTimePicker();
                datePickerDialog.updateDate(subscriptionEndYear, subscriptionEndMonth, subscriptioEndDay);
                long millis = DateTimeUtils.currentTimeMills() + (30 * 24 * 60 * 60 * 1000);
                Log.e("millis", "" + millis);

                Calendar calendarStartDate = DateTimeUtils.calendarByDate("yyyy-MM-dd", startDate);


                Calendar calendarMaxDate = Calendar.getInstance();
                calendarMaxDate.setTime(calendarStartDate.getTime());
                calendarMaxDate.add(Calendar.DATE, 30);
                //   datePickerDialog.getDatePicker().setMinDate(calendarMaxDate.getTimeInMillis());


                Calendar calendarMinDate = Calendar.getInstance();
                calendarMinDate.setTime(calendarStartDate.getTime());
                calendarMinDate.add(Calendar.DATE, 4);

                Log.e("calendar min date", calendarMinDate.getTime().toString());
                datePickerDialog.getDatePicker().setMinDate(calendarMinDate.getTimeInMillis());

                datePickerDialog.getDatePicker().setMaxDate(calendarMaxDate.getTimeInMillis());
                datePickerDialog.show();
                break;
            }
        }

    }


    private AddCartModel initCartModel() {


        AddCartModel addCartModel = new AddCartModel();
        addCartModel.setProductId(productsModel.getId());
        addCartModel.setProductQuantity(Integer.parseInt(editBuyOnceQuantity.getText().toString()));
        addCartModel.setIs_active(1);
        addCartModel.setIs_delete(0);
        addCartModel.setProductPrice(productsModel.getPrice());
        addCartModel.setUser_id(SharedPreferenceUtils.getInstance(currentActivity).getInteger(AppConstants.KEY_USER_ID));


        return addCartModel;

    }


    private void selectBuyOnce() {
        viewBuyOnceTabSelected.setVisibility(View.VISIBLE);
        textBuyOnceTabLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
        containerBuyOnce.setVisibility(View.VISIBLE);

        //   containerBuyOnceTab.setVisibility(View.VISIBLE);

        viewSubscriptionTabSelected.setVisibility(View.GONE);
        textSubscriptionTabLabel.setTextColor(getResources().getColor(R.color.grey));
        containerSubscription.setVisibility(View.GONE);
        //   containerSubscriptionTab.setVisibility(View.GONE);

    }


    private void selectSubscription() {


        viewSubscriptionTabSelected.setVisibility(View.VISIBLE);
        textSubscriptionTabLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
        containerSubscription.setVisibility(View.VISIBLE);
        //   containerSubscriptionTab.setVisibility(View.VISIBLE);

        viewBuyOnceTabSelected.setVisibility(View.GONE);
        textBuyOnceTabLabel.setTextColor(getResources().getColor(R.color.grey));
        containerBuyOnce.setVisibility(View.GONE);
        //     containerBuyOnceTab.setVisibility(View.GONE);


    }

    private boolean isMandatoryFieldsOrder() {
        editBuyOnceQuantity.setError(null);
        if (editBuyOnceQuantity.getText().toString().isEmpty())

        {
            editBuyOnceQuantity.setError(getString(R.string.error_quantity));
            editBuyOnceQuantity.requestFocus();
            return false;
        }
        return true;
    }


    private boolean isMandatoryFieldsSubscrition() {
        editSubscriptionQuantity.setError(null);
        if (editSubscriptionQuantity.getText().toString().isEmpty())

        {
            editSubscriptionQuantity.setError(getString(R.string.error_quantity));
            editSubscriptionQuantity.requestFocus();
            return false;
        }
        return true;
    }

    private void initDateAndTimePicker() {

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                if (isStartDate) {
                    subscriptionStartYear = i;
                    subscriptionStartMonth = i1;
                    subscriptionStartDay = i2;
                    textStartDate.setText(DateTimeUtils.currentDate("dd", makeCalendar(subscriptionStartYear, subscriptionStartMonth, subscriptionStartDay)));
                    textStartMonthYear.setText(DateTimeUtils.currentDate("MMMM yyyy", makeCalendar(subscriptionStartYear, subscriptionStartMonth, subscriptionStartDay)));

                    startDate = DateTimeUtils.currentDate("yyyy-MM-dd", makeCalendar(subscriptionStartYear, subscriptionStartMonth, subscriptionStartDay));


                    Calendar newCalendar = makeCalendar(subscriptionStartYear, subscriptionStartMonth, subscriptionStartDay);
                    newCalendar.add(Calendar.DATE, 30);

                    subscriptionEndYear = newCalendar.get(Calendar.YEAR);
                    subscriptionEndMonth = newCalendar.get(Calendar.MONTH);
                    subscriptioEndDay = newCalendar.get(Calendar.DAY_OF_MONTH);

                    textEndDate.setText(DateTimeUtils.currentDate("dd", newCalendar));
                    textEndMonthYear.setText(DateTimeUtils.currentDate("MMMM yyyy", newCalendar));

                    endDate = DateTimeUtils.currentDate("yyyy-MM-dd", newCalendar);


                    //    datePickerDialog.getDatePicker().setMinDate(makeCalendar(subscriptionStartYear, subscriptionStartMonth, subscriptionStartDay).getTimeInMillis());
                    datePickerDialog.getDatePicker().setMaxDate(newCalendar.getTimeInMillis());

                } else {
                    subscriptionEndYear = i;
                    subscriptionEndMonth = i1;
                    subscriptioEndDay = i2;
                    textEndDate.setText(DateTimeUtils.currentDate("dd", makeCalendar(subscriptionEndYear, subscriptionEndMonth, subscriptioEndDay)));
                    textEndMonthYear.setText(DateTimeUtils.currentDate("MMMM yyyy", makeCalendar(subscriptionEndYear, subscriptionEndMonth, subscriptioEndDay)));

                    endDate = DateTimeUtils.currentDate("yyyy-MM-dd", makeCalendar(subscriptionEndYear, subscriptionEndMonth, subscriptioEndDay));

                }


            }
        };

        datePickerDialog = new DatePickerDialog(currentActivity, dateSetListener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        //datePickerDialog.getDatePicker().setMaxDate(DateTimeUtils.currentTimeMills() - 1000);
        //   datePickerDialog.getDatePicker().setMinDate(DateTimeUtils.currentTimeMills() - 1000);

        //       datePickerDialog.getDatePicker().setMinDate(DateTimeUtils.currentTimeMills() - 10000);
    }


    private Calendar makeCalendar(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar;
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        switch (compoundButton.getId()) {
            case R.id.radioRegular: {
                isRegular = true;
                break;
            }
            case R.id.radioAlternate: {
                isRegular = false;
                break;
            }
        }

    }


    public void addToCart() {

        ((BaseActivity) currentActivity).progressDialog(currentActivity, currentActivity.getString(R.string.pdialog_message_loading), currentActivity.getString(R.string.pdialog_message_loading), false, false);

        String json = new Gson().toJson(initCartModel());
        logTesting("cart request json", json, Log.ERROR);
        JSONObject jsons = null;
        try {
            jsons = new JSONObject(json);
            new Gson().toJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_ADD_TO_CART, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                logTesting("responce is", response.toString(), Log.ERROR);
                cancelProgressDialog();
                try {
                    logTesting("is successfull add to cart", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {

                        itemsInCart();
                        toast(getString(R.string.message_added_to_cart), true);
                    } else {

                        logTesting("add to cart error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    logTesting("add to cart json exeption is", e.toString(), Log.ERROR);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                logTesting("error is", error.toString(), Log.ERROR);
                toast(getResources().getString(R.string.errorAddToCart), true);

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


    private AddSubscripionModel initAddSubModel() {


        AddSubscripionModel addSubscripionModel = new AddSubscripionModel();
        addSubscripionModel.setProductId(productsModel.getId());
        addSubscripionModel.setUser_id(SharedPreferenceUtils.getInstance(currentActivity).getInteger(AppConstants.KEY_USER_ID));
        addSubscripionModel.setStartDate(startDate);
        addSubscripionModel.setEndDate(endDate);
        addSubscripionModel.setPaymentAmount(0);
        addSubscripionModel.setPaymentStatus("Unpaid");
        addSubscripionModel.setQuantity(Integer.parseInt(editSubscriptionQuantity.getText().toString()));
        addSubscripionModel.setPrice(productsModel.getPrice());
        addSubscripionModel.setType("sub");
        addSubscripionModel.setStatus("Undelivered");
        addSubscripionModel.setRegular(isRegular);
        return addSubscripionModel;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_dashboard, menu);
        menuView = menu.findItem(R.id.action_cart).getActionView();
        textNoOfItemsInCart = (TextView) menuView.findViewById(R.id.textNoOfItemsInCart);
        RelativeLayout containetCartItems = (RelativeLayout) menuView.findViewById(R.id.containetCartItems);
        //    textNoOfItemsInCart.setText("" + String.valueOf(noOfItems));

        containetCartItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity) currentActivity).startActivity(currentActivity, CartActivity.class, bundle, false, REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_LEFT);

            }
        });
        return super.onCreateOptionsMenu(menu);
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
                        if (textNoOfItemsInCart != null) {
                            textNoOfItemsInCart.setText("" + String.valueOf(noOfItems));
                            if (noOfItems == 0) {
                                textNoOfItemsInCart.setText("");
                            } else {
                                textNoOfItemsInCart.setText("" + String.valueOf(noOfItems));

                            }
                            textNoOfItemsInCart.refreshDrawableState();

                        }
                    } else {

                        textNoOfItemsInCart.setText("");
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

    @Override
    public void onResume() {
        super.onResume();

        itemsInCart();


    }


    private void changeQuantity() {
        ((BaseActivity) currentActivity).creatingDialog(currentActivity, false, false, customDialogView, 220, 300, false);

        editQuantity = (EditText) customDialogView.findViewById(R.id.editQuantity);
        buttonChangeQuantity = (Button) customDialogView.findViewById(R.id.buttonChangeQuantity);
        if (isBuyOnce) {
            editQuantity.setText("" + Integer.parseInt(editBuyOnceQuantity.getText().toString()));

        } else {
            editQuantity.setText("" + Integer.parseInt(editSubscriptionQuantity.getText().toString()));

        }


        editQuantity.setSelection(editQuantity.getText().length());


        buttonChangeQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Validator.isNetworkAvailable(currentActivity)) {


                    if (editQuantity.getText().toString().isEmpty()) {
                        editQuantity.requestFocus();
                        editQuantity.setError(currentActivity.getResources().getString(R.string.errorQuantity));
                    } else if (Integer.parseInt(editQuantity.getText().toString()) < 2) {

                        editQuantity.requestFocus();
                        editQuantity.setError(currentActivity.getResources().getString(R.string.errorMinimumQuantity));
                    } else {
                        ((BaseActivity) currentActivity).cancelCustomDialog();
                        if (isBuyOnce) {
                            editBuyOnceQuantity.setText("" + Integer.parseInt(editQuantity.getText().toString()));
                            textBuyOnceQuantityInLitre.setText("" + (Float) (Float.parseFloat(editBuyOnceQuantity.getText().toString()) / 2));

                            textBuyOnceTotalAmount.setText("Rs. " + Integer.parseInt(editBuyOnceQuantity.getText().toString()) * productsModel.getPrice());

                        } else {
                            editSubscriptionQuantity.setText("" + Integer.parseInt(editQuantity.getText().toString()));
                            textTotalSubscriptionAmount.setText("Rs. " + Integer.parseInt(editSubscriptionQuantity.getText().toString()) * productsModel.getPrice());
                            textSubscriptionQuantityInLitre.setText("" + (Float) (Float.parseFloat(editSubscriptionQuantity.getText().toString()) / 2));

                        }
                        //     productsModel.setQuantity(Integer.parseInt(editQuantity.getText().toString()));
                    }


                } else {
                    alert(currentActivity, currentActivity.getString(R.string.alert_message_no_network), currentActivity.getString(R.string.alert_message_no_network), currentActivity.getString(R.string.labelOk), currentActivity.getString(R.string.labelCancel), false, false, AppConstants.ALERT_TYPE_NO_NETWORK);

                }


            }
        });


    }


}
