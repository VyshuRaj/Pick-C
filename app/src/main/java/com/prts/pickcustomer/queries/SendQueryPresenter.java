package com.prts.pickcustomer.queries;

/**
 * Created by LOGICON on 31-12-2017.
 */

public interface SendQueryPresenter {
    void validateTheFields(Query quey);

    boolean isValidMobileNumber(String mobile);
    boolean isValidEmailCheck(String email);
}
