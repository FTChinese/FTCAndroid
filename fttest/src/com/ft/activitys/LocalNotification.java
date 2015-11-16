package com.ft.activitys;

import org.apache.cordova.DroidGap;

import com.ft.R;

import android.os.Bundle;

public class LocalNotification extends DroidGap {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// isd.getExtras();
		super.onCreate(savedInstanceState);

		String url = getIntent().getStringExtra("url");
		super.setBooleanProperty("loadInWebView", true);
		super.setIntegerProperty("splashscreen", R.drawable.splash);
		// super.loadUrl("file:///android_asset/www/index.html");
		super.loadUrl(url);

	}
}