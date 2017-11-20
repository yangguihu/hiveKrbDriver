package smartbi.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class MysqlTest {
	
	public static void main(String[] args) {
		
	    Properties prop = new Properties();  
	    try {
	    	InputStream in = MysqlTest.class.getClassLoader().getResourceAsStream("db.properties"); 
			prop.load(in);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			
			
			//String url="jdbc:mysql://172.24.5.243:3306/test?zeroDateTimeBehavior=convertToNull";
			//String username="root";
			//String password="root";
			
			String url=prop.getProperty("url");
			String username=prop.getProperty("username");
			String password=prop.getProperty("password");
			//System.out.println( url + " "+username + " "+password);
			
			Class.forName("com.mysql.jdbc.Driver");
			//Class.forName("org.apache.hive.jdbc.HiveDriver");
			Connection conn = DriverManager.getConnection(url,username,password);
			DatabaseMetaData mtd = conn.getMetaData();
			ResultSet scrs = mtd.getSchemas();
			while(scrs.next()){
				//table_schem, table_catalog
				String t_name = scrs.getString("table_schem");
				String t_catalog = scrs.getString("table_catalog");
	//			String t_schem = scrs.getString("TABLE_SCHEM");
	//			System.out.println(t_name +"  "+t_type+"  "+ t_schem);
				System.out.println(t_name +"  "+t_catalog);
			}
			
			System.out.println("----------------------");
			
	//		ResultSet rst = mtd.getTables(conn.getCatalog(), "han_test01", "han", new String[]{"TABLE","VIEW"});
			ResultSet rst = mtd.getTables(conn.getCatalog(), null, null, new String[] {"TABLE"});
			while(rst.next()){
				//ystem.out.println("fhiafahfahf");
				String t_name = rst.getString("TABLE_NAME");
				String t_type = rst.getString("TABLE_TYPE");
				String t_schem = rst.getString("TABLE_SCHEM");
	//			String t_name = rst.getObject(1).toString();
	//			String t_type = rst.getObject(2).toString();
	//			String t_schem = rst.getObject(3).toString();
				System.out.println(t_name + "  "+ t_type + "  " + t_schem);
				//System.out.println(rst.toString());
				
			}
			
			System.out.println("----------------------");
			
//			Statement cstmt = conn.createStatement();
//		    ResultSet rs = cstmt.executeQuery("select * from hh");
//		    while (rs.next()) {
//		        System.out.println(rs.getString(1)+"   "+rs.getString(2));
//		    }
//		    rs.close();
//		    cstmt.close();
			
			
			scrs.close();
			rst.close();
		    //stmt.close();
		    conn.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
