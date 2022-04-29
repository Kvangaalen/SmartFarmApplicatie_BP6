package com.example.smartfarm_app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ApparaatLocatie implements Parcelable {
    private String productId, kasNaam;
    private String vakNummer;

    public ApparaatLocatie (String productId, String kasNaam, String vakNummer){
        this.productId = productId;
        this.kasNaam = kasNaam;
        this.vakNummer = vakNummer;
    }
    protected ApparaatLocatie (Parcel in){
        productId = in.readString();
        kasNaam = in.readString();
        vakNummer = in.readString();
    }

    public static final Creator<ApparaatLocatie> CREATOR = new Creator<ApparaatLocatie>() {
        @Override
        public ApparaatLocatie createFromParcel(Parcel in) {
            return new ApparaatLocatie(in);
        }

        @Override
        public ApparaatLocatie[] newArray(int size) {
            return new ApparaatLocatie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel (Parcel dest, int flags){
        dest.writeString(productId);
        dest.writeString(kasNaam);
        dest.writeString(vakNummer);
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getKasNaam() {
        return kasNaam;
    }

    public void setKasNaam(String kasNaam) {
        this.kasNaam = kasNaam;
    }

    public String getVakNummer() {
        return vakNummer;
    }

    public void setVakNummer(String vakNummer) {
        this.vakNummer = vakNummer;
    }
    public String toString(){ return productId; }
}
