package net.cherokeedictionary.util.conversion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.cherokeedictionary.dao.Db;

public class Main {

	public static void main(String[] args) {
		Db db = new Db();
		
		try (Connection conn = db.openConnection()) {
			ResultSet r = conn.createStatement().executeQuery("show tables");
			while (r.next()) {
				System.out.println("Table: "+r.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
