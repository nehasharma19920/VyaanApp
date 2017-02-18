package com.neon.vyaan.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.neon.vyaan.R;
import com.neon.vyaan.model.CartModel;

import java.util.List;

/**
 * Created by Mayank on 31/08/2016.
 */
public class OrderConfirmationAdapter extends RecyclerView.Adapter<OrderConfirmationAdapter.ViewHolder> {

    List<CartModel> cartModelList;
    Activity currentActivity;

    public OrderConfirmationAdapter(List<CartModel> cartModelList, Activity currentActivity) {
        this.cartModelList = cartModelList;
        this.currentActivity = currentActivity;
    }

    @Override
    public OrderConfirmationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(currentActivity).inflate(R.layout.items_order_confirmation, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(OrderConfirmationAdapter.ViewHolder holder, int position) {

        final int pos = position;

        holder.textProductName.setText(cartModelList.get(pos).getProductName());
   /*     holder.textProductQuantity.setText(cartModelList.get(pos).getProductQuantity() + " * " +
                cartModelList.get(pos).getProductPrice());*/

        holder.textProductQuantity.setText("" + (Float) (Float.parseFloat(String.valueOf(cartModelList.get(pos).getProductQuantity())) / 2) + " * " +
                (cartModelList.get(pos).getProductPrice()*2));


        holder.textProductTotal.setText("Rs. " + cartModelList.get(pos).getProductQuantity() * cartModelList.get(pos).getProductPrice());

    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textProductName;
        TextView textProductQuantity;
        TextView textProductTotal;

        public ViewHolder(View itemView) {
            super(itemView);

            textProductName = (TextView) itemView.findViewById(R.id.textProductName);
            textProductQuantity = (TextView) itemView.findViewById(R.id.textProductQuantity);
            textProductTotal = (TextView) itemView.findViewById(R.id.textProductTotal);
        }
    }
}
