package com.neon.vyaan.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mayank on 31/08/2016.
 */
public class AddCartModel implements Parcelable{

    @SerializedName("product_id")
    private int productId;

    @SerializedName("product_price")
    private int productPrice;
    @SerializedName("product_quantity")
    private int productQuantity;
    @SerializedName("is_active")
    private int is_active;
    @SerializedName("is_delete")
    private int is_delete;
    @SerializedName("user_id")
    private int user_id;

    public AddCartModel()
    {

    }

    protected AddCartModel(Parcel in) {
        productId = in.readInt();
        productPrice = in.readInt();
        productQuantity = in.readInt();
        is_active = in.readInt();
        is_delete = in.readInt();
        user_id = in.readInt();
    }

    public static final Creator<AddCartModel> CREATOR = new Creator<AddCartModel>() {
        @Override
        public AddCartModel createFromParcel(Parcel in) {
            return new AddCartModel(in);
        }

        @Override
        public AddCartModel[] newArray(int size) {
            return new AddCartModel[size];
        }
    };

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public int getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(int is_delete) {
        this.is_delete = is_delete;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(productId);
        parcel.writeInt(productPrice);
        parcel.writeInt(productQuantity);
        parcel.writeInt(is_active);
        parcel.writeInt(is_delete);
        parcel.writeInt(user_id);
    }
}
