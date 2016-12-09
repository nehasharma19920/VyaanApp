package com.neon.vyaan.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mayank on 24/08/2016.
 */
public class ProfileModel implements Parcelable {

    private String name;
    private String email;
    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("user_id")
    private int  userId;


    public ProfileModel()
    {

    }


    protected ProfileModel(Parcel in) {
        name = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        userId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phoneNumber);
        dest.writeInt(userId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProfileModel> CREATOR = new Creator<ProfileModel>() {
        @Override
        public ProfileModel createFromParcel(Parcel in) {
            return new ProfileModel(in);
        }

        @Override
        public ProfileModel[] newArray(int size) {
            return new ProfileModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
