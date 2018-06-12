package com.prts.pickcustomer.trucks;

import com.google.gson.annotations.SerializedName;

public class TripEstimateForCustomerItem{

	@SerializedName("TotalTripEstimateminValue")
	private double totalTripEstimateminValue;

	@SerializedName("TotalDistanceFare")
	private double totalDistanceFare;

	@SerializedName("ApproximateDistanceKM")
	private double approximateDistanceKM;

	@SerializedName("GST")
	private double gST;

	@SerializedName("ApproximateTimeFare")
	private double approximateTimeFare;

	@SerializedName("ApproximateTime")
	private double approximateTime;

	@SerializedName("LoadingUnLoadingCharges")
	private int loadingUnLoadingCharges;

	@SerializedName("TotalTripEstimatemaxValue")
	private double totalTripEstimatemaxValue;

	public void setTotalTripEstimateminValue(double totalTripEstimateminValue){
		this.totalTripEstimateminValue = totalTripEstimateminValue;
	}

	public double getTotalTripEstimateminValue(){
		return totalTripEstimateminValue;
	}

	public void setTotalDistanceFare(int totalDistanceFare){
		this.totalDistanceFare = totalDistanceFare;
	}

	public double getTotalDistanceFare(){
		return totalDistanceFare;
	}

	public void setApproximateDistanceKM(double approximateDistanceKM){
		this.approximateDistanceKM = approximateDistanceKM;
	}

	public double getApproximateDistanceKM(){
		return approximateDistanceKM;
	}

	public void setGST(double gST){
		this.gST = gST;
	}

	public double getGST(){
		return gST;
	}

	public void setApproximateTimeFare(double approximateTimeFare){
		this.approximateTimeFare = approximateTimeFare;
	}

	public double getApproximateTimeFare(){
		return approximateTimeFare;
	}

	public void setApproximateTime(double approximateTime){
		this.approximateTime = approximateTime;
	}

	public double getApproximateTime(){
		return approximateTime;
	}

	public void setLoadingUnLoadingCharges(int loadingUnLoadingCharges){
		this.loadingUnLoadingCharges = loadingUnLoadingCharges;
	}

	public int getLoadingUnLoadingCharges(){
		return loadingUnLoadingCharges;
	}

	public void setTotalTripEstimatemaxValue(double totalTripEstimatemaxValue){
		this.totalTripEstimatemaxValue = totalTripEstimatemaxValue;
	}

	public double getTotalTripEstimatemaxValue(){
		return totalTripEstimatemaxValue;
	}


}