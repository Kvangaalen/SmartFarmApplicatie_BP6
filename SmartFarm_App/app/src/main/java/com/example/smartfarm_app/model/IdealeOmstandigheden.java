package com.example.smartfarm_app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class IdealeOmstandigheden implements Parcelable {
    private String plantSoort, plantRas;
    private Integer idealeTempratuur, idealeLuchtvochtigheid, idealeBodemvochtigheid, idealeLux;

    public IdealeOmstandigheden(String plantSoort, String plantRas, Integer idealeTempratuur, Integer idealeLuchtvochtigheid, Integer idealeBodemvochtigheid, Integer idealeLux) {
        this.plantSoort = plantSoort;
        this.plantRas = plantRas;
        this.idealeTempratuur = idealeTempratuur;
        this.idealeLuchtvochtigheid = idealeLuchtvochtigheid;
        this.idealeBodemvochtigheid = idealeBodemvochtigheid;
        this.idealeLux = idealeLux;
    }

    protected IdealeOmstandigheden(Parcel in) {
        plantSoort = in.readString();
        plantRas = in.readString();
        idealeTempratuur = in.readInt();
        idealeLuchtvochtigheid = in.readInt();
        idealeBodemvochtigheid = in.readInt();
        idealeLux = in.readInt();
    }

    public static final Creator<IdealeOmstandigheden> CREATOR = new Creator<IdealeOmstandigheden>() {
        @Override
        public IdealeOmstandigheden createFromParcel(Parcel in) {
            return new IdealeOmstandigheden(in);
        }

        @Override
        public IdealeOmstandigheden[] newArray(int size) {
            return new IdealeOmstandigheden[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(plantSoort);
        dest.writeString(plantRas);
        dest.writeInt(idealeTempratuur);
        dest.writeInt(idealeLuchtvochtigheid);
        dest.writeInt(idealeBodemvochtigheid);
        dest.writeInt(idealeLux);
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

    public Integer getIdealeTempratuur() {
        return idealeTempratuur;
    }

    public void setIdealeTempratuur(Integer idealeTempratuur) {
        this.idealeTempratuur = idealeTempratuur;
    }

    public Integer getIdealeLuchtvochtigheid() {
        return idealeLuchtvochtigheid;
    }

    public void setIdealeLuchtvochtigheid(Integer idealeLuchtvochtigheid) {
        this.idealeLuchtvochtigheid = idealeLuchtvochtigheid;
    }

    public Integer getIdealeBodemvochtigheid() {
        return idealeBodemvochtigheid;
    }

    public void setIdealeBodemvochtigheid(Integer idealeBodemvochtigheid) {
        this.idealeBodemvochtigheid = idealeBodemvochtigheid;
    }

    public Integer getIdealeLux() {
        return idealeLux;
    }

    public void setIdealeLux(Integer idealeLux) {
        this.idealeLux = idealeLux;
    }



    public String toString() {
        return plantSoort;
    }

    }



