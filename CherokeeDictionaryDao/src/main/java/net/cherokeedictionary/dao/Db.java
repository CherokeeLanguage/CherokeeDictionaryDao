package net.cherokeedictionary.dao;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.skife.jdbi.v2.tweak.ConnectionFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.ConnectionCustomizer;
import com.mchange.v2.log.MLevel;
import com.mysql.jdbc.AbandonedConnectionCleanupThread;

public abstract class Db implements ConnectionCustomizer, ConnectionFactory {

	public static void destroy() {
		try {
			AbandonedConnectionCleanupThread.shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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

	static {
		System.setProperty("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
		System.setProperty("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", MLevel.WARNING.getSeverity());
		System.setProperty("com.mchange.v2.c3p0.management.ManagementCoordinator",
				"com.mchange.v2.c3p0.management.NullManagementCoordinator");
	}

	protected Db() {
	}

	public ConnectionCustomizer connectionConfigClass() {
		return this;
	}

	private static final Object lock = new Object();

	private static synchronized void init() {
		if (initDone) {
			return;
		}
		initConnectionPool();
		initDone = true;
	}

	private static synchronized void initConnectionPool() {
		if (pool!=null) {
			ComboPooledDataSource tmp = pool;
			pool=null;
			tmp.resetPoolManager(true);
			tmp.hardReset();
			tmp.close();
		}
		pool = new ComboPooledDataSource();
		try {
			/*
			 * TomCat memory control.
			 */
			pool.setContextClassLoaderSource("library");
			pool.setDriverClass(com.mysql.jdbc.Driver.class.getCanonicalName());
			pool.setPrivilegeSpawnedThreads(true);
		} catch (PropertyVetoException e) {
		}
		pool.setConnectionCustomizerClassName(Db.class.getName());
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
		arg0.setCatalog("cherokeedictionary");
		arg0.setAutoCommit(true);
		arg0.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
	}

	@Override
	public void onDestroy(Connection arg0, String arg1) throws Exception {
	}
}
