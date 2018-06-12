package com.prts.pickcustomer.booking;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.prts.pickcustomer.R;
import com.prts.pickcustomer.customviews.PickCCustomEditText;
import com.prts.pickcustomer.customviews.PickCCustomTextVIew;
import com.prts.pickcustomer.helpers.AlertDialogActivity;
import com.prts.pickcustomer.helpers.CredentialManager;
import com.prts.pickcustomer.helpers.DateHelper;
import com.prts.pickcustomer.helpers.KeyboardHelper;
import com.prts.pickcustomer.helpers.LogUtils;
import com.prts.pickcustomer.helpers.ProgressDialogHelper;
import com.prts.pickcustomer.helpers.SnackbarHelper;
import com.prts.pickcustomer.helpers.TextToSpeechHelper;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.home.HomeActivity;
import com.prts.pickcustomer.userrating.Feedback;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.prts.pickcustomer.helpers.Constants.DISABLE_ON_BACK_PRESS;
import static com.prts.pickcustomer.helpers.Constants.ENABLE_BOOKING;
import static com.prts.pickcustomer.helpers.Constants.dateSeperator;
import static com.prts.pickcustomer.helpers.Constants.timeSeperator;
import static com.prts.pickcustomer.helpers.SnackbarHelper.showSnackBar;
import static com.prts.pickcustomer.home.HomeActivity.mOnBackPressStatus;


public class BookingFragment extends Fragment implements BookingView, TextToSpeech.OnInitListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    final String[] CANCEL_REMARKS = {"Driver is late", "Changed my mind", "Booked another truck", "Driver denied duty"};
    private static final String TAG = "BookingFragment";
    ProgressBar progressDialog;
    public boolean mIsBookingLater = false;
    HomeActivity activity;
    @BindView(R.id.book_now_textView)
    public PickCCustomTextVIew mBookNow;
    @BindView(R.id.book_later_textView)
    public PickCCustomTextVIew mBookLater;
    @BindView(R.id.book_cancel_textView)
    public PickCCustomTextVIew mCancelBooking;
    @BindView(R.id.book_confirm_textView)
    public PickCCustomTextVIew mConfirmBooking;
    @BindView(R.id.booknow_linear)
    LinearLayout mBookLinearLayout;
    String FINISH_ACTIVITY = "finish";
    BookingPresenter mBookingPresenter;
    private List<CargoType> mCargoTypes;
    private String mCargoTypeDes = "";
    private String mDeliveryMobileNumber = "";
    private BookingListener mListener;
    private String mSelectedDateTime = "";
    private String mCancelRemarks = "";
    private int mCount;
    int selectedYear;
    int selectedMonthOfYear;
    int selectedDayOfMonth;
    private int mSelectedTruckId;
    private boolean mIsConfirmedBookingClicked;
    private Dialog mCargoTypeDialog;
    private String mSelectedDate = "";
    private String mSelectedTime = "";
    private int mSelectedopenClosedId;
    private int mSelectedLoadUnLoadId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);
        try {
            ButterKnife.bind(this, view);

            mBookingPresenter = new BookingPresenterImpl(getContext(), this);
            activity = (HomeActivity) getActivity();


            if (mCancelBooking != null) {
                mCancelBooking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createCancelRemarksDialogAndShow(CredentialManager.getBookingNO(getContext()));
                    }
                });
            }

            if (mBookingPresenter != null) {
                //  SnackbarHelper.showSnackBar(mConfirmBooking, getString(R.string.cargo_types_loading));
                mBookingPresenter.downloadCargoTypes();
            }

        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " onCreateView :- Exception" + e.getMessage());

        }
        return view;
    }


    @OnClick(R.id.book_now_textView)
    public void bookNow(View view) {

        try {

            if (mListener != null && !mListener.checkBothLocation()) {
                return;
            }

            inVisibleCancelBooking();
            inVisibleBookNowAndLater();
            visibleConfirmBooking();
            mIsBookingLater = false;

            if (mCargoTypeDialog != null) {
                mCargoTypeDialog.show();
                try {
                    SnackbarHelper.showSnackBar(mConfirmBooking, getString(R.string.please_sel_cargo));
                    TextToSpeechHelper.speakOut(getActivity(), getString(R.string.please_sel_cargo));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " bookNow :- Exception" + e.getMessage());

        }

    }

    @OnClick(R.id.book_confirm_textView)
    public void confirmBooking(View view) {
        try {
            if (!mIsConfirmedBookingClicked) {

                if (mIsBookingLater) {

                    if (mSelectedDate != null && mSelectedDate.isEmpty()) {
                        HomeActivity.mOnBackPressStatus = ENABLE_BOOKING;
                        showDatePickerDialogWithCurrentDate();
                        return;
                    }

                    if (mSelectedTime != null && mSelectedTime.isEmpty()) {
                        HomeActivity.mOnBackPressStatus = ENABLE_BOOKING;
                        showTimePickerDialogWithCurrentTime();
                        return;
                    }

                    mSelectedDateTime = mSelectedDate + " " + mSelectedTime;
                }

                if (mCargoTypeDes.isEmpty()) {

                    HomeActivity.mOnBackPressStatus = ENABLE_BOOKING;

                    try {
                        if (mCargoTypeDialog != null) {
                            mCargoTypeDialog.show();
                            try {
                                SnackbarHelper.showSnackBar(mConfirmBooking, getString(R.string.please_sel_cargo));
                                TextToSpeechHelper.speakOut(getActivity(), getString(R.string.please_sel_cargo));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return;
                }

                if (mListener != null) {
                    mListener.invisibleRateCardLayout(mIsBookingLater);
                }


                if (mIsBookingLater) {
                    mOnBackPressStatus = ENABLE_BOOKING;
                    inVisibleConfirmBooking();
                } else {
                    visibleCancelBookingLayout();
                }

                view.setEnabled(false);
                mIsConfirmedBookingClicked = true;
                callBookNowAPI();
            } else {
                showSnackBar(mConfirmBooking, getString(R.string.please_wait_proc_booking));
            }

        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " confirmBooking :- Exception" + e.getMessage());

        }
    }


    public void callBookNowAPI() {
        try {
            BookingInfor bookingInfor = new BookingInfor();
            bookingInfor.setBookingNo("");

            if (!mIsBookingLater)
                mSelectedDateTime = DateHelper.getCurrentDate() + " " + DateHelper.getCurrentTime();

            bookingInfor.setRequiredDate(mSelectedDateTime);
            bookingInfor.setBookingDate(mSelectedDateTime);

            if (mDeliveryMobileNumber.length() <= 0) {
                mDeliveryMobileNumber = CredentialManager.getMobileNO(getActivity());
            }

            bookingInfor.setCustomerID(CredentialManager.getMobileNO(getContext()));
            bookingInfor.setReceiverMobileNo(mDeliveryMobileNumber);

            if (HomeActivity.mFromAddress.equalsIgnoreCase(HomeActivity.DefaultSrc)) {
                if (mListener != null) {
                    mListener.latestLocation();
                }
            }

            if (HomeActivity.mToAddress.equalsIgnoreCase(HomeActivity.DefaultDes)) {
                if (mListener != null) {
                    mListener.latestLocation();
                }
            }

            // LogUtils.appendLog(getActivity(), "Final Loc " + HomeActivity.mFromAddress + "\n" + HomeActivity.mToAddress);
            // Log.e(TAG, "Final Loc " + HomeActivity.mFromAddress + "\n" + HomeActivity.mToAddress);

            bookingInfor.setLocationFrom(HomeActivity.mFromAddress);
            bookingInfor.setLocationTo(HomeActivity.mToAddress);
            bookingInfor.setCargoDescription(mCargoTypeDes);
            bookingInfor.setVehicleGroup(mSelectedTruckId);
            bookingInfor.setVehicleType(mSelectedopenClosedId);
            bookingInfor.setRemarks("");
            bookingInfor.setLatitude(HomeActivity.mPickUpLatLng.latitude);
            bookingInfor.setLongitude(HomeActivity.mPickUpLatLng.longitude);
            bookingInfor.setToLatitude(HomeActivity.mDropLatLng.latitude);
            bookingInfor.setToLongitude(HomeActivity.mDropLatLng.longitude);
            bookingInfor.setLoadingUnLoading(mSelectedLoadUnLoadId);

            Log.e(TAG, "JSON OBJ " + new Gson().toJson(bookingInfor));

            LogUtils.appendLog(getActivity(), "Booking Object " + new Gson().toJson(bookingInfor));

            if (mBookingPresenter != null) {
                mBookingPresenter.confirmBooking(bookingInfor);
            }

        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " callBookNowAPI :- Exception" + e.getMessage());

        }
    }

    private void visibleCancelBookingLayout() {
        try {
            mOnBackPressStatus = DISABLE_ON_BACK_PRESS;
            visibleCancelBooking();
            inVisibleConfirmBooking();
            inVisibleBookNowAndLater();
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " visibleCancelBookingLayout :- Exception" + e.getMessage());
        }
    }

    public void cancelBooking(String bookingNo, final String cancelRemarks) {
        try {
            Cancel cancel = new Cancel();
            cancel.setBookingNo(bookingNo);
            cancel.setRemarks(cancelRemarks);
            Log.e(TAG, "cancel " + new Gson().toJson(cancel));

            if (mBookingPresenter != null) {
                mBookingPresenter.cancelBooking(cancel);
            }

        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " cancelBooking :- Exception" + e.getMessage());

        }
    }


    public void blink(final View view, final int noOfTimes) {
        try {
            TextToSpeechHelper.speakOut(getActivity(), getString(R.string.proceed_for_booking));
            mCount = 0;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (view.getVisibility() == View.VISIBLE) {
                        view.setVisibility(View.INVISIBLE);
                    } else {
                        view.setVisibility(View.VISIBLE);
                    }
                    if (mCount < noOfTimes) {
                        final int timeToBlink = 300;    //in millis seconds
                        handler.postDelayed(this, timeToBlink);
                        mCount++;
                    } else {
                        view.setVisibility(View.VISIBLE);
                    }
                }
            }, 1);
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " blink :- Exception" + e.getMessage());

        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        try {
            selectedYear = year;
            selectedMonthOfYear = monthOfYear;
            selectedDayOfMonth = dayOfMonth;
            Date date1 = new Date(selectedYear - 1900, selectedMonthOfYear, selectedDayOfMonth);
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            mSelectedDate = formatter.format(date1);
            showTimePickerDialogWithCurrentTime();
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " onDateSet :- Exception" + e.getMessage());

        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

        try {
            mSelectedTime = hourOfDay + timeSeperator + minute;
            String date = selectedDayOfMonth + dateSeperator + selectedMonthOfYear + dateSeperator + selectedYear;
            Log.d(TAG, "onDateSet: bookLater " + date);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 30);
            long currentDateAndTimeInMilliSeconds = calendar.getTimeInMillis();
            calendar.set(selectedYear, selectedMonthOfYear, selectedDayOfMonth,
                    hourOfDay, minute, 0);
            long selectedDateAndTimeMilliSeconds = calendar.getTimeInMillis();

            if (selectedDateAndTimeMilliSeconds <= currentDateAndTimeInMilliSeconds) {
                // showTimePickerDialogWithCurrentTime();
                // Toast.makeText(activity, R.string.time_les_than_current_time, Toast.LENGTH_LONG).show();
                return;
            }


            mOnBackPressStatus = DISABLE_ON_BACK_PRESS;

        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " onTimeSet :- Exception" + e.getMessage());

        }
    }


    @Override
    public void onInit(int status) {

    }

    public void selectedTruckInformation(int truckId, int openClosedId, int loadUnloadId, int selectedTruckPosition) {
        mSelectedTruckId = truckId;
        mSelectedopenClosedId = openClosedId;
        mSelectedLoadUnLoadId = loadUnloadId;
    }


    public interface BookingListener {
        void visibleRateCard();

        void invisibleRateCardLayout(boolean isBookingLater);

        void setStartProgressTime(boolean isBookingLater);

        boolean checkBothLocation();

        void drawDirectionBetweenSourceToDestination();

        void bookingCancelledSuccessfully();

        void latestLocation();

       // void bookingNoCreated(String bookingNo);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof BookingListener) {
            mListener = (BookingListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void visibleConfirmBookingLayout(boolean isBookingLater) {
        try {

            this.mIsBookingLater = isBookingLater;

            if (mListener != null) {
                mListener.visibleRateCard();
            }
            inVisibleBookNowAndLater();
            inVisibleCancelBooking();
            visibleConfirmBooking();
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " visibleConfirmBookingLayout :- Exception" + e.getMessage());

        }
    }

    public void visibleBookNowAndLater() {

        try {
            if (mBookNow != null)
                mBookNow.setVisibility(View.VISIBLE);
            if (mBookLater != null)
                mBookLater.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " visibleBookNowAndLater :- Exception" + e.getMessage());

        }
    }

    public void visibleConfirmBooking() {

        try {

            HomeActivity.mOnBackPressStatus = ENABLE_BOOKING;
            if (mConfirmBooking != null)
                mConfirmBooking.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " visibleConfirmBooking :- Exception" + e.getMessage());

        }

    }

    public void inVisibleConfirmBooking() {

        try {
            if (mConfirmBooking != null)
                mConfirmBooking.setVisibility(View.GONE);

        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " inVisibleConfirmBooking :- Exception" + e.getMessage());

        }
    }

    public void inVisibleBookNowAndLater() {

        try {
            if (mBookNow != null)
                mBookNow.setVisibility(View.GONE);

            if (mBookLater != null)
                mBookLater.setVisibility(View.GONE);
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " inVisibleBookNowAndLater :- Exception" + e.getMessage());

        }
    }


    public void inVisibleCancelBooking() {
        try {
            if (mCancelBooking != null) {
                mCancelBooking.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " inVisibleCancelBooking :- Exception" + e.getMessage());

        }

    }

    public void visibleCancelBooking() {
        if (mCancelBooking != null)
            mCancelBooking.setVisibility(View.VISIBLE);
    }


    @OnClick(R.id.book_later_textView)
    public void bookLater(View view) {
        try {

            if (mListener != null && !mListener.checkBothLocation()) {
                return;
            }

            visibleConfirmBookingLayout(true);
            mIsBookingLater = true;
            progressDialog = ((HomeActivity) activity).mProgressDialog;
            showDatePickerDialogWithCurrentDate();
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " bookLater :- Exception" + e.getMessage());

        }

    }


    private void showDatePickerDialogWithCurrentDate() {

        try {
            Calendar calendar = Calendar.getInstance();

            int Year = calendar.get(Calendar.YEAR);
            int Month = calendar.get(Calendar.MONTH);
            int Day = calendar.get(Calendar.DAY_OF_MONTH);
            calendar.add(Calendar.DATE, 2);
            DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, Year, Month, Day);
            datePickerDialog.setThemeDark(false);
            datePickerDialog.showYearPickerFirst(false);
            datePickerDialog.setMinDate(Calendar.getInstance());
            datePickerDialog.setMaxDate(calendar);
            datePickerDialog.setAccentColor(Color.parseColor("#009688"));
            datePickerDialog.setTitle(getString(R.string.sel_date));
            datePickerDialog.show(activity.getFragmentManager(), "DatePickerDialog");
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " bookLater :- Exception" + e.getMessage());

        }
    }

    private void showTimePickerDialogWithCurrentTime() {

        try {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR);
            int minute = calendar.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, hour, minute, false);
            timePickerDialog.setThemeDark(false);
            timePickerDialog.setAccentColor(Color.parseColor("#009688"));
            timePickerDialog.setTitle(getString(R.string.sel_time));
            timePickerDialog.show(activity.getFragmentManager(), "DatePickerDialog");
            timePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " showTimePickerDialogWithCurrentTime :- Exception" + e.getMessage());
        }
    }


    public void createCancelRemarksDialogAndShow(final String bookingNo) {

        try {
            final List<Feedback> mFeedback = new ArrayList<>();

            if (CANCEL_REMARKS != null) {

                for (String cancelRemark : CANCEL_REMARKS) {
                    Feedback feedback = new Feedback();
                    feedback.setText(cancelRemark);
                    mFeedback.add(feedback);
                }
            }

            final Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.cancel_booking_layout);
            dialog.setCancelable(false);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            dialog.show();

            PickCCustomTextVIew cancelBtn = (PickCCustomTextVIew) dialog.findViewById(R.id.cancelBookingTV);
            PickCCustomTextVIew doNotCancelBtn = (PickCCustomTextVIew) dialog.findViewById(R.id.donotCancelTV);
            final ListView cancelRemarksListView = (ListView) dialog.findViewById(R.id.listViewCancelDialog);
            final UserCancelBookingAdapter adapter = new UserCancelBookingAdapter(getContext(), mFeedback);
            cancelRemarksListView.setAdapter(adapter);
            cancelRemarksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    boolean isSelected = mFeedback.get(position).isSelected();
                    mFeedback.get(position).setSelected(!isSelected);
                    cancelRemarksListView.invalidate();
                    adapter.notifyDataSetChanged();
                }
            });
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCancelRemarks = "";

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mFeedback.size(); i++) {
                        if (mFeedback.get(i).isSelected()) {
                            sb.append(mFeedback.get(i).getText()).append(",");
                        }
                    }
                    if (sb.length() > 0) {
                        sb.setLength(sb.length() - 1);
                        mCancelRemarks = sb.toString();
                    }


                    if (mCancelRemarks.isEmpty()) {
                        Toast.makeText(activity, R.string.selec_reacson_to_cancel, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    cancelBooking(bookingNo, mCancelRemarks);

                    dialog.dismiss();
                }
            });

            doNotCancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " createCancelRemarksDialogAndShow :- Exception" + e.getMessage());

        }
    }


    @Override
    public void notAbleToCargoTypes() {
        try {
            mOnBackPressStatus = FINISH_ACTIVITY;
            ToastHelper.showToastLenShort(getActivity()
                    , getString(R.string.cargo_types_not_ava));
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " notAbleToCargoTypes :- Exception" + e.getMessage());

        }
    }

    @Override
    public void setCargoTypes(List<CargoType> cargoTypes) {
        try {
            mCargoTypes = cargoTypes;
            if (mCargoTypes != null && mCargoTypes.size() > 0) {
                createAndShowCargoMaterialDialog();
            } else {
                ToastHelper.showToastLenShort(getActivity(), getString(R.string.cargo_types_not_ava));
            }
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " setCargoTypes :- Exception" + e.getMessage());

        }
    }

    @Override
    public void noInternet() {
        try {
            ToastHelper.noInternet(getContext());
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " nointernet :- Exception" + e.getMessage());
        }
    }

    @Override
    public void showProgressDialog() {
        creatDialog(getString(R.string.boo_process));
    }

    void creatDialog(String title) {
        ProgressDialogHelper.showProgressDialog(getContext(), title);
    }

    @Override
    public void dismissDialog() {
        ProgressDialogHelper.dismissDialog();
    }

    @Override
    public void bookingNotCreated(String message) {
        try {
            ToastHelper.showToastLenShort(getActivity(), message);
            mIsConfirmedBookingClicked=false;

            if (mListener != null) {
                mListener.bookingCancelledSuccessfully();
            }
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " bookingNotCreated :- Exception" + e.getMessage());

        }
    }

    @Override
    public void bookingCreated(String bookingNo) {
        try {
            mIsConfirmedBookingClicked=false;
            Log.e(TAG, "Booking Num" + bookingNo);

            if (getActivity()!=null) {
                CredentialManager.setBookingNo(getActivity(), bookingNo);
            }

            if (!mIsBookingLater) {
                if (mListener != null) {
                    mListener.setStartProgressTime(false);
                }

                TextToSpeechHelper.speakOut(getActivity(), getString(R.string.truck_book_success));
                Toast.makeText(getActivity(), R.string.waiting_for_driver, Toast.LENGTH_SHORT).show();

            } else {
                TextToSpeechHelper.speakOut(getActivity(), getString(R.string.truck_schedule));
                AlertDialogActivity.showAlertDialogActivity(getActivity(), getString(R.string.truck_booked),
                        getString(R.string.truck_sc_with) + bookingNo + getString(R.string.at) + mSelectedDateTime + ".", getString(R.string.ok), null,
                        new AlertDialogActivity.OnPositiveBtnClickListener() {
                            @Override
                            public void onPositiveBtnClick() {

                                if (mListener != null)
                                    mListener.bookingCancelledSuccessfully();
                            }
                        }, null);

                ToastHelper.showToastLenShort(getActivity(), getString(R.string.driver_will_arrive) + mSelectedDateTime);
                mIsBookingLater = false;

            }
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " bookingCreated :- Exception" + e.getMessage());

        }

    }

    @Override
    public void bookingCanceled(String s) {
        ToastHelper.showToastLenShort(getActivity(), s);
        try {
            if (mListener != null) {
                mListener.bookingCancelledSuccessfully();
            }

        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " bookingCanceled :- Exception" + e.getMessage());

        }
    }

    @Override
    public void tryAgain(String s) {
        ToastHelper.showToastLenShort(getActivity(), s);
    }

    private void createAndShowCargoMaterialDialog() {
        try {
            mCargoTypeDialog = new Dialog(activity);
            mCargoTypeDialog.setTitle(R.string.sel_ct);
            mCargoTypeDialog.setContentView(R.layout.cargo_types);
            mCargoTypeDialog.setCancelable(false);

            if (mCargoTypeDialog.getWindow() != null) {
                mCargoTypeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }


            final PickCCustomEditText weightET = mCargoTypeDialog.findViewById(R.id.weightEditText);
            ImageView closeIv = mCargoTypeDialog.findViewById(R.id.closeIV);
            Button nextBtn = mCargoTypeDialog.findViewById(R.id.nextBtn);
            ListView cargoTypeListView = mCargoTypeDialog.findViewById(R.id.listView);
            final CargoTypeAdapter cargoTypeAdapter = new CargoTypeAdapter(getContext(), mCargoTypes);
            cargoTypeListView.setAdapter(cargoTypeAdapter);
            cargoTypeListView.setItemsCanFocus(false);

            closeIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCargoTypeDialog.dismiss();
                    HomeActivity.mOnBackPressStatus = ENABLE_BOOKING;
                }
            });
            weightET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        KeyboardHelper.openKeyboard(getContext(), v, false); // false for hiding
                    }
                }
            });
            weightET.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KeyboardHelper.openKeyboard(getContext(), v, true);

                }
            });

            nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCargoTypeDes = "";
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mCargoTypes.size(); i++) {

                        if (mCargoTypes.get(i).isChecked) {
                            sb.append(mCargoTypes.get(i).getDescription()).append(",");
                           // sb.append(mCargoTypes.get(i).getDescription()).append(",");
                        }
                    }

                    if (sb.length() > 0) {
                        sb.setLength(sb.length() - 1);
                    }


                    mCargoTypeDes = sb.toString();
                    Log.e(TAG,"CargoTypeDes "+mCargoTypeDes);


                    if (mCargoTypeDes.isEmpty()) {
                        TextToSpeechHelper.speakOut(getActivity(), getString(R.string.please_sel_cargo));
                        showSnackBar(v, getString(R.string.please_sel_cargo));
                        return;
                    }

                    try {
                        mCargoTypes.get(CargoTypeAdapter.mSelectedPosition).setChecked(false);
                        CargoTypeAdapter.mSelectedPosition = -1;
                        cargoTypeAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e(TAG,"CargoTypeDes Exception "+e.getMessage());
                    }

                    if (!weightET.getText().toString().isEmpty()) {

                        double weight = Double.parseDouble(weightET.getText().toString());

                        if (weight != 0) {
                            switch (mSelectedTruckId) {
                                case 1000:
                                    if (weight > 700) {
                                        TextToSpeechHelper.speakOut(getActivity(), getString(R.string.wt_not_gt_700_kgs));
                                        showSnackBar(weightET, getString(R.string.wt_not_gt_700_kgs));
                                        return;
                                    }
                                    break;
                                case 1001:
                                    if (weight > 1030) {
                                        TextToSpeechHelper.speakOut(getActivity(), getString(R.string.wt_not_gt_1030_kgs));

                                        showSnackBar(weightET, getString(R.string.wt_not_gt_1030_kgs));
                                        return;
                                    }
                                    break;
                                case 1002:
                                    if (weight > 1250) {
                                        TextToSpeechHelper.speakOut(getActivity(), getString(R.string.not_gt_1250));

                                        showSnackBar(weightET, getString(R.string.not_gt_1250));
                                        return;
                                    }
                                    break;
                                case 1003:
                                    if (weight > 2700) {
                                        TextToSpeechHelper.speakOut(getActivity(), getString(R.string.wt_shouldnt_greater_than_2700));
                                        showSnackBar(weightET, getString(R.string.wt_shouldnt_greater_than_2700));
                                        return;
                                    }

                                    break;
                            }

                            mCargoTypeDes = getString(R.string.total) + weight + getString(R.string.tons_with) + mCargoTypeDes + getString(R.string.mat);
                        }
                    }

                    mCargoTypeDialog.dismiss();
                    showDialogToAskMobileNumber();

                    if (mListener != null)
                        mListener.visibleRateCard();
                }
            });
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " createAndShowCargoMaterialDialog :- Exception" + e.getMessage());

        }
    }

    private void showDialogToAskMobileNumber() {
        try {
            final Dialog dialog = new Dialog(activity);
            dialog.setContentView(R.layout.ask_mobile_number);
            dialog.setCancelable(false);

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

            dialog.show();
            TextToSpeechHelper.speakOut(getActivity(), getString(R.string.use_moble));
            final PickCCustomEditText mobileNoET = (PickCCustomEditText) dialog.findViewById(R.id.mobileNoEditText);
            mobileNoET.setText(CredentialManager.getMobileNO(getActivity()));
            Button okBtn = (Button) dialog.findViewById(R.id.nextBtn);
            final CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.checkBox);
            selectSameMoileNoCheckBox(checkBox, mobileNoET);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    Log.e(TAG, "onCheckedChanged: b " + isChecked);
                    if (isChecked) {
                        selectSameMoileNoCheckBox(checkBox, mobileNoET);
                    } else {
                        unSelectSameMoileNoCheckBox(checkBox, mobileNoET);
                    }
                }
            });
            okBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        mDeliveryMobileNumber = CredentialManager.getMobileNO(getContext());
                    } else {
                        String mobileNo = mobileNoET.getText().toString();

                        if (mobileNo.isEmpty() || mobileNo.length() < 10) {
                            showSnackBar(v, getString(R.string.enter_10_digi));
                            return;
                        }

                        mDeliveryMobileNumber = mobileNo;
                    }
                    mOnBackPressStatus = ENABLE_BOOKING;
                    dialog.dismiss();

                    TextToSpeechHelper.speakOut(getActivity(), getString(R.string.please_confirm_booking));

                    if (mListener != null)
                        mListener.drawDirectionBetweenSourceToDestination();

                }
            });
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " showDialogToAskMobileNumber :- Exception" + e.getMessage());

        }
    }

    private void selectSameMoileNoCheckBox(CheckBox checkBox, PickCCustomEditText mobileNoET) {
        try {
            checkBox.setChecked(true);
            mobileNoET.setEnabled(false);
            mobileNoET.setText(CredentialManager.getMobileNO(getContext()));
            Log.e(TAG, "selectSameMoileNoCheckBox: ");
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " selectSameMoileNoCheckBox :- Exception" + e.getMessage());

        }
    }

    private void unSelectSameMoileNoCheckBox(CheckBox checkBox, PickCCustomEditText mobileNoET) {
        try {
            checkBox.setChecked(false);
            mobileNoET.setEnabled(true);
            mobileNoET.requestFocus();
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " unSelectSameMoileNoCheckBox :- Exception" + e.getMessage());

        }
    }


}