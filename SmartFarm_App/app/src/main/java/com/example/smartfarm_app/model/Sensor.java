package com.example.smartfarm_app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Sensor implements Parcelable {
    private Integer sensorId;
    private String productId, sensorType;

    public Sensor (Integer sensorId, String productId, String sensorType) {
       this.sensorId =sensorId;
       this.productId = productId;
       this.sensorType =sensorType;
    }
    protected Sensor (Parcel in){
        sensorId = in.readInt();
        productId = in.readString();
        sensorType = in.readString();
    }

    public static final Creator<Sensor> CREATOR = new Creator<Sensor>() {
        @Override
        public Sensor createFromParcel(Parcel in) {
            return new Sensor(in);
        }

        @Override
        public Sensor[] newArray(int size) {
            return new Sensor[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel (Parcel dest, int flags){
        dest.writeInt(sensorId);
        dest.writeString(productId);
        dest.writeString(sensorType);
    }

    public Integer getSensorId() {
        return sensorId;
    }

    public void setSensorId(Integer sensorId) {
        this.sensorId = sensorId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }
}
