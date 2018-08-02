package Machine.Application;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Mani Shah
 * @version 1.0
 * @since 7/28/2018  15:41
 */
final class MachineModel
{
	private Connection connection;
	private ObservableList<StatementData> observableList = FXCollections.observableArrayList();

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
			System.out.println("Empty Database Detected...\nCreating a new one...");
			createMainTable();
			System.out.println("Database AccountDB.sqlite created...");
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

	private void createMainTable() throws SQLException
	{

		String query = "CREATE TABLE IF NOT EXISTS Customer_Information (\n"
				+ "	Username text PRIMARY KEY NOT NULL UNIQUE,\n"
				+ "	Password text NOT NULL,\n"
				+ "	Firstname text NOT NULL,\n"
				+ "	Lastname text NOT NULL,\n"
				+ "	CurrentBalance text NOT NULL,\n"
				+ "	DateCreated text NOT NULL,\n"
				+ "	PersonalDBPath text\n"
				+ ")";
		createPrepStmtExecute(query);
		System.out.println("Table created Customer_Information");

	}

	private void createPrepStmtExecute(String query) throws SQLException
	{
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

	void createStatementTable(String user) throws SQLException
	{
		String query = "CREATE TABLE IF NOT EXISTS "+user+ " (\n"
				+ "	Date text NOT NULL,\n"
				+ "	Type text NOT NULL,\n"
				+ "	Amount text NOT NULL,\n"
				+ "	PreviousBalance text NOT NULL,\n"
				+ "	CurrentBalance text NOT NULL\n"
				+ ")";
		createPrepStmtExecute(query);
		System.out.println("Statement Table created: " + user);
	}

	ObservableList<StatementData> getStatement(String username) throws SQLException
	{
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		String query = "SELECT * from "+ username;
		Connection connection = DatabaseConnect.connector("AccountDB.sqlite");
		try {
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				observableList.add(new StatementData(resultSet.getString("Date"),resultSet.getString("Type"),
						resultSet.getString("Amount"),resultSet.getString("PreviousBalance"),
						resultSet.getString("CurrentBalance")));
			}
			return observableList;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			resultSet.close();
			preparedStatement.close();
			connection.close();
		}
		return null;
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

	boolean isFirstTimeRunning() throws SQLException
	{
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		String query = "SELECT * from Customer_Information";
		try {
			preparedStatement = connection.prepareStatement(query);
			resultSet = preparedStatement.executeQuery();
			if (!resultSet.isBeforeFirst()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			resultSet.close();
			preparedStatement.close();
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


	void saveUserToDatabase(String...values) throws SQLException
	{
		PreparedStatement preparedStatement = null;

		String query = "INSERT into Customer_Information(username,password,firstname,lastname,currentbalance,datecreated,email)" +
				"VALUES(?,?,?,?,?,?,?)";

		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,values[0]);
			preparedStatement.setString(2,values[1]);
			preparedStatement.setString(3,values[2]);
			preparedStatement.setString(4,values[3]);
			preparedStatement.setString(5,"0.00");
			preparedStatement.setString(6,getDate(true));
			preparedStatement.setString(7,values[4]);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			assert preparedStatement != null;
			preparedStatement.close();
		}
	}


	void updateMainDB(String balance, String user) throws SQLException
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

	void insertToStatementDB(String table, String type, String amount, String prevBal, String newBal) throws SQLException
	{
		PreparedStatement preparedStatement = null;
		String query = "INSERT into "+table+"(date,type,amount,previousbalance,currentbalance)" +
				"VALUES(?,?,?,?,?)";

		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,getDate(false));
			preparedStatement.setString(2,type);
			preparedStatement.setString(3,amount);
			preparedStatement.setString(4,prevBal);
			preparedStatement.setString(5,newBal);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			assert preparedStatement != null;
			preparedStatement.close();
		}

	}

	private String getDate(boolean time)
	{
		SimpleDateFormat dateFormat;
		dateFormat = time ? new SimpleDateFormat("MM/dd/yyyy hh:mm:ss")
				:new SimpleDateFormat("MM/dd/yyyy");

		return dateFormat.format(new Date());
	}



}
