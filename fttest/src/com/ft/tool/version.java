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
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class version {
	/** Called when the activity is first created. */
	String newVerName = "";// 新版本名称
	int newVerCode = -1;// 新版本号
	ProgressDialog pd = null;
	String UPDATE_SERVERAPK = "ApkUpdateAndroid.apk";
	String url_vesion = "http://123.150.200.181/ver.js";
	String url_apk = "http://123.150.200.181/ftandroid.apk";
	DroidGap gap;

	public void updateVersion(DroidGap _gap) {
		gap = _gap;
		if (getServerVer()) {
			int verCode = this.getVerCode(gap);
			if (newVerCode > verCode) {
				doNewVersionUpdate();// 更新版本
			} else {
				// alert("")
				notNewVersionUpdate();// 提示已是最新版本
			}
		}
	}

	/**
	 * 获得版本号
	 */
	public int getVerCode(Context context) {
		int verCode = -1;
		try {
			String packName = context.getPackageName();
			verCode = context.getPackageManager().getPackageInfo(packName, 0).versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			Log.e("版本号获取异常", e.getMessage());
		}
		return verCode;
	}

	/**
	 * 获得版本名称
	 */
	public String getVerName(Context context) {
		String verName = "";
		try {
			String packName = context.getPackageName();
			verName = context.getPackageManager().getPackageInfo(packName, 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e("版本名称获取异常", e.getMessage());
		}
		return verName;
	}

	/**
	 * 从服务器端获得版本号与版本名称
	 * 
	 * @return
	 */
	public boolean getServerVer() {
		try {
			URL url = new URL(url_vesion);
			HttpURLConnection httpConnection = (HttpURLConnection) url
					.openConnection();
			httpConnection.setDoInput(true);
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("GET");
			httpConnection.connect();
			InputStreamReader reader = new InputStreamReader(
					httpConnection.getInputStream());
			BufferedReader bReader = new BufferedReader(reader);
			String json = bReader.readLine();
			JSONArray array = new JSONArray(json);
			JSONObject jsonObj = array.getJSONObject(0);
			newVerCode = Integer.parseInt(jsonObj.getString("verCode"));
			newVerName = jsonObj.getString("verName");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;// 如果这里改为false 则不更新会退出程序
		}
		return true;
	}

	/**
	 * 不更新版本
	 */
	public void notNewVersionUpdate() {
		int verCode = this.getVerCode(gap);
		String verName = this.getVerName(gap);
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本：");
		sb.append(verName);
		sb.append(" Code:");
		sb.append(verCode);
		sb.append("\n已是最新版本，无需更新");
		Dialog dialog = new AlertDialog.Builder(gap).setTitle("软件更新")
				.setMessage(sb.toString())
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						gap.finish();
					}
				}).create();
		dialog.show();
	}

	/**
	 * 更新版本
	 */
	public void doNewVersionUpdate() {
		int verCode = this.getVerCode(gap);
		String verName = this.getVerName(gap);
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本：");
		sb.append(verName);
		sb.append(" Code:");
		sb.append(verCode);
		sb.append(",发现版本：");
		sb.append(newVerName);
		sb.append(" Code:");
		sb.append(newVerCode);
		sb.append(",是否更新");
		Dialog dialog = new AlertDialog.Builder(gap)
				.setTitle("软件更新")
				.setMessage(sb.toString())
				.setPositiveButton("更新", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						pd = new ProgressDialog(gap);
						pd.setTitle("正在下载");
						pd.setMessage("请稍后。。。");
						pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						downFile(url_apk);
					}
				})
				.setNegativeButton("暂不更新",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								// finish();
							}
						}).create();
		// 显示更新框
		dialog.show();
	}

	/**
	 * 下载apk
	 */
	public void downFile(final String url) {
		pd.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(
								Environment.getExternalStorageDirectory(),
								UPDATE_SERVERAPK);
						fileOutputStream = new FileOutputStream(file);
						byte[] b = new byte[1024];
						int charb = -1;
						int count = 0;
						while ((charb = is.read(b)) != -1) {
							fileOutputStream.write(b, 0, charb);
							count += charb;
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			pd.cancel();
			update();
		}
	};

	/**
	 * 下载完成，通过handler将下载对话框取消
	 */
	public void down() {
		new Thread() {
			public void run() {
				Message message = handler.obtainMessage();
				handler.sendMessage(message);
			}
		}.start();
	}

	/**
	 * 安装应用
	 */
	public void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), UPDATE_SERVERAPK)),
				"application/vnd.android.package-archive");
		gap.startActivity(intent);
	}

}
