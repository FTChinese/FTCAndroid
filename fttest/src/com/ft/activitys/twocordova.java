package com.ft.activitys;

import org.apache.cordova.CordovaWebViewClient;
import org.apache.cordova.DroidGap;

import com.ft.CustomCordovaWebViewClient;
import com.ft.R;
import com.ft.cordovaFT;
import com.ft.tool.Dialogtoo;
import com.ft.tool.GlobalTool;
import com.ft.tool.ProgressBarAsyncTask;
import com.ft.tool.checkfile;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;

public class twocordova extends DroidGap {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		String urlid = getIntent().getExtras().getString("urlid");
		String indexcont= GlobalTool.getResources(this, "indexcont.txt");
		String indexcont2= GlobalTool.getResources(this, "indexcont2.txt");
		super.onCreate(savedInstanceState);
		super.init();
		super.setIntegerProperty("splashscreen", R.drawable.splash);
		String index_html = GlobalTool.getResources(this, "index.html");
		String base_url = GlobalTool.getResources(this, "base_url.txt");
		
		CordovaWebViewClient webViewClient = new CustomCordovaWebViewClient(this);
		webViewClient.setWebView(this.appView);
		this.appView.setWebViewClient(webViewClient);
		
		int sdkid=android.os.Build.VERSION.SDK_INT;
		
		if(sdkid>=19){
			this.appView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
			if((GlobalTool.isWiFiActive(this) && !indexcont.equals(indexcont2) || indexcont.equals("0"))){
				this.appView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
			}
			super.loadUrl(base_url, 4000);
		}else{
			super.loadUrl("file:///android_asset/index.html", 3000);
			this.appView.loadDataWithBaseURL(base_url, index_html, "text/html","utf-8", null);
		}

		super.appView.setVerticalScrollBarEnabled(true);
		super.appView.setHorizontalScrollBarEnabled(false);
		if (!GlobalTool.isConn(getApplicationContext())) {
			GlobalTool.setNetworkMethod(twocordova.this);
		} else {
			checkfile checkfile = new checkfile(this, urlid);
			checkfile.execute();
		}

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Dialogtoo dialogtoo = new Dialogtoo();
			dialogtoo.exitdialog(twocordova.this);
		}
		return true;

	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
}