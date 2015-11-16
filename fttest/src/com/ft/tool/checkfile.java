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
 * ���ɸ���Ķ��󣬲�����execute����֮�� ����ִ�е���onProExecute���� ���ִ��doInBackgroup����
 * 
 */
public class checkfile extends AsyncTask<Integer, Integer, String> {

	String url_check = "http://m.ftchinese.com/index.php/jsapi/story_status/";
	private DroidGap gap;
	int status = 0;
	String urlid = "";

	// ������ء���[{"status":"1"}]������ʾ���¿ɶ�
	// ������ء���[{"status":"0"}]������ʾ���±�ǽ
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
	 * �����String������ӦAsyncTask�еĵ�����������Ҳ���ǽ���doInBackground�ķ���ֵ��
	 * ��doInBackground����ִ�н���֮�������У�����������UI�̵߳��� ���Զ�UI�ռ��������
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

	// �÷���������UI�̵߳���,����������UI�̵߳��� ���Զ�UI�ռ��������
	@Override
	protected void onPreExecute() {
		// textView.setText("��ʼִ���첽�߳�");
	}

	/**
	 * �����Intege������ӦAsyncTask�еĵڶ�������
	 * ��doInBackground�������У���ÿ�ε���publishProgress�������ᴥ��onProgressUpdateִ��
	 * onProgressUpdate����UI�߳���ִ�У����п��Զ�UI�ռ���в���
	 */
	@Override
	protected void onProgressUpdate(Integer... values) {
	}

}
