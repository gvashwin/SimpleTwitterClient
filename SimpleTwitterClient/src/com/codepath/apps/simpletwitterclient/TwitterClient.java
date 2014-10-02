package com.codepath.apps.simpletwitterclient;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; 
	public static final String REST_URL = "https://api.twitter.com/1.1/"; 
	public static final String REST_CONSUMER_KEY = "H68LpC5xSWytoIhABcqR2eaZk";       
	public static final String REST_CONSUMER_SECRET = "mhdxk1SSZyDhaGqdmF3VHj2zIaCPbGOAU2zY7imO0UqCon9CUH"; 
	public static final String REST_CALLBACK_URL = "oauth://cpbasictweets";

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}
	
	/*
	 * End point to get the home time line for an authenticated user
	 */
	public void getHomeTimeline(String filter,long id,AsyncHttpResponseHandler handler){
		String apiUrl = getApiUrl("statuses/home_timeline.json");
		if(filter.equalsIgnoreCase("since")) {
			Log.i("debug", "Twitter Client :getting tweets since "+id);
			RequestParams params = new RequestParams();
			params.put("since_id", ""+id);
			client.get(apiUrl, params, handler);
			
		} else if (filter.equalsIgnoreCase("after")) {
			Log.i("debug", "Twitter Client :getting tweets after "+id);
			RequestParams params = new RequestParams();
			params.put("max_id", ""+id);
			client.get(apiUrl, params, handler);
		} else {
			Log.i("debug", "Twitter Client : getting tweets");
			client.get(apiUrl, null, handler);
		}
		
		
	}
	
	/*
	 * End point to get the account info of authenticated user.
	 * Can be used to get the img url to account picture.
	 * Can be used to get the account name to be displayed. 
	 */
	
	public void getAccountInfo(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");
		client.get(apiUrl, null, handler);
	}
	
	/*
	 * End point to get the account setting info
	 * Can be used to get the screen name (@twitter_handle) of the account
	 */
	public void getAccountSettings(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/settings.json");
		client.get(apiUrl, null, handler);
	}
	
	/*
	 * End point to post tweets on authenticated accounts.
	 */
	public void postTweet(String tweetText, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", tweetText);
		client.post(apiUrl, params, handler);
	}

	// CHANGE THIS
	// DEFINE METHODS for different API endpoints here
	/*
	public void getInterestingnessList(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
		// Can specify query string params directly or through RequestParams.
		RequestParams params = new RequestParams();
		params.put("format", "json");
		client.get(apiUrl, params, handler);
	}*/

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}