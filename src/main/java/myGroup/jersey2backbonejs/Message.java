package myGroup.jersey2backbonejs;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Message {
	private int id;
	private String content;
	private String createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = sqlDate2jsDate(createTime);
	}

	static SimpleDateFormat fromFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.SSS"); // MySQL timestamp
	static SimpleDateFormat toFormat = new SimpleDateFormat(
			"yyyy/MM/dd HH:mm:ss"); // Javascript Date Class Argument

	public static String sqlDate2jsDate(String date) {
		try {
			return toFormat.format(fromFormat.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
}
