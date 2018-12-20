package Machine.Application.Controllers.Model;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author Mani Shah
 * @version 1.0
 * @since 7/28/2018  15:26
 */
final class DatabaseConnect
{
	static Connection connector(String file)
	{
		try{
			Class.forName("org.sqlite.JDBC");
			return DriverManager.getConnection("jdbc:sqlite:"+file);
		} catch (Exception e) {
			e.printStackTrace();
			return null ;
		}
	}



}
