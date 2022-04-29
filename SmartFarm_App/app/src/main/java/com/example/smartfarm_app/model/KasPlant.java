package com.example.smartfarm_app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class KasPlant implements Parcelable {
    private Integer plantId, vakNummer;
    private String kasNaam, plantSoort, plantRas, gebruikersNaam;

    public KasPlant (Integer plantId, String kasNaam, String plantSoort, String plantRas,Integer vakNummer,String gebruikersNaam){
        this.plantId = plantId;
        this.kasNaam = kasNaam;
        this.plantSoort = plantSoort;
        this.plantRas = plantRas;
        this.vakNummer =vakNummer;
        this.gebruikersNaam = gebruikersNaam;
    }
    protected KasPlant (Parcel in){
        plantId = in.readInt();
        kasNaam = in.readString();
        plantSoort = in.readString();
        plantRas = in.readString();
        vakNummer = in.readInt();
    }

    public static final Creator<KasPlant> CREATOR = new Creator<KasPlant>() {
        @Override
        public KasPlant createFromParcel(Parcel in) {
            return new KasPlant(in);
        }

        @Override
        public KasPlant[] newArray(int size) {
            return new KasPlant[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel (Parcel dest, int flags){
        dest.writeInt(plantId);
        dest.writeString(kasNaam);
        dest.writeString(plantSoort);
        dest.writeString(plantRas);
        dest.writeInt(vakNummer);
    }

    public Integer getPlantId() {
        return plantId;
    }

    public void setPlantId(Integer plantId) {
        this.plantId = plantId;
    }

    public Integer getVakNummer() {
        return vakNummer;
    }

    public void setVakNummer(Integer vakNummer) {
        this.vakNummer = vakNummer;
    }

    public String getKasNaam() {
        return kasNaam;
    }

    public void setKasNaam(String kasNaam) {
        this.kasNaam = kasNaam;
    }

    public String getPlantSoort() {
        return plantSoort;
    }

    public void setPlantSoort(String plantSoort) {
        this.plantSoort = plantSoort;
    }

    public String getPlantRas() {
        return plantRas;
    }

    public void setPlantRas(String plantRas) {
        this.plantRas = plantRas;
    }
}
