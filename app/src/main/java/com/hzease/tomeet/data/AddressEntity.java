package com.hzease.tomeet.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.services.core.LatLonPoint;


public class AddressEntity implements Parcelable {
    public boolean isChoose;
    public String title;
    public String snippet;
    public LatLonPoint latLonPoint;

    public AddressEntity(boolean isChoose, LatLonPoint latLonPoint, String snippet, String title) {
        this.isChoose = isChoose;
        this.latLonPoint = latLonPoint;
        this.snippet = snippet;
        this.title = title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isChoose ? (byte) 1 : (byte) 0);
        dest.writeString(this.title);
        dest.writeString(this.snippet);
        dest.writeParcelable(this.latLonPoint, flags);
    }

    protected AddressEntity(Parcel in) {
        this.isChoose = in.readByte() != 0;
        this.title = in.readString();
        this.snippet = in.readString();
        this.latLonPoint = in.readParcelable(LatLonPoint.class.getClassLoader());
    }

    public static final Creator<AddressEntity> CREATOR = new Creator<AddressEntity>() {
        @Override
        public AddressEntity createFromParcel(Parcel source) {
            return new AddressEntity(source);
        }

        @Override
        public AddressEntity[] newArray(int size) {
            return new AddressEntity[size];
        }
    };

    @Override
    public String toString() {
        return "AddressEntity{" +
                "isChoose=" + isChoose +
                ", title='" + title + '\'' +
                ", snippet='" + snippet + '\'' +
                ", latLonPoint=" + latLonPoint +
                '}';
    }
}
