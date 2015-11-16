package com.ft.tool.ahibernate.demo.dao.impl;

import com.ft.tool.ahibernate.dao.impl.BaseDaoImpl;
import com.ft.tool.ahibernate.demo.model.Student;
import com.ft.tool.ahibernate.demo.util.DBHelper;

import android.content.Context;

//如果您是J2EE高手一定希望支持接口吧,按下面的写法即可:
//写一个接口:public interface StudentDao extends BaseDao<Student> {}
//实现接口: public class StudentDaoImpl extends BaseDaoImpl<Student> implements StudentDao
public class StudentDaoImpl extends BaseDaoImpl<Student> {
	public StudentDaoImpl(Context context) {
		super(new DBHelper(context));
	}
}
