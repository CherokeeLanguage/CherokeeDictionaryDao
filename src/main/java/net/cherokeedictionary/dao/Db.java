package net.cherokeedictionary.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

import org.skife.jdbi.v2.tweak.ConnectionFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.ConnectionCustomizer;
import com.mchange.v2.log.MLevel;

public class Db implements ConnectionFactory, ConnectionCustomizer {

	public static void destroy() {
		ComboPooledDataSource tmp = pool;
		pool=null;
		try {
			tmp.resetPoolManager(true);
			tmp.hardReset();
			sleep(50);
		} catch (Exception e) {
		}
		try {
			tmp.close();
			sleep(50);
		} catch (Exception e) {
		}
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			Driver next = drivers.nextElement();
			try {
				DriverManager.deregisterDriver(next);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}

	private static boolean initDone = false;

	private static ComboPooledDataSource pool;

	private static String username;

	private static String password;

	static {
		System.setProperty("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
		System.setProperty("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", MLevel.WARNING.getSeverity());
		System.setProperty("com.mchange.v2.c3p0.management.ManagementCoordinator",
				"com.mchange.v2.c3p0.management.NullManagementCoordinator");
	}

	public Db() {
	}

	private static synchronized void init() {
		if (initDone) {
			return;
		}
		initConnectionPool();
		initDone = true;
	}

	private static synchronized void initConnectionPool() {
		loadDbPropertiesFile();
		
		if (pool!=null) {
			ComboPooledDataSource tmp = pool;
			pool=null;
			tmp.resetPoolManager(true);
			tmp.hardReset();
			tmp.close();
		}
		pool = new ComboPooledDataSource();
		pool.setUser(username);
		pool.setPassword(password);
	}

	private static void loadDbPropertiesFile() {
		Properties props = new Properties();
		InputStream is = new Object().getClass().getResourceAsStream("/db.properties");
		if (is==null) {
			is=Db.class.getResourceAsStream("/db.properties");
		}
		if (is==null) {
			throw new RuntimeException("Unable to read db.properties file! Put one into src/main/resources/.");
		}
		try {
			props.load(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		try {
			is.close();
		} catch (IOException e) {
		}
		username=props.getProperty("jdbc.username");
		password=props.getProperty("jdbc.password");
	}

	/**
	 * Returns a new pooled SQL Connection object.
	 * 
	 * @return
	 */
	public Connection openConnection() {
		if (!initDone) {
			init();
		}
		try {
			return pool.getConnection();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void onAcquire(Connection arg0, String arg1) throws Exception {
	}

	@Override
	public void onCheckIn(Connection arg0, String arg1) throws Exception {
	}

	@Override
	public void onCheckOut(Connection arg0, String arg1) throws Exception {
		arg0.setCatalog("likespreadsheets");
		arg0.setAutoCommit(true);
		arg0.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
	}

	@Override
	public void onDestroy(Connection arg0, String arg1) throws Exception {
	}
}
