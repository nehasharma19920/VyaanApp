package com.neon.vyaan.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import com.neon.vyaan.model.AddressModel;
import com.neon.vyaan.model.CityModel;
import com.neon.vyaan.model.LocalityModel;
import com.neon.vyaan.model.UpdateAddressModel;
import com.neon.vyaan.utils.SharedPreferenceUtils;
import com.neon.vyaan.utils.Validator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditAddressActivity extends BaseActivity {

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

    AddressModel addressModel;

    int cityId;
    int localityId;


    @Override
    protected void initViews() {


        addressModel = getIntent().getParcelableExtra(ADDRESS_MODEL);
        cityId = addressModel.getCityId();
        localityId = addressModel.getLocalityId();
        editAddressTitle = (EditText) findViewById(R.id.editAddressTitle);
        editName = (EditText) findViewById(R.id.editName);
        editAddressLine1 = (EditText) findViewById(R.id.editAddressLine1);
        editAddressLine2 = (EditText) findViewById(R.id.editAddressLine2);

        editCity = (EditText) findViewById(R.id.editCity);
        editLocaliy = (EditText) findViewById(R.id.editLocaliy);

        editLandmark = (EditText) findViewById(R.id.editLandmark);
        editPhoneNo = (EditText) findViewById(R.id.editPhoneNo);
        btnAddAddress = (Button) findViewById(R.id.btnAddAddress);
        editPinCode = (EditText) findViewById(R.id.editPinCode);

        editAddressTitle.setText(addressModel.getAddressTitle());
        editName.setText(addressModel.getName());
        editAddressLine1.setText(addressModel.getAddressLine1());
        editAddressLine2.setText(addressModel.getAddressLine2());
        editCity.setText(addressModel.getCityName());
        editLocaliy.setText(addressModel.getLocalityName());
        editLandmark.setText(addressModel.getLandmark());
        editPhoneNo.setText("" + addressModel.getPhoneNumber());
        editPinCode.setText("" + addressModel.getPincode());


        getSupportActionBar().setTitle(R.string.title_edit_address);
    }

    @Override
    protected void initContext() {
        currentActivity = EditAddressActivity.this;
        context = EditAddressActivity.this;
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
        setContentView(R.layout.activity_edit_address);
    }

    @Override
    public void onAlertClicked(int alertType) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddAddress: {

                if (Validator.isNetworkAvailable(currentActivity)) {
                    if (isMandatoryFields()) {
                        editAddress();

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_TAG_CITY_ACTIVITY: {
                    cityModel = data.getParcelableExtra(CITY_MODEL);
                    cityId = cityModel.getId();
                    editCity.setText(cityModel.getName());
                    break;
                }

                case REQUEST_TAG_LOCALITY_ACTIVITY: {
                    localityModel = data.getParcelableExtra(LOCALITY_MODEL);
                    editLocaliy.setText(localityModel.getName());
                    localityId = localityModel.getId();
                    break;
                }
            }
        }
    }


    private UpdateAddressModel initUpdateAddressModel()


    {


        UpdateAddressModel updateAddressModel = new UpdateAddressModel();
        updateAddressModel.setAddressTitle(editAddressTitle.getText().toString());
        updateAddressModel.setUserId(SharedPreferenceUtils.getInstance(currentActivity).getInteger(AppConstants.KEY_USER_ID));
        updateAddressModel.setName(editName.getText().toString());
        updateAddressModel.setAddressLine1(editAddressLine1.getText().toString());
        updateAddressModel.setAddressLine2(editAddressLine2.getText().toString());
        updateAddressModel.setCityID(cityId);
        updateAddressModel.setLocalityID(localityId);
        updateAddressModel.setLandmark(editLandmark.getText().toString());
        updateAddressModel.setPhoneNumber(Long.parseLong(editPhoneNo.getText().toString()));
        updateAddressModel.setAddressId(Integer.parseInt(addressModel.getId()));
        updateAddressModel.setPincode(Integer.parseInt(editPinCode.getText().toString()));


        return updateAddressModel;

    }


    public void editAddress() {


        String json = new Gson().toJson(initUpdateAddressModel());
        logTesting("edit address request json", json, Log.ERROR);
        JSONObject jsons = null;
        try {
            jsons = new JSONObject(json);
            new Gson().toJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_UPDATE_ADDRESS, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                logTesting("responce is", response.toString(), Log.ERROR);

                try {
                    logTesting("is successfull update address", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {

                        setResult(RESULT_OK);
                        finish();
                    } else {
                        cancelProgressDialog();
                        logTesting("update address error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    logTesting("update address json exeption is", e.toString(), Log.ERROR);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cancelProgressDialog();
                logTesting("error is", error.toString(), Log.ERROR);
                toast(getResources().getString(R.string.errorUpdateAddress), true);

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
