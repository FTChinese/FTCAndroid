package com.ft.model;

import com.ft.tool.ahibernate.annotation.Column;
import com.ft.tool.ahibernate.annotation.Id;
import com.ft.tool.ahibernate.annotation.Table;

@Table(name = "t_article")
public class Article {
	@Id
	@Column(name = "id")
	private int id; // 主键,int类型,数据库建表时此字段会设为自增长

	@Column(name = "article_id")
	private String article_id; // 年龄一般是数值,用type = "INTEGER"规范一下吧.
	@Column(name = "cheadline")
	private String cheadline;// 标题
	@Column(name = "pubdate")
	private int pubdate;// 发布时间
	@Column(name = "articleUrl")
	private String articleUrl;// URL
	@Column(name = "clongleadbody")
	private String clongleadbody;// URL
	@Column(name = "type", length = 2, type = "INTEGER")
	private int type;// type 0：默认为0，没有推送过，1为推送过

	@Column(name = "intodate")
	private long intodate;// intodate插入时间

	public long getIntodate() {
		return intodate;
	}

	public void setIntodate(long intodate) {
		this.intodate = intodate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getArticle_id() {
		return article_id;
	}

	public void setArticle_id(String article_id) {
		this.article_id = article_id;
	}

	public String getCheadline() {
		return cheadline;
	}

	public void setCheadline(String cheadline) {
		this.cheadline = cheadline;
	}

	public int getPubdate() {
		return pubdate;
	}

	public void setPubdate(int pubdate) {
		this.pubdate = pubdate;
	}

	public String getArticleUrl() {
		return articleUrl;
	}

	public void setArticleUrl(String articleUrl) {
		this.articleUrl = articleUrl;
	}

	public String getClongleadbody() {
		return clongleadbody;
	}

	public void setClongleadbody(String clongleadbody) {
		this.clongleadbody = clongleadbody;
	}

}
