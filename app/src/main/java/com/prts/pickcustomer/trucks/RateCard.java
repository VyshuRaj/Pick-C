package com.prts.pickcustomer.trucks;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RateCard implements Serializable {

	@SerializedName("Category")
	private int category;

	@SerializedName("DriverAssistance")
	private int driverAssistance;

	@SerializedName("VehicleType")
	private int vehicleType;

	@SerializedName("BaseFare")
	private int baseFare;

	@SerializedName("CancellationFee")
	private int cancellationFee;

	@SerializedName("WaitingFare")
	private int waitingFare;

	@SerializedName("OverNightCharges")
	private int overNightCharges;

	@SerializedName("VehicleTypeDescription")
	private String vehicleTypeDescription;

	@SerializedName("VehicleCategoryDescription")
	private String vehicleCategoryDescription;

	@SerializedName("RideTimeFare")
	private int rideTimeFare;

	@SerializedName("RateTypeDescription")
	private String rateTypeDescription;

	@SerializedName("RateType")
	private int rateType;

	@SerializedName("DistanceFare")
	private int distanceFare;

	@SerializedName("BaseKM")
	private int baseKM;

	public void setCategory(int category){
		this.category = category;
	}

	public int getCategory(){
		return category;
	}

	public void setDriverAssistance(int driverAssistance){
		this.driverAssistance = driverAssistance;
	}

	public int getDriverAssistance(){
		return driverAssistance;
	}

	public void setVehicleType(int vehicleType){
		this.vehicleType = vehicleType;
	}

	public int getVehicleType(){
		return vehicleType;
	}

	public void setBaseFare(int baseFare){
		this.baseFare = baseFare;
	}

	public int getBaseFare(){
		return baseFare;
	}

	public void setCancellationFee(int cancellationFee){
		this.cancellationFee = cancellationFee;
	}

	public int getCancellationFee(){
		return cancellationFee;
	}

	public void setWaitingFare(int waitingFare){
		this.waitingFare = waitingFare;
	}

	public int getWaitingFare(){
		return waitingFare;
	}

	public void setOverNightCharges(int overNightCharges){
		this.overNightCharges = overNightCharges;
	}

	public int getOverNightCharges(){
		return overNightCharges;
	}

	public void setVehicleTypeDescription(String vehicleTypeDescription){
		this.vehicleTypeDescription = vehicleTypeDescription;
	}

	public String getVehicleTypeDescription(){
		return vehicleTypeDescription;
	}

	public void setVehicleCategoryDescription(String vehicleCategoryDescription){
		this.vehicleCategoryDescription = vehicleCategoryDescription;
	}

	public String getVehicleCategoryDescription(){
		return vehicleCategoryDescription;
	}

	public void setRideTimeFare(int rideTimeFare){
		this.rideTimeFare = rideTimeFare;
	}

	public int getRideTimeFare(){
		return rideTimeFare;
	}

	public void setRateTypeDescription(String rateTypeDescription){
		this.rateTypeDescription = rateTypeDescription;
	}

	public String getRateTypeDescription(){
		return rateTypeDescription;
	}

	public void setRateType(int rateType){
		this.rateType = rateType;
	}

	public int getRateType(){
		return rateType;
	}

	public void setDistanceFare(int distanceFare){
		this.distanceFare = distanceFare;
	}

	public int getDistanceFare(){
		return distanceFare;
	}

	public void setBaseKM(int baseKM){
		this.baseKM = baseKM;
	}

	public int getBaseKM(){
		return baseKM;
	}

	@Override
 	public String toString(){
		return 
			"RateCard{" +
			"category = '" + category + '\'' + 
			",driverAssistance = '" + driverAssistance + '\'' + 
			",vehicleType = '" + vehicleType + '\'' + 
			",baseFare = '" + baseFare + '\'' + 
			",cancellationFee = '" + cancellationFee + '\'' + 
			",waitingFare = '" + waitingFare + '\'' + 
			",overNightCharges = '" + overNightCharges + '\'' + 
			",vehicleTypeDescription = '" + vehicleTypeDescription + '\'' + 
			",vehicleCategoryDescription = '" + vehicleCategoryDescription + '\'' + 
			",rideTimeFare = '" + rideTimeFare + '\'' + 
			",rateTypeDescription = '" + rateTypeDescription + '\'' + 
			",rateType = '" + rateType + '\'' + 
			",distanceFare = '" + distanceFare + '\'' + 
			",baseKM = '" + baseKM + '\'' + 
			"}";
		}
}