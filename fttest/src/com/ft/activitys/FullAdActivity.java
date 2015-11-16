package com.ft.activitys;



import com.adsame.main.AdsameFullAd;
import com.adsame.main.AdsameManager;
import com.adsame.main.FullAdListener;
import com.ft.R;
import com.ft.cordovaFT;
import com.ft.tool.GlobalTool;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.cordova.DroidGap;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

/***********************************************************************
 *                              全屏广告
 **********************************************************************/

public class FullAdActivity extends Activity implements FullAdListener {

	private AdsameFullAd fullAd = null;
	private Button button = null;
	private boolean isReadyAd = false;
	private static final String cID = "20420107";
	private static final String publishID = "25";
	
	public void onCreate(Bundle savedInstanceState) {
//		 String FullAdActivityid = GlobalTool.getResources(this, "fulladactivityid.txt");
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		super.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		setContentView(R.layout.activity_fullad);

		AdsameManager.setPublishID(publishID);
		fullAd = new AdsameFullAd(this, cID);
		fullAd.setFullAdListener(this);

		button = (Button) findViewById(R.id.jump_btn);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				accessNextPage();
			}
		});
	new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				if (!isReadyAd) {
					accessNextPage();
				}
			}
		}, 3000);
	}
	
	private void accessNextPage() {
		Intent intent = new Intent(FullAdActivity.this, cordovaFT.class);
		intent.putExtra("fulladActivity", "fulladActivity");
		this.startActivity(intent);
		this.finish();
	}

	@Override
	public boolean onClickFullAd(String arg0) {
        this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(arg0)));  
    finish();
		return true;
	}

	@Override
	public void onDismissFullAd() {
		accessNextPage();
	}

	@Override
	public void onFailedFullAd(int arg0) {
		
	}

	@Override
	public void onReadyFullAd(int arg0) {
		isReadyAd=true;
		RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.fullad_layout);
		fullAd.show(rLayout);
		button.setVisibility(View.VISIBLE);
		
	}

	@Override
	public void onShowFullAd() {
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (fullAd != null) {
			fullAd.release();
			fullAd = null;
			AdsameManager.clear();
		}
	}
}
