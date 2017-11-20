package smartbi.jdbc;

import java.io.IOException;
import java.security.PrivilegedAction;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections.map.HashedMap;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hive.jdbc.HiveConnection;
import org.slf4j.Logger;

import smartbi.user.IUser;
import smartbi.usermanager.UserManagerModule;

public class HiveKerberosDriver extends org.apache.hive.jdbc.HiveDriver{
	//static Logger log = Logger.ge
	static Map<String, String> propMap = new HashedMap();
	static{
		Connection conn = JdbcUtil.getConn(false);
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select * from hivekrb_configer");
			while(rs.next()){
				propMap.put(rs.getString("name"), rs.getString("value"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			//log.error(e);
		}
	}
	
	public Connection connect(String url, Properties info) throws SQLException {
		IUser user = UserManagerModule.getInstance().getCurrentUser();		
		
		final Properties prop = new Properties();
		prop.put("user", user.getName());
		prop.put("password", user.getName());
		
		//===============kerberos====================
		String conf_path = propMap.get("kerberos.kdc.conf.path");
		String kerberos_realm = propMap.get("kerberos.realm.name");
		String principal_name = propMap.get("principal.name");
		String keytab_path = propMap.get("keytab.path");
		org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
	    conf.set("hadoop.security.authentication", "kerberos");
	    conf.set("java.security.krb5.kdc", conf_path);
	    conf.set("java.security.krb5.realm", kerberos_realm);
	    UserGroupInformation.setConfiguration(conf);
	    
	    //如果aegisuser不为空。从aegisuser 中读取用户加代理逻辑
	    //url=url+";principal="+ MyConstant.HIVE_KERBEROS_PRINCIPAL_NAME;
	    
	    /*
	    String principalName = MyConstant.HIVE_KERBEROS_PRINCIPAL_NAME;
	    String keyTabPath = MyConstant.HIVE_KERBEROS_KEYTAB_PATH;
	    if(aegisUser!=null){
	    	if (aegisUser.getUserType() != com.bdos.commons.utils.Constants.SUPERADMIN
					&& aegisUser.getUserType() != com.bdos.commons.utils.Constants.SYSTEMUSER){
	    		principalName = aegisUser.getUserName();
		    	keyTabPath = aegisUser.getKeytabFilePath();
			}
	    	
	    }
	    */
	    Connection conn = null;
	    final String fi_url =url;
	    
	    try {
	    	UserGroupInformation ugi = UserGroupInformation.loginUserFromKeytabAndReturnUGI(principal_name, keytab_path);
	    	//ugi.setConfiguration(conf);
	    	conn = ugi.doAs(new PrivilegedAction<Connection>() {
				@Override
				public Connection run() {
					Connection conn2 = null;
					try {
						
						return acceptsURL(fi_url) ? new HiveConnection(fi_url, prop) : null;
						//log.info("url {},user {},password {}",new Object[]{fi_url,user,password});
					} catch (SQLException e) {
						e.printStackTrace();
						//Object[] objects = { fi_url, user, password };
						//log.error("getConn connection error url:{}  user:{}  pwd:{}", objects);
					}
					return conn2;
				}
		   });
	    } catch (IOException e) {
			e.printStackTrace();
			//log.error("getConn set kerberos configure error");
		}
		return conn;
	}
	
	public static void main(String[] args) {
		Set<String> keySet = propMap.keySet();
		for (String name : keySet) {
			System.out.println(name + "  "+ propMap.get(name));
		}
	}
}
