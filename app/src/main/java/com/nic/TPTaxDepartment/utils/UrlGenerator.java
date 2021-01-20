package com.nic.TPTaxDepartment.utils;


import com.nic.TPTaxDepartment.Application.NICApplication;
import com.nic.TPTaxDepartment.R;

/**
 * Created by Achanthi Sundar  on 21/03/16.
 */
public class UrlGenerator {



    public static String getLoginUrl() {
        return NICApplication.getAppString(R.string.LOGIN_URL);
    }

    public static String saveTradersUrl() {
        return NICApplication.getAppString(R.string.SAVE_TRADERS_URL);
    }
    public static String TradersUrl() {
        return NICApplication.getAppString(R.string.TRADERS_URL);
    }

    public static String prodOpenUrl() {
        return NICApplication.getAppString(R.string.PROD_OPEN_URL);
    }

    public static String getServicesListUrl() {
        return NICApplication.getAppString(R.string.BASE_SERVICES_URL);
    }

    public static String getTankPondListUrl() {
        return NICApplication.getAppString(R.string.APP_MAIN_SERVICES_URL);
    }


    public static String getTnrdHostName() {
        return NICApplication.getAppString(R.string.TNRD_HOST_NAME);
    }



}
