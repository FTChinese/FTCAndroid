package com.ft.model.dao.impl;

import com.ft.model.Alldata;
import com.ft.model.util.FTDBHelper;
import com.ft.tool.ahibernate.dao.impl.BaseDaoImpl;

import android.content.Context;

//�������J2EE����һ��ϣ��֧�ֽӿڰ�,�������д������:
public class AlldataDaoImpl extends BaseDaoImpl<Alldata> {
	public AlldataDaoImpl(Context context) {
		super(new FTDBHelper(context));
	}
}
