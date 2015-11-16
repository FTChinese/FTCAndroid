package com.ft.tool.ahibernate.demo.dao.impl;

import com.ft.tool.ahibernate.dao.impl.BaseDaoImpl;
import com.ft.tool.ahibernate.demo.model.Teacher;
import com.ft.tool.ahibernate.demo.util.DBHelper;

import android.content.Context;

public class TeacherDaoImpl extends BaseDaoImpl<Teacher> {
	public TeacherDaoImpl(Context context) {
		super(new DBHelper(context));
	}
}
