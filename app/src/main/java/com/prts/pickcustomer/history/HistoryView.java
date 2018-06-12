package com.prts.pickcustomer.history;

import com.prts.pickcustomer.helpers.DialogBox;

import java.util.List;

/**
 * Created by satya on 20-Dec-17.
 */

public interface HistoryView {
    void initializeTheViews();
    void noInternet();

    void setDataToRecylerView(List<HistoryData> historyData);

    void noHistoryData(String s);

    DialogBox getDialogInstance();

    void visibleNoHistroyData();
}
