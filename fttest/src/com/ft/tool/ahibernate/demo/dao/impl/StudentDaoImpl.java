package com.ft.tool.ahibernate.demo.dao.impl;

import com.ft.tool.ahibernate.dao.impl.BaseDaoImpl;
import com.ft.tool.ahibernate.demo.model.Student;
import com.ft.tool.ahibernate.demo.util.DBHelper;

import android.content.Context;

//�������J2EE����һ��ϣ��֧�ֽӿڰ�,�������д������:
//дһ���ӿ�:public interface StudentDao extends BaseDao<Student> {}
//ʵ�ֽӿ�: public class StudentDaoImpl extends BaseDaoImpl<Student> implements StudentDao
public class StudentDaoImpl extends BaseDaoImpl<Student> {
	public StudentDaoImpl(Context context) {
		super(new DBHelper(context));
	}
}
