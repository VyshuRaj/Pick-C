package com.prts.pickcustomer.queries;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LOGICON on 31-12-2017.
 */

public class Query {
    @SerializedName("Name")
    String name;
    @SerializedName("Email")
    String email;
    @SerializedName("MobileNo")
    String mobile;
    @SerializedName("Message")
    String query;
    @SerializedName("Subject")
    String subject;
    @SerializedName("Type")
    String contactUs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContactUs() {
        return contactUs;
    }

    public void setContactUs(String contactUs) {
        this.contactUs = contactUs;
    }

}
