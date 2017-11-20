package smartbi.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import smartbi.user.IUser;
import smartbi.usermanager.UserManagerModule;

public class TestDriver implements Driver {

	private static Driver jdbcDriver = null;

	public TestDriver() {
		try {
			jdbcDriver = new com.mysql.jdbc.Driver();
		} catch (SQLException e) {
		}
	}
	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		//String newURL = "jdbc:mysql://192.168.1.10:3060/northwind?useOldAliasMetadataBehavior=true&useUnicode=true&characterEncoding=GBK";
		String driverClass = "com.mysql.jdbc.Driver";
		try {
			ClassLoader clzLoader = (ClassLoader) Class.forName("smartbi.repository.DAOModule")
					.getDeclaredField("classLoader").get(null);
			Class Clazz = clzLoader == null ? Class.forName(driverClass) : clzLoader.loadClass(driverClass);
			Driver jdbcDriver = (Driver) Clazz.newInstance();
		} catch (Exception e) {

		}
		IUser user = UserManagerModule.getInstance().getCurrentUser();
		Properties prop = new Properties();
		prop.put("user", user.getName());
		prop.put("password", user.getName());
		//return DriverManager.getConnection(newURL , prop );
		return DriverManager.getConnection(url , prop );
	}

	@Override
	public boolean acceptsURL(String url) throws SQLException {
		return url.startsWith("jdbc:smartbi:hivekerberos:");
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
		return null;
	}

	@Override
	public int getMajorVersion() {
		return jdbcDriver.getMajorVersion();
	}

	@Override
	public int getMinorVersion() {
		return jdbcDriver.getMinorVersion();
	}

	@Override
	public boolean jdbcCompliant() {
		return jdbcDriver.jdbcCompliant();
	}
	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

}
