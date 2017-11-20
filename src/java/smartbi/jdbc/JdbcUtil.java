package smartbi.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JdbcUtil {
	static Properties prop =null;
	static{
		prop = new Properties();
		InputStream in = JdbcUtil.class.getClassLoader().getResourceAsStream("db.properties");
		try {
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//获取jdbc连接
	public static Connection getConn(boolean isMeta) {
		Connection conn = null;
		String driver="";
		String url="";
		String username="";
		String pwd = "";
		if(isMeta){
			driver=prop.getProperty("meta_driver");
			url=prop.getProperty("meta_url");
			username=prop.getProperty("meta_username");
			pwd = prop.getProperty("meta_password");
		}else{
			driver=prop.getProperty("driver");
			url=prop.getProperty("url");
			username=prop.getProperty("username");
			pwd = prop.getProperty("password");
		}
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, pwd);
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
        return conn;
	}
		
	//关闭连接
	public static void close(Connection conn, Statement st, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (st != null) {
					st.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
