package smartbi.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class BfdHiveDriver {
	public static void main(String[] args) {
		String jdbcURL = "jdbc:hive2://bgs-5p243-yanxufei:10000/default;principal=hive/bgs-5p243-yanxufei@BFD.COM;";
		try {
//			Thread.sleep(30 * 1000);
			
			Class.forName("smartbi.jdbc.HiveKerberosDriver");
//			Class.forName("org.apache.hive.jdbc.HiveDriver");
			Connection conn = DriverManager.getConnection(jdbcURL);
			
			System.out.println(conn.getCatalog());
			System.out.println("----------------------");
			
			DatabaseMetaData mtd = conn.getMetaData();
			ResultSet scrs = mtd.getSchemas();
			while(scrs.next()){
				//table_schem, table_catalog
//				String t_schem = scrs.getString("table_schem");
//				String t_catalog = scrs.getString("table_catalog");
				String t_schem = scrs.getString("TABLE_SCHEM");
//				System.out.println(t_name +"  "+t_type+"  "+ t_schem);
				System.out.println(t_schem);
			}
			
			System.out.println("----------------------");
			
//			ResultSet rst = mtd.getTables(conn.getCatalog(), "han_test01", "han", new String[]{"TABLE","VIEW"});
			ResultSet rst = mtd.getTables(conn.getCatalog(), "default", null, new String[]{"TABLE","VIEW"});
			while(rst.next()){
				String t_name = rst.getString("TABLE_NAME");
				String t_type = rst.getString("TABLE_TYPE");
				String t_schem = rst.getString("TABLE_SCHEM");
//				String t_name = rst.getObject(1).toString();
//				String t_type = rst.getObject(2).toString();
//				String t_schem = rst.getObject(3).toString();
				System.out.println(t_name + "  "+ t_type + "  " + t_schem);
				//System.out.println(rst.toString());
				
			}
			
			System.out.println("----------------------");
			
			scrs.close();
			rst.close();
		    //stmt.close();
		    conn.close();
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
