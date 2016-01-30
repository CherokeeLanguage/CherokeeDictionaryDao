package net.cherokeedictionary.util;

import java.util.Date;

public class DaoUtils {
	
	public static final JsonConverter json;
	
	static {
		json = new JsonConverter();
	}
	
	public static String properFormedPronunciation(String pronunciation) {
		pronunciation=pronunciation.replace("a.", "ạ");
		pronunciation=pronunciation.replace("e.", "ẹ");
		pronunciation=pronunciation.replace("i.", "ị");
		pronunciation=pronunciation.replace("o.", "ọ");
		pronunciation=pronunciation.replace("u.", "ụ");
		pronunciation=pronunciation.replace("v.", "ṿ");
		pronunciation=pronunciation.replace("A.", "Ạ");
		pronunciation=pronunciation.replace("E.", "Ẹ");
		pronunciation=pronunciation.replace("I.", "Ị");
		pronunciation=pronunciation.replace("O.", "Ọ");
		pronunciation=pronunciation.replace("U.", "Ụ");
		pronunciation=pronunciation.replace("V.", "Ṿ");
		pronunciation=pronunciation.replace("1", "¹");
		pronunciation=pronunciation.replace("2", "²");
		pronunciation=pronunciation.replace("3", "³");
		pronunciation=pronunciation.replace("4", "⁴");
		pronunciation=pronunciation.replace("?", "ɂ");
		return pronunciation;
	}
	
	public static java.sql.Timestamp asSqlTimestamp(Date date) {
		return date!=null?new java.sql.Timestamp(date.getTime()):null;
	}
	
	public static java.sql.Date asSqlDate(Date date) {
		return date!=null?new java.sql.Date(date.getTime()):null;
	}
	
	public static java.util.Date asDate(java.sql.Date sqlDate) {
		return sqlDate != null ? new java.util.Date(sqlDate.getTime()) : null;
	}

	public static java.util.Date asDate(java.sql.Timestamp sqlTimestamp) {
		return sqlTimestamp != null ? new java.util.Date(sqlTimestamp.getTime())
				: null;
	}
}
