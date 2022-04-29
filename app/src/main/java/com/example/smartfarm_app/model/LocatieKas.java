package com.example.smartfarm_app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LocatieKas implements Parcelable {
    private String gebruikersNaam, kasNaam, straatNaam, huisNummer, plaats, postcode;

    public LocatieKas(String gebruikersNaam, String kasNaam, String straatNaam, String huisNummer, String plaats, String postcode){
        this.gebruikersNaam = gebruikersNaam;
        this.kasNaam = kasNaam;
        this.straatNaam = straatNaam;
        this.huisNummer = huisNummer;
        this.plaats = plaats;
        this.postcode = postcode;
    }
    protected LocatieKas (Parcel in){
        gebruikersNaam = in.readString();
        kasNaam = in.readString();
        straatNaam = in.readString();
        huisNummer = in.readString();
        plaats = in.readString();
        postcode = in.readString();
    }

    public static final Creator<LocatieKas> CREATOR = new Creator<LocatieKas>() {
        @Override
        public LocatieKas createFromParcel(Parcel in) {
            return new LocatieKas(in);
        }

        @Override
        public LocatieKas[] newArray(int size) {
            return new LocatieKas[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel (Parcel dest, int flags){
        dest.writeString(gebruikersNaam);
        dest.writeString(kasNaam);
        dest.writeString(straatNaam);
        dest.writeString(huisNummer);
        dest.writeString(plaats);
        dest.writeString(postcode);
    }

    public String getGebruikersNaam() {
        return gebruikersNaam;
    }

    public void setGebruikersNaam(String gebruikersNaam) {
        this.gebruikersNaam = gebruikersNaam;
    }

    public String getKasNaam() {
        return kasNaam;
    }

    public void setKasNaam(String kasNaam) {
        this.kasNaam = kasNaam;
    }

    public String getStraatNaam() {
        return straatNaam;
    }

    public void setStraatNaam(String straatNaam) {
        this.straatNaam = straatNaam;
    }

    public String getHuisNummer() {
        return huisNummer;
    }

    public void setHuisNummer(String huisNummer) {
        this.huisNummer = huisNummer;
    }

    public String getPlaats() {
        return plaats;
    }

    public void setPlaats(String plaats) {
        this.plaats = plaats;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String toString(){ return kasNaam; }
}
