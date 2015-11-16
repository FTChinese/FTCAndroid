package com.ft.model;

import com.ft.tool.ahibernate.annotation.Column;
import com.ft.tool.ahibernate.annotation.Id;
import com.ft.tool.ahibernate.annotation.Table;

@Table(name = "t_alldata")
public class Alldata {
	@Id
	@Column(name = "id")
	private int id; // ����,int����,���ݿ⽨��ʱ���ֶλ���Ϊ������

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	@Column(name = "type", length = 2, type = "INTEGER")
	private int type;// type 0��Ĭ��Ϊ0������30���Ӽ�⣬ 1,����״̬
	@Column(name = "data")
	private String data;// ����
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
