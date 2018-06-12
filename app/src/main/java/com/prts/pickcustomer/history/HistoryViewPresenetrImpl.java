package com.prts.pickcustomer.history;

import android.content.Context;
import android.util.Log;

import com.prts.pickcustomer.helpers.ActivityHelper;
import com.prts.pickcustomer.helpers.DialogBox;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.restapi.RestClient;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prts.pickcustomer.helpers.CredentialManager.getHeaders;
import static com.prts.pickcustomer.restapi.RestAPIConstants.BASE_URL;
import static com.prts.pickcustomer.restapi.RestAPIConstants.TIME_OUT;

/**
 * Created by satya on 20-Dec-17.
 */

public class HistoryViewPresenetrImpl implements HistoryViewPresenter {
    private Context mContext;
    private HistoryView mHistoryView;
    private DialogBox mDialogBox;

    HistoryViewPresenetrImpl(Context bookingHistoryActivity, HistoryView bookingHistoryActivity1) {
        mContext = bookingHistoryActivity;
        mHistoryView = bookingHistoryActivity1;
        mDialogBox = mHistoryView.getDialogInstance();
        mHistoryView.initializeTheViews();
    }


    @Override
    public void getBookingHistory(final String mobileNumber) {

        if (!NetworkHelper.hasNetworkConnection(mContext)) {
            mHistoryView.noInternet();
            return;
        }
        mDialogBox.showDialog("Getting booking history data");
        RestClient.getRestService(BASE_URL).getBookingHistory(getHeaders(mContext), mobileNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<HistoryData>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<HistoryData> historyData) {
                        if (historyData != null && historyData.size() > 0) {
                            Log.e("TAG", "From " + historyData.get(0).getLocationFrom());
                            Log.e("TAG", "To " + historyData.get(0).getLocationTo());
                            mDialogBox.dismissDialog();
                            mHistoryView.setDataToRecylerView(historyData);
                        } else {
                            mHistoryView.visibleNoHistroyData();
                            mDialogBox.dismissDialog();
                            mHistoryView.noHistoryData("Booking history is not available");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            mDialogBox.dismissDialog();
                            String message = e.getMessage();
                            if (message.contains(TIME_OUT)) {
                                getBookingHistory(mobileNumber);
                            } else {
                                ActivityHelper.handleException(mContext, message);
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }
}
