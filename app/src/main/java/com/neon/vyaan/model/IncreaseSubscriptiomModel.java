package com.neon.vyaan.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mayank on 01/09/2016.
 */
public class IncreaseSubscriptiomModel implements Parcelable {
    @SerializedName("product_id")
    private int productId;
    @SerializedName("user_id")
    private int user_id;

    @SerializedName("start_date")
    private String startDate;
    @SerializedName("end_date")
    private String endDate;


    @SerializedName("payment_amount")
    private int paymentAmount;

    @SerializedName("payment_status")
    private String paymentStatus;


    private int quantity;
    private int price;
    private String type;
    private String status;
    private int subscription_id;

    public IncreaseSubscriptiomModel() {

    }


    protected IncreaseSubscriptiomModel(Parcel in) {
        productId = in.readInt();
        user_id = in.readInt();
        startDate = in.readString();
        endDate = in.readString();
        paymentAmount = in.readInt();
        paymentStatus = in.readString();
        quantity = in.readInt();
        price = in.readInt();
        type = in.readString();
        status = in.readString();
        subscription_id = in.readInt();
    }

    public static final Creator<IncreaseSubscriptiomModel> CREATOR = new Creator<IncreaseSubscriptiomModel>() {
        @Override
        public IncreaseSubscriptiomModel createFromParcel(Parcel in) {
            return new IncreaseSubscriptiomModel(in);
        }

        @Override
        public IncreaseSubscriptiomModel[] newArray(int size) {
            return new IncreaseSubscriptiomModel[size];
        }
    };

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSubscription_id() {
        return subscription_id;
    }

    public void setSubscription_id(int subscription_id) {
        this.subscription_id = subscription_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(productId);
        parcel.writeInt(user_id);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeInt(paymentAmount);
        parcel.writeString(paymentStatus);
        parcel.writeInt(quantity);
        parcel.writeInt(price);
        parcel.writeString(type);
        parcel.writeString(status);
        parcel.writeInt(subscription_id);
    }
}
