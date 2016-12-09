package com.neon.vyaan.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neon.vyaan.R;
import com.neon.vyaan.activity.AllAddresses;
import com.neon.vyaan.activity.BaseActivity;
import com.neon.vyaan.activity.CartActivity;
import com.neon.vyaan.activity.Dashboard;
import com.neon.vyaan.activity.EditAddressActivity;
import com.neon.vyaan.activity.OrderConfirmation;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.model.AddressModel;

import java.util.List;

/**
 * Created by Mayank on 20/08/2016.
 */
public class AllAddressesAdapter extends RecyclerView.Adapter<AllAddressesAdapter.ViewHolder> {

    Activity currentActivity;
    List<AddressModel> addressModelList;

    Bundle bundle;

    public AllAddressesAdapter(Activity currentActivity, List<AddressModel> addressModelList) {
        this.currentActivity = currentActivity;
        this.addressModelList = addressModelList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(currentActivity).inflate(R.layout.items_address, parent, false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final int pos = position;
        holder.textAddressType.setText(addressModelList.get(pos).getAddressTitle());
        holder.textUserName.setText(addressModelList.get(pos).getName());
        holder.textAddress.setText(addressModelList.get(pos).getAddressLine1() + addressModelList.get(pos).getAddressLine2());
        holder.textCity.setText(addressModelList.get(pos).getCityName());
        holder.textState.setText(addressModelList.get(pos).getLocalityName());
        holder.textPinCode.setText("" + addressModelList.get(pos).getPincode());
        holder.textPhoneNo.setText("" + addressModelList.get(pos).getPhoneNumber());

        holder.imageAddressEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bundle == null) {
                    bundle = new Bundle();
                }

                bundle.putParcelable(AppConstants.ADDRESS_MODEL, addressModelList.get(pos));

                ((BaseActivity) currentActivity).startActivity(currentActivity, EditAddressActivity.class, bundle, true, AppConstants.REQUEST_TAG_EDIT_ADDRESS_ACTIVITY, true, AppConstants.ANIMATION_SLIDE_LEFT);

            }
        });
        holder.containerAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (((AllAddresses) currentActivity).isSubscription) {
                    alert(currentActivity, currentActivity.getString(R.string.titlePlaceSubscription), currentActivity.getString(R.string.messagePlaceSubscription), currentActivity.getString(R.string.labelOk), currentActivity.getString(R.string.labelCancel), true, true, AppConstants.ALERT_TYPE_NO_NETWORK, pos);

                } else {


                    if (bundle == null) {
                        bundle = new Bundle();
                    }

                    bundle.putParcelableArrayList(AppConstants.CART_MODEL_LIST, ((AllAddresses) currentActivity).cartModelList);
                    bundle.putString(AppConstants.ADDRESS_ID, addressModelList.get(pos).getId());

                    ((BaseActivity) currentActivity).startActivity(currentActivity, OrderConfirmation.class, bundle, true, AppConstants.REQUEST_TAG_NO_RESULT, true, AppConstants.ANIMATION_SLIDE_LEFT);


                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return addressModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textAddressType;
        TextView textUserName;
        TextView textAddress;
        TextView textCity;
        TextView textState;
        TextView textPinCode;
        TextView textPhoneNo;
        LinearLayout containerAddress;
        ImageView imageAddressEdit;

        public ViewHolder(View itemView) {
            super(itemView);

            textAddressType = (TextView) itemView.findViewById(R.id.textAddressType);
            textUserName = (TextView) itemView.findViewById(R.id.textUserName);
            textAddress = (TextView) itemView.findViewById(R.id.textAddress);
            textCity = (TextView) itemView.findViewById(R.id.textCity);
            textState = (TextView) itemView.findViewById(R.id.textState);
            textPinCode = (TextView) itemView.findViewById(R.id.textPinCode);
            textPhoneNo = (TextView) itemView.findViewById(R.id.textPhoneNo);
            containerAddress = (LinearLayout) itemView.findViewById(R.id.containerAddress);
            imageAddressEdit = (ImageView) itemView.findViewById(R.id.imageAddressEdit);
        }
    }


    public void alert(Context context, String title, String message, String positiveButton, String negativeButton, boolean isNegativeButton, boolean isTitle, final int alertType, final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (isTitle) {
            builder.setTitle(title);
        }


        builder.setMessage(message);
        builder.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ((AllAddresses) currentActivity).placeSubscription(addressModelList.get(pos).getId());

            }
        });
        if (isNegativeButton) {
            builder.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ((AllAddresses) currentActivity).toast(currentActivity.getResources().getString(R.string.messageSubscriptionCancelled), false);
                    ((AllAddresses) currentActivity).finish();
                }
            });
        }

        builder.show();


    }


}
