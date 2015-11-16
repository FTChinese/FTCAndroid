package com.ft;

import org.apache.cordova.DroidGap;
import com.ft.tool.pushData;

import android.util.Log;

public class JavascriptClient {
	DroidGap ctx;
	public JavascriptClient(DroidGap _ctx) {
		ctx = _ctx;
	}
	public String is_push() {  
		pushData pushData = new pushData(ctx.getBaseContext());
		return pushData.is_push();
	 }
	 public void show(String str) {  
		 Log.i("JavascriptClient", "Updater:++++++++++++++++++++++++++++++="+str);
     }  
     public void set_push(String data) {  
    	 //o为推送，1为停止推送
    	 pushData pushData = new pushData(ctx.getBaseContext());
    	 pushData.set_push(data);
     }  
}
