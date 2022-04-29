package com.example.smartfarm_app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Gemetenverschilwaarde implements Parcelable {
    private String productid, datum, tijdstip;
    private int  gemetentempratuur, gemetenluchtvochtigheid, gemetenbodemvochtigheid, gemetenlux;

    public Gemetenverschilwaarde(String productid, String datum, String tijdstip, int gemetentempratuur, int gemetenluchtvochtigheid, int gemetenbodemvochtigheid, int gemetenlux){
        this.productid = productid;
        this.datum = datum;
        this.tijdstip = tijdstip;
        this.gemetentempratuur = gemetentempratuur;
        this.gemetenluchtvochtigheid = gemetenluchtvochtigheid;
        this.gemetenbodemvochtigheid = gemetenbodemvochtigheid;
        this. gemetenlux = gemetenlux;
    }

    protected Gemetenverschilwaarde(Parcel in) {
        productid = in.readString();
        datum = in.readString();
        tijdstip = in.readString();
        gemetentempratuur = in.readInt();
        gemetenluchtvochtigheid = in.readInt();
        gemetenbodemvochtigheid = in.readInt();
        gemetenlux = in.readInt();
    }

    public static final Creator<Gemetenverschilwaarde> CREATOR = new Creator<Gemetenverschilwaarde>() {
        @Override
        public Gemetenverschilwaarde createFromParcel(Parcel in) {
            return new Gemetenverschilwaarde(in);
        }

        @Override
        public Gemetenverschilwaarde[] newArray(int size) {
            return new Gemetenverschilwaarde[size];
        }
    };

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getTijdstip() {
        return tijdstip;
    }

    public void setTijdstip(String tijdstip) {
        this.tijdstip = tijdstip;
    }

    public int getGemetentempratuur() {
        return gemetentempratuur;
    }

    public void setGemetentempratuur(int gemetentempratuur) {
        this.gemetentempratuur = gemetentempratuur;
    }

    public int getGemetenluchtvochtigheid() {
        return gemetenluchtvochtigheid;
    }

    public void setGemetenluchtvochtigheid(int gemetenluchtvochtigheid) {
        this.gemetenluchtvochtigheid = gemetenluchtvochtigheid;
    }

    public int getGemetenbodemvochtigheid() {
        return gemetenbodemvochtigheid;
    }

    public void setGemetenbodemvochtigheid(int gemetenbodemvochtigheid) {
        this.gemetenbodemvochtigheid = gemetenbodemvochtigheid;
    }

    public int getGemetenlux() {
        return gemetenlux;
    }

    public void setGemetenlux(int gemetenlux) {
        this.gemetenlux = gemetenlux;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productid);
        parcel.writeString(datum);
        parcel.writeString(tijdstip);
        parcel.writeInt(gemetentempratuur);
        parcel.writeInt(gemetenluchtvochtigheid);
        parcel.writeInt(gemetenbodemvochtigheid);
        parcel.writeInt(gemetenlux);
    }
}
