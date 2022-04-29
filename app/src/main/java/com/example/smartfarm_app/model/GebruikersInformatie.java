package com.example.smartfarm_app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GebruikersInformatie implements Parcelable {
    private String gebruikersNaam, naamEigenaar, emailAdres, telefoonEigenaar, wachtwoord, bedrijfsNaam;

    public GebruikersInformatie (String gebruikersNaam, String naamEigenaar, String emailAdres, String telefoonEigenaar, String wachtwoord,String bedrijfsNaam){
        this.gebruikersNaam = gebruikersNaam;
        this.naamEigenaar = naamEigenaar;
        this.emailAdres = emailAdres;
        this.telefoonEigenaar = telefoonEigenaar;
        this.wachtwoord = wachtwoord;
        this.bedrijfsNaam = bedrijfsNaam;
    }
    protected GebruikersInformatie (Parcel in){
        gebruikersNaam = in.readString();
        naamEigenaar = in.readString();
        emailAdres = in.readString();
        telefoonEigenaar = in.readString();
        wachtwoord = in.readString();
        bedrijfsNaam = in.readString();
    }

    public static final Creator<GebruikersInformatie> CREATOR = new Creator<GebruikersInformatie>() {
        @Override
        public GebruikersInformatie createFromParcel(Parcel in) {
            return new GebruikersInformatie(in);
        }

        @Override
        public GebruikersInformatie[] newArray(int size) {
            return new GebruikersInformatie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel (Parcel dest, int flags){
        dest.writeString(gebruikersNaam);
        dest.writeString(naamEigenaar);
        dest.writeString(emailAdres);
        dest.writeString(telefoonEigenaar);
        dest.writeString(wachtwoord);
        dest.writeString(bedrijfsNaam);
    }

    public String getGebruikersNaam() {
        return gebruikersNaam;
    }

    public void setGebruikersNaam(String gebruikersNaam) {
        this.gebruikersNaam = gebruikersNaam;
    }

    public String getNaamEigenaar() {
        return naamEigenaar;
    }

    public void setNaamEigenaar(String naamEigenaar) {
        this.naamEigenaar = naamEigenaar;
    }

    public String getEmailAdres() {
        return emailAdres;
    }

    public void setEmailAdres(String emailAdres) {
        this.emailAdres = emailAdres;
    }

    public String getTelefoonEigenaar() {
        return telefoonEigenaar;
    }

    public void setTelefoonEigenaar(String telefoonEigenaar) {
        this.telefoonEigenaar = telefoonEigenaar;
    }

    public String getWachtwoord() {
        return wachtwoord;
    }

    public void setWachtwoord(String wachtwoord) {
        this.wachtwoord = wachtwoord;
    }

    public String getBedrijfsNaam() {
        return bedrijfsNaam;
    }

    public void setBedrijfsNaam(String bedrijfsNaam) {
        this.bedrijfsNaam = bedrijfsNaam;
    }
}
