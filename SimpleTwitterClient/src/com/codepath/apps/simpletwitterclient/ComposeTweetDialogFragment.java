package com.codepath.apps.simpletwitterclient;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ComposeTweetDialogFragment extends DialogFragment {
	private ImageView ivUsrProfilePic;
	private TextView tvUsrProfileName;
	private TextView tvUsrTwitterHandle;
	private TextView tvCharsRemaining;
	private Button btnSendTweet;
	private EditText etComposedTweet;
	private final int MAX_CHAR = 140;
	private TwitterClient client;
	ImageLoader imgLoader;
	/*
	 * Todo :
	 * 	Display the account pic
	 * 	Display the account name
	 *  Display the account twitter handle
	 *  Change the button to make it looks pretty
	 *  Add method to post the tweet in timline.
	 * 
	 */
	
	public interface ComposeTweetDialogFragmentListener {
		public void postComposedTweet(String tweetText);
	}
	
	public static ComposeTweetDialogFragment getInstance(String title) {
		ComposeTweetDialogFragment dialog = new ComposeTweetDialogFragment();
		dialog.setStyle(dialog.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
		Bundle args = new Bundle();
		dialog.setArguments(args);
		return dialog;
	}
	
	public void setUpDialogFragment(){
		getDialog().setTitle("Compose Tweet");
		getDialog().getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#f5f8fa")));
		final WindowManager.LayoutParams params = getDialog().getWindow()
				.getAttributes();
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.CENTER;

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		imgLoader = ImageLoader.getInstance();
		setUpDialogFragment();
		View view = inflater.inflate(R.layout.compose_tweet_dialog, container);
		ivUsrProfilePic = (ImageView) view.findViewById(R.id.ivProfileImg);
		tvUsrProfileName = (TextView) view.findViewById(R.id.tvProfileName);
		tvUsrTwitterHandle = (TextView) view.findViewById(R.id.tvProfileTwitterHandle);
		tvCharsRemaining = (TextView) view.findViewById(R.id.tvCharsLeft);
		btnSendTweet = (Button) view.findViewById(R.id.btnSendTweet);
		etComposedTweet = (EditText) view.findViewById(R.id.etComposeTweet);
		imgLoader.displayImage(getArguments().getString("imgUrl"), ivUsrProfilePic);
		tvUsrProfileName.setText(getArguments().getString("name"));
		tvUsrTwitterHandle.setText(getArguments().getString("tHandle"));
		setupListeners();
		return view;
		
	}

	private void setupListeners() {
		etComposedTweet.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				int tweetLen = etComposedTweet.getText().toString().length();
				int charsLeft = MAX_CHAR - tweetLen;
				
				if(charsLeft <= 10) {
					tvCharsRemaining.setTextColor(Color.RED);
				} else if (charsLeft > 10 && charsLeft <= 40) {
					tvCharsRemaining.setTextColor(Color.parseColor("#FA9C05"));
				} else {
					
				}
				tvCharsRemaining.setText(""+charsLeft);
			}
			
		});
		//btnSendTweet.setOnClickListener(this);
		
		
		btnSendTweet.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Log.i("debug", "button clicked to post tweet");
				ComposeTweetDialogFragmentListener timelineActivity = (ComposeTweetDialogFragmentListener) getActivity();
				timelineActivity.postComposedTweet(etComposedTweet.getText().toString());
				dismiss();
				
			}
			
		});
	}
	
	
	

}
