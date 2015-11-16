package com.ft.tool.servicepush;

import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import com.ft.model.Alldata;
import com.ft.model.Article;
import com.ft.model.dao.impl.AlldataDaoImpl;
import com.ft.model.dao.impl.ArticleDaoImpl;
import com.ft.plugins.localnotification.AlarmHelper;
import com.ft.tool.JSON;
import com.ft.tool.pushData;

import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;

import android.app.Activity;
import android.text.format.Time;
import android.util.Log;

public class Updater extends Thread {
	private boolean runon = true;
//	static final int DELAY = 30 * 60 * 1000;
	 static final int DELAY = 1000*60;
	 static final int TreeDELAY = 30*60*1000 ;
	private AlarmHelper alarm = null;
	JSONArray optionsArr;
	ForegroundService foregroundService;
	public static final String PLUGIN_NAME = "Updater_Notification";
	JSON json = new JSON();

	public boolean isRunon() {
		return runon;
	}

	public void setRunon(boolean runon) {
		this.runon = runon;
	}

	public Updater(ForegroundService FS) {
		super("UpdaterService-Updater");
		foregroundService = FS;

	}

	@Override
	public void run() {
		alarm = new AlarmHelper(foregroundService.getBaseContext());
		pushData pushData= new pushData(foregroundService.getBaseContext());
		while (runon) {
//			Log.i("Updater", "Updater:++++++++++++++++++++++++++++++="+runon);
			Alldata push_d = pushData.getpush();
			if(push_d.getData().equals("1")){
				runon=false;
			}
			try {
				Thread.sleep(DELAY);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Time time = new Time();
			time.setToNow();
			int hour = time.hour;
			String pushdata=push_d.getData();
			if (hour > 7 && hour < 23 && three_time() && pushdata.equals("0")) {
				Article article = json.getArticleUrl(
//						"http://m.ftchinese.com/index.php/jsapi/push_story/",
						"http://m.ftchinese.com/index.php/jsapi/push_app_story",
						foregroundService.getBaseContext());
				if (article != null) {
					this.add(false, article.getArticle_id(), "FTÖÐÎÄÍø",
							article.getCheadline(), article.getCheadline(),
							article.getArticle_id(), article.getPubdate() + "");
					ArticleDaoImpl articleDao = new ArticleDaoImpl(
							foregroundService.getBaseContext());
					article.setType(1);
					articleDao.update(article);
					
				}
			}

	

		}
	}
	
	public boolean three_time(){
		boolean stat=false;
		AlldataDaoImpl alldataDao = new AlldataDaoImpl(foregroundService.getBaseContext());
		Alldata alldata = new Alldata();
		List<Alldata> list3 = alldataDao.rawQuery(
				"select * from t_alldata where type=0 Limit 1", null);
		for (Alldata alldata_data : list3) {
			alldata = alldata_data;
		}
		String data_time="10";
		alldata.setType(0);
		if(alldata.getData()!=null){
			data_time=alldata.getData();
			if(!isNumeric(data_time)){data_time="10";}
		}else{
			Alldata alldata2 = new Alldata();
			alldata2.setData("10");
			alldata2.setType(0);
			alldataDao.insert(alldata2);
		}
		
		Calendar calendar = Calendar.getInstance();
		long _time = Long.parseLong(data_time);
		long cleartime = calendar.getTimeInMillis();
		if((cleartime-_time)<0){
			ArticleDaoImpl articleDao = new ArticleDaoImpl(foregroundService.getBaseContext());
			articleDao.execSql("DELETE FROM t_article", null);
		}
		if((cleartime-_time)>TreeDELAY  || (cleartime-_time)<0){
			alldata.setData(cleartime+"");
			alldataDao.update(alldata);
			stat=true;
		}
		return stat;
	}
	
	public static boolean isNumeric(String str){
	    Pattern pattern = Pattern.compile("[0-9]*");
	    return pattern.matcher(str).matches();   
	 } 
	/**
	 * Set an alarm
	 * 
	 * @param repeatDaily
	 *            If true, the alarm interval will be set to one day.
	 * @param alarmTitle
	 *            The title of the alarm as shown in the Android notification
	 *            panel
	 * @param alarmSubTitle
	 *            The subtitle of the alarm
	 * @param alarmId
	 *            The unique ID of the notification
	 * @param cal
	 *            A calendar object that represents the time at which the alarm
	 *            should first be started
	 * @return A pluginresult.
	 */
	public PluginResult add(boolean repeatDaily, String alarmUrl,
			String alarmTitle, String alarmSubTitle, String alarmTicker,
			String alarmId, String time) {

		Calendar cal = Calendar.getInstance();
		boolean result = alarm.addAlarm(repeatDaily, alarmUrl, alarmTitle,
				alarmSubTitle, alarmTicker, alarmId, cal);
		if (result) {
			return new PluginResult(PluginResult.Status.OK);
		} else {
			return new PluginResult(PluginResult.Status.ERROR);
		}
	}

}
