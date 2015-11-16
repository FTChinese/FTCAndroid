package com.ft.model.util;

import com.ft.model.Alldata;
import com.ft.model.Article;
import com.ft.tool.ahibernate.util.MyDBHelper;

import android.content.Context;

public class FTDBHelper extends MyDBHelper {
	private static final String DBNAME = "ftchinese.db";// 数据库名
	private static final int DBVERSION = 2;
	private static final Class<?>[] clazz = { Article.class,Alldata.class };// 要初始化的表

	public FTDBHelper(Context context) {
		super(context, DBNAME, null, DBVERSION, clazz);
	}

}
