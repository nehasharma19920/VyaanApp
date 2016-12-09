package com.neon.vyaan.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mayank on 31/08/2016.
 */
public class UpdateAddressModel implements Parcelable{


    @SerializedName("user_id")
    private int userId;
    @SerializedName("address_title")
    private String addressTitle;
    private String name;

    @SerializedName("address_line_1")
    private String addressLine1;
    @SerializedName("address_line_2")
    private String addressLine2;
    @SerializedName("city_id")
    private int cityID;
    @SerializedName("locality_id")
    private int localityID;
    private String landmark;
    @SerializedName("phone_number")
    private long phoneNumber;
    private int pincode;
    @SerializedName("address_id")
    private int addressId;

    public  UpdateAddressModel()
    {

    }


    protected UpdateAddressModel(Parcel in) {
        userId = in.readInt();
        addressTitle = in.readString();
        name = in.readString();
        addressLine1 = in.readString();
        addressLine2 = in.readString();
        cityID = in.readInt();
        localityID = in.readInt();
        landmark = in.readString();
        phoneNumber = in.readLong();
        pincode = in.readInt();
        addressId = in.readInt();
    }

    public static final Creator<UpdateAddressModel> CREATOR = new Creator<UpdateAddressModel>() {
        @Override
        public UpdateAddressModel createFromParcel(Parcel in) {
            return new UpdateAddressModel(in);
        }

        @Override
        public UpdateAddressModel[] newArray(int size) {
            return new UpdateAddressModel[size];
        }
    };

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAddressTitle() {
        return addressTitle;
    }

    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public int getLocalityID() {
        return localityID;
    }

    public void setLocalityID(int localityID) {
        this.localityID = localityID;
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

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(userId);
        parcel.writeString(addressTitle);
        parcel.writeString(name);
        parcel.writeString(addressLine1);
        parcel.writeString(addressLine2);
        parcel.writeInt(cityID);
        parcel.writeInt(localityID);
        parcel.writeString(landmark);
        parcel.writeLong(phoneNumber);
        parcel.writeInt(pincode);
        parcel.writeInt(addressId);
    }
}
