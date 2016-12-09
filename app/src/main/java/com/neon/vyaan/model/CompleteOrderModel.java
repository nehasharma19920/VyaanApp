package com.neon.vyaan.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.CompoundButton;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mayank on 31/08/2016.
 */


public class CompleteOrderModel implements Parcelable {

    @SerializedName("order_items")
    private List<PlaceOrderModel> order_items;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("address_id")
    private String addressId;

    public CompleteOrderModel ()
    {

    }


    public static class PlaceOrderModel implements Parcelable {


        @SerializedName("product_id")
        private int productId;
        @SerializedName("start_date")
        private String startDate;
        @SerializedName("end_date")
        private String endDate;
        @SerializedName("order_status")
        private String orderStatus;
        @SerializedName("payment_mode")
        private String paymentMode;
        @SerializedName("payment_status")
        private String paymentStatus;

        private String type;
        private int quantity;

        @SerializedName("payment_amount")
        private int paymentAmount;

        public PlaceOrderModel() {

        }


        protected PlaceOrderModel(Parcel in) {
            productId = in.readInt();
            startDate = in.readString();
            endDate = in.readString();
            orderStatus = in.readString();
            paymentMode = in.readString();
            paymentStatus = in.readString();
            type = in.readString();
            quantity = in.readInt();
            paymentAmount = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(productId);
            dest.writeString(startDate);
            dest.writeString(endDate);
            dest.writeString(orderStatus);
            dest.writeString(paymentMode);
            dest.writeString(paymentStatus);
            dest.writeString(type);
            dest.writeInt(quantity);
            dest.writeInt(paymentAmount);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<PlaceOrderModel> CREATOR = new Creator<PlaceOrderModel>() {
            @Override
            public PlaceOrderModel createFromParcel(Parcel in) {
                return new PlaceOrderModel(in);
            }

            @Override
            public PlaceOrderModel[] newArray(int size) {
                return new PlaceOrderModel[size];
            }
        };

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getPaymentMode() {
            return paymentMode;
        }

        public void setPaymentMode(String paymentMode) {
            this.paymentMode = paymentMode;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getPaymentAmount() {
            return paymentAmount;
        }

        public void setPaymentAmount(int paymentAmount) {
            this.paymentAmount = paymentAmount;
        }
    }


    protected CompleteOrderModel(Parcel in) {
        order_items = in.createTypedArrayList(PlaceOrderModel.CREATOR);
        userId = in.readInt();
        addressId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(order_items);
        dest.writeInt(userId);
        dest.writeString(addressId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CompleteOrderModel> CREATOR = new Creator<CompleteOrderModel>() {
        @Override
        public CompleteOrderModel createFromParcel(Parcel in) {
            return new CompleteOrderModel(in);
        }

        @Override
        public CompleteOrderModel[] newArray(int size) {
            return new CompleteOrderModel[size];
        }
    };

    public List<PlaceOrderModel> getOrder_items() {
        return order_items;
    }

    public void setOrder_items(List<PlaceOrderModel> order_items) {
        this.order_items = order_items;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
}

