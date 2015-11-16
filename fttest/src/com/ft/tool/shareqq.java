package com.ft.tool;

import java.io.ByteArrayOutputStream;

import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import com.ft.R;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;

import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;

/**
 * ���ɸ���Ķ��󣬲�����execute����֮�� ����ִ�е���onProExecute���� ���ִ��doInBackgroup����
 * 
 */
public class shareqq extends AsyncTask<Integer, Integer, String> {
	private Context context;
	private String url;
	private String img;
	private String text;
	private String description;
	private String to;
	
	public static final String APP_ID = "wx46532c3f790bf06a";//��ʽ
//	public static final String APP_ID = "wx432783291d671efb";

	public shareqq(String _url, Context _context, String _text,
			String _description, String _to, String _img) {
		super();
		this.context = _context;
		this.url = _url;
		this.img = _img;
		this.text = _text;
		this.description = _description;
		this.to = _to;
	}

	/**
	 * �����Integer������ӦAsyncTask�еĵ�һ������ �����String����ֵ��ӦAsyncTask�ĵ���������
	 * �÷�������������UI�̵߳��У���Ҫ�����첽�����������ڸ÷����в��ܶ�UI���еĿռ�������ú��޸�
	 * ���ǿ��Ե���publishProgress��������onProgressUpdate��UI���в���
	 */
	@Override
	protected String doInBackground(Integer... params) {
		WXWebpageObject localWXWebpageObject = new WXWebpageObject();
		localWXWebpageObject.webpageUrl = url;
		WXMediaMessage localWXMediaMessage = new WXMediaMessage(localWXWebpageObject);
		localWXMediaMessage.title = text;// ����̫��������΢�Ż���ʾ������������û��֤������������೤��
		localWXMediaMessage.description = description;
		InputStream is = null;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(img);
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1200);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 1200);
			
			HttpResponse response;
			response = client.execute(get);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			long fileSize = entity.getContentLength();  
//			Log.i("fileSize",img+":::"+fileSize+"");
		} catch (Exception e) {
			Resources r = context.getResources();
			is = r.openRawResource(R.drawable.conftbig);
		}
		BitmapDrawable bmpDraw = new BitmapDrawable(is);
		Bitmap bmp = bmpDraw.getBitmap();
		localWXMediaMessage.thumbData = bmpToByteArray(bmp, true);
		SendMessageToWX.Req localReq = new SendMessageToWX.Req();
		localReq.transaction = System.currentTimeMillis() + "";
		localReq.message = localWXMediaMessage;
		if (to.startsWith("moment")) {
			localReq.scene = SendMessageToWX.Req.WXSceneTimeline;
		}
		IWXAPI api = WXAPIFactory.createWXAPI(context, APP_ID, true);
	    api.registerApp(APP_ID);
		api.sendReq(localReq);
		return "";
	}

	public static byte[] bmpToByteArray(final Bitmap bmp,
			final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * �����String������ӦAsyncTask�еĵ�����������Ҳ���ǽ���doInBackground�ķ���ֵ��
	 * ��doInBackground����ִ�н���֮�������У�����������UI�̵߳��� ���Զ�UI�ռ��������
	 */
	@Override
	protected void onPostExecute(String result) {

	}

	// �÷���������UI�̵߳���,����������UI�̵߳��� ���Զ�UI�ռ��������
	@Override
	protected void onPreExecute() {
		// textView.setText("��ʼִ���첽�߳�");
		
	
//		textView.setText("��ʼִ���첽�߳�");
	}

	/**
	 * �����Intege������ӦAsyncTask�еĵڶ�������
	 * ��doInBackground�������У���ÿ�ε���publishProgress�������ᴥ��onProgressUpdateִ��
	 * onProgressUpdate����UI�߳���ִ�У����п��Զ�UI�ռ���в���
	 */
	@Override
	protected void onProgressUpdate(Integer... values) {
		int vlaue = values[0];
	}

}
