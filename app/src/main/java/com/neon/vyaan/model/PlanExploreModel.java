package com.neon.vyaan.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mayank on 04/09/2016.
 */
public class PlanExploreModel implements Parcelable {

    @SerializedName("total_packets")
    private int totalPackets;
    @SerializedName("price")
    private int price;
    @SerializedName("product_pack_quantity")
    private int productPackQuantity;
    @SerializedName("delibery_date")
    private String deliberyDate;
    @SerializedName("status")
    private String status;

    protected PlanExploreModel(Parcel in) {
        totalPackets = in.readInt();
        price = in.readInt();
        productPackQuantity = in.readInt();
        deliberyDate = in.readString();
        status = in.readString();
    }

    public static final Creator<PlanExploreModel> CREATOR = new Creator<PlanExploreModel>() {
        @Override
        public PlanExploreModel createFromParcel(Parcel in) {
            return new PlanExploreModel(in);
        }

        @Override
        public PlanExploreModel[] newArray(int size) {
            return new PlanExploreModel[size];
        }
    };

    public int getTotalPackets() {
        return totalPackets;
    }

    public void setTotalPackets(int totalPackets) {
        this.totalPackets = totalPackets;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getProductPackQuantity() {
        return productPackQuantity;
    }

    public void setProductPackQuantity(int productPackQuantity) {
        this.productPackQuantity = productPackQuantity;
    }

    public String getDeliberyDate() {
        return deliberyDate;
    }

    public void setDeliberyDate(String deliberyDate) {
        this.deliberyDate = deliberyDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(totalPackets);
        parcel.writeInt(price);
        parcel.writeInt(productPackQuantity);
        parcel.writeString(deliberyDate);
        parcel.writeString(status);
    }
}
