package com.neon.vyaan.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Mayank on 19/08/2016.
 */
public class ProductsModel implements Parcelable {

    private int id;
    private String name;
    @SerializedName("product_tag")
    private String productTag;

    private List<String > images;
    private int price;
    private int quantity;
    @SerializedName("product_icon")
    private String productIcon;
    private String about;

    protected ProductsModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        productTag = in.readString();
        images = in.createStringArrayList();
        price = in.readInt();
        quantity = in.readInt();
        productIcon = in.readString();
        about = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(productTag);
        dest.writeStringList(images);
        dest.writeInt(price);
        dest.writeInt(quantity);
        dest.writeString(productIcon);
        dest.writeString(about);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductsModel> CREATOR = new Creator<ProductsModel>() {
        @Override
        public ProductsModel createFromParcel(Parcel in) {
            return new ProductsModel(in);
        }

        @Override
        public ProductsModel[] newArray(int size) {
            return new ProductsModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String  getProductTag() {
        return productTag;
    }

    public void setProductTag(String  productTag) {
        this.productTag = productTag;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductIcon() {
        return productIcon;
    }

    public void setProductIcon(String productIcon) {
        this.productIcon = productIcon;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
