package Machine.Application;

import java.sql.*;

/**
 * @author Mani Shah
 * @version 1.0
 * @since 7/28/2018  15:26
 */
class DatabaseConnect
{
	static Connection connector()
	{
		try{
			Class.forName("org.sqlite.JDBC");
			return DriverManager.getConnection("jdbc:sqlite:AccountDB.sqlite");
		} catch (Exception e) {
			e.printStackTrace();
			return null ;
		}
	}



}
