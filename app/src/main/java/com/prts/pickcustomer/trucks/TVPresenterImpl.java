package com.prts.pickcustomer.trucks;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.prts.pickcustomer.R;
import com.prts.pickcustomer.helpers.ActivityHelper;
import com.prts.pickcustomer.helpers.LogUtils;
import com.prts.pickcustomer.helpers.NetworkHelper;
import com.prts.pickcustomer.restapi.RestClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.prts.pickcustomer.helpers.CredentialManager.getHeaders;
import static com.prts.pickcustomer.restapi.RestAPIConstants.BASE_URL;
import static com.prts.pickcustomer.restapi.RestAPIConstants.GET_TRIP_ESTIMATE;
import static com.prts.pickcustomer.restapi.RestAPIConstants.MONITOR_DRIVER;
import static com.prts.pickcustomer.restapi.RestAPIConstants.SELECTED_RATE_CARD;
import static com.prts.pickcustomer.restapi.RestAPIConstants.TIME_OUT;

/**
 * Created by LOGICON on 19-12-2017.
 */

public class TVPresenterImpl implements TruckViewPresenetr {
    private Context mContext;
    private TruckListView mTruckListView;

    TVPresenterImpl(Context context, TruckListView truckListView) {
        mContext = context;
        mTruckListView = truckListView;
    }


    private void getTruckGroupFromServer() {

        RestClient.getRestService(BASE_URL).getVehicleGroups()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<VehicleGroupType>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<VehicleGroupType> vehicleGroupTypes) {
                        try {
                            Log.e("TAG","vehicleTypes."+vehicleGroupTypes.get(0).getLookupCode());
                            mTruckListView.setDefaultTrucks(vehicleGroupTypes);
                        } catch (Exception e) {
                            LogUtils.appendLog(mContext, "TCF" + " getTruckGroupFromServer onNext :- Exception" + e.getMessage());

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        try {
                            LogUtils.appendLog(mContext, "TCF" + " getTruckGroupFromServer onError :- Exception" + e.getMessage());

                            String message=e.getMessage();
                            if (message.contains(TIME_OUT)) {
                                getTruckGroupFromServer();
                            }else{
                                ActivityHelper.handleException(mContext,message);
                            }


                        } catch (Exception e1) {
                            LogUtils.appendLog(mContext, "TCF" + " getTruckGroupFromServer onError :- Exception" + e1.getMessage());

                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void getTruckTypesFromServer() {


        RestClient.getRestService(BASE_URL).getOpenClosedList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<VehicleType>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<VehicleType> vehicleTypes) {
                        try {
                            if (vehicleTypes!=null && vehicleTypes.size()>0) {
                                mTruckListView.setDefaultOpenClosed(vehicleTypes);
                            } else {
                                mTruckListView.trucksNotavailable("Vehicle types are not available");
                            }
                        } catch (Exception e) {
                            LogUtils.appendLog(mContext, "TCF" + " getTruckTypesFromServer onError :- Exception" + e.getMessage());

                            mTruckListView.trucksNotavailable("Vehicle types are not available");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        try {
                            LogUtils.appendLog(mContext, "TCF" + " getTruckTypesFromServer onError :- Exception" + e.getMessage());

                            String message=e.getMessage();
                            if (message.contains(TIME_OUT)) {
                                getTruckTypesFromServer();
                            }else{
                                ActivityHelper.handleException(mContext,message);
                            }
                        } catch (Exception e1) {
                            LogUtils.appendLog(mContext, "TCF" + " getTruckTypesFromServer onError :- Exception" + e1.getMessage());

                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getTrucksFromServer() {
        try {
            List<VehicleGroupType> trucks = new ArrayList<>();
            List<VehicleType> openClosedList = new ArrayList<>();
            int[] openClose = {1300, 1301};
            String[] openCloseStr = {"Open", "Closed"};

            for (int i = 0; i < 2; i++) {
                VehicleType vehicleType = new VehicleType();
                vehicleType.setLookupID(openClose[i]);
                vehicleType.setLookupCode(openCloseStr[i]);
                openClosedList.add(vehicleType);
            }
            int[] vehicleGroupIds = {1000, 1001, 1002, 1003};
            String[] vehicleGroupDescriptions = {"Mini", "Small", "Medium", "Large"};
            int[] openDrawbles = {R.drawable.open_0_75_ton, R.drawable.open_1_ton, R.drawable.open_1_5_ton, R.drawable.open_2_ton};

            for (int i = 0; i < vehicleGroupIds.length; i++) {
                VehicleGroupType type = new VehicleGroupType();
                type.setLookupID(vehicleGroupIds[i]);
                type.setLookupCode(vehicleGroupDescriptions[i]);
                type.setOpenImages(openDrawbles[i]);
                type.setDefaultTime("No trucks");
                trucks.add(type);
            }

            mTruckListView.setDefaultTrucks(trucks);
            mTruckListView.setDefaultOpenClosed(openClosedList);

            getTruckGroupFromServer();
            getTruckTypesFromServer();

        } catch (Exception e) {
            LogUtils.appendLog(mContext, "TCF" + " getTrucksFromServer onError :- Exception" + e.getMessage());

        }

    }



    @Override
    public void getSelectedRateCard(final int openClosedId, final int truckId) {

        if(!NetworkHelper.hasNetworkConnection(mContext)){
            mTruckListView.noInternet();
            return;
        }

        LogUtils.appendLog(mContext,"-----getSelectedRateCard-----");
        LogUtils.appendLog(mContext,"API:-"+BASE_URL+SELECTED_RATE_CARD+"/"+openClosedId+"/"+truckId);
        LogUtils.appendLog(mContext,"Headers:-"+getHeaders(mContext));
      //  LogUtils.appendLog(mContext,"Body:-"+new Gson().toJson(tripEstimate));


        RestClient.getRestService(BASE_URL).getSelectedTruckRateCard(getHeaders(mContext),String.valueOf(openClosedId),String.valueOf(truckId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<RateCard>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<RateCard> rateCards) {
                        try {
                            LogUtils.appendLog(mContext,"getSelectedRateCard NearByPlacesResponse:-"+new Gson().toJson(rateCards)+"\n\n\n\n\n");

                            if (rateCards!=null && rateCards.size()>0) {
                                mTruckListView.setRateCardData(rateCards.get(0));
                            }else {
                                mTruckListView.trucksNotavailable("Ratecard is not available");
                            }
                        } catch (Exception e) {
                            LogUtils.appendLog(mContext, "TCF" + " getSelectedRateCard onError :- Exception" + e.getMessage());

                            mTruckListView.trucksNotavailable("Ratecard is not available");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.appendLog(mContext,"getSelectedRateCard onError:-"+new Gson().toJson(e)+"\n\n\n\n\n");

                        try {
                           // LogUtils.appendLog(mContext, "TCF" + " getSelectedRateCard onError :- Exception" + e.getMessage());

                            String message=e.getMessage();
                            if (message.contains(TIME_OUT)) {
                                getSelectedRateCard(openClosedId,truckId);
                               // getTruckTypesFromServer();
                            }else{
                                ActivityHelper.handleException(mContext,message);
                            }


                        } catch (Exception e1) {
                            LogUtils.appendLog(mContext, "TCF" + " getSelectedRateCard onError :- Exception" + e1.getMessage());

                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void getTripEstaimate(final TripEstimate tripEstimate) {
        if(!NetworkHelper.hasNetworkConnection(mContext)){
            mTruckListView.noInternet();
            return;
        }

        LogUtils.appendLog(mContext,"-----getTripEstaimate-----");
        LogUtils.appendLog(mContext,"API:-"+BASE_URL+GET_TRIP_ESTIMATE);
        LogUtils.appendLog(mContext,"Headers:-"+getHeaders(mContext));
         LogUtils.appendLog(mContext,"Body:-"+new Gson().toJson(tripEstimate));

        // mTruckListView.showProgressDialog();
        RestClient.getRestService(BASE_URL).getTripEstaimate(getHeaders(mContext),tripEstimate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TripEstimateRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TripEstimateRes response) {
                        try {
                            LogUtils.appendLog(mContext,"getTripEstaimate NearByPlacesResponse:-"+new Gson().toJson(response)+"\n\n\n\n\n");

                            //mRideView.dismissDialog();
                            if(response!=null)
                            mTruckListView.setTripEstimateData(response);
                        } catch (Exception e) {
                            LogUtils.appendLog(mContext, "TCF" + " getTripEstaimate onNext :- Exception" + e.getMessage());

                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        try {
                            LogUtils.appendLog(mContext,"getTripEstaimate onError:-"+new Gson().toJson(e)+"\n\n\n\n\n");

                            // LogUtils.appendLog(mContext, "TCF" + " getTripEstaimate onError :- Exception" + e.getMessage());

                            String message=e.getMessage();
                            if (message.contains(TIME_OUT)) {
                                getTripEstaimate(tripEstimate);
                            }else{
                                ActivityHelper.handleException(mContext,message);
                            }
                        } catch (Exception e1) {
                            LogUtils.appendLog(mContext, "TCF" + " getTripEstaimate onError :- Exception" + e1.getMessage());

                        }


                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
