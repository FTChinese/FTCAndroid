package com.ft.tool;

import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Map;

import org.apache.cordova.DroidGap;
import org.apache.http.util.EncodingUtils;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.sip.SipSession.State;
import android.util.Log;

@SuppressLint("NewApi")
public class GlobalTool {
	public static void sendReq(String url ,Context context) {
		Map<String, String> mapRequest = CRequest.URLRequest(url); 
		String to ="" ;
		String shareurl ="" ;
		String title ="" ;
		String img="" ;
		String description="" ;
		try {
			to=URLDecoder.decode(mapRequest.get("to"));
			shareurl=URLDecoder.decode(mapRequest.get("url"));
			title=URLDecoder.decode(mapRequest.get("title"));
			img=URLDecoder.decode(mapRequest.get("img"));
			description=URLDecoder.decode(mapRequest.get("description"));
//			url = URLDecoder.decode(url);
		} catch (Exception e) {
			Log.i("URLDecoderException", e.toString());
		}
		shareqq asyncTask = new shareqq( shareurl , context,  title, description, to, img);
		asyncTask.execute();
    }
	
	
	public static String getassets(Resources a, String fileName) {
		String res = "";
		try {
			InputStream in = a.getAssets().open(fileName);
			int length = in.available();
			byte[] buffer = new byte[length];
			in.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public static String getResources(DroidGap av, String fileName) {
		String res = "";
		res = readdata(av, fileName);
		if (res.equals("")) {
			res = getassets(av.getResources(), fileName);
		}
		return res;
	}

	public static String readdata(Activity av, String fileName) {
		String res = "";
		try {
			InputStream in = av.openFileInput(fileName);
			int length = in.available();
			byte[] buffer = new byte[length];
			in.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return res;

		// try {
		// FileInputStream inputStream = av.openFileInput(fileName);
		// int length = inputStream.available();
		// byte[] bytes = new byte[1024];
		// ByteArrayOutputStream arrayOutputStream = new
		// ByteArrayOutputStream();
		// while (inputStream.read(bytes) != -1) {
		// arrayOutputStream.write(bytes, 0, bytes.length);
		// }
		// inputStream.close();
		// arrayOutputStream.close();
		// String content = new String(arrayOutputStream.toByteArray());
		// return content;
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// return "";
	}

	/*
	 * �ж����������Ƿ��ѿ� 2012-08-20true �Ѵ� false δ��
	 */
	public static boolean isConn(Context context) {
		boolean bisConnFlag = false;
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo network = conManager.getActiveNetworkInfo();
		if (network != null) {
			bisConnFlag = conManager.getActiveNetworkInfo().isAvailable();
		}
		return bisConnFlag;
	}
	/*
	 * �ж�WIFI �Ѵ� false δ��
	 */
	public static boolean isWiFiActive(Context context) {    
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);    
        if (connectivity != null) {    
            NetworkInfo[] infos = connectivity.getAllNetworkInfo();    
            if (infos != null) {    
            	for(NetworkInfo ni : infos){
            		if(ni.getTypeName().equals("WIFI") && ni.isConnected()){
            			return true;
            		}
            	}
            }    
        }    
        return false;    
    } 

	/*
	 * �������������
	 */
	public static void setNetworkMethod(final Context context) {
		// ��ʾ�Ի���
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("����������ʾ")
				.setMessage("�������Ӳ�����,�Ƿ��������?")
				.setPositiveButton("����", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = null;
						// �ж��ֻ�ϵͳ�İ汾 ��API����10 ����3.0�����ϰ汾
						if (android.os.Build.VERSION.SDK_INT > 10) {
							intent = new Intent(
									android.provider.Settings.ACTION_WIRELESS_SETTINGS);
						} else {
							intent = new Intent();
							ComponentName component = new ComponentName(
									"com.android.settings",
									"com.android.settings.WirelessSettings");
							intent.setComponent(component);
							intent.setAction("android.intent.action.VIEW");
						}
						context.startActivity(intent);
					}
				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}).show();
	}
}
