package com.ft.model.dao.impl;

import com.ft.model.Article;
import com.ft.model.util.FTDBHelper;
import com.ft.tool.ahibernate.dao.impl.BaseDaoImpl;

import android.content.Context;

//�������J2EE����һ��ϣ��֧�ֽӿڰ�,�������д������:
public class ArticleDaoImpl extends BaseDaoImpl<Article> {
	public ArticleDaoImpl(Context context) {
		super(new FTDBHelper(context));
	}
}
