package net.cherokeedictionary.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.cherokeedictionary.dao.DaoCherokeeDictionary;
import net.cherokeedictionary.dao.Db;

public class Migrate {

	public static void main(String[] args) {
		DaoCherokeeDictionary.Util.init();
	}

}
