package com.ft;

import java.io.FileOutputStream;
import java.util.regex.Pattern;
import org.apache.cordova.CordovaWebViewClient;
import org.apache.cordova.DroidGap;

import com.ft.activitys.FullAdActivity;
import com.ft.tool.GlobalTool;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.provider.Settings.Secure;

public class CustomCordovaWebViewClient extends CordovaWebViewClient {
	DroidGap ctx;
	public CustomCordovaWebViewClient(DroidGap _ctx) {
		super(_ctx);
		ctx = _ctx;
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap bitmap) {
		String android_id = Secure.getString(view.getContext().getContentResolver(),Secure.ANDROID_ID);
		super.onPageStarted(view, url, bitmap);

		view.loadUrl("javascript:{var androidWeixin=true;var android_iframe_load=true;var ft_android_id='"+android_id+"';};");
	}

	public static boolean  noopen(String str){
	    Pattern pattern = Pattern.compile(".*noopen=true.*");
	    return pattern.matcher(str).matches();   
	 } 
	// 在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次
	@Override
	public void onLoadResource(WebView view, String url) {	
		super.onLoadResource(view, url);
	}
        
	
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		if(url.startsWith("ftcweixin://?url=")){
			GlobalTool.sendReq(url,this.ctx);
		}
		if( url.contains("isad=1")){
			   view.stopLoading();  
		        view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));  
		        return true;  		
		}
//		return false ;
		return super.shouldOverrideUrlLoading(view, url);
	}
	private String indexcont2 = "indexcont2.txt";
	@Override
	public void onPageFinished(WebView view, String url) {
		// view.loadUrl("javascript:{" +
		// "var script = document.createElement('script');" +
		// "script.type = 'text/javascript';" +
		// "script.src = 'file:///android_asset/test.js';" +
		// "document.getElementsByTagName('head').item(0).appendChild(script); };");
		// String link =
		// view.getContext().getApplicationContext().getFilesDir()+ "test.js";
		// view.loadUrl(link);
		// String cordova = this.getFromAsset("cordova-2.4.0.js");
		// String LocalNotification = this.getFromAsset("LocalNotification.js");
		// view.loadUrl("javascript:( "+cordova+LocalNotification+ ")();");
//view.loadUrl("javascript:{function android_page_fininshed(){window.location.reload();};setTimeout(android_page_fininshed,1000);};");
		 super.onPageFinished(view, url);
//		view.loadUrl("javascript:{ftjavacriptapp.set_push('0');};");
//		view.loadUrl("javascript:{alert(androidWeixin);};");
		int sdkid=android.os.Build.VERSION.SDK_INT;
		String base_url = GlobalTool.getResources(this.ctx, "base_url.txt");
		String indexcont= GlobalTool.getResources(this.ctx, "indexcont.txt");
		if(sdkid>=19){
			view.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		}
		if(GlobalTool.isWiFiActive(this.ctx) && sdkid>=19 && base_url.equals(url) ){
			try {
				FileOutputStream indexcontStream = this.ctx.openFileOutput(indexcont2,Activity.MODE_PRIVATE);
				indexcontStream.write((indexcont+"").getBytes()); 
				indexcontStream.close(); 
				indexcontStream=null;
			} catch (Exception e) {
			}	
			
		}
		
	}

	@Override
	public void doUpdateVisitedHistory(WebView view, String url,
			boolean isReload) {
		super.doUpdateVisitedHistory(view, url, isReload);
	}

	@Override
	public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) {

	}

}