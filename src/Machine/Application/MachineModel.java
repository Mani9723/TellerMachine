package Machine.Application;

import java.sql.*;

/**
 * @author Mani Shah
 * @version 1.0
 * @since 7/28/2018  15:41
 */
class MachineModel
{
	private Connection connection;

	MachineModel(String user)
	{
		// Default
	}
	MachineModel()
	{
		connection = DatabaseConnect.connector();
		if(connection == null){
			System.out.println("Database not connected");
			System.exit(1);
		}
	}

	boolean isDbConnected() {
		try {
			return !connection.isClosed();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	boolean validateLogin(String user, String pass) throws SQLException
	{
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String query = "SELECT * from Customer_Information where username = ? and password = ?";

		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,user);
			preparedStatement.setString(2,pass);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				return true;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		finally {
			preparedStatement.close();
			resultSet.close();
		}
		return false;
	}

	boolean isUsernameUnique(String user)
	{


		return false;
	}



}
