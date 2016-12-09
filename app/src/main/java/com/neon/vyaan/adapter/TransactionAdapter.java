package com.neon.vyaan.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neon.vyaan.R;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.model.BillsModel;
import com.neon.vyaan.model.TransactionModel;
import com.neon.vyaan.utils.ImageHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

/**
 * Created by Mayank on 20/08/2016.
 */
public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    Activity currentActivity;
    List<TransactionModel> transactionModelList;

    public TransactionAdapter(Activity currentActivity, List<TransactionModel> transactionModelList) {
        this.currentActivity = currentActivity;
        this.transactionModelList = transactionModelList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(currentActivity).inflate(R.layout.items_transactions, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int pos = position;

        Picasso.with(currentActivity).load(AppConstants.BASE_URL_IMAGES + "/" + transactionModelList.get(pos).getProductImage()).into(new Target() {
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


        holder.textProductName.setText("" + transactionModelList.get(pos).getProductName());
        holder.textOrderId.setText("" + transactionModelList.get(pos).getMonthlySubscriptionId());
        holder.textStartDate.setText(transactionModelList.get(pos).getStartDate());
        holder.textEndDate.setText(transactionModelList.get(pos).getEndDate());

        if (transactionModelList.get(pos).getType().equalsIgnoreCase("sub")) {
            holder.textType.setText(currentActivity.getResources().getString(R.string.labelSubscriptin));

            holder.textLabelStartDate.setText(currentActivity.getResources().getString(R.string.labelStartDate));
            holder.containerEndDate.setVisibility(View.VISIBLE);
            holder.conteinerPlan.setVisibility(View.VISIBLE);

        } else {

            holder.textLabelStartDate.setText(currentActivity.getResources().getString(R.string.labelOrderdedOn));
            holder.containerEndDate.setVisibility(View.GONE);
            holder.conteinerPlan.setVisibility(View.GONE);
        }


        holder.textStatus.setText(transactionModelList.get(pos).getOrderStatus());
    /*    if (transactionModelList.get(pos).getIsRegular() == 1) {
            holder.textStatus.setText(currentActivity.getResources().getString(R.string.labelRegular));
        } else {
            holder.textStatus.setText(currentActivity.getResources().getString(R.string.labelAlternate));

        }*/

        holder.textAmount.setText("" + transactionModelList.get(pos).getPaymentAmount());


    }

    @Override
    public int getItemCount() {
        return transactionModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textProductName;
        TextView textOrderId;
        TextView textStartDate;
        TextView textEndDate;
        TextView textType;
        TextView textStatus;
        TextView textAmount;
        TextView textLabelStartDate;
        LinearLayout containerEndDate;
        LinearLayout conteinerPlan;
        Button buttonPayNow;
        ImageView imageProduct;


        public ViewHolder(View itemView) {
            super(itemView);

            textProductName = (TextView) itemView.findViewById(R.id.textProductName);
            textOrderId = (TextView) itemView.findViewById(R.id.textOrderId);
            textStartDate = (TextView) itemView.findViewById(R.id.textStartDate);
            textEndDate = (TextView) itemView.findViewById(R.id.textEndDate);
            textType = (TextView) itemView.findViewById(R.id.textType);
            textStatus = (TextView) itemView.findViewById(R.id.textStatus);
            textAmount = (TextView) itemView.findViewById(R.id.textAmount);
            textLabelStartDate = (TextView) itemView.findViewById(R.id.textLabelStartDate);
            containerEndDate = (LinearLayout) itemView.findViewById(R.id.containerEndDate);
            conteinerPlan = (LinearLayout) itemView.findViewById(R.id.conteinerPlan);
            buttonPayNow = (Button) itemView.findViewById(R.id.buttonPayNow);
            imageProduct = (ImageView) itemView.findViewById(R.id.imageProduct);

        }
    }
}
