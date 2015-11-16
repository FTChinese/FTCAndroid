package com.ft.tool;

import java.io.BufferedReader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


import org.apache.cordova.DroidGap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import android.app.Activity;
import android.os.AsyncTask;

/**
 * ���ɸ���Ķ��󣬲�����execute����֮�� ����ִ�е���onProExecute���� ���ִ��doInBackgroup����
 * 
 */
public class DownHomePage extends AsyncTask<Integer, Integer, String> {

	private DroidGap gap;
	int status = 0;
	private String fileName = "index.html";
	private String indexcont = "indexcont.txt";
	public DownHomePage(DroidGap _gap) {
		this.gap = _gap;
	}
	private String Stream2String(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), 1024*1024); //ǿ�ƻ����СΪ1M
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        
        try {
            while ((line = reader.readLine()) != null) {  //�����з�
                sb.append(line);  
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
            inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
	@Override
	protected String doInBackground(Integer... params) {
		String base_url = GlobalTool.getResources(this.gap, "base_url.txt");
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(base_url);
		HttpResponse response;
		try {
			response = client.execute(get);
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			String pageHTML = Stream2String(is);
			int d=pageHTML.length();
			if(pageHTML.contains("</html>")){
				FileOutputStream outputStream = gap.openFileOutput(fileName,Activity.MODE_PRIVATE);
				outputStream.write(pageHTML.getBytes()); 
				outputStream.close(); 
				outputStream=null;
				
				FileOutputStream indexcontStream = gap.openFileOutput(indexcont,Activity.MODE_PRIVATE);
				indexcontStream.write((d+"").getBytes()); 
				indexcontStream.close(); 
				indexcontStream=null;
				
			}
			pageHTML=null;
			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		HttpClient adclient = new DefaultHttpClient();
		HttpGet adget = new HttpGet("http://app.ftchinese.com/android/adfull.txt");
		HttpResponse adresponse;
		try {
			adresponse = adclient.execute(adget);
			HttpEntity adentity = adresponse.getEntity();
			InputStream adis = adentity.getContent();
			String adpageHTML = Stream2String(adis);
			FileOutputStream adoutputStream = gap.openFileOutput("fulladactivityid.txt",Activity.MODE_PRIVATE);
			adoutputStream.write(adpageHTML.getBytes()); 
			adoutputStream.close(); 
			adoutputStream=null;
			adpageHTML=null;
			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return "1";
	}

	/**
	 * �����String������ӦAsyncTask�еĵ�����������Ҳ���ǽ���doInBackground�ķ���ֵ��
	 * ��doInBackground����ִ�н���֮�������У�����������UI�̵߳��� ���Զ�UI�ռ��������
	 */
	@Override
	protected void onPostExecute(String result) {
		if (status == 1) {

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
