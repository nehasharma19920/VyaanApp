package com.neon.vyaan.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neon.vyaan.R;
import com.neon.vyaan.activity.BaseActivity;
import com.neon.vyaan.activity.PayForSubscription;
import com.neon.vyaan.activity.PayUActivity;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.model.BillsModel;
import com.neon.vyaan.model.OrdersModel;
import com.neon.vyaan.utils.DateTimeUtils;
import com.neon.vyaan.utils.ImageHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Mayank on 31/08/2016.
 */
public class ViewBillsAdapter extends RecyclerView.Adapter<ViewBillsAdapter.ViewHolder> {

    Activity currentActivity;
    List<BillsModel> billsModelList;

    public ViewBillsAdapter(Activity currentActivity, List<BillsModel> billsModelList) {
        this.currentActivity = currentActivity;
        this.billsModelList = new ArrayList<>();

        this.billsModelList = billsModelList;
    }


    @Override
    public ViewBillsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(currentActivity).inflate(R.layout.items_view_bills, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewBillsAdapter.ViewHolder holder, int position) {

        final int pos = position;

        if (billsModelList.get(pos).getPaymentAmount() == 0) {
            holder.buttonPayNow.setVisibility(View.GONE);
        } else {
            holder.buttonPayNow.setVisibility(View.VISIBLE);
        }

        Picasso.with(currentActivity).load(AppConstants.BASE_URL_IMAGES + "/" + billsModelList.get(pos).getProductImage()).into(new Target() {
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


        holder.textOrderId.setText("" + billsModelList.get(pos).getMonthlySubscriptionId());
        holder.textProductName.setText("" + billsModelList.get(pos).getProductName());


        holder.textStartDate.setText(DateTimeUtils.dateInParticularFormat("yyyy-MM-dd", "dd-MM-yyyy", billsModelList.get(pos).getStartDate()));


        holder.textEndDate.setText(DateTimeUtils.dateInParticularFormat("yyyy-MM-dd", "dd-MM-yyyy", billsModelList.get(pos).getEndDate()));


        if (billsModelList.get(pos).getType().equalsIgnoreCase("sub")) {
            holder.textType.setText(currentActivity.getResources().getString(R.string.labelSubscriptin));
        }

        if (billsModelList.get(pos).getIsRegular() == 1) {
            holder.textPlan.setText(currentActivity.getResources().getString(R.string.labelRegular));
        } else {
            holder.textPlan.setText(currentActivity.getResources().getString(R.string.labelAlternate));
        }

        holder.textAmount.setText("" + billsModelList.get(pos).getPaymentAmount());
        holder.textPaymentStatus.setText("" + billsModelList.get(pos).getPaymentStatus());
        holder.textQuantity.setText((""+billsModelList.get(pos).getUserSubscribedQuantity()));
     //   holder.textQuantityLiteres.setText(""+(billsModelList.get(pos).getUserSubscribedQuantity() / 2));

        holder.textQuantityLiteres.setText("" + (Float) (Float.parseFloat(String.valueOf(billsModelList.get(pos).getUserSubscribedQuantity())) / 2));

        holder.buttonPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(currentActivity, PayForSubscription.class);
                Bundle bundle = new Bundle();
                //     bundle.putString("order_num", "123");
                bundle.putString("order_date", DateTimeUtils.currentDate("dd-MM-yyyy", Calendar.getInstance()));
                bundle.putString("customer_id", "cID");
                bundle.putInt("MONTHLY_SUBSCRIPTION_ID", billsModelList.get(pos).getMonthlySubscriptionId());
                bundle.putInt("amount", billsModelList.get(pos).getPaymentAmount());
                bundle.putString("mode", "online");
                intent.putExtras(bundle);
                ((BaseActivity) (currentActivity)).startActivityForResult(intent, AppConstants.REQUEST_TAG_PAYU_SUBSCRIPTION_PAYMENT_ACTIVITY);
            }
        });

        holder.textPrice.setText("" + billsModelList.get(pos).getProductPrice());
        holder.textPaymentStatus.setText("" + billsModelList.get(pos).getPaymentStatus());
        holder.textOrderStatus.setText("" + billsModelList.get(pos).getOrderStatus());


    }

    @Override
    public int getItemCount() {
        return billsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textProductName;
        TextView textOrderId;
        TextView textType;
        TextView textStartDate;
        TextView textEndDate;
        TextView textQuantity;
        TextView textAmount;
        TextView textPaymentStatus;
        TextView textPrice;
        TextView textPlan;
        TextView textOrderStatus;
        TextView textQuantityLiteres;
        Button buttonPayNow;
        ImageView imageProduct;
        LinearLayout containerViewBills;

        public ViewHolder(View itemView) {
            super(itemView);
            textProductName = (TextView) itemView.findViewById(R.id.textProductName);
            textOrderId = (TextView) itemView.findViewById(R.id.textOrderId);
            textStartDate = (TextView) itemView.findViewById(R.id.textStartDate);
            textEndDate = (TextView) itemView.findViewById(R.id.textEndDate);
            textType = (TextView) itemView.findViewById(R.id.textType);
            textPlan = (TextView) itemView.findViewById(R.id.textPlan);
            textPrice = (TextView) itemView.findViewById(R.id.textPrice);
            textAmount = (TextView) itemView.findViewById(R.id.textAmount);
            textPaymentStatus = (TextView) itemView.findViewById(R.id.textPaymentStatus);
            textQuantity = (TextView) itemView.findViewById(R.id.textQuantity);
            textOrderStatus = (TextView) itemView.findViewById(R.id.textOrderStatus);
            textQuantityLiteres = (TextView) itemView.findViewById(R.id.textQuantityLiteres);
            buttonPayNow = (Button) itemView.findViewById(R.id.buttonPayNow);
            imageProduct = (ImageView) itemView.findViewById(R.id.imageProduct);
            containerViewBills = (LinearLayout) itemView.findViewById(R.id.containerViewBills);

        }
    }
}
