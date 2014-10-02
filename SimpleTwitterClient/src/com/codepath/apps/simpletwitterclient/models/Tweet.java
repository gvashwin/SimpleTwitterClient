package com.codepath.apps.simpletwitterclient.models;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {
	private String text;
	private String timeStamp;
	private long uid;
	private int reTweetCount;
	private User user;
	private long id;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	// Getters and Setters
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public int getReTweetCount() {
		return reTweetCount;
	}
	public void setReTweetCount(int reTweetCount) {
		this.reTweetCount = reTweetCount;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public String toString(){
		return ""+this.text;
	}
	
	
	// Factory method to create a tweet from json object
	public static Tweet fromJsonObject(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		try {
			tweet.text = jsonObject.getString("text");
			tweet.uid = jsonObject.getLong("id");
			tweet.timeStamp = jsonObject.getString("created_at");
			tweet.reTweetCount = jsonObject.getInt("retweet_count");
			tweet.user = User.fromJsonObject(jsonObject.getJSONObject("user"));
			tweet.id = jsonObject.getLong("id");
		} catch(JSONException e) {
			e.printStackTrace();
			return null;
		}
		return tweet;
	}
	public static ArrayList fromJsonArray(
			JSONArray jsonResponseArray) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		JSONObject tweetObject;
		for(int i=0; i<jsonResponseArray.length(); i++) {
			tweetObject = null;
			try {
				tweetObject = jsonResponseArray.getJSONObject(i);	
			} catch (JSONException e) {
				e.printStackTrace();
				continue;
			}
			Tweet t = Tweet.fromJsonObject(tweetObject);
			if(t != null) {
				tweets.add(t);
			}
			
		}
		return tweets;
	}
}
