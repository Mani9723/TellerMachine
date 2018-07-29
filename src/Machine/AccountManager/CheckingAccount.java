package Machine.AccountManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Defines checking account for any user.
 *
 * @author Mani Shah
 * @version 1.0
 * @since 7/22/2018  20:06
 */
public class CheckingAccount implements Account
{
	private double currentBalance;

//	/**
//	 * Holds account information for current user.
//	 */
//	private static String ACCOUNT_DIR_PATH;

	private static String DATABASE_PATH;

	public CheckingAccount(boolean isFirstTime)
	{
		String directory = System.getProperty("user.dir");
		File file = new File(directory);
		if(file.mkdirs()){
			System.out.println("Directory " + directory + " created");
		}
		setLoginDirPath(directory+"\\AccountDB.sqlite");
		if(isFirstTime) {
			createFile(DATABASE_PATH);
		}
	}

//	public CheckingAccount(String userAccountName)
//	{
//		String directory = System.getProperty("user.dir").concat("\\src");
//		File file = new File(directory);
//		if(file.mkdirs()){
//			System.out.println("Directory " + directory + " created");
//		}
//		ACCOUNT_DIR_PATH = directory.concat(userAccountName).concat(".txt");
//		createFile(ACCOUNT_DIR_PATH);
//		//setCurrentBalance(initializeBalance());
//	}
	//DEFAULT
	public CheckingAccount()
	{
		String directory = System.getProperty("user.dir").concat("\\src");
		setLoginDirPath(directory+"\\AccountDB.sqlite");
	}

	private void createFile(String path)
	{
		File file = new File(path);
		if(!file.exists()){
			try{
				if(file.createNewFile())
					System.out.println("New File " + path);
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}

//	private double initializeBalance()
//	{
//		String balance = "";
//		Scanner input = null;
//		try {
//			input = new Scanner(new File(ACCOUNT_DIR_PATH));
//			while(input.hasNextLine())
//				balance = input.nextLine();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		assert input != null;
//		input.close();
//
//		return balance.equalsIgnoreCase("") ? 0.00
//				: Double.parseDouble(balance);
//	}

	private void setLoginDirPath(String path)
	{
		DATABASE_PATH = path;
	}

	private void setCurrentBalance(double balance)
	{
		this.currentBalance = balance;
	}

	@Override
	public double showBalance()
	{
		return currentBalance;
	}

	@Override
	public void deposit(double amount)
	{
		setCurrentBalance(showBalance() + amount);
		//logChange();
	}

	public void withdraw(double amount)
	{
		setCurrentBalance(showBalance() - amount);
		//logChange();
	}

//	private void logChange()
//	{
//		PrintWriter outputStream = null;
//		try{
//			outputStream = new PrintWriter(new FileOutputStream(ACCOUNT_DIR_PATH,true));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		assert outputStream != null;
//		outputStream.append("\n").append(Double.toString(showBalance()));
//		outputStream.close();
//	}

	public String getLoginDirPath()
	{
		return DATABASE_PATH;
	}
	//public String getAccountDirPath()
//	{
//		return ACCOUNT_DIR_PATH;
//	}
}
