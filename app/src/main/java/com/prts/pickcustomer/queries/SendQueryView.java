package com.prts.pickcustomer.queries;

/**
 * Created by LOGICON on 31-12-2017.
 */

public interface SendQueryView {
    void noInternet();
    void enterName(String name);

    void enterEmail(String s);

    void enterMobileNumber(String s);

    void enterSuject(String s);

    void enterQuery(String s);

    void showProgressDialog();

    void dismissDialog();

    void tryAgain(String string);

    void getToPreviousPage();

    void proceedFurther();
}
