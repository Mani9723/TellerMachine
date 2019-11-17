package Machine.Application.Controllers.Model;

import Machine.Application.Controllers.StatementData;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 *
 * This class handles all the database enquiries made by the program.
 *
 * @author Mani Shah
 * @version 1.0
 * @since 7/28/2018  15:41
 */
@SuppressWarnings({"ConstantConditions", "TryFinallyCanBeTryWithResources"})
public final class MachineModel
{

	private Connection connection;
	private ObservableList<StatementData> observableList = FXCollections.observableArrayList();

	public MachineModel()
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

	/**
	 * Method verifies that the database for the application exists.
	 * If it does not exist then it implies that this is a first time use
	 * and creates a new one in the same directory.
	 *
	 * @throws SQLException - SQL Exception
	 */
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

	/**
	 * Makes sure that the databse is connected.
	 * @return True if Connected
	 */
	@SuppressWarnings("unused")
	public boolean isDbConnected() {
		try {
			return !connection.isClosed();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Creates the main table with all the different users.
	 * General Table with all the users that are currently registered.
	 * Only contains the summary not the account transactions of any such detail.
	 * @throws SQLException - Exception
	 */
	private void createMainTable() throws SQLException
	{
		String query = "CREATE TABLE IF NOT EXISTS Customer_Information (\n"
				+ "	Username text PRIMARY KEY NOT NULL UNIQUE,\n"
				+ "	Password text NOT NULL,\n"
				+ "	Firstname text NOT NULL,\n"
				+ "	Lastname text NOT NULL,\n"
				+ "	CurrentBalance text NOT NULL,\n"
				+ "	AccountNumber text NOT NULL,\n"
				+ "	DateCreated text NOT NULL,\n"
				+ "	Email text NOT NULL,\n"
				+ "	TempPassword text,\n"
				+ "	ExpireTime text\n"
				+ ")";
		createPrepStmtExecute(query);
		System.out.println("Table created Customer_Information");

	}

	/**
	 * General method that generates a SQL statement to be used for queries.
	 * @param query - String query
	 * @throws SQLException - Exception
	 */
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

	/**
	 * Specific database table for each user.
	 * THis table details the transaction history of the user.
	 * @param user - Specific user
	 * @throws SQLException - Exception
	 */
	public void createStatementTable(String user) throws SQLException
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

	/**
	 *
	 * @param username
	 * @return
	 */
	public ObservableList<StatementData> getStatement(String username)
	{
		String query = "SELECT * from "+ username;

		try (Connection connection = DatabaseConnect.connector("AccountDB.sqlite")) {
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			if (connection != null) preparedStatement = connection.prepareStatement(query);
			if (preparedStatement != null) resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				observableList.add(new StatementData(resultSet.getString("Date"), resultSet.getString("Type"),
						"$"+resultSet.getString("Amount"), "$"+resultSet.getString("PreviousBalance"),
						"$"+resultSet.getString("CurrentBalance")));
			}
			return observableList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void deleteUser(String username) throws SQLException
	{
		PreparedStatement preparedStatement;
		String query = "DELETE FROM Customer_Information where username = ?";

		preparedStatement = connection.prepareStatement(query);
		preparedStatement.setString(1,username);
		preparedStatement.execute();

		deleteUserTable(username);
	}

	private void deleteUserTable(String username) throws SQLException
	{
		String query = "DROP TABLE " + username;
		PreparedStatement preparedStatement;

		preparedStatement = connection.prepareStatement(query);
		preparedStatement.execute();
	}

	public boolean validateLogin(String user, String pass) throws SQLException
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

	public boolean isFirstTimeRunning()
	{
		String query = "SELECT * from Customer_Information";
		try (PreparedStatement preparedStatement = connection.prepareStatement(query)
		     ;ResultSet resultSet = preparedStatement.executeQuery()) {
			if (!resultSet.isBeforeFirst()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean isUsernameTaken(String user) throws SQLException
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

	public String getAccountInfo(String currUser, String colName ) throws SQLException
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
	public String getUsername(String email ) throws SQLException
	{
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String query = "SELECT * from Customer_Information WHERE Email = ?";

		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,email);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next()){
				return resultSet.getString("Username");
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

	public String verfiyEmailAddres(String request) throws SQLException
	{
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String query = "SELECT * from Customer_Information WHERE Email = ?";
		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, request);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next())
				return resultSet.getString("Email");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			resultSet.close();
		}
		return "Unknown";
	}

	public boolean emailAlreadyExists(String request) throws SQLException
	{
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String query = "SELECT * from Customer_Information WHERE Email = ?";
		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, request);
			resultSet = preparedStatement.executeQuery();
			if(resultSet.next())
				if(resultSet.getString("Email").equalsIgnoreCase(request))
					return true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			preparedStatement.close();
			resultSet.close();
		}
		return false;
	}

	public void updateNewPassword(String user, String newPass) throws SQLException
	{
		PreparedStatement preparedStatement = null;

		String query = "UPDATE Customer_Information set Password = ?, TempPassword = ?, " +
				"ExpireTime = ? where Username = ?";

		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,newPass);
			preparedStatement.setString(2,"");
			preparedStatement.setString(3,"");
			preparedStatement.setString(4,user);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			assert preparedStatement != null;
			preparedStatement.close();
		}
	}


	public void saveUserToMainDB(String...values) throws SQLException
	{
		PreparedStatement preparedStatement = null;

		String query = "INSERT into Customer_Information(username,password,firstname,lastname,currentbalance,datecreated,email,AccountNumber)" +
				"VALUES(?,?,?,?,?,?,?,?)";

		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,values[0]);
			preparedStatement.setString(2,values[1]);
			preparedStatement.setString(3,values[2]);
			preparedStatement.setString(4,values[3]);
			preparedStatement.setString(5,"0.00");
			preparedStatement.setString(6,getDate(true));
			preparedStatement.setString(7,values[4]);
			preparedStatement.setString(8,generateAccountNumber());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			assert preparedStatement != null;
			preparedStatement.close();
		}
	}

	private String generateAccountNumber()
	{
		int minimum = 1000, maximum = 9999;
		String accNum = "40866870";
		Random rand = new Random();
		int num = minimum + rand.nextInt((maximum - minimum) + 1);
		accNum += Integer.toString(num);
		return accNum;
	}


	public void updateBalanceMainDB(String balance, String user) throws SQLException
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

	public void updateTempPassCells(String user, String tempPass, String time) throws SQLException
	{
		PreparedStatement preparedStatement = null;

		String query = "UPDATE Customer_Information set TempPassword = ?, ExpireTime = ? where Username = ?";

		try{
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,tempPass);
			preparedStatement.setString(2,time);
			preparedStatement.setString(3,user);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			assert preparedStatement != null;
			preparedStatement.close();
		}
	}

	public void insertToStatementDB(String table, String type, String amount, String prevBal, String newBal) throws SQLException
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

	public PdfDocument saveStatementToPdf(String username, File file) throws SQLException, FileNotFoundException
	{
		String date, type, amount, prevBal,  currBal;
		String query = "SELECT * FROM " + username;
		PreparedStatement preparedStatement;
		ResultSet resultSet;
		preparedStatement = connection.prepareStatement(query);
		resultSet = preparedStatement.executeQuery();

		PdfWriter writer = new PdfWriter(file.getAbsoluteFile()+"/Statement.pdf");
		PdfDocument pdfDocument = new PdfDocument(writer);
		Document document = new Document(pdfDocument,PageSize.A4);
		Table table = new Table(5);

		Cell cell;

		document.add(new Paragraph("Bank Of American").setTextAlignment(TextAlignment.CENTER).setBold());
		document.add(new Paragraph("Transaction History: " + getAccountInfo(username,"FirstName").toUpperCase())
				.setTextAlignment(TextAlignment.CENTER).setBold());
		cell = new Cell().add(new Paragraph("Date")).setBold().setItalic();
		table.addCell(cell);
		cell = new Cell().add(new Paragraph("Type")).setBold().setItalic();
		table.addCell(cell);
		cell = new Cell().add(new Paragraph("Amount")).setBold().setItalic();
		table.addCell(cell);
		cell = new Cell().add(new Paragraph("Previous Balance")).setBold().setItalic();
		table.addCell(cell);
		cell = new Cell().add(new Paragraph("New Balance")).setBold().setItalic();
		table.addCell(cell);


		while(resultSet.next()){
			date = resultSet.getString("Date");
			cell = new Cell().add(new Paragraph(date)).setPadding(10).setHorizontalAlignment(HorizontalAlignment.CENTER);
			table.addCell(cell);

			type = resultSet.getString("Type");
			cell = new Cell().add(new Paragraph(type)).setPadding(10);
			table.addCell(cell);

			amount = resultSet.getString("Amount");
			cell = new Cell().add(new Paragraph("$"+amount)).setPadding(10);
			table.addCell(cell);

			prevBal = resultSet.getString("PreviousBalance");
			cell = new Cell().add(new Paragraph("$"+prevBal)).setPadding(10).setHorizontalAlignment(HorizontalAlignment.CENTER);
			table.addCell(cell);

			currBal = resultSet.getString("CurrentBalance");
			cell = new Cell().add(new Paragraph("$"+currBal)).setPadding(10);
			table.addCell(cell);
		}
		table.setHorizontalAlignment(HorizontalAlignment.CENTER);
		document.add(table);
		document.close();
		preparedStatement.close();
		resultSet.close();
		return document.getPdfDocument();
	}

	private String getDate(boolean time)
	{
		SimpleDateFormat dateFormat;
		dateFormat = time ? new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
				:new SimpleDateFormat("MM/dd/yyyy");

		return dateFormat.format(new Date());
	}
}
