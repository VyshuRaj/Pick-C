package com.prts.pickcustomer.invoice;

import android.content.Context;
import com.prts.pickcustomer.R;
import com.prts.pickcustomer.helpers.ActivityHelper;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.restapi.RestClient;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prts.pickcustomer.helpers.CredentialManager.getHeaders;
import static com.prts.pickcustomer.restapi.RestAPIConstants.BASE_URL;

public class InovicePresnterImp implements InvoicePresenter {
    private Context mContext;
    private InvoiceView mInvoiceView;

    InovicePresnterImp(Context invoiceActivity, InvoiceView invoiceActivity1) {
        mContext = invoiceActivity;
        mInvoiceView = invoiceActivity1;
    }

    @Override
    public void sendInoviceEmail(final String emailIdentered, final String bookingNo) {

        if (!NetworkHelper.hasNetworkConnection(mContext)) {
            mInvoiceView.noInterNet();
            return;
        }

       // mInvoiceView.showProgressDialog(mContext.getString(R.string.sending_mail));

        RestClient.getRestService(BASE_URL).sendInvoiceMail(bookingNo, emailIdentered)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean isSent) {
                        mInvoiceView.dismissDialog();

                        if (isSent) {
                            mInvoiceView.messageSent(mContext.getString(R.string.invoice_sent_mail));
                        } else {
                            mInvoiceView.invoiceDetailsNotAvailable("Sending invoice details failed");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        try {
                            mInvoiceView.dismissDialog();
                            ActivityHelper.handleException(mContext, String.valueOf(e.getMessage()));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void getTheInvoceDetails(final String bookingNumber) {

        if (!NetworkHelper.hasNetworkConnection(mContext)) {
            mInvoiceView.noInterNet();
            return;
        }

        mInvoiceView.showProgressDialog("Generating Your Invoice");

        RestClient.getRestService(BASE_URL).getUserInvoiceDetails(getHeaders(mContext), bookingNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserTripInvoice>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserTripInvoice userTripInvoice) {
                        mInvoiceView.dismissDialog();
                        mInvoiceView.setInoviceDetails(userTripInvoice);
                    }

                    @Override
                    public void onError(Throwable e) {

                        try {
                            mInvoiceView.dismissDialog();
                            ActivityHelper.handleException(mContext, e.getMessage());

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
