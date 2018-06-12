package com.prts.pickcustomer.trucks;

import com.google.gson.annotations.SerializedName;

public class VehicleGroupType {//mini/small

	@SerializedName("LookupId")
	private int lookupID;

	@SerializedName("LookupCategory")
	private String lookupCategory;

	@SerializedName("Image")
	private String image;

	@SerializedName("LookupDescription")
	private String lookupDescription;

	@SerializedName("LookupCode")
	private String lookupCode;

	private int openImages;
	private int unselectedOpenImages;

	private int closedImages;

	public int getClosedImages() {
		return closedImages;
	}

	public void setClosedImages(int closedImages) {
		this.closedImages = closedImages;
	}

	boolean isSelected;

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	public int getOpenImages() {
		return openImages;
	}

	public String getDefaultTime() {
		return defaultTime;
	}

	public void setDefaultTime(String defaultTime) {
		this.defaultTime = defaultTime;
	}

	private String defaultTime;
	public void setOpenImages(int openImages) {
		this.openImages = openImages;
	}

	public void setLookupID(int lookupID){
		this.lookupID = lookupID;
	}

	public int getLookupID(){
		return lookupID;
	}

	public void setLookupCategory(String lookupCategory){
		this.lookupCategory = lookupCategory;
	}

	public String getLookupCategory(){
		return lookupCategory;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setLookupDescription(String lookupDescription){
		this.lookupDescription = lookupDescription;
	}

	public String getLookupDescription(){
		return lookupDescription;
	}

	public void setLookupCode(String lookupCode){
		this.lookupCode = lookupCode;
	}

	public String getLookupCode(){
		return lookupCode;
	}


}