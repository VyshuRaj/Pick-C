package com.prts.pickcustomer.history;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.customviews.ProgressDialog;
import com.prts.pickcustomer.helpers.CredentialManager;
import com.prts.pickcustomer.helpers.DialogBox;
import com.prts.pickcustomer.helpers.ProgressDialogHelper;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.invoice.InvoiceActivity;
import com.prts.pickcustomer.trucks.OnItemClick;

import java.util.ArrayList;
import java.util.List;

import static com.prts.pickcustomer.invoice.InvoiceActivity.BOOKING_NO;

public class BookingHistoryActivity extends AppCompatActivity implements HistoryView, OnItemClick, DialogBox {
    private static final String TAG = "BookingHistoryActivity";
    HistoryViewPresenter mHistoryViewPresenter;
    RecyclerView recyclerView;
    TextView nobookingsTV;
    LinearLayout bookingHistoryLayout;
    List<HistoryData> mHistoryData;
    private String mMobileNumber;
    private BookingAdapter mBookingAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            mHistoryViewPresenter = new HistoryViewPresenetrImpl(BookingHistoryActivity.this, this);
            mMobileNumber = CredentialManager.getMobileNO(BookingHistoryActivity.this);

            if (mMobileNumber != null && mMobileNumber.length() > 0) {
                mHistoryViewPresenter.getBookingHistory(mMobileNumber);
            } else {
                bookingHistoryLayout.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initializeTheViews() {
        try {
            nobookingsTV = findViewById(R.id.no_bookings);
            bookingHistoryLayout = findViewById(R.id.bookingHistoryLayout);

            mHistoryData = new ArrayList<>();
            recyclerView = findViewById(R.id.recyclerView);
            mBookingAdapter = new BookingAdapter(BookingHistoryActivity.this, mHistoryData, this);
            recyclerView.setAdapter(mBookingAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setHasFixedSize(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void noInternet() {
        ToastHelper.noInternet(BookingHistoryActivity.this);
    }

    @Override
    public void setDataToRecylerView(List<HistoryData> historyData) {
        try {
            if (historyData != null && historyData.size() > 0) {
                mHistoryData = historyData;
                bookingHistoryLayout.setVisibility(View.GONE);
                mBookingAdapter = new BookingAdapter(BookingHistoryActivity.this, mHistoryData, this);
                recyclerView.setAdapter(mBookingAdapter);
                recyclerView.invalidate();
                mBookingAdapter.notifyDataSetChanged();
            } else {
                bookingHistoryLayout.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            Log.e(TAG, "Exception" + e.getMessage());
        }
    }


    @Override
    public void noHistoryData(String s) {
        bookingHistoryLayout.setVisibility(View.VISIBLE);
        ToastHelper.showToastLenShort(BookingHistoryActivity.this, s);
    }

    @Override
    public DialogBox getDialogInstance() {
        return this;
    }

    @Override
    public void visibleNoHistroyData() {
        bookingHistoryLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(int pos, View view) {
        try {
            switch (view.getId()) {
                case R.id.cancel_booking_TV:
                    try {
                        mHistoryViewPresenter.getBookingHistory(mMobileNumber);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    try {

                        if (!mHistoryData.get(pos).isCancel()) {
                            String bookingNO = mHistoryData.get(pos).getBookingNo();
                            Intent intent = new Intent(BookingHistoryActivity.this, InvoiceActivity.class);
                            intent.putExtra(BOOKING_NO, bookingNO);
                            intent.putExtra("SendInvoice", "no");
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showDialog(String title) {
        ProgressDialogHelper.showProgressDialog(BookingHistoryActivity.this, title);
    }

    @Override
    public void dismissDialog() {
        ProgressDialogHelper.dismissDialog();
    }
}
