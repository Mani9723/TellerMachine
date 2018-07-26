package Machine.Application;

//import BankAccountManager.files.hashPassword;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class ValidateAndWrite
{
	private static String user, pass;
	public static final String mainAccountPath = System.getProperty("user.dir")+"\\src\\BankAccountManager\\files\\accountFiles\\AccountFilePath";

	ValidateAndWrite()
	{
		//DEFAULT
	}
	ValidateAndWrite(String user, String pass)
	{
		ValidateAndWrite.user = user;
		ValidateAndWrite.pass = pass;
	}

	boolean validatePassword()
	{
		//hashPassword hash = new hashPassword("krsna",pass);
		//String securePassword = hash.toString();
		File file = new File(System.getProperty("user.dir")+"\\src\\BankAccountManager\\files\\accountFiles\\Password.txt");
		//return comparePassword(createScannerObject(file),securePassword,user);
		return true;
	}

	private Scanner createScannerObject(File file)
	{
		Scanner inputStream = null;
		try {
			inputStream = new Scanner(file);
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		}
		return inputStream;
	}

	private boolean comparePassword(Scanner inputStream,String securePassword, String user)
	{
		if (inputStream == null) throw new AssertionError();
		while (inputStream.hasNext()) {
			String tempLine = inputStream.nextLine();
			if(tempLine.contains(user) && tempLine.contains(securePassword)) {
				writeTempAccountFilePath(ValidateAndWrite.mainAccountPath, tempLine.split(",")[4]);
				inputStream.close();
				return true;
			}
		}
		inputStream.close();
		return false;
	}
	private void writeTempAccountFilePath(String filePath, String valueToWrite)
	{
		PrintWriter outputStream = null;
		try{
			outputStream = new PrintWriter(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		outputStream.write(valueToWrite);
		outputStream.flush();
		outputStream.close();
	}
	void appendToPasswordFile(String filePath,String content)
	{
		PrintWriter outputStream = null;
		try{
			outputStream = new PrintWriter(new FileOutputStream(filePath,true));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		outputStream.append("\n");
		outputStream.append(content);
		outputStream.flush();
		outputStream.close();

	}
	String retrieveCurrentAccountPath()
	{
		Scanner inputStream = createScannerObject(new File(getMainAccountPath()));
		return inputStream.nextLine();
	}

	private String getMainAccountPath()
	{
		return ValidateAndWrite.mainAccountPath;
	}

	boolean checkUserAvailability(String username)
	{
		Scanner inputStream =
				createScannerObject(new File(System.getProperty("user.dir")+"\\src\\BankAccountManager\\files\\accountFiles\\Password.txt"));
		while (inputStream.hasNext()){
			String line = inputStream.nextLine();
			if(username.equalsIgnoreCase(line.split(",")[0])){
				return false;
			}
		}
		inputStream.close();
		return true;
	}


}
