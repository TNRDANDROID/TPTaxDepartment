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


    public String gender_code;
    public String gender_name_en;
    public String gender_name_ta;

}
