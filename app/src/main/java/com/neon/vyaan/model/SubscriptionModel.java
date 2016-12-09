package com.neon.vyaan.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mayank on 30/08/2016.
 */
public class SubscriptionModel implements Parcelable {

    @SerializedName("monthly_subscription_id")
    private int monthlySubscriptionId;
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
    @SerializedName("product_packed_quantity")
    private int productPackedQuantity;
    @SerializedName("is_regular")
    private int isRegular;

    @SerializedName("product_price")
    private int product_price;


    protected SubscriptionModel(Parcel in) {
        monthlySubscriptionId = in.readInt();
        userId = in.readInt();
        productId = in.readInt();
        startDate = in.readString();
        endDate = in.readString();
        paymentStatus = in.readString();
        paymentAmount = in.readInt();
        type = in.readString();
        productName = in.readString();
        productImage = in.readString();
        productPackedQuantity = in.readInt();
        isRegular = in.readInt();
        product_price = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(monthlySubscriptionId);
        dest.writeInt(userId);
        dest.writeInt(productId);
        dest.writeString(startDate);
        dest.writeString(endDate);
        dest.writeString(paymentStatus);
        dest.writeInt(paymentAmount);
        dest.writeString(type);
        dest.writeString(productName);
        dest.writeString(productImage);
        dest.writeInt(productPackedQuantity);
        dest.writeInt(isRegular);
        dest.writeInt(product_price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubscriptionModel> CREATOR = new Creator<SubscriptionModel>() {
        @Override
        public SubscriptionModel createFromParcel(Parcel in) {
            return new SubscriptionModel(in);
        }

        @Override
        public SubscriptionModel[] newArray(int size) {
            return new SubscriptionModel[size];
        }
    };

    public int getMonthlySubscriptionId() {
        return monthlySubscriptionId;
    }

    public void setMonthlySubscriptionId(int monthlySubscriptionId) {
        this.monthlySubscriptionId = monthlySubscriptionId;
    }

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

    public int getProduct_price() {
        return product_price;
    }

    public void setProduct_price(int product_price) {
        this.product_price = product_price;
    }
}
