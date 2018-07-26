package Machine.AccountManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

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
	private final static String ACCOUNT_DIR_PATH = System.getProperty("user.dir").concat("\\src\\Log.txt");
	private final static String LOGIN_DIR_PATH = System.getProperty("user.dir").concat("\\src\\PassLog.txt");

	public CheckingAccount()
	{
		createFile(ACCOUNT_DIR_PATH);
		createFile(LOGIN_DIR_PATH);
		setCurrentBalance(initializeBalance());
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

	private double initializeBalance()
	{
		String balance = "";
		Scanner input = null;
		try {
			input = new Scanner(new File(ACCOUNT_DIR_PATH));
			while(input.hasNextLine())
				balance = input.nextLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		assert input != null;
		input.close();

		return balance.equalsIgnoreCase("") ? 0.00
				: Double.parseDouble(balance);
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
		logChange();
	}

	public void withdraw(double amount)
	{
		setCurrentBalance(showBalance() - amount);
		logChange();
	}

	private void logChange()
	{
		PrintWriter outputStream = null;
		try{
			outputStream = new PrintWriter(new FileOutputStream(ACCOUNT_DIR_PATH,true));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		assert outputStream != null;
		outputStream.append("\n").append(Double.toString(showBalance()));
		outputStream.close();
	}
}
