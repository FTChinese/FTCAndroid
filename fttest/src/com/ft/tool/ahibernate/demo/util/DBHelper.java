package com.ft.tool.ahibernate.demo.util;

import com.ft.tool.ahibernate.util.MyDBHelper;
import com.ft.tool.ahibernate.demo.model.Student;
import com.ft.tool.ahibernate.demo.model.Teacher;

import android.content.Context;

public class DBHelper extends MyDBHelper {
	private static final String DBNAME = "student.db";// 数据库名
	private static final int DBVERSION = 1;
	private static final Class<?>[] clazz = { Teacher.class, Student.class };// 要初始化的表

	public DBHelper(Context context) {
		super(context, DBNAME, null, DBVERSION, clazz);
	}

}
