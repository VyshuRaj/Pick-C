package com.prts.pickcustomer.booking;

import com.google.gson.annotations.SerializedName;

public class CargoType {


	@SerializedName("LookupId")
	private int id;

	@SerializedName("Image")
	private String image;

	@SerializedName("LookupCategory")
	private String category;

	@SerializedName("LookupDescription")
	private String description;

	@SerializedName("LookupCode")
	private String name;

	boolean isChecked;

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean checked) {
		isChecked = checked;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setCategory(String category){
		this.category = category;
	}

	public String getCategory(){
		return category;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}


}