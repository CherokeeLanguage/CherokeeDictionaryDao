package net.cherokeedictionary.util;

import java.util.Date;

public class DaoUtils {
	
	public static final JsonConverter json;
	
	static {
		json = new JsonConverter();
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
