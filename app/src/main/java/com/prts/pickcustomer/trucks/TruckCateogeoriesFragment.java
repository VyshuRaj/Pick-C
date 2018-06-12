package com.prts.pickcustomer.trucks;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.prts.pickcustomer.R;
import com.prts.pickcustomer.customviews.PickCCustomTextVIew;
import com.prts.pickcustomer.helpers.Constants;
import com.prts.pickcustomer.helpers.CredentialManager;
import com.prts.pickcustomer.helpers.DeviceResolutionHelper;
import com.prts.pickcustomer.helpers.LogUtils;
import com.prts.pickcustomer.helpers.SnackbarHelper;
import com.prts.pickcustomer.helpers.TextToSpeechHelper;
import com.prts.pickcustomer.helpers.ToastHelper;
import com.prts.pickcustomer.home.HomeActivity;
import com.prts.pickcustomer.zoom.ZoomPictureActivity;
import com.squareup.picasso.Picasso;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.prts.pickcustomer.restapi.RestAPIConstants.WEB_API_ADDRESS;


/**
 * A simple {@link Fragment} subclass.
 */
public class TruckCateogeoriesFragment extends Fragment implements Constants, TextToSpeech.OnInitListener, TruckListView, OnItemClick {
    TruckViewPresenetr mTruckViewPresenetr;
    @BindView(R.id.confirm_booking_rateCardLayout)
    LinearLayout mTripEstimateRateCardLL;
    @BindView(R.id.recylerView)
    RecyclerView mRecylerView;
    @BindView(R.id.loadingTV_2)
    TextView mLoading;
    @BindView(R.id.unlodingTV_2)
    TextView mUnLoading;
    @BindView(R.id.loadingImageView)
    ImageView mLoadingImage;
    List<VehicleType> mOpenClosedTypes;
    List<VehicleGroupType> mVehicleTypes;
    private static final int LOADING_SELECTED = 1370;
    private static final int UN_LOADING_SELECTED = 1371;
    private static final int ALL_SELECTED = 1372;
    private static final int NONE_SELECTED = 1373;
    private int loadingIVSelectedStatus = NONE_SELECTED;
    private static final String TAG = "TCF";
    PopupWindow mPopupWindow;
    boolean loadingStatus[] = {false, false};
    private static final float UN_SELECTED_FLOAT_VALUE = 0.3f;
    private static final float SELECTED_FLOAT_VALUE = 1.0f;
    private int mSelectedVehicleOpenOrCloseId = 1300;
    private int mSelectedTruckId = 1000;
    private OnTruckSelectedListner mListener;
    private int mIntSelectedTruckPosition = 0;
    @BindView(R.id.rateCardTV_confirmBookingLayout)
    PickCCustomTextVIew mRateCard;
    @BindView(R.id.rideEstimateTV_confirmBookingLayout)
    PickCCustomTextVIew mRideEstimate;
    @BindView(R.id.truck_categeories_top_linearLayout)
    LinearLayout mTruckMainLayout;
    private Dialog mRateCardDialog;
    private Dialog mTripEstimateDialog;
    private ImageView mRateCardIV;
    private PickCCustomTextVIew baseFareTV;
    private PickCCustomTextVIew baseKM_TV;
    private PickCCustomTextVIew rideTimeFareTV;
    private PickCCustomTextVIew truckDescRateTV;
    private ImageView closeRateCardDialogIV;
    private String mStrOpenCloseDes;
    private String mStrTrucksDesc;
    private PopupWindow popupWindow;
    @BindView(R.id.gridView)
    GridView mGridView;
    private TrucksGridAdapter mTrucksGridAdapter;
    private EditText mPickUpLocation;
    private EditText mDropLocation;
    private TextView mMinFare;
    private TextView mMaxFare;
    private PickCCustomTextVIew mLoadingTv;
    private PickCCustomTextVIew mEstimatedTravelTime;
    private PickCCustomTextVIew mEstimatedDistance;
    private static final String estimateTravelTimeText = "Approximate travel time: ";
    private static final String estimateTravelDistanceText = "Approximate travel distance: ";
    private LinearLayout mLinearLayout;
    private int mSelectedPos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_truck_cateogeories, container, false);
            ButterKnife.bind(this, view);
            loadingIVSelectedStatus = NONE_SELECTED;
            mRecylerView.setHasFixedSize(true);
            mRecylerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

            mLoadingImage.setImageResource(getLoadingIconBasedOnSelection(loadingIVSelectedStatus, true));
            mLoadingImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupWindow = showLoadUnloadPopup();
                    mPopupWindow.setWidth(DeviceResolutionHelper.getDeviceWidth(getActivity()).x / 2);
                    mPopupWindow.showAsDropDown(v, 0, -(v.getHeight() * 3) - (v.getHeight() / 2));

                    mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            mListener.onTruckSelected(mSelectedTruckId, mSelectedVehicleOpenOrCloseId, loadingIVSelectedStatus, mSelectedPos);
                        }
                    });
                }
            });

            try {
                createRateCardDialog();
                createTripEstmateDialog();
                mTruckViewPresenetr = new TVPresenterImpl(getContext(), this);
                mTruckViewPresenetr.getTrucksFromServer();
                mListener.onTruckSelected(mSelectedTruckId, mSelectedVehicleOpenOrCloseId, loadingIVSelectedStatus, mSelectedPos);
                mTruckViewPresenetr.getSelectedRateCard(mSelectedVehicleOpenOrCloseId, mSelectedTruckId);
                mTruckMainLayout.setVisibility(View.VISIBLE);
                mTripEstimateRateCardLL.setVisibility(GONE);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }


    @OnClick(R.id.rateCardTV_confirmBookingLayout)
    public void showRateCardDialog(View view) {

        try {
            if (mRateCardDialog != null)
                mRateCardDialog.show();
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " showRateCardDialog :- Exception" + e.getMessage());

        }

    }

    @SuppressLint("CutPasteId")
    private void createRateCardDialog() {

        try {
            if (mRateCardDialog == null) {
                mRateCardDialog = new Dialog(getActivity());
                mRateCardDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                if (mRateCardDialog.getWindow() != null) {
                    mRateCardDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                }

                mRateCardDialog.setContentView(R.layout.show_rate_card);
                mRateCardIV = mRateCardDialog.findViewById(R.id.rateCardImageView);
                baseFareTV = mRateCardDialog.findViewById(R.id.min_base_fare_textView);
                baseKM_TV = mRateCardDialog.findViewById(R.id.per_km_fare_textView);
                rideTimeFareTV = mRateCardDialog.findViewById(R.id.ride_time_fare_textView);
                truckDescRateTV = mRateCardDialog.findViewById(R.id.truck_desc_rateCardDialog);
                closeRateCardDialogIV = mRateCardDialog.findViewById(R.id.close_rate_card_imageVIew);
                mLinearLayout = mRateCardDialog.findViewById(R.id.image_zoom);
            }

            try {
                mLinearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(getActivity(), ZoomPictureActivity.class);
                            intent.putExtra("selectedTruckID", mSelectedTruckId);
                            intent.putExtra("selectedvehicleTypeID", mSelectedVehicleOpenOrCloseId);
                            startActivity(intent);
                        } catch (Exception e) {
                            LogUtils.appendLog(getActivity(), TAG + " showRateCardDialog :- Exception" + e.getMessage());

                        }
                    }
                });
            } catch (Exception e) {
                LogUtils.appendLog(getActivity(), TAG + " createRateCardDialog :- Exception" + e.getMessage());

            }


            try {
                closeRateCardDialogIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRateCardDialog.dismiss();
                    }
                });
            } catch (Exception e) {
                LogUtils.appendLog(getActivity(), TAG + " createRateCardDialog :- Exception" + e.getMessage());

            }
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " createRateCardDialog :- Exception" + e.getMessage());

        }
    }

    @SuppressLint("CutPasteId")
    private void createTripEstmateDialog() {

        try {
            if (mTripEstimateDialog == null) {
                mTripEstimateDialog = new Dialog(getActivity());
                mTripEstimateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                if (mTripEstimateDialog.getWindow() != null) {
                    mTripEstimateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                }
                mTripEstimateDialog.setContentView(R.layout.activity_ride_estimate);
                mPickUpLocation = mTripEstimateDialog.findViewById(R.id.from_lcation_textView);
                mDropLocation = mTripEstimateDialog.findViewById(R.id.to_lcation_textView);
                mMinFare = mTripEstimateDialog.findViewById(R.id.min_est_fare_tv);
                mMaxFare = mTripEstimateDialog.findViewById(R.id.max_est_fare_tv);
                mLoadingTv = mTripEstimateDialog.findViewById(R.id.LoadingStatus);
                mEstimatedTravelTime = mTripEstimateDialog.findViewById(R.id.estimateTravelTimeTV);
                mEstimatedDistance = mTripEstimateDialog.findViewById(R.id.estimateTravelDistanceTV);
            }
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " createTripEstmateDialog :- Exception" + e.getMessage());

        }
    }

    private void loadingStatusconfirming(int loadingCode) {

        String loadingStatus = getString(R.string.none);

        switch (loadingCode) {
            case 1370:
                loadingStatus = getString(R.string.lstatus);
                // mLoadingTv.setText(R.string.lstatus);
                break;
            case 1371:
                loadingStatus = getString(R.string.ulstatus);
                //  mLoadingTv.setText(R.string.ulstatus);
                break;
            case 1372:
                loadingStatus = getString(R.string.lulsttatus);
                // mLoadingTv.setText(R.string.lulsttatus);
                break;
            case 1373:
                loadingStatus = getString(R.string.none);
                // mLoadingTv.setText(R.string.none);
                break;
        }


        mLoadingTv.setText(loadingStatus);

    }

    @OnClick(R.id.rideEstimateTV_confirmBookingLayout)
    public void showEstimate(View view) {

        try {
            if (mTripEstimateDialog != null) {
                mTripEstimateDialog.show();
            }
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " showEstimate :- Exception" + e.getMessage());

        }
    }


    @Override
    public void setDefaultTrucks(List<VehicleGroupType> truck) {
        try {
            if (truck != null) {
                if (mVehicleTypes == null) {
                    mVehicleTypes = truck;
                } else {
                    for (int i = 0; i < truck.size(); i++) {
                        mVehicleTypes.get(i).setLookupID(truck.get(i).getLookupID());
                        mVehicleTypes.get(i).setLookupCode(truck.get(i).getLookupCode());
                        mVehicleTypes.get(i).setLookupCategory(truck.get(i).getLookupCategory());
                        mVehicleTypes.get(i).setLookupDescription(truck.get(i).getLookupDescription());
                    }
                }
                try {
                    setAdapterToGridView();
                } catch (Exception e) {
                    LogUtils.appendLog(getActivity(), TAG + " setDefaultTrucks :- Exception" + e.getMessage());

                }


            }
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " setDefaultTrucks :- Exception" + e.getMessage());

        }

    }

    @Override
    public void setDefaultOpenClosed(List<VehicleType> openClosedList) {
        try {
            if (mOpenClosedTypes == null) {
                mOpenClosedTypes = openClosedList;
            } else {
                for (int i = 0; i < openClosedList.size(); i++) {
                    mOpenClosedTypes.get(i).setLookupID(openClosedList.get(i).getLookupID());
                    mOpenClosedTypes.get(i).setLookupCode(openClosedList.get(i).getLookupCode());
                }
            }

            mStrOpenCloseDes = mOpenClosedTypes.get(0).getLookupDescription();
            mStrTrucksDesc = mVehicleTypes.get(0).getLookupDescription();
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " setDefaultOpenClosed :- Exception" + e.getMessage());

        }
    }

    @Override
    public void noInternet() {
        ToastHelper.noInternet(getContext());
    }

    @Override
    public void trucksNotavailable(String s) {
        ToastHelper.showToastLenShort(getActivity(), s);
    }

    @Override
    public void setRateCardData(RateCard rateCard) {
        try {
            if (rateCard != null) {
                setDataToViews(rateCard);
            }
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " setRateCardData :- Exception" + e.getMessage());

        }
    }

    @Override
    public void setTripEstimateData(TripEstimateRes response) {
        List<TripEstimateForCustomerItem> data = response.getTripEstimateForCustomer();

        Log.e(TAG, "TripEstimateForCustomerItem" + new Gson().toJson(data));


        if (data != null) {
            mPickUpLocation.setText(HomeActivity.mFromAddress);
            mDropLocation.setText(HomeActivity.mToAddress);

            Log.e(TAG, "loadingIVSelectedStatus " + loadingIVSelectedStatus);
            loadingStatusconfirming(loadingIVSelectedStatus);

            for (int i = 0; i < data.size(); i++) {
                double minD = data.get(i).getTotalTripEstimateminValue();
                double maxD = data.get(i).getTotalTripEstimatemaxValue();
                String minsStr = rupee + " " + minD;
                String maxsStr = rupee + " " + maxD;
                mMinFare.setText(minsStr);
                mMaxFare.setText(maxsStr);
                String tripDistance = String.valueOf(data.get(i).getApproximateDistanceKM());
                String tripDuration = String.valueOf(data.get(i).getApproximateTime());
                mEstimatedDistance.setText(String.format("* %s%s km", estimateTravelDistanceText, tripDistance));
                mEstimatedTravelTime.setText(String.format("* %s%s min", estimateTravelTimeText, tripDuration));
            }

        } else {
            SnackbarHelper.showSnackBar(mTripEstimateRateCardLL, "TripEstimate details are not available");
        }
    }


    @SuppressLint("DefaultLocale")
    private void setDataToViews(RateCard rateCard) {
        try {
            baseFareTV.setText(String.format("%s %d  for first %d Km", rupee, rateCard.getBaseFare(), rateCard.getBaseKM()));
            baseKM_TV.setText(String.format("%s %d / Km  after %d Km", rupee, rateCard.getDistanceFare(), rateCard.getBaseKM()));
            rideTimeFareTV.setText(String.format("%s %d /min", rupee, rateCard.getRideTimeFare()));


            String url = getTruckSelectedIconForRateCard(mSelectedTruckId, mSelectedVehicleOpenOrCloseId);

            mStrTrucksDesc = rateCard.getVehicleCategoryDescription();
            mStrOpenCloseDes = rateCard.getVehicleTypeDescription();
            Picasso.with(getActivity()).load(url).into(mRateCardIV);
            truckDescRateTV.setText(String.format("Truck Type :  %s - %s Truck", mStrTrucksDesc, mStrOpenCloseDes));

        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " setDataToViews :- Exception" + e.getMessage());

        }
    }


    public static String getTruckSelectedIconForRateCard(int vehicleGroupId, int vehicletypeId) {
        switch (vehicleGroupId) {
            case 1000:
                switch (vehicletypeId) {
                    case VEHICLE_TYPE_OPEN:
                        return WEB_API_ADDRESS + "Images/64-Mini-open.png";
                    case VEHICLE_TYPE_CLOSED:
                        return WEB_API_ADDRESS + "Images/64-Mini-closed.png";
                }
                return WEB_API_ADDRESS + "Images/64-Mini-open.png";
            case 1001:
                switch (vehicletypeId) {
                    case VEHICLE_TYPE_OPEN:
                        return WEB_API_ADDRESS + "Images/64-Small-open.png";
                    case VEHICLE_TYPE_CLOSED:
                        return WEB_API_ADDRESS + "Images/64-Small-closed.png";
                }
                return WEB_API_ADDRESS + "Images/64-Small-open.png";
            case 1002:
                switch (vehicletypeId) {
                    case VEHICLE_TYPE_OPEN:
                        return WEB_API_ADDRESS + "Images/64-Small-open.png";
                    case VEHICLE_TYPE_CLOSED:
                        return WEB_API_ADDRESS + "Images/64-Small-closed.png";
                }
                return WEB_API_ADDRESS + "Images/64-Small-open.png";
            case 1003:
                switch (vehicletypeId) {
                    case VEHICLE_TYPE_OPEN:
                        return WEB_API_ADDRESS + "Images/64-Large-open.png";
                    case VEHICLE_TYPE_CLOSED:
                        return WEB_API_ADDRESS + "Images/64-Large-closed.png";
                }
                return WEB_API_ADDRESS + "Images/64-Large-open.png";
        }
        return WEB_API_ADDRESS + "Images/64-Mini-open.png";
    }


    private void setAdapterToGridView() {
        try {

            mGridView.setNumColumns(mVehicleTypes.size());
            mTrucksGridAdapter = new TrucksGridAdapter(getContext(), mVehicleTypes, this);
            mGridView.setAdapter(mTrucksGridAdapter);

        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " setAdapterToGridView :- Exception" + e.getMessage());

        }
    }

    @Override
    public void onItemClick(final int pos, View view) {

        try {
            mIntSelectedTruckPosition = pos;
            mSelectedPos = pos;
            mSelectedTruckId = mVehicleTypes.get(pos).getLookupID();

            for (int i = 0; i < mVehicleTypes.size(); i++) {
                if (pos == i) {
                    mVehicleTypes.get(pos).setOpenImages(getTruckSelectedIcon(mSelectedTruckId, mSelectedVehicleOpenOrCloseId));
                } else {
                    mVehicleTypes.get(i).setOpenImages(getTruckIcon(mVehicleTypes.get(i).getLookupID()));
                }

                mTrucksGridAdapter.notifyDataSetChanged();
            }

            showOpenClosedPopup(view, pos);

        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " onItemClick :- Exception" + e.getMessage());

        }
    }

    public void showOpenClosedPopup(View view, final int selectedTruckPos) {
        try {
            popupWindow = new PopupWindow(getActivity());
            ListView openClosedLV = new ListView(getActivity());

            switch (mSelectedTruckId) {
                case 1000:
                    openClosedLV.addHeaderView(LayoutInflater.from(getActivity()).inflate(R.layout.header_pop_up_list_item, null), null, false);
                    break;
                case 1001:
                    openClosedLV.addHeaderView(LayoutInflater.from(getActivity()).inflate(R.layout.header_pop_up_list_item_medium, null), null, false);
                    break;
                case 1002:
                    openClosedLV.addHeaderView(LayoutInflater.from(getActivity()).inflate(R.layout.header_pop_up_list_item_large, null), null, false);
                    break;
                case 1003:
                    openClosedLV.addHeaderView(LayoutInflater.from(getActivity()).inflate(
                            R.layout.header_pop_up_list_item_very_large, null), null, false);
                    break;
            }

            final OpenClosedListViewAdapter vehicleTypesAdapter = new OpenClosedListViewAdapter(selectedTruckPos);
            openClosedLV.setAdapter(vehicleTypesAdapter);

            try {
                popupWindow.setFocusable(true);
                popupWindow.setWidth(DeviceResolutionHelper.getDeviceWidth(getActivity()).x / 3);
                popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appThemeBgColorDark)));
                popupWindow.setContentView(openClosedLV);
                popupWindow.showAtLocation(view, Gravity.CENTER, Gravity.CENTER, 120);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {

                    }
                });
            } catch (Exception e) {
                LogUtils.appendLog(getActivity(), TAG + " showOpenClosedPopup :- Exception" + e.getMessage());

            }
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " showOpenClosedPopup :- Exception" + e.getMessage());

        }

    }

    private String getLoadingDes() {
        String s = "";
        switch (loadingIVSelectedStatus) {
            case LOADING_SELECTED:
                s = "loading";
                break;
            case UN_LOADING_SELECTED:
                s = "unloading";
                break;
            case ALL_SELECTED:
                s = "loading and unloading";
                break;
            case NONE_SELECTED:
                s = "";
                break;
            default:
                s = "";
                break;
        }
        return s;
    }

    private void itemChanges(int pos) {

        try {
            for (int i = 0; i < mVehicleTypes.size(); i++) {
                if (pos == i) {
                    mVehicleTypes.get(pos).setOpenImages(getTruckSelectedIcon(mSelectedTruckId, mSelectedVehicleOpenOrCloseId));
                } else {
                    mVehicleTypes.get(i).setOpenImages(getTruckIcon(mVehicleTypes.get(i).getLookupID()));
                }

                mTrucksGridAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " itemChanges :- Exception" + e.getMessage());

        }
    }


    public void setETAToSelectedTextView(String duration, int selectedTruckPosition) {
        try {
            //No trucks
            if (mVehicleTypes != null && mVehicleTypes.size() > 0) {
                for (int i = 0; i < mVehicleTypes.size(); i++) {

                    if (mIntSelectedTruckPosition != -1) {
                        if (mVehicleTypes.get(mIntSelectedTruckPosition).getLookupID() == mVehicleTypes.get(i).getLookupID()) {
                            mVehicleTypes.get(mIntSelectedTruckPosition).setDefaultTime("");
                            mVehicleTypes.get(mIntSelectedTruckPosition).setDefaultTime(duration);
                            mTrucksGridAdapter.notifyDataSetChanged();
                            mIntSelectedTruckPosition = -1;
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " setETAToSelectedTextView :- Exception" + e.getMessage());

        }
    }

    public void visibleRateCard() {
        try {
            getTripEstimate();
            mTruckMainLayout.setVisibility(GONE);
            mTripEstimateRateCardLL.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " visibleRateCard :- Exception" + e.getMessage());
        }
    }

    public void inVisibleRateCardLayout() {
        try {
            mTripEstimateRateCardLL.setVisibility(GONE);
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " inVisibleRateCardLayout :- Exception" + e.getMessage());

        }
    }

    public void setAllRequiredValues(boolean isBookingLater, int mSelectedLoadUnLoadId) {
        try {
            CredentialManager.setSelectedVehicleGroupID(getActivity(), mSelectedTruckId);
            CredentialManager.setSelectedVehicleTypeID(getActivity(), mSelectedVehicleOpenOrCloseId);
            CredentialManager.setSelectedTruckWeightDesc(getActivity(), mStrTrucksDesc);
            CredentialManager.setIsBookingLater(getActivity(), isBookingLater);
            CredentialManager.setLoadingUnloadingStatus(getActivity(), mSelectedLoadUnLoadId);
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " setAllRequiredValues :- Exception" + e.getMessage());
        }
    }

    public void inVisibleTruckLayout() {
        try {
            mTruckMainLayout.setVisibility(GONE);
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " inVisibleTruckLayout :- Exception" + e.getMessage());
        }
    }


    public void visibleTruckLayout() {

        try {
            if (mTruckMainLayout != null) {
                mTruckMainLayout.setVisibility(View.VISIBLE);
            }

            if (mTripEstimateRateCardLL != null)
                mTripEstimateRateCardLL.setVisibility(GONE);

        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " visibleTruckLayout :- Exception" + e.getMessage());

        }
    }


    @Override
    public void onInit(int status) {

    }

    public class OpenClosedListViewAdapter extends BaseAdapter {

        int mSelectedTruckPos = -1;

        OpenClosedListViewAdapter(int selectedTruckPos) {
            int[] images = {getTruckSelectedIcon(mSelectedTruckId, VEHICLE_TYPE_OPEN), getTruckSelectedIcon(mSelectedTruckId, VEHICLE_TYPE_CLOSED)};
            mSelectedTruckPos = selectedTruckPos;
            for (int i = 0; i < mOpenClosedTypes.size(); i++) {
                mOpenClosedTypes.get(i).setImages(images[i]);
            }
        }

        @Override
        public int getCount() {
            return mOpenClosedTypes.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            @SuppressLint("ViewHolder") final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.open_closed_item, parent, false);
            TextView textView = (TextView) view.findViewById(R.id.text1_TV);
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_Image);
            textView.setText(mOpenClosedTypes.get(position).getLookupCode());
            imageView.setImageResource(mOpenClosedTypes.get(position).getImages());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedVehicleOpenOrCloseId = mOpenClosedTypes.get(position).getLookupID();
                    String type = mOpenClosedTypes.get(position).getLookupCode();
                    itemChanges(mSelectedTruckPos);
                    String group = mVehicleTypes.get(mSelectedTruckPos).getLookupDescription() == null ? "" : mVehicleTypes.get(mSelectedTruckPos).getLookupDescription();
                    group = group.replaceAll("null", "");
                    SnackbarHelper.showSnackBar(mLoadingImage, group + " " + type + " truck selected");
                    TextToSpeechHelper.speakOut(getActivity(), group + " " + type + " truck selected");

                    for (int i = 0; i < mOpenClosedTypes.size(); i++) {

                        if (position == i) {
                            boolean isSelected = mOpenClosedTypes.get(i).isSelected();
                            mOpenClosedTypes.get(i).setSelected(!isSelected);
                        } else {
                            mOpenClosedTypes.get(i).setSelected(false);
                        }
                    }

                    notifyDataSetChanged();

                    try {

                        if (loadingIVSelectedStatus == NONE_SELECTED) {
                            if (mIntSelectedTruckPosition != -1) {
                                String cat = mVehicleTypes.get(mIntSelectedTruckPosition).getLookupDescription().replaceAll("null", "");
                                SnackbarHelper.showSnackBar(mLoadingImage, cat + " " + type + " truck is selected");
                            }
                        } else {
                            if (mIntSelectedTruckPosition != -1) {
                                String cat = mVehicleTypes.get(mIntSelectedTruckPosition).getLookupDescription().replaceAll("null", "");
                                SnackbarHelper.showSnackBar(mLoadingImage, cat + " " + type + " truck with " + getLoadingDes() + " is selected");

                            }
                        }
                    } catch (Exception e) {
                        LogUtils.appendLog(getActivity(), TAG + " visibleTruckLayout :- Exception" + e.getMessage());

                    }

                    mLoadingImage.setImageResource(getLoadingIconBasedOnSelection(loadingIVSelectedStatus, true));

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < mOpenClosedTypes.size(); i++) {
                                mOpenClosedTypes.get(i).setSelected(false);
                            }
                            notifyDataSetChanged();
                            popupWindow.dismiss();
                        }
                    }, 1000);

                    try {


                        mListener.onTruckSelected(mSelectedTruckId, mSelectedVehicleOpenOrCloseId, loadingIVSelectedStatus, position);

                      //  getTripEstimate();

                        mTruckViewPresenetr.getSelectedRateCard(mSelectedVehicleOpenOrCloseId, mSelectedTruckId);

                    } catch (Exception e) {
                        LogUtils.appendLog(getActivity(), TAG + " OpenClosedListViewAdapter :- Exception" + e.getMessage());
                    }
                }
            });

            try {
                if (mOpenClosedTypes.get(position).isSelected()) {
                    textView.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.appThemeYellow));
                    textView.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.appThemeBgColorLight));
                } else {
                    textView.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.appThemeBgColorLight));
                    textView.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.appThemeYellow));
                }

            } catch (Exception e) {
                LogUtils.appendLog(getActivity(), TAG + " OpenClosedListViewAdapter :- Exception" + e.getMessage());

            }
            return view;
        }
    }

    private void getTripEstimate() {

        try {

            String start = String.valueOf(HomeActivity.mPickUpLatLng);
            String stop = String.valueOf(HomeActivity.mDropLatLng);

            if (start != null && start.length() > 0 && stop != null && stop.length() > 0) {
                TripEstimate tripEstimate = new TripEstimate();
                tripEstimate.setVehicleType(mSelectedVehicleOpenOrCloseId);
                tripEstimate.setVehicleGroup(mSelectedTruckId);
                int length = String.valueOf(HomeActivity.mPickUpLatLng).length();
                Log.e(TAG, HomeActivity.mPickUpLatLng + ":mPickUpLatLng Length " + length);
                String fromLocation = null;
                try {
                    fromLocation = String.valueOf(HomeActivity.mPickUpLatLng).substring(10, length - 1);
                    Log.e(TAG, HomeActivity.mPickUpLatLng + ":fromLocation " + fromLocation);
                } catch (Exception e) {
                    fromLocation = start;
                }
                int lengths = String.valueOf(HomeActivity.mDropLatLng).length();
                Log.e(TAG, HomeActivity.mDropLatLng + ":mDropLatLng Length " + lengths);
                String toLocation = null;

                try {
                    toLocation = String.valueOf(HomeActivity.mDropLatLng).substring(10, lengths - 1);
                    Log.e(TAG, HomeActivity.mDropLatLng + ":toLocation " + toLocation);
                } catch (Exception e) {
                    toLocation = stop;
                }

                tripEstimate.setFrmLatLong(fromLocation);
                tripEstimate.setToLatLong(toLocation);
                tripEstimate.setLdUdCharges(loadingIVSelectedStatus);
                Log.e(TAG, "TripEstimate OnItemClick" + new Gson().toJson(tripEstimate));
                mTruckViewPresenetr.getTripEstaimate(tripEstimate);
            }

        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " getTripEstimate :- Exception" + e.getMessage());

        }

    }

    public int getTruckIcon(int vehicleGroupId) {

        switch (vehicleGroupId) {
            case 1000:
                return R.drawable.open_0_75_ton;
            case 1001:
                return R.drawable.open_1_ton;
            case 1002:
                return R.drawable.open_1_5_ton;

            case 1003:
                return R.drawable.open_2_ton;
        }

        return R.drawable.vehicle_open1;
    }

    private PopupWindow showLoadUnloadPopup() {
        try {
            final PopupWindow popupWindow = new PopupWindow(getActivity());
            final ListView loadUnLoadListView = new ListView(getActivity());
            final LoadUnloadPopupListviewAdapter popupListviewAdapter = new LoadUnloadPopupListviewAdapter();
            loadUnLoadListView.setAdapter(popupListviewAdapter);
            loadUnLoadListView.setSelection(1);
            loadUnLoadListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                }
            });

            popupWindow.setFocusable(true);
            int width = (DeviceResolutionHelper.getDeviceWidth(getActivity()).x * 39) / 100;
            // popupWindow.setWidth(400);
            popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appThemeBgColorDark)));
            popupWindow.setContentView(loadUnLoadListView);
            return popupWindow;
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " showLoadUnloadPopup :- Exception" + e.getMessage());

        }

        return null;
    }

    private int getLoadingIconBasedOnSelection(int loadingIVSelectedStatus, boolean canChangeText) {
        try {
            switch (loadingIVSelectedStatus) {
                case NONE_SELECTED:
                    if (canChangeText) {
                        mLoading.setAlpha(UN_SELECTED_FLOAT_VALUE);
                        mUnLoading.setAlpha(UN_SELECTED_FLOAT_VALUE);
                    }
                    switch (mSelectedVehicleOpenOrCloseId) {
                        case VEHICLE_TYPE_OPEN:
                            return R.drawable.none_selected_open;
                        case VEHICLE_TYPE_CLOSED:
                            return R.drawable.none_selected_closed;
                    }
                    break;
                case LOADING_SELECTED:

                    if (canChangeText) {
                        mLoading.setAlpha(SELECTED_FLOAT_VALUE);
                        mUnLoading.setAlpha(UN_SELECTED_FLOAT_VALUE);

                    }
                    switch (mSelectedVehicleOpenOrCloseId) {
                        case VEHICLE_TYPE_OPEN:
                            return R.drawable.loading_open;
                        case VEHICLE_TYPE_CLOSED:
                            return R.drawable.loading_closed;
                    }
                    break;
                case UN_LOADING_SELECTED:

                    if (canChangeText) {
                        mLoading.setAlpha(UN_SELECTED_FLOAT_VALUE);
                        mUnLoading.setAlpha(SELECTED_FLOAT_VALUE);
                    }
                    switch (mSelectedVehicleOpenOrCloseId) {
                        case VEHICLE_TYPE_OPEN:
                            return R.drawable.unloading_open;
                        case VEHICLE_TYPE_CLOSED:
                            return R.drawable.unloading_closed;
                    }

                    break;
                case ALL_SELECTED:

                    if (canChangeText) {
                        mLoading.setAlpha(SELECTED_FLOAT_VALUE);
                        mUnLoading.setAlpha(SELECTED_FLOAT_VALUE);
                    }

                    switch (mSelectedVehicleOpenOrCloseId) {
                        case VEHICLE_TYPE_OPEN:
                            return R.drawable.all_selected_open;
                        case VEHICLE_TYPE_CLOSED:
                            return R.drawable.all_selected_closed;
                    }
                    break;
            }
            return R.drawable.loading_open;
        } catch (Exception e) {
            LogUtils.appendLog(getActivity(), TAG + " getLoadingIconBasedOnSelection :- Exception" + e.getMessage());

        }

        return R.drawable.loading_open;
    }

    public int getTruckSelectedIcon(int vehicleGroupId, int vehicleOpenOrClosedtypeId) {
        switch (vehicleGroupId) {
            case 1000:
                switch (vehicleOpenOrClosedtypeId) {
                    case VEHICLE_TYPE_OPEN:
                        return R.drawable.mini_opened_truck;
                    case VEHICLE_TYPE_CLOSED:
                        return R.drawable.mini_closed_truck;
                }
                return R.drawable.mini_opened_truck;
            case 1001:
                switch (vehicleOpenOrClosedtypeId) {
                    case VEHICLE_TYPE_OPEN:
                        return R.drawable.small_open_truck;
                    case VEHICLE_TYPE_CLOSED:
                        return R.drawable.small_closed_truck;
                }
                return R.drawable.small_open_truck;
            case 1002:
                switch (vehicleOpenOrClosedtypeId) {
                    case VEHICLE_TYPE_OPEN:
                        return R.drawable.meduim_open_truck;
                    case VEHICLE_TYPE_CLOSED:
                        return R.drawable.medium_closed_truck;
                }
                return R.drawable.meduim_open_truck;
            case 1003:
                switch (vehicleOpenOrClosedtypeId) {
                    case VEHICLE_TYPE_OPEN:
                        return R.drawable.large_open_truck;
                    case VEHICLE_TYPE_CLOSED:
                        return R.drawable.large_closed_truck;
                }
                return R.drawable.large_open_truck;
        }
        return R.drawable.vehicle_open1_selected;
    }

    class LoadUnloadPopupListviewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return loadingStatus.length;
        }

        @Override
        public Object getItem(int position) {
            return loadingStatus[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            @SuppressLint("ViewHolder") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pop_up_list_item_assistane, parent, false);

            try {
                TextView textView = view.findViewById(R.id.text1_TV);
                ImageView imageView = view.findViewById(R.id.iv_Image);

                switch (position) {
                    case 0:
                        textView.setText(R.string.loading);
                        imageView.setImageResource(getLoadingIconBasedOnSelection(LOADING_SELECTED, false));
                        break;
                    case 1:
                        textView.setText(R.string.un_loading);
                        imageView.setImageResource(getLoadingIconBasedOnSelection(UN_LOADING_SELECTED, false));
                        break;
                }

                if (loadingStatus[position]) {
                    textView.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.appThemeYellow));
                    textView.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.appThemeBgColorLight));
                } else {
                    textView.setBackgroundColor(ContextCompat.getColor(parent.getContext(), R.color.appThemeBgColorLight));
                    textView.setTextColor(ContextCompat.getColor(parent.getContext(), R.color.appThemeYellow));
                }

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        loadingStatus[position] = !loadingStatus[position];
                        notifyDataSetChanged();

                        if (loadingStatus[0] && loadingStatus[1]) {
                            loadingIVSelectedStatus = ALL_SELECTED;
                            TextToSpeechHelper.speakOut(getActivity(), getString(R.string.both_selected));
                        }
                        if (!loadingStatus[0] && loadingStatus[1]) {
                            loadingIVSelectedStatus = UN_LOADING_SELECTED;
                            TextToSpeechHelper.speakOut(getActivity(), getString(R.string.unloading_sel));

                        }
                        if (loadingStatus[0] && !loadingStatus[1]) {
                            loadingIVSelectedStatus = LOADING_SELECTED;
                            TextToSpeechHelper.speakOut(getActivity(), getString(R.string.loading_sel));
                        }
                        if (!loadingStatus[0] && !loadingStatus[1]) {
                            loadingIVSelectedStatus = NONE_SELECTED;
                        }

                        mLoadingImage.setImageResource(getLoadingIconBasedOnSelection(loadingIVSelectedStatus, true));
                        notifyDataSetChanged();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mPopupWindow.dismiss();
                            }
                        }, 2000);


                    }
                });

            } catch (Exception e) {
                LogUtils.appendLog(getActivity(), TAG + " LoadUnloadPopupListviewAdapter :- Exception" + e.getMessage());

            }
            return view;
        }
    }

    public interface OnTruckSelectedListner {
        void onTruckSelected(int truckId, int openClosedId, int loadUnloadId, int selectedTruckPosition);
        boolean setSrcDestText();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnTruckSelectedListner) {
            mListener = (OnTruckSelectedListner) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
