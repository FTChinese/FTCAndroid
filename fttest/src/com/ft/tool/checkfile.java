package com.ft.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.cordova.DroidGap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 生成该类的对象，并调用execute方法之后 首先执行的是onProExecute方法 其次执行doInBackgroup方法
 * 
 */
public class checkfile extends AsyncTask<Integer, Integer, String> {

	String url_check = "http://m.ftchinese.com/index.php/jsapi/story_status/";
	private DroidGap gap;
	int status = 0;
	String urlid = "";

	// 如果返回——[{"status":"1"}]——表示文章可读
	// 如果返回——[{"status":"0"}]——表示文章被墙
	public checkfile(DroidGap _gap, String _urlid) {
		super();
		this.gap = _gap;
		this.urlid = _urlid;
	}

	@Override
	protected String doInBackground(Integer... params) {
		try {
			String _url = url_check + this.urlid;
			URL url = new URL(_url);
			HttpURLConnection httpConnection = (HttpURLConnection) url
					.openConnection();
			httpConnection.setRequestMethod("GET");
			httpConnection.connect();
			InputStreamReader reader = new InputStreamReader(
					httpConnection.getInputStream());
			BufferedReader bReader = new BufferedReader(reader);
			String json = bReader.readLine();
			if(json.equals("[]")){
				status=1;
			}else{
				JSONArray array = new JSONArray(json);
				JSONObject jsonObj = array.getJSONObject(0);
				status = jsonObj.getInt("status");			
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		return status + "";
	}

	/**
	 * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
	 * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
	 */
	@Override
	protected void onPostExecute(String result) {
		String base_url = GlobalTool.getResources(this.gap, "base_url.txt");
//		Log.i("Updater", "onPostExecute:++++++++++++++++++++++++++++++="+urlid);
		if (status == 1) {
			try {
				Thread.sleep(3500);
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.gap.loadUrl("javascript:{location.href='#/story/"+urlid+"';};");
//			this.gap.loadUrl("javascript:{setTimeout(function(){alert(location.href);location.href='#/story/"+urlid+"';},1000);};");
			  
		}
	}

	// 该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
	@Override
	protected void onPreExecute() {
		// textView.setText("开始执行异步线程");
	}

	/**
	 * 这里的Intege参数对应AsyncTask中的第二个参数
	 * 在doInBackground方法当中，，每次调用publishProgress方法都会触发onProgressUpdate执行
	 * onProgressUpdate是在UI线程中执行，所有可以对UI空间进行操作
	 */
	@Override
	protected void onProgressUpdate(Integer... values) {
	}

}
