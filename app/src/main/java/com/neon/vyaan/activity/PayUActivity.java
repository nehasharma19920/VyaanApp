package com.neon.vyaan.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.neon.vyaan.R;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.utils.DateTimeUtils;
import com.neon.vyaan.utils.SharedPreferenceUtils;
import com.payu.custombrowser.PayUWebViewClient;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class PayUActivity extends BaseActivity {


    private static final String TAG = "MainActivity";
    WebView webviewPayment;
    String amount = "";

    Bundle bundle;


    @Override
    protected void initViews() {

        getSupportActionBar().setTitle(getResources().getString(R.string.title_pay_u_activity));

    }

    @Override
    protected void initContext() {
        currentActivity = PayUActivity.this;
        context = PayUActivity.this;
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
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


        try {
            amount = getIntent().getStringExtra("amount");
            //   amount = "1";
            //  amount = getIntent().getStringExtra("order_date");
            //  amount = getIntent().getStringExtra("mode");
            //  amount = "1";
        } catch (Exception e) {

        }

        webviewPayment = (WebView) findViewById(R.id.webviewPayment);
        webviewPayment.getSettings().setJavaScriptEnabled(true);
        webviewPayment.getSettings().setDomStorageEnabled(true);
        webviewPayment.getSettings().setLoadWithOverviewMode(true);
        webviewPayment.getSettings().setUseWideViewPort(true);
        //webviewPayment.loadUrl("http://www.google.com");
        /*webviewPayment
                .loadUrl("128.199.193.113/rakhi/payment/endpoint?order_id=aAbBcC45&amount=10");*/


        //	webviewPayment.loadUrl("http://timesofindia.com/");
        StringBuilder url_s = new StringBuilder();
        //http://merirakhi.com/processor/payment/endpoint?order_id=aAbBcC&amount=10&currency=USD
        url_s.append("https://secure.payu.in/_payment");

        Log.e(TAG, "call url " + url_s);

        //webviewPayment.loadUrl(url_s.toString());
        //String postData = "username=my_username&password=my_password";
        webviewPayment.postUrl(url_s.toString(), EncodingUtils.getBytes(getPostString(), "utf-8"));

        //	webviewPayment.loadUrl("http://128.199.193.113/rakhi/payment/endpoint?order_id=aAbBcC45&amount=0.10&currency=USD");


        webviewPayment.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);


                Log.e("page load for order", "finish!" + url);
            }

            @SuppressWarnings("unused")
            public void onReceivedSslError(WebView view) {
                Log.e("Error for orde", "Exception caught!");
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {


                if (url.toString().equals(AppConstants.URL_PAYMENT_SUCCESS)) {
                    toast("Order placed successfully", true);
//                    alert(currentActivity, getString(R.string.message_order_placed_popuop), getString(R.string.message_order_placed_popuop), getString(R.string.labelOk), getString(R.string.labelCancel), false, false, ALERT_TYPE_NO_NETWORK);


                    if (bundle == null) {
                        bundle = new Bundle();

                    }
                    bundle.putString(AppConstants.KEY_PAY_FOR_ORDER, AppConstants.KEY_PAY_FOR_ORDER);

                    Intent i = new Intent(PayUActivity.this, Dashboard.class);
                    i.putExtras(bundle);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // setResult(RESULT_OK);
                    startActivity(i);
                    finish();

                } else if (url.toString().equals(AppConstants.URL_PAYMENT_FAIL)) {
                    alert(currentActivity, getString(R.string.message_order_not_placed_popuop), getString(R.string.message_order_not_placed_popuop), getString(R.string.labelOk), getString(R.string.labelCancel), false, false, ALERT_TYPE_NO_NETWORK);

                } else {
                    super.onPageStarted(view, url, favicon);
                }


            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Log.e("override page for orde", url.toString());

                return super.shouldOverrideUrlLoading(view, url);


             /*   if (url.toString().equals(AppConstants.URL_PAYMENT_SUCCESS)) {
                    alert(currentActivity, getString(R.string.message_order_placed_popuop), getString(R.string.message_order_placed_popuop), getString(R.string.labelOk), getString(R.string.labelCancel), false, false, ALERT_TYPE_NO_NETWORK);

                    if (bundle == null) {
                        bundle = new Bundle();
                        bundle.putString(AppConstants.KEY_STARTING_ACTIVITY, AppConstants.KEY_PAY_FOR_ORDER);
                    }

                    Intent i = new Intent(PayUActivity.this, Dashboard.class);
                    i.putExtras(bundle);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    setResult(RESULT_OK);
                    finish();
                    return true;
                } else if (url.toString().equals(AppConstants.URL_PAYMENT_FAIL)) {
                    alert(currentActivity, getString(R.string.message_order_not_placed_popuop), getString(R.string.message_order_not_placed_popuop), getString(R.string.labelOk), getString(R.string.labelCancel), false, false, ALERT_TYPE_NO_NETWORK);
                    return true;
                } else {


                    //    view.loadUrl(url);
                    return false;
                }*/

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //   getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    private String getPostString() {

             /*  staging credenials*/
 /*       String key = "gtKFFx";//"JBZaLc";
        String salt = "eCwWELxi";//"GQs7yium";*/

      /*  production credenials*/
        String key = "IfZO2m";//"JBZaLc";
        String salt = "IcSgIBRS";//"GQs7yium";

        String txnid = String.valueOf(DateTimeUtils.currentTimeMills());
        String amount = this.amount;
        String firstname = SharedPreferenceUtils.getInstance(PayUActivity.this).getString(AppConstants.KEY_USER_NAME);
       /* firstname = "mayank";*/
        String email = SharedPreferenceUtils.getInstance(PayUActivity.this).getString(AppConstants.KEY_USER_EMAIL);
        String productInfo = "Cart Value";

        Log.e("first name email", firstname + email);

        StringBuilder post = new StringBuilder();
        post.append("key=");
        post.append(key);
        post.append("&");
        post.append("txnid=");
        post.append(txnid);
        post.append("&");
        post.append("amount=");
        post.append(amount);
        post.append("&");
        post.append("productinfo=");
        post.append(productInfo);
        post.append("&");
        post.append("firstname=");
        post.append(firstname);
        post.append("&");
        post.append("email=");
        post.append(email);
        post.append("&");
        post.append("phone=");
        post.append("7795771000");//post.append("8904896130");
        post.append("&");
        post.append("surl=");
        post.append("http://vyaandairy.com/success.php");
        post.append("&");
        post.append("furl=");
        post.append("http://vyaandairy.com/fail.php");
        post.append("&");


        StringBuilder checkSumStr = new StringBuilder();
        /* =sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||salt) */
        MessageDigest digest = null;
        String hash;
        try {
            digest = MessageDigest.getInstance("SHA-512");// MessageDigest.getInstance("SHA-256");

            checkSumStr.append(key);
            checkSumStr.append("|");
            checkSumStr.append(txnid);
            checkSumStr.append("|");
            checkSumStr.append(amount);
            checkSumStr.append("|");
            checkSumStr.append(productInfo);
            checkSumStr.append("|");
            checkSumStr.append(firstname);
            checkSumStr.append("|");
            checkSumStr.append(email);
            checkSumStr.append("|||||||||||");
            checkSumStr.append(salt);

            digest.update(checkSumStr.toString().getBytes());

            hash = bytesToHexString(digest.digest());
            post.append("hash=");
            post.append(hash);
            post.append("&");
            Log.i(TAG, "SHA result is " + hash);
        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

       /* post.append("service_provider=");
        post.append("payu_paisa");*/
        return post.toString();
    }

    private JSONObject getProductInfo() {
        try {
            //create payment part object
            JSONObject productInfo = new JSONObject();

            JSONObject jsonPaymentPart = new JSONObject();
            jsonPaymentPart.put("name", "TapFood");
            jsonPaymentPart.put("description", "Lunchcombo");
            jsonPaymentPart.put("value", "500");
            jsonPaymentPart.put("isRequired", "true");
            jsonPaymentPart.put("settlementEvent", "EmailConfirmation");

            //create payment part array
            JSONArray jsonPaymentPartsArr = new JSONArray();
            jsonPaymentPartsArr.put(jsonPaymentPart);

            //paymentIdentifiers
            JSONObject jsonPaymentIdent = new JSONObject();
            jsonPaymentIdent.put("field", "CompletionDate");
            jsonPaymentIdent.put("value", "31/10/2012");

            //create payment part array
            JSONArray jsonPaymentIdentArr = new JSONArray();
            jsonPaymentIdentArr.put(jsonPaymentIdent);

            productInfo.put("paymentParts", jsonPaymentPartsArr);
            productInfo.put("paymentIdentifiers", jsonPaymentIdentArr);

            Log.e(TAG, "product Info = " + productInfo.toString());
            return productInfo;


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    @Override
    public void onAlertClicked(int alertType) {

        switch (alertType) {
            case ALERT_TYPE_CANCEL_TRANSCATION: {
                finish();
            }
        }
    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void onBackPressed() {


        alert(currentActivity, getString(R.string.title_cancel_transaction), getString(R.string.message_cancel_transaction), getString(R.string.labelOk), getString(R.string.labelCancel), true, true, ALERT_TYPE_CANCEL_TRANSCATION);


    }


}
