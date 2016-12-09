package com.neon.vyaan.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neon.vyaan.R;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.model.OrdersModel;
import com.neon.vyaan.utils.DateTimeUtils;
import com.neon.vyaan.utils.ImageHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * Created by Mayank on 21/08/2016.
 */
public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {


    Activity currentActivity;
    List<OrdersModel> ordersModelList;

    public OrdersAdapter(Activity currentActivity, List<OrdersModel> ordersModelList) {
        this.currentActivity = currentActivity;
        this.ordersModelList = ordersModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(currentActivity).inflate(R.layout.items_my_orders, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int pos = position;

        Picasso.with(currentActivity).load(AppConstants.BASE_URL_IMAGES + "/" + ordersModelList.get(pos).getProductImage()).into(new Target() {
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

        holder.textOrderId.setText("" + ordersModelList.get(pos).getMonthlySubscriptionId());
        holder.textStartDate.setText(DateTimeUtils.dateInParticularFormat("yyyy-MM-dd", "dd-MM-yyyy", ordersModelList.get(pos).getStartDate()));


        holder.textAmount.setText("" + ordersModelList.get(pos).getPaymentAmount());
        holder.textProductName.setText("" + ordersModelList.get(pos).getProductName());
        holder.textOrderStatus.setText("" + ordersModelList.get(pos).getOrderStatus());


        holder.textOrderType.setText("" + ordersModelList.get(pos).getType());
        holder.textQuantity.setText("" + ordersModelList.get(pos).getUserQuantity());
        holder.textAmount.setText("Rs. " + +ordersModelList.get(pos).getPaymentAmount());
        holder.textModeOfPayment.setText("" + ordersModelList.get(pos).getPaymentMode());
        holder.textPaymentStatus.setText("" + ordersModelList.get(pos).getPaymentStatus());


        holder.textEndDate.setText(DateTimeUtils.dateInParticularFormat("yyyy-MM-dd", "dd-MM-yyyy", ordersModelList.get(pos).getEndDate()));

        holder.textProductPrice.setText("Rs. " +
                ordersModelList.get(pos).getProductPrice());


        if (ordersModelList.get(pos).getType().equals("sub")) {
            holder.textOrderType.setText(currentActivity.getResources().getString(R.string.labelSubscriptin));
            holder.textLabelStatDate.setText(currentActivity.getResources().getString(R.string.labelStartDate));
            holder.containerModeOfPayment.setVisibility(View.GONE);
            holder.containerEndDate.setVisibility(View.VISIBLE);
            holder.textQuantity.setText("" + (ordersModelList.get(pos).getUserQuantity()));
            holder.textQuantityLiteres.setText("" + (Float) (Float.parseFloat(ordersModelList.get(pos).getUserQuantity()) / 2));


        } else {
            holder.textOrderType.setText(currentActivity.getResources().getString(R.string.labelOneTimePurchase));
            holder.textLabelStatDate.setText(currentActivity.getResources().getString(R.string.labelDateOfPurchase));
            holder.textQuantityLiteres.setText("" + (Float) (Float.parseFloat(ordersModelList.get(pos).getUserQuantity()) / 2));
            holder.containerModeOfPayment.setVisibility(View.VISIBLE);
            holder.containerEndDate.setVisibility(View.GONE);


        }


    }

    @Override
    public int getItemCount() {
        return ordersModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textOrderType;
        TextView textLabelStatDate;
        TextView textProductName;
        TextView textOrderId;
        ImageView imageProduct;
        TextView textStartDate;
        TextView textEndDate;
        TextView textQuantity;
        TextView textAmount;
        TextView textOrderStatus;
        TextView textModeOfPayment;
        TextView textPaymentStatus;
        TextView textProductPrice;
        TextView textQuantityLiteres;

        LinearLayout containerEndDate;
        LinearLayout containerModeOfPayment;


        public ViewHolder(View itemView) {
            super(itemView);

            textOrderType = (TextView) itemView.findViewById(R.id.textOrderType);
            textLabelStatDate = (TextView) itemView.findViewById(R.id.textLabelStatDate);
            textProductName = (TextView) itemView.findViewById(R.id.textProductName);
            textOrderId = (TextView) itemView.findViewById(R.id.textOrderId);
            imageProduct = (ImageView) itemView.findViewById(R.id.imageProduct);
            textStartDate = (TextView) itemView.findViewById(R.id.textStartDate);
            textEndDate = (TextView) itemView.findViewById(R.id.textEndDate);
            textQuantity = (TextView) itemView.findViewById(R.id.textQuantity);
            textAmount = (TextView) itemView.findViewById(R.id.textAmount);
            textOrderStatus = (TextView) itemView.findViewById(R.id.textOrderStatus);
            textModeOfPayment = (TextView) itemView.findViewById(R.id.textModeOfPayment);
            textPaymentStatus = (TextView) itemView.findViewById(R.id.textPaymentStatus);
            textProductPrice = (TextView) itemView.findViewById(R.id.textProductPrice);
            textQuantityLiteres = (TextView) itemView.findViewById(R.id.textQuantityLiteres);
            containerEndDate = (LinearLayout) itemView.findViewById(R.id.containerEndDate);
            containerModeOfPayment = (LinearLayout) itemView.findViewById(R.id.containerModeOfPayment);


        }
    }
}
