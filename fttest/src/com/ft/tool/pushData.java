package com.ft.tool;

import java.util.List;

import org.apache.cordova.DroidGap;

import android.content.Context;

import com.ft.model.Alldata;
import com.ft.model.dao.impl.AlldataDaoImpl;

public class pushData {
	Context ctx;
	public pushData(Context _ctx) {
		ctx = _ctx;
	}
	public Alldata getpush() {  
		AlldataDaoImpl alldataDao = new AlldataDaoImpl(ctx);
		Alldata alldata = new Alldata();
		List<Alldata> list3 = alldataDao.rawQuery("select * from t_alldata where type=1 Limit 1", null);
		for (Alldata alldata_data : list3) {
			alldata = alldata_data;
		}
		alldata.setType(1);
		if(alldata.getData()==null){
			alldata.setData("0");
			alldataDao.insert(alldata);
		}
		return alldata;
	}
	public String is_push() {  
		Alldata alldata = this.getpush();
		return alldata.getData();
	 }
	public  void set_push(String data) {
		Alldata alldata = this.getpush();
		alldata.setData(data);
		AlldataDaoImpl alldataDao = new AlldataDaoImpl(ctx);
		alldataDao.update(alldata);
	}
}
