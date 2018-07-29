package Machine.Application;

import java.sql.*;
import java.text.SimpleDateFormat;

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

	boolean isUsernameTaken(String user) throws SQLException
	{
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String query = "SELECT * from Customer_Information where username = ?";

		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,user);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				if(resultSet.getString("Username").equalsIgnoreCase(user))
					return true;
			}
		}catch (Exception e){
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			resultSet.close();
		}
		return false;
	}

	String getAccountInfo(String currUser, String colName ) throws SQLException
	{
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String query = "SELECT * from Customer_Information where username = ?";

		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,currUser);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				return resultSet.getString(colName);
			}
		}catch (Exception e){
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			resultSet.close();
		}
		return "Unknown";
	}


	void saveToDatabase(String...values) throws SQLException
	{
		PreparedStatement preparedStatement = null;

		String query = "INSERT into Customer_Information(username,password,firstname,lastname,currentbalance,datecreated)" +
				"VALUES(?,?,?,?,?,?)";

		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,values[0]);
			preparedStatement.setString(2,values[1]);
			preparedStatement.setString(3,values[2]);
			preparedStatement.setString(4,values[3]);
			preparedStatement.setString(5,"0.00");
			preparedStatement.setString(6,getDate());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
		}
	}


	private String getDate()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date date = new java.util.Date();
		return dateFormat.format(date);
	}

}
