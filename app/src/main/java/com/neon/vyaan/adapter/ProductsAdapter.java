package com.neon.vyaan.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neon.vyaan.R;
import com.neon.vyaan.activity.BaseActivity;
import com.neon.vyaan.activity.ProductDetails;
import com.neon.vyaan.activity.SignUpActivity;
import com.neon.vyaan.constants.AppConstants;
import com.neon.vyaan.model.ProductsModel;
import com.neon.vyaan.widgets.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mayank on 19/08/2016.
 */
public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> implements AppConstants {

    Activity currentActivity;
    List<ProductsModel> productsModelList;

    Bundle bundle;

    public ProductsAdapter(Activity currentActivity, List<ProductsModel> productsModelList) {
        this.currentActivity = currentActivity;
        this.productsModelList = productsModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(currentActivity).inflate(R.layout.items_products, parent, false);
        int height = parent.getMeasuredHeight() / 3;
        view.setMinimumHeight(height);
        //view.setMinimumHeight(height);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;
        holder.textProductName.setText(productsModelList.get(pos).getName());
        Log.e("product name", productsModelList.get(pos).getName());
        if (productsModelList.get(pos).getProductIcon() != null || productsModelList.get(pos).getProductIcon().isEmpty()) {
            String imageUrl = AppConstants.BASE_URL_IMAGES + "/" + productsModelList.get(pos).getProductIcon();
            Picasso.with(currentActivity).load(imageUrl).into(holder.imageProduct);

        }

      //  holder.imageProduct.setAlpha(0.4f);
       // holder.imageProduct.setTin(currentActivity.getResources().getColor(R.color.black));
        if (pos % 2 != 0) {
            holder.viewVerticalLine.setVisibility(View.GONE);
        } else {

            holder.viewVerticalLine.setVisibility(View.VISIBLE);
        }

        if (pos==productsModelList.size()-1||pos==productsModelList.size()-2)
        {
            holder.viewHorizintalLine.setVisibility(View.GONE);
        }
        else
        {
            holder.viewHorizintalLine.setVisibility(View.VISIBLE);
        }
        holder.textProductName.setText(productsModelList.get(pos).getName());

        holder.containerProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pos == 0) {
                    if (bundle == null) {
                        bundle = new Bundle();
                    }

                    bundle.putParcelable(PRODUCT_DETAILS, productsModelList.get(pos));
                    ((BaseActivity) currentActivity).startActivity(currentActivity, ProductDetails.class, bundle, true, AppConstants.REQUEST_TAG_NO_RESULT, true, ANIMATION_SLIDE_UP);

                } else {
                    ((BaseActivity) currentActivity).alert(currentActivity, ((BaseActivity) currentActivity).getString(R.string.alert_product_coming_soon), ((BaseActivity) currentActivity).getString(R.string.alert_product_coming_soon), ((BaseActivity) currentActivity).getString(R.string.labelOk), ((BaseActivity) currentActivity).getString(R.string.labelCancel), false, false, ALERT_TYPE_NO_NETWORK);

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return productsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageProduct;
        TextView textProductName;
        View viewVerticalLine;
        View viewHorizintalLine;
        LinearLayout containerProduct;

        public ViewHolder(View itemView) {
            super(itemView);

            imageProduct = (ImageView) itemView.findViewById(R.id.imageProduct);
            textProductName = (TextView) itemView.findViewById(R.id.textProductName);
            viewVerticalLine = (View) itemView.findViewById(R.id.viewVerticalLine);
            viewHorizintalLine = (View) itemView.findViewById(R.id.viewHorizintalLine);
            containerProduct = (LinearLayout) itemView.findViewById(R.id.containerProduct);
        }
    }
}
