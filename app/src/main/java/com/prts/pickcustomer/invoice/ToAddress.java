package com.prts.pickcustomer.invoice;

/**
 * Created by LOGICON on 31-12-2017.
 */

public class ToAddress {
    String addres = "";
    String cityName = "";
    String stateName = "";
    String getPostalCode ="";

    public String getAddres() {
        return addres;
    }

    public void setAddres(String addres) {
        this.addres = addres;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getGetPostalCode() {
        return getPostalCode;
    }

    public void setGetPostalCode(String getPostalCode) {
        this.getPostalCode = getPostalCode;
    }
}
