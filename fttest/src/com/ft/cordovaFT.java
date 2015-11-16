package com.ft;

import com.networkbench.agent.impl.NBSAppAgent;

import org.apache.cordova.CordovaWebViewClient;
import org.apache.cordova.DroidGap;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;
import com.ft.activitys.FullAdActivity;
import com.ft.model.Alldata;
import com.ft.tool.Dialogtoo;
import com.ft.tool.DownHomePage;
import com.ft.tool.GlobalTool;
import com.ft.tool.ProgressBarAsyncTask;
import com.ft.tool.pushData;
import com.ft.tool.servicepush.ForegroundService;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

@SuppressLint("NewApi")
public class cordovaFT extends DroidGap implements IWXAPIEventHandler {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		 Intent fulladintent=getIntent();
		 String fulladstring=fulladintent.getStringExtra("fulladActivity");
		 if(null == fulladstring){
			 Intent adintent= new Intent(cordovaFT.this, FullAdActivity.class);
			 startActivity(adintent);
			 this.finish();
			 super.onDestroy();
		 }
		String index_html = GlobalTool.getResources(this, "index.html");
		String base_url = GlobalTool.getResources(this, "base_url.txt");
		String indexcont = GlobalTool.getResources(this, "indexcont.txt");
		String indexcont2 = GlobalTool.getResources(this, "indexcont2.txt");
		super.onCreate(savedInstanceState);
		super.init();

	

		// super.setBooleanProperty("loadInWebView", true);
		// super.setIntegerProperty("loadUrlTimeoutValue", 600000000);

		super.setIntegerProperty("splashscreen", R.drawable.splash);
		super.appView.getSettings().setJavaScriptEnabled(true);
		super.appView.getSettings().setSupportMultipleWindows(true);
		super.appView.getSettings().setPluginState(PluginState.ON);
		CordovaWebViewClient webViewClient = new CustomCordovaWebViewClient(this);
//iframe打开a标签
		super.appView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onCreateWindow(WebView view, boolean dialog,
					boolean userGesture, android.os.Message resultMsg) {
				WebView newWebView = new WebView(view.getContext());
				WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
				transport.setWebView(newWebView);
				resultMsg.sendToTarget();
				return true;
			}
		});

		JavascriptClient javascriptClient = new JavascriptClient(this);
		webViewClient.setWebView(this.appView);
		this.appView.setWebViewClient(webViewClient);
		this.appView.addJavascriptInterface(javascriptClient, "ftjavacriptapp");

		int sdkid = android.os.Build.VERSION.SDK_INT;
		if (sdkid >= 19) {
			this.appView.getSettings()
					.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
			if ((GlobalTool.isWiFiActive(this) && !indexcont.equals(indexcont2) || indexcont
					.equals("0"))) {
				this.appView.getSettings().setCacheMode(
						WebSettings.LOAD_CACHE_ELSE_NETWORK);
			}

			super.loadUrl(base_url, 3000);
		} else {
			super.loadUrl("file:///android_asset/index.html", 3000);
			this.appView.loadDataWithBaseURL(base_url, index_html, "text/html",
					"utf-8", null);
		}
		super.appView.setVerticalScrollBarEnabled(true);
		super.appView.setHorizontalScrollBarEnabled(false);
		// set scrollbar style
		super.appView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

		pushData pushData = new pushData(this.getBaseContext());
		Alldata push_d = pushData.getpush();
		if (push_d.getData().equals("0")) {
			startService(new Intent(cordovaFT.this, ForegroundService.class));
		}

		ProgressBarAsyncTask asyncTask = new ProgressBarAsyncTask(this);
		asyncTask.execute();

		// 设置AppKey以及ppChannel
		String appkey = GlobalTool.getResources(this, "appkey.txt");
		String channel = GlobalTool.getResources(this, "channel.txt");
		StatService.setAppKey(appkey);// appkey必须在mtj网站上注册生成，该设置也可以在AndroidManifest.xml中填写
		StatService.setAppChannel(channel);// appChannel是应用的发布渠道，不需要在mtj网站上注册，直接填写就可以
		StatService.setOn(this, StatService.EXCEPTION_LOG);
		StatService.setLogSenderDelayed(10);
		StatService.setSendLogStrategy(this, SendStrategyEnum.APP_START, 1,
				false);

		DownHomePage downhomepage = new DownHomePage(this);
		downhomepage.execute();

		super.appView.setOnLongClickListener(new View.OnLongClickListener() {
			public boolean onLongClick(View v) {
				return true;
			}
		});

//		NBSAppAgent.setLicenseKey("95a11b364bfb4fde85e326e5efc76c1c")
//				.withLocationServiceEnabled(true).start(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Dialogtoo dialogtoo = new Dialogtoo();
			dialogtoo.exitdialog(cordovaFT.this);
		}
		return true;

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

	}

	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResp(BaseResp arg0) {
		// TODO Auto-generated method stub

	}

}