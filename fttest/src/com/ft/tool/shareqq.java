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
 * 生成该类的对象，并调用execute方法之后 首先执行的是onProExecute方法 其次执行doInBackgroup方法
 * 
 */
public class shareqq extends AsyncTask<Integer, Integer, String> {
	private Context context;
	private String url;
	private String img;
	private String text;
	private String description;
	private String to;
	
	public static final String APP_ID = "wx46532c3f790bf06a";//正式
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
	 * 这里的Integer参数对应AsyncTask中的第一个参数 这里的String返回值对应AsyncTask的第三个参数
	 * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
	 * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
	 */
	@Override
	protected String doInBackground(Integer... params) {
		WXWebpageObject localWXWebpageObject = new WXWebpageObject();
		localWXWebpageObject.webpageUrl = url;
		WXMediaMessage localWXMediaMessage = new WXMediaMessage(localWXWebpageObject);
		localWXMediaMessage.title = text;// 不能太长，否则微信会提示出错。不过博主没验证过具体能输入多长。
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
	 * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
	 * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
	 */
	@Override
	protected void onPostExecute(String result) {

	}

	// 该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
	@Override
	protected void onPreExecute() {
		// textView.setText("开始执行异步线程");
		
	
//		textView.setText("开始执行异步线程");
	}

	/**
	 * 这里的Intege参数对应AsyncTask中的第二个参数
	 * 在doInBackground方法当中，，每次调用publishProgress方法都会触发onProgressUpdate执行
	 * onProgressUpdate是在UI线程中执行，所有可以对UI空间进行操作
	 */
	@Override
	protected void onProgressUpdate(Integer... values) {
		int vlaue = values[0];
	}

}
