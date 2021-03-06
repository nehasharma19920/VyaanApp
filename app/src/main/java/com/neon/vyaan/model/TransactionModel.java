package com.neon.vyaan.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mayank on 31/08/2016.
 */
public class TransactionModel implements Parcelable {

    @SerializedName("user_id")
    private int userId;
    @SerializedName("product_id")
    private int productId;
    @SerializedName("start_date")
    private String startDate;
    @SerializedName("end_date")
    private String endDate;
    @SerializedName("payment_status")
    private String paymentStatus;
    @SerializedName("payment_amount")
    private int paymentAmount;

    private String type;
    @SerializedName("product_name")
    private String productName;
    @SerializedName("product_image")
    private String productImage;
    @SerializedName("monthly_subscription_id")
    private int monthlySubscriptionId;
    @SerializedName("product_packed_quantity")
    private int productPackedQuantity;
    @SerializedName("is_regular")
    private int isRegular;

    @SerializedName("order_status")
    private String orderStatus;

    protected TransactionModel(Parcel in) {
        userId = in.readInt();
        productId = in.readInt();
        startDate = in.readString();
        endDate = in.readString();
        paymentStatus = in.readString();
        paymentAmount = in.readInt();
        type = in.readString();
        productName = in.readString();
        productImage = in.readString();
        monthlySubscriptionId = in.readInt();
        productPackedQuantity = in.readInt();
        isRegular = in.readInt();
        orderStatus = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeInt(productId);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(paymentStatus);
        dest.writeInt(paymentAmount);
        dest.writeString(type);
        dest.writeString(productName);
        dest.writeString(productImage);
        dest.writeInt(monthlySubscriptionId);
        dest.writeInt(productPackedQuantity);
        dest.writeInt(isRegular);
        dest.writeString(orderStatus);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TransactionModel> CREATOR = new Creator<TransactionModel>() {
        @Override
        public TransactionModel createFromParcel(Parcel in) {
            return new TransactionModel(in);
        }

        @Override
        public TransactionModel[] newArray(int size) {
            return new TransactionModel[size];
        }
    };

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

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

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public int getMonthlySubscriptionId() {
        return monthlySubscriptionId;
    }

    public void setMonthlySubscriptionId(int monthlySubscriptionId) {
        this.monthlySubscriptionId = monthlySubscriptionId;
    }

    public int getProductPackedQuantity() {
        return productPackedQuantity;
    }

    public void setProductPackedQuantity(int productPackedQuantity) {
        this.productPackedQuantity = productPackedQuantity;
    }

    public int getIsRegular() {
        return isRegular;
    }

    public void setIsRegular(int isRegular) {
        this.isRegular = isRegular;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

}
