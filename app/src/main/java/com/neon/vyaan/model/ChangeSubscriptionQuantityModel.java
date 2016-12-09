package com.neon.vyaan.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mayank on 30/08/2016.
 */
public class ChangeSubscriptionQuantityModel implements Parcelable{

    @SerializedName("start_date")
    private String startDate;
    @SerializedName("end_date")
    private String endDate;
    @SerializedName("product_id")
    private int productId;
    @SerializedName("subscription_id")
    private int subscriptionId;
    @SerializedName("total_packets")
    private int totalPackets;


    public ChangeSubscriptionQuantityModel()
    {

    }

    protected ChangeSubscriptionQuantityModel(Parcel in) {
        startDate = in.readString();
        endDate = in.readString();
        productId = in.readInt();
        subscriptionId = in.readInt();
        totalPackets = in.readInt();
    }

    public static final Creator<ChangeSubscriptionQuantityModel> CREATOR = new Creator<ChangeSubscriptionQuantityModel>() {
        @Override
        public ChangeSubscriptionQuantityModel createFromParcel(Parcel in) {
            return new ChangeSubscriptionQuantityModel(in);
        }

        @Override
        public ChangeSubscriptionQuantityModel[] newArray(int size) {
            return new ChangeSubscriptionQuantityModel[size];
        }
    };

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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public int getTotalPackets() {
        return totalPackets;
    }

    public void setTotalPackets(int totalPackets) {
        this.totalPackets = totalPackets;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeInt(productId);
        parcel.writeInt(subscriptionId);
        parcel.writeInt(totalPackets);
    }
}
