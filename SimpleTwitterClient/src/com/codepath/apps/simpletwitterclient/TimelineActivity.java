package com.codepath.apps.simpletwitterclient;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codepath.apps.simpletwitterclient.ComposeTweetDialogFragment.ComposeTweetDialogFragmentListener;
import com.codepath.apps.simpletwitterclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

import android.R.menu;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TimelineActivity extends FragmentActivity implements ComposeTweetDialogFragmentListener {
	private ActionBar actionBar;
	private PullToRefreshListView lvTweets;
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private TwitterArrayAdapter aTweets;
	
	private MenuInflater menuInflater;
	private MenuItem writeTweet;
	private long lastLoadedTweetId = 0;
	private long topTweetId = 0;
	boolean pull2Refresh = false;
	
	String usrScreenName;
	String usrTwitterHandle;
	String profileImgUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		client = TwitterApplication.getRestClient();
		getProfileInfo();
		setupView();
		setupAllListeners();
		getProfileInfo();
		//boolean since, boolean max, long tweetId
		fillTimeline(false,0); // Called to fill the timeline in home screen with tweets
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		setupActionbar(menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	/*
	 * This method is called to setup the action bar
	 * and action bar items.
	 */
	private void setupActionbar(Menu menu) {
		actionBar = getActionBar();
		// Setting color
		actionBar.setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#55ACEE")));
		// Setting title
		actionBar.setTitle("Home");
		// Setting new icon
		getActionBar().setIcon(R.drawable.ic_twitter_logo);
		menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.timeline_actions, menu);
		writeTweet = menu.findItem(R.id.miWriteTweet);
			
	}
	
	/*
	 * This method is invoked when the user 
	 * press the compose tweet icon on the action bar.
	 * This method brings up the dialog fragment through which 
	 * the uset can compose his/her tweet.
	 */
	public void composeTweet(MenuItem item) {
		FragmentManager fm = getSupportFragmentManager();
		ComposeTweetDialogFragment ctdf = ComposeTweetDialogFragment.getInstance("Compose");
		Bundle args = ctdf.getArguments();
        args.putString("name", usrScreenName);
        args.putString("tHandle", usrTwitterHandle);
        args.putString("imgUrl", profileImgUrl);
        ctdf.setArguments(args);
        ctdf.setRetainInstance(true);
		ctdf.show(fm, "Compose");
	}
	
	/*
	 * This method is called to setup various
	 * listeners in the timeline activity
	 */
	private void setupAllListeners() {
		
		// Setting up OnRefreshListener for pull to refresh
		lvTweets.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
				// Filing the time line again with new results from json
				//boolean since, boolean max, long tweetId
				pull2Refresh = true;
				Log.i("debug","TimelineActivity - Pull to refresh called onRefresh");
				fillTimeline(false,topTweetId); // Need to fix this.
				lvTweets.onRefreshComplete();
			}
		});
		
		lvTweets.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(TimelineActivity.this, TweetViewActivity.class);
				Tweet selectedTweet = tweets.get(position);
				i.putExtra("tweetText", selectedTweet.getText());
				startActivity(i);
				
			}
			
		});
		
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
		    @Override
		    public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
		    	if(lastLoadedTweetId!=0) {
		    		fillTimeline(true,lastLoadedTweetId); 
		    	} else {
		    		Log.i("debug","TimelineActibity : Inside setting up Endless Scorller");
		    	}
		    }
        });
	}

	/*
	 * Function to setup the views references
	 * and adapters
	 */
	private void setupView() {
		lvTweets = (PullToRefreshListView) findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>(); 
		aTweets = new TwitterArrayAdapter(this, tweets);
		lvTweets.setAdapter(aTweets);
	}

	/*
	 * This function is called to get the home time line 
	 * from through api call and fill the list view with the 
	 * latest tweets.
	 */
	
	public void fillTimeline(boolean max, long tweetId) {
		
		JsonHttpResponseHandler reponseHandler = new JsonHttpResponseHandler(){
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.i("debug",e.toString());
				Log.i("debug",s.toString());
			}

			@Override
			public void onSuccess(int statusCode, JSONArray jsonResponseArray) {
				Log.i("debug","Return Status Code"+statusCode);
				Log.i("debug",jsonResponseArray.toString());
				ArrayList<Tweet> tweets = Tweet.fromJsonArray(jsonResponseArray);
				Log.i("debug",tweets.toString());
				int numTweets = tweets.size();
				Log.i("debug","Number of tweets is "+ numTweets);
				if(numTweets==0) return;
				Log.i("debug","last tweet is  "+tweets.get(numTweets-1).getText()+", and id is "+ tweets.get(numTweets-1).getId());
				if(pull2Refresh) {
					aTweets.clear();
					pull2Refresh = false;
				} 
				Log.i("debug","No pull to refresh add to the end");
				aTweets.addAll(tweets);
				aTweets.notifyDataSetChanged();
				lastLoadedTweetId = tweets.get(numTweets-1).getId();
				
			}
			
		};
		
		if(max == true) {
			Log.i("debug","Fetching tweets to fill home timeline max="+tweetId);
			client.getHomeTimeline("after",lastLoadedTweetId,reponseHandler);
		} else {
			// New call to fill the timeline no tweets has been loaded yet
			Log.i("debug","Fetching tweets to fill home timeline");
			aTweets.clear();
			client.getHomeTimeline("none",0,reponseHandler);
			aTweets.notifyDataSetChanged();
		}
		
		
	}
	
	/* This function is called to get the user account info
	 * Account Name.
	 * Screen Name (aka twitter handle)
	 * Img URL to the account picture.
	 */
	private void getProfileInfo() {
		Log.i("debug", "Getting profile info");
		client.getAccountInfo(new JsonHttpResponseHandler(){

			@Override
			public void onFailure(Throwable e, String s) {
				Log.i("debug",e.toString());
				Log.i("debug",s.toString());
			}

			@Override
			public void onSuccess(JSONObject jsonObject) {
				Log.i("debug","Profile info json is :"+jsonObject.toString());
				try {
					String name = jsonObject.getString("name");
					String tHandle = jsonObject.getString("screen_name");
					String url = jsonObject.getString("profile_image_url");
					usrScreenName = new String(name);
					usrTwitterHandle = new String(tHandle);
					profileImgUrl = new String(url);
					Log.i("debug",usrScreenName+" - "+usrTwitterHandle+" - "+profileImgUrl);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		});
		client.getAccountSettings(new JsonHttpResponseHandler(){

			@Override
			public void onFailure(Throwable e, String s) {
				Log.i("debug",e.toString());
				Log.i("debug",s.toString());
			}

			@Override
			public void onSuccess(JSONObject jsonObject) {
				//Log.i("debug",jsonObject.toString());
				
				
			}
			
		});
		
		
	}



	@Override
	public void postComposedTweet(String tweetText) {
		/*
		 * To do need to check the return code of the post request
		 */
		Log.i("debug","tweet to post is "+tweetText);
		client.postTweet(tweetText, new JsonHttpResponseHandler(){
			@Override
			public void onFailure(Throwable e, String s) {
				Log.i("debug",e.toString());
				Log.i("debug",s.toString());
			}

			@Override
			public void onSuccess(JSONObject jsonObject) {
				Log.i("debug",jsonObject.toString());
				// Calling fill tiem line again to reflect the
				// tweet to show on home timeline
				pull2Refresh = true;
				Log.i("debug","TimelineActivity - Pull to refresh called onRefresh");
				fillTimeline(false,topTweetId); 
				lvTweets.onRefreshComplete(); 
			}
			
		});
		
	}
}
