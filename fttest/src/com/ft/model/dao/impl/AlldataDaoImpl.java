package com.ft.model.dao.impl;

import com.ft.model.Alldata;
import com.ft.model.util.FTDBHelper;
import com.ft.tool.ahibernate.dao.impl.BaseDaoImpl;

import android.content.Context;

//如果您是J2EE高手一定希望支持接口吧,按下面的写法即可:
public class AlldataDaoImpl extends BaseDaoImpl<Alldata> {
	public AlldataDaoImpl(Context context) {
		super(new FTDBHelper(context));
	}
}
