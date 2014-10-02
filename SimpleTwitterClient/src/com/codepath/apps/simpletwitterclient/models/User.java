package com.codepath.apps.simpletwitterclient.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
	private String name;
	private String imgUrl;
	private long uid;
	private String screenName;
	
	// Getters and Setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	
	//Factory method to create a user from json object
	public static User fromJsonObject(JSONObject jsonObject) {
		User user = new User();
		try {
			user.name = jsonObject.getString("name");
			user.uid = jsonObject.getLong("id");
			user.screenName = jsonObject.getString("screen_name");
			user.imgUrl = jsonObject.getString("profile_image_url");
		} catch(JSONException e) {
			e.printStackTrace();
			return null;
		}
		return user;
	}

}
