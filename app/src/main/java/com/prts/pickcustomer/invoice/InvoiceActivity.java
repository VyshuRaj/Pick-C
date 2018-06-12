package com.prts.pickcustomer.invoice;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.prts.pickcustomer.R;
import com.prts.pickcustomer.customviews.PickCCustomEditText;
import com.prts.pickcustomer.helpers.CredentialManager;
import com.prts.pickcustomer.helpers.ProgressDialogHelper;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.helpers.ValidationHelper;
import com.prts.pickcustomer.helpers.ViewHelper;
import com.prts.pickcustomer.queries.SendingQueriesToPickCActivity;
import com.prts.pickcustomer.userrating.UserRatingActivity;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.prts.pickcustomer.helpers.SnackbarHelper.showSnackBar;

public class InvoiceActivity extends AppCompatActivity implements InvoiceView {
    private static final String TAG = "InvoiceActivity";
    public static final String BOOKING_NO = "bookingNo";
    public static final String DRIVERID = "driver";
    boolean disableOnBackPressed = false;
    TextView loadingUnloadTV;
    ImageView iv_map;
    @BindView(R.id.dteOfInvoice)
    TextView dteOfInvoiceTV;
    @BindView(R.id.customerName)
    TextView customerNameTV;
    @BindView(R.id.crnNumber)
    TextView crnNumberTV;
    @BindView(R.id.totalBilledAmountTV)
    TextView totalBilledAmountTV;
    @BindView(R.id.baseFareInKms)
    TextView baseFareInKms;
    @BindView(R.id.baseFareFor4KmTV)
    TextView baseFareFor4KmTV;
    @BindView(R.id.distanceFareforTenKmsTV)
    TextView distanceFareforTenKmsTV;
    @BindView(R.id.distanceForKms)
    TextView distanceForKms;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.travelTimeFareForMinTV)
    TextView travelTimeFareForMinTV;
    @BindView(R.id.totalFareTV)
    TextView totalFareTV;
    @BindView(R.id.loadingAndUnloadingChargesTV)
    TextView loadingAndUnloadingChargesTV;
    @BindView(R.id.waitingNightTimeChargesTV)
    TextView waitingNightTimeChargesTV;
    @BindView(R.id.paymentTypeTV)
    TextView paymentTypeTV;
    @BindView(R.id.otherChargesTV)
    TextView otherChargesTV;
    @BindView(R.id.otherChargesTotalTV)
    TextView otherChargesTotalTV;
    @BindView(R.id.serviceTaxTV)
    TextView serviceTaxTV;
    @BindView(R.id.totalBillAmountTV)
    TextView totalBillAmountTV;
    @BindView(R.id.totalTripKilometersTV)
    TextView totalTripKilometersTV;
    @BindView(R.id.totalTripTimeTV)
    TextView totalTripTimeTV;
    @BindView(R.id.driverIdWithName)
    TextView driverIdWithName;
    @BindView(R.id.driverIdWithoutName)
    TextView driverIdWithoutName;
    @BindView(R.id.vehicleTypeTV)
    TextView vehicleTypeTV;
    @BindView(R.id.vehicleCategoryTV)
    TextView vehicleCategoryTV;
    @BindView(R.id.sourceTimeTV)
    TextView sourceTimeTV;
    @BindView(R.id.sourceAddressTV)
    TextView sourceAddressTV;
    @BindView(R.id.destinationTimeTV)
    TextView destinationTimeTV;
    @BindView(R.id.destinationAddressTV)
    TextView destinationAddressTV;
    @BindView(R.id.rateDriverBut)
    Button rateDriverBut;
    @BindView(R.id.imagestar)
    ImageView imagestar;
    InvoicePresenter mInvoicePresenter;
    String driverID;
    String bookingNo;
    String sendEmailForCustomer;
    private boolean mIsInvoiseSent;
    private UserTripInvoice mUserInvoiceDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        mInvoicePresenter = new InovicePresnterImp(InvoiceActivity.this, this);

        try {
            Intent intent = getIntent();
            bookingNo = intent.getStringExtra(BOOKING_NO);
            sendEmailForCustomer = intent.getStringExtra("SendInvoice");
        } catch (Exception e) {
            e.printStackTrace();
        }

        mInvoicePresenter.getTheInvoceDetails(bookingNo);
    }

    public void contactUs(View view) {
        try {
            startActivity(new Intent(getApplicationContext(), SendingQueriesToPickCActivity.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void emailInvoice(View view) {
        try {
            String email = CredentialManager.getEmailId(getApplicationContext());
            createAndShowDeliveryPersonMobileNumberDialog(email);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rateTheDriver(View view) {
        try {

            Intent driverRating = new Intent(getApplicationContext(), UserRatingActivity.class);
            driverRating.putExtra(DRIVERID, driverID);
            driverRating.putExtra("amount", String.format("₹ %s", mUserInvoiceDetails.getTotalBillAmount()));
            driverRating.putExtra("truckType", String.format("%s - %s Truck", mUserInvoiceDetails.getVehicleGroup(), mUserInvoiceDetails.getVehicleType()));
            driverRating.putExtra("fromLoc", mUserInvoiceDetails.getLocationFrom());
            driverRating.putExtra("toLoc", mUserInvoiceDetails.getLocationTo());
            driverRating.putExtra("driverName", mUserInvoiceDetails.getDriverName());

            startActivity(driverRating);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void createAndShowDeliveryPersonMobileNumberDialog(final String email) {
        try {
            final Dialog dialog = new Dialog(InvoiceActivity.this);
            dialog.setContentView(R.layout.enter_reciver_email);
            dialog.setCancelable(false);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            }
            dialog.show();


            final PickCCustomEditText mobileNoET = (PickCCustomEditText) dialog.findViewById(R.id.mobileNoEditText);
            mobileNoET.setText(email);
            Button okBtn = dialog.findViewById(R.id.nextBtn);
            final CheckBox checkBox = dialog.findViewById(R.id.checkBox);
            selectSameMoileNoCheckBox(checkBox, mobileNoET);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        selectSameMoileNoCheckBox(checkBox, mobileNoET);
                    } else {
                        unSelectSameMoileNoCheckBox(checkBox, mobileNoET);
                    }

                }
            });
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String enteredEmail = email;
                        if (!checkBox.isChecked()) {
                            enteredEmail = ViewHelper.getString(mobileNoET);

                            if (enteredEmail.isEmpty()) {
                                showSnackBar(v, getString(R.string.enter_email));
                                return;
                            }

                            if (!ValidationHelper.isValidEmail(enteredEmail)) {
                                showSnackBar(v, getString(R.string.valid_email));
                                return;
                            }
                        }
                        dialog.dismiss();
                        disableOnBackPressed = true;
                        mInvoicePresenter.sendInoviceEmail(enteredEmail, bookingNo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void selectSameMoileNoCheckBox(CheckBox checkBox, PickCCustomEditText mobileNoET) {
        checkBox.setChecked(true);
        mobileNoET.setEnabled(false);
    }

    private void unSelectSameMoileNoCheckBox(CheckBox checkBox, PickCCustomEditText mobileNoET) {
        checkBox.setChecked(false);
        mobileNoET.setEnabled(true);
        mobileNoET.requestFocus();
    }

    @Override
    public void noInterNet() {
        ToastHelper.noInternet(InvoiceActivity.this);
    }

    @Override
    public void showProgressDialog(String string) {
        ProgressDialogHelper.showProgressDialog(InvoiceActivity.this, string);
    }

    @Override
    public void dismissDialog() {
        ProgressDialogHelper.dismissDialog();
    }

    @Override
    public void messageSent(String s) {
        disableOnBackPressed = false;
        mIsInvoiseSent = true;
        //  ToastHelper.showToastLenShort(InvoiceActivity.this, s);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setInoviceDetails(UserTripInvoice userTripInvoice) {

        try {
            mUserInvoiceDetails = userTripInvoice;
            Log.e(TAG, "userTripInvoice " + userTripInvoice);
            driverID = userTripInvoice.getDriverID();
            dteOfInvoiceTV.setText(userTripInvoice.getInvoiceDate());
            customerNameTV.setText(String.format(" %s", userTripInvoice.getCustomerName()));
            crnNumberTV.setText(" " + userTripInvoice.getBookingNo());
            totalBilledAmountTV.setText("₹ " + userTripInvoice.getTotalBillAmount());
            baseFareInKms.setText("Base Fare for " + userTripInvoice.getBaseKM() + " km");
            baseFareFor4KmTV.setText("₹ " + userTripInvoice.getBaseFare());
            distanceFareforTenKmsTV.setText("₹ " + userTripInvoice.getDistanceFare());
            distanceForKms.setText("Distance Fare for " + userTripInvoice.getDistanceKM() + " km");
            textView3.setText("Travel Time Fare for " + userTripInvoice.getTravelTime() + " Mins");
            travelTimeFareForMinTV.setText("₹ " + userTripInvoice.getTravelTimeFare());
            totalFareTV.setText("₹ " + userTripInvoice.getTotalFare());
            loadingAndUnloadingChargesTV.setText("₹ " + userTripInvoice.getLoadingUnLoadingCharges());
            waitingNightTimeChargesTV.setText("₹ " + userTripInvoice.getWaitingCharges());
            otherChargesTV.setText("₹ " + userTripInvoice.getOtherCharges());
            otherChargesTotalTV.setText("₹ " + userTripInvoice.getTotalAmount());
            serviceTaxTV.setText("₹ " + userTripInvoice.getGSTTax());
            totalBillAmountTV.setText("₹ " + userTripInvoice.getTotalBillAmount());
            totalTripKilometersTV.setText(userTripInvoice.getTotalDistanceKm());
            totalTripTimeTV.setText(String.valueOf(userTripInvoice.getTravelTime()));
            driverIdWithName.setText(userTripInvoice.getDriverName());
            driverIdWithoutName.setText(driverID);
            vehicleTypeTV.setText(userTripInvoice.getVehicleType());
            sourceAddressTV.setText(userTripInvoice.getLocationFrom());
            destinationAddressTV.setText(userTripInvoice.getLocationTo());
            sourceTimeTV.setText(userTripInvoice.getStartTime());
            destinationTimeTV.setText(userTripInvoice.getEndTime());
            vehicleCategoryTV.setText(userTripInvoice.getVehicleGroup());
            paymentTypeTV.setText(userTripInvoice.getPaymentType());
            setLoadingUnloadingTextview(Integer.parseInt(userTripInvoice.getLoadingUnloading()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        double fromLatitude = 0.0;
        double fromLongitude = 0.0;
        double toLatitude = 0.0;
        double toLongitude = 0.0;

        try {
            try {

                fromLatitude = Double.parseDouble(userTripInvoice.getFromLatitute() == null ? "0.0" : userTripInvoice.getFromLatitute());
                fromLongitude = Double.parseDouble(userTripInvoice.getFromLongitude() == null ? "0.0" : userTripInvoice.getFromLongitude());
                toLatitude = Double.parseDouble(userTripInvoice.getToLatitute() == null ? "0.0" : userTripInvoice.getToLatitute());
                toLongitude = Double.parseDouble(userTripInvoice.getToLongitude() == null ? "0.0" : userTripInvoice.getToLongitude());
                iv_map = findViewById(R.id.iv_map);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            String ll = fromLatitude + "," + fromLongitude;
            String ln = toLatitude + "," + toLongitude;

            String STATIC_MAP_API_ENDPOINT = "http://maps.googleapis.com/maps/api/staticmap?size=640x400&path=";
            String marker_src = "color:0x006400|label:|" + ll;
            String marker_dest = "color:0xFF0000|label:|" + ln;
            String path = null;

            try {
                marker_src = URLEncoder.encode(marker_src, "UTF-8");
                marker_dest = URLEncoder.encode(marker_dest, "UTF-8");
                path = "weight:0|color:blue|geodesic:false|" + ll + "|" + ln;
                path = URLEncoder.encode(path, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            STATIC_MAP_API_ENDPOINT = STATIC_MAP_API_ENDPOINT + path + "&markers=" + marker_src + "&markers=" + marker_dest;
            Picasso.with(InvoiceActivity.this).load(STATIC_MAP_API_ENDPOINT).into(iv_map);

            sendEmaitoCustomer();

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void invoiceDetailsNotAvailable(String s) {
        ToastHelper.showToastLenShort(InvoiceActivity.this, s);
    }

    private void sendEmaitoCustomer() {

        {
            if (sendEmailForCustomer.contains("no")) {
                rateDriverBut.setVisibility(View.GONE);
                imagestar.setVisibility(View.GONE);
            }

            if (sendEmailForCustomer.contains("yes")) {
                disableOnBackPressed = true;
                mInvoicePresenter.sendInoviceEmail(CredentialManager.getEmailId(InvoiceActivity.this), bookingNo);
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (disableOnBackPressed) {
            if (sendEmailForCustomer.contains("no")) {
                super.onBackPressed();
            }
            Toast.makeText(this, "Please rate the driver", Toast.LENGTH_SHORT).show();
        } else {
            if (sendEmailForCustomer.contains("no")) {
                super.onBackPressed();
            } else {
                Toast.makeText(this, "Please rate the driver", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void setLoadingUnloadingTextview(int loadingCode) {
        loadingUnloadTV = (TextView) findViewById(R.id.loadingUnloadTV);

        switch (loadingCode) {
            case 1370:
                loadingUnloadTV.setText(R.string.loading_charges);
                break;
            case 1371:
                loadingUnloadTV.setText(R.string.unloading_charges);
                break;
            case 1372:
                loadingUnloadTV.setText(R.string.loading_unloading_charges);
                break;
            case 1373:
                loadingUnloadTV.setText(R.string.loading_unloading_charges);
                break;

        }
    }


}
