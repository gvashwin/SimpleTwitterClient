package com.codepath.apps.simpletwitterclient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils.TruncateAt;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpletwitterclient.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TwitterArrayAdapter extends
		ArrayAdapter<Tweet> {
	ImageLoader imgLoader;
	public TwitterArrayAdapter(Context context, List<Tweet> tweets) {
		super(context, R.layout.tweet_item_view, tweets);
		imgLoader = ImageLoader.getInstance();
	}
	private static class ViewHolder {
		//ImageView icTinyRetweet;
		//TextView tvRetweetBy;
		TextView tvScreenName;
		TextView tvTwitterHandle;
		TextView tvTimeStamp;
		ImageView ivUserImg;	
		TextView tvTweetText;
		ImageView icReply;
		ImageView icRetweet;
		TextView tvRetweetCount;
		ImageView icFav;
		TextView tvFavCount;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Tweet t = getItem(position);
		ViewHolder viewHolder;
		if(convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.tweet_item_view, parent, false);
			
			//viewHolder.icTinyRetweet = (ImageView) convertView.findViewById(R.id.icTinyReweet);
			//viewHolder.tvRetweetBy = (TextView) convertView.findViewById(R.id.tvRetweetedBy);
			viewHolder.tvScreenName = (TextView) convertView.findViewById(R.id.tvUserScreenName);
			viewHolder.tvTwitterHandle = (TextView) convertView.findViewById(R.id.tvTwitterHandle);
			viewHolder.tvTimeStamp = (TextView) convertView.findViewById(R.id.tvTimeStamp);
			
			viewHolder.ivUserImg = (ImageView) convertView.findViewById(R.id.ivUsrImg);
			viewHolder.tvTweetText = (TextView) convertView.findViewById(R.id.tvTweetText);
			viewHolder.icReply = (ImageView) convertView.findViewById(R.id.icReply);
			viewHolder.icRetweet = (ImageView) convertView.findViewById(R.id.icRetweet);
			viewHolder.tvRetweetCount = (TextView) convertView.findViewById(R.id.tvRetweet);
			viewHolder.icFav = (ImageView) convertView.findViewById(R.id.icFav);
			viewHolder.tvFavCount = (TextView) convertView.findViewById(R.id.tvFav);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tvTweetText.setText(t.getText());
		viewHolder.tvRetweetCount.setText(""+t.getReTweetCount());
		
		Log.i("debug", "Loading  image"+t.getUser().getImgUrl());
		String bigImgUrl = t.getUser().getImgUrl().replace("_normal", "_bigger");
		String normalImgUrl = t.getUser().getImgUrl();
		imgLoader.displayImage(normalImgUrl, viewHolder.ivUserImg);
		
		String screenName = "<b>"+t.getUser().getName()+"</b>";
		viewHolder.tvScreenName.setText(Html.fromHtml(screenName));
		//viewHolder.tvScreenName.set
		
		viewHolder.tvTwitterHandle.setHint("");
		viewHolder.tvTwitterHandle.setText("@"+t.getUser().getScreenName());
		//viewHolder.tvTwitterHandle.setEllipsize(TruncateAt.END);
		
		/*long timeStamp = Long.parseLong(t.getTimeStamp());
		String elapsedTime = ""+DateUtils.getRelativeTimeSpanString((timeStamp*1000),System.currentTimeMillis(),DateUtils.MINUTE_IN_MILLIS);
		viewHolder.tvTimeStamp.setText(elapsedTime);*/
		
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);
	 
		String relativeDate = "";
		try {
			long dateMillis = sf.parse(t.getTimeStamp()).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		viewHolder.tvTimeStamp.setText(relativeDate);
		return convertView;
	}


}
