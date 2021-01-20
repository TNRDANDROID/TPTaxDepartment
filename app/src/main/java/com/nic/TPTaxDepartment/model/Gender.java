package com.nic.TPTaxDepartment.model;


import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class Gender {


    public String getGender_code() {
        return gender_code;
    }

    public void setGender_code(String gender_code) {
        this.gender_code = gender_code;
    }

    public String getGender_name_en() {
        return gender_name_en;
    }

    public void setGender_name_en(String gender_name_en) {
        this.gender_name_en = gender_name_en;
    }

    public String getGender_name_ta() {
        return gender_name_ta;
    }

    public void setGender_name_ta(String gender_name_ta) {
        this.gender_name_ta = gender_name_ta;
    }

/*
    public static Creator<Gender> getCREATOR() {
        return CREATOR;
    }
*/

    public String gender_code;
    public String gender_name_en;
    public String gender_name_ta;

/*
    public Gender(Bundle postBundle){

        this.gender_code = postBundle.getString("gender_code");
        this.gender_name_en = postBundle.getString("gender_name_en");
        this.gender_name_ta = postBundle.getString("gender_name_ta");

    }
*/

/*
    protected Gender(Parcel in) {
        this.gender_code = in.readString();
        this.gender_name_en = in.readString();
        this.gender_name_ta = in.readString();
    }
*/

/*
    public static final Creator<Gender> CREATOR = new Creator<Gender>() {
        @Override
        public Gender createFromParcel(Parcel in) {
            return new Gender(in);
        }

        @Override
        public Gender[] newArray(int size) {
            return new Gender[size];
        }
    };
*/

/*
    @Override
    public int describeContents() {
        return 0;
    }
*/

/*
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.gender_code ,
                this.gender_name_en,
                this.gender_name_ta
        });
    }
*/

/*
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
*/

/*
    @Override
    public int hashCode() {
        return super.hashCode();
    }
*/
}
