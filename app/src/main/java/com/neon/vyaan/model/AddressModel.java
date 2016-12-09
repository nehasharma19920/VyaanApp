package com.neon.vyaan.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mayank on 20/08/2016.
 */
public class AddressModel implements Parcelable{

    private String id;
    private String name;
    @SerializedName("product_tag")
    private String productTag;
    private String images;
    @SerializedName("address_line_1")
    private String addressLine1;
    @SerializedName("address_line_2")
    private String addressLine2;
    @SerializedName("city_name")
    private String cityName;
    @SerializedName("locality_name")
    private String localityName;
    private String landmark;
    @SerializedName("phone_number")
    private long phoneNumber;
    private int pincode;
    @SerializedName("address_title")
    private String addressTitle;
    @SerializedName("city_id")
    private int cityId;
    @SerializedName("locality_id")
    private int localityId;


    public AddressModel()
    {

    }


    protected AddressModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        productTag = in.readString();
        images = in.readString();
        addressLine1 = in.readString();
        addressLine2 = in.readString();
        cityName = in.readString();
        localityName = in.readString();
        landmark = in.readString();
        phoneNumber = in.readLong();
        pincode = in.readInt();
        addressTitle = in.readString();
        cityId = in.readInt();
        localityId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(productTag);
        dest.writeString(images);
        dest.writeString(addressLine1);
        dest.writeString(addressLine2);
        dest.writeString(cityName);
        dest.writeString(localityName);
        dest.writeString(landmark);
        dest.writeLong(phoneNumber);
        dest.writeInt(pincode);
        dest.writeString(addressTitle);
        dest.writeInt(cityId);
        dest.writeInt(localityId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AddressModel> CREATOR = new Creator<AddressModel>() {
        @Override
        public AddressModel createFromParcel(Parcel in) {
            return new AddressModel(in);
        }

        @Override
        public AddressModel[] newArray(int size) {
            return new AddressModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductTag() {
        return productTag;
    }

    public void setProductTag(String productTag) {
        this.productTag = productTag;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getLocalityName() {
        return localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public static Creator<AddressModel> getCREATOR() {
        return CREATOR;
    }

    public String getAddressTitle() {
        return addressTitle;
    }

    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getLocalityId() {
        return localityId;
    }

    public void setLocalityId(int localityId) {
        this.localityId = localityId;
    }
}

