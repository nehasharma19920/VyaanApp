package com.neon.vyaan.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mayank on 31/08/2016.
 */
public class LocalityModel implements Parcelable{

    private int id;
    private String name;

    protected LocalityModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<LocalityModel> CREATOR = new Creator<LocalityModel>() {
        @Override
        public LocalityModel createFromParcel(Parcel in) {
            return new LocalityModel(in);
        }

        @Override
        public LocalityModel[] newArray(int size) {
            return new LocalityModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
    }


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
}
