package Machine.Application;

import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * @author Mani Shah
 * @version 1.0
 * @since 7/28/2018  15:41
 */
final class MachineModel
{
	private Connection connection;

	MachineModel()
	{
		connection = DatabaseConnect.connector("AccountDB.sqlite");
		if(connection == null){
			System.out.println("Database not connected");
			System.exit(1);
		}
		try {
			checkIfTableExists();
		}catch (SQLException e){
			e.printStackTrace();
		}
	}

	private void checkIfTableExists() throws SQLException
	{
		DatabaseMetaData databaseMetaData = connection.getMetaData();
		ResultSet resultSet = databaseMetaData.getTables(null, null,
				"Customer_Information", null);
		if(!resultSet.next()){
			createTable();
		}
		resultSet.close();
	}

	boolean isDbConnected() {
		try {
			return !connection.isClosed();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	void createTable() throws SQLException
	{
//		String query = "CREATE TABLE IF NOT EXISTS Customer_Information (\n" +
//				"`Username TEXT PRIMARY KEY NOT NULL UNIQUE,\n" +
//				"`Password TEXT NOT NULL,\n" +
//				"`Firstname TEXT NOT NULL,\n" +
//				"`Lastname TEXT NOT NULL,\n" +
//				"`Currentbalance TEXT NOT NULL UNIQUE,\n" +
//				"`DateCreated TEXT NOT NULL,\n" +
//				"`PersonalDBPath TEXT NOT NULL,\n" +
//				");";

		String query = "CREATE TABLE IF NOT EXISTS Customer_Information (\n"
				+ "	Username text PRIMARY KEY NOT NULL UNIQUE,\n"
				+ "	Password text NOT NULL,\n"
				+ "	Firstname text NOT NULL,\n"
				+ "	Lastname text NOT NULL,\n"
				+ "	CurrentBalance text NOT NULL,\n"
				+ "	DateCreated text NOT NULL,\n"
				+ "	PersonalDBPath text\n"
				+ ")";
		PreparedStatement preparedStatement = null;

		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			assert preparedStatement != null;
			preparedStatement.close();
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
			assert preparedStatement != null;
			preparedStatement.close();
			assert resultSet != null;
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
			assert preparedStatement != null;
			preparedStatement.close();
			assert resultSet != null;
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
			if(preparedStatement != null && resultSet != null) {
				preparedStatement.close();
				resultSet.close();
			}
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
			assert preparedStatement != null;
			preparedStatement.close();
		}
	}


	void updateBalance(String balance, String user) throws SQLException
	{
		PreparedStatement preparedStatement = null;

		String query = "UPDATE Customer_Information set CurrentBalance = ? where Username = ?";

		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,balance);
			preparedStatement.setString(2,user);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			assert preparedStatement != null;
			preparedStatement.close();
		}
	}

	private String getDate()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		java.util.Date date = new java.util.Date();
		return dateFormat.format(date);
	}

}
