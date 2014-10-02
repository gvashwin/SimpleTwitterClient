package com.codepath.apps.simpletwitterclient;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetViewActivity extends Activity {
	
	private ImageView ivAuthorPic;
	private TextView tvAuthorName;
	private TextView tvAuthorTwitterHandle;
	private TextView tvDetailTweet;
	
	/*
	 * Todo :
	 * get the information on the user and the tweet from the timeline activity
	 * populate the views on with the received data
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet_view);
		setupView();
		// need to get data out of intent and set it to detail tweet text view
		// To continue from here
	}
	
	/*
	 * This funciton is calle to setup
	 * all the view references.
	 */
	private void setupView() {
		ivAuthorPic = (ImageView) findViewById(R.id.ivAuthorImg);
		tvAuthorName = (TextView) findViewById(R.id.tvAuthorName);
		tvAuthorTwitterHandle = (TextView) findViewById(R.id.tvAuthorTwitterHandle);
		tvDetailTweet = (TextView) findViewById(R.id.tvDetailedTweet);
	}
}
