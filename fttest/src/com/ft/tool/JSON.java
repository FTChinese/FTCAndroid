package com.ft.tool;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ft.model.Article;
import com.ft.model.dao.impl.ArticleDaoImpl;
import com.ft.tool.ahibernate.demo.MainActivity;
import com.ft.tool.ahibernate.demo.dao.impl.StudentDaoImpl;
import com.ft.tool.ahibernate.demo.dao.impl.TeacherDaoImpl;
import com.ft.tool.ahibernate.demo.model.Student;
import com.ft.tool.ahibernate.demo.model.Teacher;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class JSON {

	public static void clearArticleMoth(Context ctx) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1); // �õ�ǰһ����
		long cleartime = calendar.getTimeInMillis();
		ArticleDaoImpl articleDao = new ArticleDaoImpl(ctx);
//		 articleDao.execSql("DELETE FROM t_article", null);
		articleDao.execSql("DELETE FROM t_article  WHERE intodate < "+ cleartime, null);
	}

	public static Article getArticleUrl(String path, Context ctx) {
		clearArticleMoth(ctx);
		ArticleDaoImpl articleDao = new ArticleDaoImpl(ctx);
		try {

			String json = null;
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();// ����HttpURLConnection����,���ǿ��Դ������л�ȡ��ҳ����.
			conn.setConnectTimeout(5 * 1000); // ��λ�Ǻ��룬���ó�ʱʱ��Ϊ5��
			conn.setRequestMethod("GET"); // HttpURLConnection��ͨ��HTTPЭ������path·���ģ�������Ҫ��������ʽ,���Բ����ã���ΪĬ��ΪGET
			if (conn.getResponseCode() == 200) {// �ж��������Ƿ���200�룬����ʧ��
				InputStream is = conn.getInputStream(); // ��ȡ������
				byte[] data = readStream(is); // ��������ת�����ַ�����
				json = new String(data); // ���ַ�����ת�����ַ���
				JSONArray jsonArray = new JSONArray(json); // ����ֱ��Ϊһ��������ʽ�����Կ���ֱ��
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject item = jsonArray.getJSONObject(i); // ÿ����¼���ɼ���Object�������
					Article article = new Article();
					String article_id = item.getString("id");
					String cheadline = item.getString("cheadline");
					article.setArticle_id(article_id);
					article.setArticleUrl(item.getString("url"));
					article.setCheadline(cheadline);
					// article.setClongleadbody(item.getString("clongleadbody"));
					article.setPubdate(item.getInt("pubdate"));
					article.setType(0);
					article.setIntodate(System.currentTimeMillis());
					if (!articleDao.isExist(
							"select * from t_article where article_id = ? ",
							new String[] { article_id })) {
						Long aid = articleDao.insert(article);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		Article articles = null;
		List<Article> list3 = articleDao.rawQuery(
				"select * from t_article where type=0 Limit 1", null);
		for (Article articledata : list3) {
			articles = articledata;
		}
		return articles;
	}

	/**
	 * ��ȡ"������ʽ"��JSON���ݣ� ������ʽ��[{"id":1,"name":"С��"},{"id":2,"name":"Сè"}]
	 * 
	 * @param path
	 *            ��ҳ·��
	 * @return ����List
	 * @throws Exception
	 */
	public static List<Map<String, String>> getJSONArray(String path)
			throws Exception {
		String json = null;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();// ����HttpURLConnection����,���ǿ��Դ������л�ȡ��ҳ����.
		conn.setConnectTimeout(5 * 1000); // ��λ�Ǻ��룬���ó�ʱʱ��Ϊ5��
		conn.setRequestMethod("GET"); // HttpURLConnection��ͨ��HTTPЭ������path·���ģ�������Ҫ��������ʽ,���Բ����ã���ΪĬ��ΪGET
		if (conn.getResponseCode() == 200) {// �ж��������Ƿ���200�룬����ʧ��
			InputStream is = conn.getInputStream(); // ��ȡ������
			byte[] data = readStream(is); // ��������ת�����ַ�����
			json = new String(data); // ���ַ�����ת�����ַ���

			// ������ʽ��[{"id":1,"name":"С��","age":22},{"id":2,"name":"Сè","age":23}]
			JSONArray jsonArray = new JSONArray(json); // ����ֱ��Ϊһ��������ʽ�����Կ���ֱ��
														// ��android�ṩ�Ŀ��JSONArray��ȡJSON���ݣ�ת����Array
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject item = jsonArray.getJSONObject(i); // ÿ����¼���ɼ���Object�������
				int id = item.getInt("id"); // ��ȡ�����Ӧ��ֵ
				String name = item.getString("name");

				map = new HashMap<String, String>(); // ��ŵ�MAP����
				map.put("id", id + "");
				map.put("name", name);
				list.add(map);
			}
		}

		// ***********��������******************
		for (Map<String, String> list2 : list) {
			String id = list2.get("id");
			String name = list2.get("name");
			
		}
		return list;
	}

	/**
	 * ��ȡ"������ʽ"��JSON���ݣ�
	 * ������ʽ��{"total":2,"success":true,"arrayData":[{"id":1,"name"
	 * :"С��"},{"id":2,"name":"Сè"}]}
	 * 
	 * @param path
	 *            ��ҳ·��
	 * @return ����List
	 * @throws Exception
	 */
	public static List<Map<String, String>> getJSONObject(String path)
			throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();// ����HttpURLConnection����,���ǿ��Դ������л�ȡ��ҳ����.
		conn.setConnectTimeout(5 * 1000); // ��λ�Ǻ��룬���ó�ʱʱ��Ϊ5��
		conn.setRequestMethod("GET"); // HttpURLConnection��ͨ��HTTPЭ������path·���ģ�������Ҫ��������ʽ,���Բ����ã���ΪĬ��ΪGET
		if (conn.getResponseCode() == 200) {// �ж��������Ƿ���200�룬����ʧ��
			InputStream is = conn.getInputStream(); // ��ȡ������
			byte[] data = readStream(is); // ��������ת�����ַ�����
			String json = new String(data); // ���ַ�����ת�����ַ���

			// ������ʽ��{"total":2,"success":true,"arrayData":[{"id":1,"name":"С��"},{"id":2,"name":"Сè"}]}
			JSONObject jsonObject = new JSONObject(json); // ���ص�������ʽ��һ��Object���ͣ����Կ���ֱ��ת����һ��Object
			int total = jsonObject.getInt("total");
			Boolean success = jsonObject.getBoolean("success");
			Log.i("abc", "total:" + total + " | success:" + success); // ��������

			JSONArray jsonArray = jsonObject.getJSONArray("arrayData");// ������һ���������ݣ�������getJSONArray��ȡ����
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject item = jsonArray.getJSONObject(i); // �õ�ÿ������
				int id = item.getInt("id"); // ��ȡ�����Ӧ��ֵ
				String name = item.getString("name");

				map = new HashMap<String, String>(); // ��ŵ�MAP����
				map.put("id", id + "");
				map.put("name", name);
				list.add(map);
			}
		}

		// ***********��������******************

		for (Map<String, String> list2 : list) {
			String id = list2.get("id");
			String name = list2.get("name");
			Log.i("abc", "id:" + id + " | name:" + name);
		}
		return list;
	}

	/**
	 * ��ȡ���͸��ӵ�JSON���� ������ʽ�� {"name":"С��", "age":23,
	 * "content":{"questionsTotal":2, "questions": [ { "question":
	 * "what's your name?", "answer": "С��"},{"question": "what's your age",
	 * "answer": "23"}] } }
	 * 
	 * @param path
	 *            ��ҳ·��
	 * @return ����List
	 * @throws Exception
	 */
	public static List<Map<String, String>> getJSON(String path)
			throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();// ����HttpURLConnection����,���ǿ��Դ������л�ȡ��ҳ����.
		conn.setConnectTimeout(5 * 1000); // ��λ�Ǻ��룬���ó�ʱʱ��Ϊ5��
		conn.setRequestMethod("GET"); // HttpURLConnection��ͨ��HTTPЭ������path·���ģ�������Ҫ��������ʽ,���Բ����ã���ΪĬ��ΪGET
		if (conn.getResponseCode() == 200) {// �ж��������Ƿ���200�룬����ʧ��
			InputStream is = conn.getInputStream(); // ��ȡ������
			byte[] data = readStream(is); // ��������ת�����ַ�����
			String json = new String(data); // ���ַ�����ת�����ַ���

			/*
			 * ������ʽ�� {"name":"С��", "age":23, "content":{"questionsTotal":2,
			 * "questions": [ { "question": "what's your name?", "answer":
			 * "С��"},{"question": "what's your age", "answer": "23"}] } }
			 */
			JSONObject jsonObject = new JSONObject(json); // ���ص�������ʽ��һ��Object���ͣ����Կ���ֱ��ת����һ��Object
			String name = jsonObject.getString("name");
			int age = jsonObject.getInt("age");
			Log.i("abc", "name:" + name + " | age:" + age); // ��������

			JSONObject contentObject = jsonObject.getJSONObject("content"); // ��ȡ�����еĶ���
			String questionsTotal = contentObject.getString("questionsTotal"); // ��ȡ�����е�һ��ֵ
			Log.i("abc", "questionsTotal:" + questionsTotal); // ��������

			JSONArray contentArray = contentObject.getJSONArray("questions"); // ��ȡ�����е�����
			for (int i = 0; i < contentArray.length(); i++) {
				JSONObject item = contentArray.getJSONObject(i); // �õ�ÿ������
				String question = item.getString("question"); // ��ȡ�����Ӧ��ֵ
				String answer = item.getString("answer");

				map = new HashMap<String, String>(); // ��ŵ�MAP����
				map.put("question", question);
				map.put("answer", answer);
				list.add(map);
			}
		}

		// ***********��������******************

		for (Map<String, String> list2 : list) {
			String question = list2.get("question");
			String answer = list2.get("answer");
			Log.i("abc", "question:" + question + " | answer:" + answer);
		}
		return list;
	}

	/**
	 * ��������ת�����ַ�����
	 * 
	 * @param inputStream
	 *            ������
	 * @return �ַ�����
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inputStream) throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			bout.write(buffer, 0, len);
		}
		bout.close();
		inputStream.close();

		return bout.toByteArray();
	}
}