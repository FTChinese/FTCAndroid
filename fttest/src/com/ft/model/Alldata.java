package com.ft.model;

import com.ft.tool.ahibernate.annotation.Column;
import com.ft.tool.ahibernate.annotation.Id;
import com.ft.tool.ahibernate.annotation.Table;

@Table(name = "t_alldata")
public class Alldata {
	@Id
	@Column(name = "id")
	private int id; // 主键,int类型,数据库建表时此字段会设为自增长

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
	private int type;// type 0：默认为0，推送30分钟检测， 1,推送状态
	@Column(name = "data")
	private String data;// 标题
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
