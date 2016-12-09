package com.neon.vyaan.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mayank on 20/08/2016.
 */
public class CartModel implements Parcelable {


    private int id;
    @SerializedName("product_id")
    private int productId;
    @SerializedName("product_quantity")
    private int productQuantity;
    @SerializedName("product_price")
    private int productPrice;
    @SerializedName("is_ative")
    private int isActive;
    @SerializedName("is_delete")
    private int isDelete;

    @SerializedName("product_name")
    private String productName;
    @SerializedName("product_pack_quantity")
    private String productPackQuantity;
    @SerializedName("product_icon")
    private String productIcon;
    @SerializedName("product_unit")
    private String productUnit;


    protected CartModel(Parcel in) {
        id = in.readInt();
        productId = in.readInt();
        productQuantity = in.readInt();
        productPrice = in.readInt();
        isActive = in.readInt();
        isDelete = in.readInt();
        productName = in.readString();
        productPackQuantity = in.readString();
        productIcon = in.readString();
        productUnit = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(productId);
        dest.writeInt(productQuantity);
        dest.writeInt(productPrice);
        dest.writeInt(isActive);
        dest.writeInt(isDelete);
        dest.writeString(productName);
        dest.writeString(productPackQuantity);
        dest.writeString(productIcon);
        dest.writeString(productUnit);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartModel> CREATOR = new Creator<CartModel>() {
        @Override
        public CartModel createFromParcel(Parcel in) {
            return new CartModel(in);
        }

        @Override
        public CartModel[] newArray(int size) {
            return new CartModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPackQuantity() {
        return productPackQuantity;
    }

    public void setProductPackQuantity(String productPackQuantity) {
        this.productPackQuantity = productPackQuantity;
    }

    public String getProductIcon() {
        return productIcon;
    }

    public void setProductIcon(String productIcon) {
        this.productIcon = productIcon;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

}
