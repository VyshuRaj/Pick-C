package com.prts.pickcustomer.trucks;


import com.google.gson.annotations.SerializedName;


public class VehicleType {//open/closed

	boolean isSelected;

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

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

	int images;

	public int getImages() {
		return images;
	}

	public void setImages(int images) {
		this.images = images;
	}
}