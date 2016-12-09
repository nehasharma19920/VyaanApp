package com.neon.vyaan.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.neon.vyaan.application.VyaanApplication;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.interfaces.AlertClicked;
import com.neon.vyaan.model.CartModel;
import com.neon.vyaan.model.ChangeSubscriptionQuantityModel;
import com.neon.vyaan.utils.DateTimeUtils;
import com.neon.vyaan.utils.ImageHelper;
import com.neon.vyaan.utils.SharedPreferenceUtils;
import com.neon.vyaan.utils.Validator;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mayank on 20/08/2016.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> implements AlertClicked {

    Activity currentActivity;
    List<CartModel> cartModelList;
    View customDialogView;


    EditText editQuantity;
    Button buttonChangeQuantity;

    int selectedPosition = 0;

    public CartAdapter(Activity currentActivity, List<CartModel> cartModelList) {
        this.currentActivity = currentActivity;
        this.cartModelList = cartModelList;

        customDialogView = LayoutInflater.from(currentActivity).inflate(R.layout.dialog_edit_quantity, null, false);
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(currentActivity).inflate(R.layout.items_cart, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CartAdapter.ViewHolder holder, final int position) {
        final int pos = position;

        Picasso.with(currentActivity).load(AppConstants.BASE_URL_IMAGES + "/" + cartModelList.get(pos).getProductIcon()).into(new Target() {
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
        holder.textProductName.setText(cartModelList.get(pos).getProductName());//
        holder.textSingleProductQuantity.setText("" + cartModelList.get(pos).getProductPackQuantity() + " " + cartModelList.get(pos).getProductUnit());
        holder.textPricePerUnit.setText("" + cartModelList.get(pos).getProductPrice());
        holder.textQuantity.setText("" + cartModelList.get(pos).getProductQuantity());

        holder.textQuantityInLiter.setText("" + (Float) (Float.parseFloat(holder.textQuantity.getText().toString()) / 2));

        holder.textTotalPrice.setText("" + cartModelList.get(pos).getProductPrice() * cartModelList.get(pos).getProductQuantity());

        holder.textRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = pos;
                alert(currentActivity, currentActivity.getString(R.string.alert_message_remove_item_from_cart), currentActivity.getString(R.string.alert_message_remove_item_from_cart), currentActivity.getString(R.string.labelOk), currentActivity.getString(R.string.labelCancel), true, false, AppConstants.ALERT_TYPE_CART_ITEM_REMOVE);

            }
        });

        holder.textEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((BaseActivity) currentActivity).creatingDialog(currentActivity, false, false, customDialogView, 220, 300, false);
                changeQuantity(pos);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    @Override
    public void onAlertClicked(int alertType) {

        switch (alertType) {
            case AppConstants.ALERT_TYPE_CART_ITEM_REMOVE: {
                removeProductRequest(selectedPosition);
            }
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageProduct;
        TextView textProductName;
        TextView textSingleProductQuantity;
        TextView textPricePerUnit;
        TextView textQuantity;
        TextView textTotalPrice;
        TextView textRemove;
        TextView textEdit;
        TextView textQuantityInLiter;


        public ViewHolder(View itemView) {
            super(itemView);

            imageProduct = (ImageView) itemView.findViewById(R.id.imageProduct);
            textProductName = (TextView) itemView.findViewById(R.id.textProductName);
            textSingleProductQuantity = (TextView) itemView.findViewById(R.id.textSingleProductQuantity);
            textPricePerUnit = (TextView) itemView.findViewById(R.id.textPricePerUnit);
            textQuantity = (TextView) itemView.findViewById(R.id.textQuantity);
            textTotalPrice = (TextView) itemView.findViewById(R.id.textTotalPrice);
            textRemove = (TextView) itemView.findViewById(R.id.textRemove);
            textEdit = (TextView) itemView.findViewById(R.id.textEdit);
            textQuantityInLiter = (TextView) itemView.findViewById(R.id.textQuantityInLiter);
        }
    }


    private void changeQuantity(final int pos) {
        editQuantity = (EditText) customDialogView.findViewById(R.id.editQuantity);
        buttonChangeQuantity = (Button) customDialogView.findViewById(R.id.buttonChangeQuantity);
        editQuantity.setText("" + cartModelList.get(pos).getProductQuantity());

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
                        changeQuantityRequest(pos);
                    }


                } else {
                    alert(currentActivity, currentActivity.getString(R.string.alert_message_no_network), currentActivity.getString(R.string.alert_message_no_network), currentActivity.getString(R.string.labelOk), currentActivity.getString(R.string.labelCancel), false, false, AppConstants.ALERT_TYPE_NO_NETWORK);

                }


            }
        });


    }


    public void changeQuantityRequest(int position) {


        ((BaseActivity) currentActivity).progressDialog(currentActivity, currentActivity.getString(R.string.pdialog_message_loading), currentActivity.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsons = new JSONObject();
        try {
            jsons.put("product_id", cartModelList.get(position).getProductId());
            jsons.put("product_quantity", editQuantity.getText().toString());
            jsons.put("user_id", SharedPreferenceUtils.getInstance(currentActivity).getInteger(AppConstants.KEY_USER_ID));

            ((BaseActivity) currentActivity).logTesting("update product jsons", jsons.toString(), Log.ERROR);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AppConstants.URL_UPDATE_CART_ITEMS, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ((BaseActivity) currentActivity).logTesting("quantity request responce is", response.toString(), Log.ERROR);

                try {
                    ((BaseActivity) currentActivity).cancelProgressDialog();
                    ((BaseActivity) currentActivity).logTesting("is successfull request responce", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {

                        ((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.messageProductQuantityUpdated), true);
                        ((CartActivity) currentActivity).getCartItems();
                    } else {
                        ((BaseActivity) currentActivity).cancelProgressDialog();

                        ((BaseActivity) currentActivity).logTesting("request responce quan error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    ((BaseActivity) currentActivity).logTesting("request responce quan json exeption is", e.toString(), Log.ERROR);
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


    public void alert(Context context, String title, String message, String positiveButton, String negativeButton, boolean isNegativeButton, boolean isTitle, final int alertType) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        if (isTitle) {
            builder.setTitle(title);
        }


        builder.setMessage(message);
        builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onAlertClicked(alertType);
                dialog.cancel();

            }
        });
        if (isNegativeButton) {
            builder.setNegativeButton(negativeButton, null);
        }

        builder.show();


    }

    public void removeProductRequest(int position) {


        ((BaseActivity) currentActivity).progressDialog(currentActivity, currentActivity.getString(R.string.pdialog_message_loading), currentActivity.getString(R.string.pdialog_message_loading), false, false);

        JSONObject jsons = null;
        String COMPLETE_URL_REMOVE_ITEMS_FROM_CART = AppConstants.URL_REMOVE_ITEMS_FROM_CART + "?item_id_cart=" + cartModelList.get(position).getId();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, COMPLETE_URL_REMOVE_ITEMS_FROM_CART, jsons, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ((BaseActivity) currentActivity).logTesting("remove product responce is", response.toString(), Log.ERROR);

                try {
                    ((BaseActivity) currentActivity).cancelProgressDialog();
                    ((BaseActivity) currentActivity).logTesting("is successfull remove product", "hi" + response.getBoolean(AppConstants.KEY_ERROR), Log.ERROR);
                    if (!response.getBoolean(AppConstants.KEY_ERROR)) {

                        ((BaseActivity) currentActivity).toast(currentActivity.getResources().getString(R.string.messageProductSuccessfullyDeleted), true);
                        ((CartActivity) currentActivity).getCartItems();
                    } else {
                        ((BaseActivity) currentActivity).cancelProgressDialog();

                        ((BaseActivity) currentActivity).logTesting("remove product quan error", "true", Log.ERROR);
                    }


                } catch (JSONException e) {
                    ((BaseActivity) currentActivity).logTesting("remove product quan json exeption is", e.toString(), Log.ERROR);
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


}
