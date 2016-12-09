package com.neon.vyaan.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mayank on 19/08/2016.
 */
public class SignUpModel implements Parcelable {

    private String name;
    @SerializedName("email")
    private String emailId;
    private String password;
    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("is_app_user")
    private int isAppUser = 1;
    @SerializedName("is_admin")
    private int isAdmin = 0;


    public SignUpModel() {

    }

    protected SignUpModel(Parcel in) {
        name = in.readString();
        emailId = in.readString();
        password = in.readString();
        phoneNumber = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(emailId);
        dest.writeString(password);
        dest.writeString(phoneNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SignUpModel> CREATOR = new Creator<SignUpModel>() {
        @Override
        public SignUpModel createFromParcel(Parcel in) {
            return new SignUpModel(in);
        }

        @Override
        public SignUpModel[] newArray(int size) {
            return new SignUpModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getIsAppUser() {
        return isAppUser;
    }

    public void setIsAppUser(int isAppUser) {
        this.isAppUser = isAppUser;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }
}
