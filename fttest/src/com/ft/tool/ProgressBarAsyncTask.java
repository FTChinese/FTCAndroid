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
public class ProgressBarAsyncTask extends AsyncTask<Integer, Integer, String> {

	String newVerName = "";// �°汾����
	double newVerCode = -1;// �°汾��
	double verCode = 0;// �����汾
	ProgressDialog pd = null;
	String UPDATE_SERVERAPK = "ApkUpdateAndroid.apk";
//	String url_vesion = "http://app.ftchinese.com/android/version.js";
//	String url_apk = "http://app.ftchinese.com/android/ftnew.apk";
	// String url_vesion = "http://123.150.200.181/version.js";
	// String url_apk = "http://123.150.200.181/ftnew.apk";

	private DroidGap gap;
	public ProgressBarAsyncTask(DroidGap _gap) {
		super();
		this.gap = _gap;
		verCode = Double.parseDouble(GlobalTool.getResources(this.gap, "vercode.txt"));
	}

	/**
	 * �����Integer������ӦAsyncTask�еĵ�һ������ �����String����ֵ��ӦAsyncTask�ĵ���������
	 * �÷�������������UI�̵߳��У���Ҫ�����첽�����������ڸ÷����в��ܶ�UI���еĿռ�������ú��޸�
	 * ���ǿ��Ե���publishProgress��������onProgressUpdate��UI���в���
	 */
	@Override
	protected String doInBackground(Integer... params) {
		int serverVer = 0;
		if (getServerVer()) {
			serverVer = 1;
		}

		return serverVer + "";
	}

	/**
	 * �����String������ӦAsyncTask�еĵ�����������Ҳ���ǽ���doInBackground�ķ���ֵ��
	 * ��doInBackground����ִ�н���֮�������У�����������UI�̵߳��� ���Զ�UI�ռ��������
	 */
	@Override
	protected void onPostExecute(String result) {
		if (newVerCode > verCode) {
			doNewVersionUpdate();// ���°汾
		} else {
			// notNewVersionUpdate();// ��ʾ�������°汾
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
		int vlaue = values[0];
		// progressBar.setProgress(vlaue);
	}

	/**
	 * �ӷ������˻�ð汾����汾����
	 * 
	 * @return
	 */
	public boolean getServerVer() {
		try {
			String url_vesion = GlobalTool.getResources(this.gap, "version.txt");
			URL url = new URL(url_vesion);
			HttpURLConnection httpConnection = (HttpURLConnection) url
					.openConnection();
			// httpConnection.setDoInput(true);
			// httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("GET");
			httpConnection.connect();
			InputStreamReader reader = new InputStreamReader(
					httpConnection.getInputStream());
			BufferedReader bReader = new BufferedReader(reader);
			String json = bReader.readLine();
			JSONArray array = new JSONArray(json);
			JSONObject jsonObj = array.getJSONObject(0);
			newVerCode = Double.parseDouble(jsonObj.getString("verCode"));
			newVerName = jsonObj.getString("verName");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;// ��������Ϊfalse �򲻸��»��˳�����
		}
		return true;
	}

	/**
	 * �����°汾
	 */
	public void notNewVersionUpdate() {
		String verName = "�ɰ汾";
		StringBuffer sb = new StringBuffer();
		sb.append("��ǰ�汾��");
		sb.append(verName);
		sb.append(" Code:");
		sb.append(verCode);
		sb.append("\n�������°汾���������");
		Dialog dialog = new AlertDialog.Builder(gap).setTitle("�������")
				.setMessage(sb.toString())
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						// gap.finish();
					}
				}).create();
		dialog.show();
	}

	/**
	 * ���°汾
	 */
	public void doNewVersionUpdate() {
		// int verCode = this.getVerCode(gap);
		String verName = "�ɰ汾";
		StringBuffer sb = new StringBuffer();
		sb.append("��ǰ�汾��");
		sb.append(verName);
		sb.append(" Code:");
		sb.append(verCode);
		sb.append(",���ְ汾��");
		sb.append(newVerName);
		sb.append(" Code:");
		sb.append(newVerCode);
		sb.append(",�Ƿ����");
		Dialog dialog = new AlertDialog.Builder(gap)
				.setTitle("�������")
				.setMessage(sb.toString())
				.setPositiveButton("����", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						pd = new ProgressDialog(gap);
						pd.setTitle("��������");
						pd.setMessage("���Ժ󡣡���");
						pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						String url_apk=GlobalTool.getResources(gap, "apk.txt");
						downFile(url_apk);
					}
				})
				.setNegativeButton("�ݲ�����",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								// finish();
							}
						}).create();
		// ��ʾ���¿�
		dialog.show();
	}

	/**
	 * ����apk
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
	 * ������ɣ�ͨ��handler�����ضԻ���ȡ��
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
	 * ��װӦ��
	 */
	public void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), UPDATE_SERVERAPK)),
				"application/vnd.android.package-archive");
		gap.startActivity(intent);
	}

}
