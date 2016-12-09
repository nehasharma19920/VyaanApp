package com.neon.vyaan.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.neon.vyaan.R;
import com.neon.vyaan.application.VyaanApplication;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.model.AddAddressModel;
import com.neon.vyaan.model.CityModel;
import com.neon.vyaan.model.LocalityModel;
import com.neon.vyaan.utils.SharedPreferenceUtils;
import com.neon.vyaan.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewAddress extends BaseActivity {

    EditText editAddressTitle;
    EditText editName;
    EditText editAddressLine1;
    EditText editAddressLine2;

    EditText editCity;
    EditText editLocaliy;

    EditText editLandmark;
    EditText editPhoneNo;
    EditText editPinCode;

    Button btnAddAddress;

    CityModel cityModel;
    LocalityModel localityModel;


    @Override
    protected void initViews() {
        editAddressTitle = (EditText) findViewById(R.id.editAddressTitle);
        editName = (EditText) findViewById(R.id.editName);
        editAddressLine1 = (EditText) findViewById(R.id.editAddressLine1);
        editAddressLine2 = (EditText) findViewById(R.id.editAddressLine2);

        editCity = (EditText) findViewById(R.id.editCity);
        editLocaliy = (EditText) findViewById(R.id.editLocaliy);

        editLandmark = (EditText) findViewById(R.id.editLandmark);
        editPhoneNo = (EditText) findViewById(R.id.editPhoneNo);
        editPinCode = (EditText) findViewById(R.id.editPinCode);
        btnAddAddress = (Button) findViewById(R.id.btnAddAddress);


        getSupportActionBar().setTitle(R.string.title_address);
    }

    @Override
    protected void initContext() {
        currentActivity = NewAddress.this;
        context = NewAddress.this;
    }

    @Override
    protected void initListners() {
        btnAddAddress.setOnClickListener(this);
        editCity.setOnClickListener(this);
        editLocaliy.setOnClickListener(this);
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
        setContentView(R.layout.activity_new_address);
    }

    @Override
    public void onAlertClicked(int alertType) {

    }


    private AddAddressModel initAddAddressModel()


    {


        AddAddressModel addAddressModel = new AddAddressModel();
        addAddressModel.setAddressTitle(editAddressTitle.getText().toString());
        addAddressModel.setUserId(SharedPreferenceUtils.getInstance(currentActivity).getInteger(AppConstants.KEY_USER_ID));
        addAddressModel.setName(editName.getText().toString());
        addAddressModel.setAddressLine1(editAddressLine1.getText().toString());
        addAddressModel.setAddressLine2(editAddressLine2.getText().toString());
        addAddressModel.setCityID(cityModel.getId());
        addAddressModel.setLocalityID(localityModel.getId());
        addAddressModel.setLandmark(editLandmark.getText().toString());
        addAddressModel.setPhoneNumber(Long.parseLong(editPhoneNo.getText().toString()));
        addAddressModel.setPincode(Integer.parseInt(editPinCode.getText().toString()));


        return addAddressModel;

    }

    private boolean isMandatoryFields() {

        editAddressTitle.setError(null);
        editName.setError(null);
        editAddressLine1.setError(null);
        editAddressLine2.setError(null);

        editCity.setError(null);
        editLocaliy.setError(null);

        editLandmark.setError(null);
        editPhoneNo.setError(null);

        if (editAddressTitle.getText().toString().isEmpty()) {
            editAddressTitle.setError(getResources().getString(R.string.error_address_title));
            editAddressTitle.requestFocus();
            return false;
        } else if (editName.getText().toString().isEmpty()) {
            editName.setError(getResources().getString(R.string.error_enter_name));
            editName.requestFocus();
            return false;
        } else if (editAddressLine1.getText().toString().isEmpty()) {
            editAddressLine1.setError(getResources().getString(R.string.error_address));
            editAddressLine1.requestFocus();
            return false;
        } else if (editCity.getText().toString().isEmpty()) {
            editCity.setError(getResources().getString(R.string.error_city));
            editCity.requestFocus();
            return false;
        } else if (editLocaliy.getText().toString().isEmpty()) {
            editLocaliy.setError(getResources().getString(R.string.error_locality));
            editLocaliy.requestFocus();
            return false;
        } else if (editPinCode.getText().toString().isEmpty()) {
            editPinCode.setError(getResources().getString(R.string.error_pincode));
            editPinCode.requestFocus();
            return false;
        } else if (!Validator.getInstance().validateNumber(context, editPhoneNo.getText().toString()).equals("")) {
            editPhoneNo.setError(Validator.getInstance().validateNumber(context, editPhoneNo.getText().toString()));
            editPhoneNo.requestFocus();
            return false;
        }
        return true;

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnAddAddress: {

                if (Validator.isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFields()) {

                        if (bundle == null) {
                            bundle = new Bundle();

                        }
                        bundle.putString(keyMobileNo, editPhoneNo.getText().toString());
                        startActivity(currentActivity, OtpValidation.class, bundle, true, REQUEST_TAG_NUMBER_VERIFICATION, true, ANIMATION_SLIDE_LEFT);


                    }
                } else {
                    alert(currentActivity, getString(R.string.alert_message_no_network), getString(R.string.alert_message_no_network), getString(R.string.labelOk), getString(R.string.labelCancel), false, false, ALERT_TYPE_NO_NETWORK);

                }

                break;
            }
            case R.id.editCity: {
                startActivity(currentActivity, SelectCityActivity.class, bundle, true, REQUEST_TAG_CITY_ACTIVITY, true, ANIMATION_SLIDE_LEFT);

                break;
            }
            case R.id.editLocaliy: {

                if (cityModel == null) {
                    toast(getResources().getString(R.string.error_select_city), true);
                } else {
                    if (bundle == null) {
                        bundle = new Bundle();
                    }
                    bundle.putParcelable(CITY_MODEL, cityModel);
                    startActivity(currentActivity, SelectLocalityActivity.class, bundle, true, REQUEST_TAG_LOCALITY_ACTIVITY, true, ANIMATION_SLIDE_LEFT);

                }


                break;
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAG_CITY_ACTIVITY: {
                    cityModel = data.getParcelableExtra(CITY_MODEL);
                    editCity.setText(cityModel.getName());
                    break;
                }

                case REQUEST_TAG_LOCALITY_ACTIVITY: {
                    localityModel = data.getParcelableExtra(LOCALITY_MODEL);
                    editLocaliy.setText(localityModel.getName());
                    break;
                }

                case REQUEST_TAG_NUMBER_VERIFICATION: {
                    addNewAddress();
                }
            }
        }
    }


    public void addNewAddress() {


        String json = new Gson().toJson(initAddAddressModel());
        logTesting("add address request json", json, Log.ERROR);
        JSONObject jsons = null;
        try {
            jsons = new JSONObject(json);
            new Gson().toJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_ADD_ADDRESS, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                logTesting("responce is", response.toString(), Log.ERROR);

                try {
                    logTesting("is successfull add address", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {

                        setResult(RESULT_OK);
                        finish();
                    } else {
                        cancelProgressDialog();
                        logTesting("add address error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    logTesting("add address json exeption is", e.toString(), Log.ERROR);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                logTesting("error is", error.toString(), Log.ERROR);
                toast(getResources().getString(R.string.errorLogin), true);

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
