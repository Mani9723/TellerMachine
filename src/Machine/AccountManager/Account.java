package Machine.AccountManager;

/**
 * Defines a general interface that must be implemented
 *
 * @author Mani Shah
 * @version 1.0
 * @since 7/22/2018  20:02
 */
public interface Account
{
	/**
	 * Shows Balance
	 * @return balance
	 */
	double showBalance();

	/**
	 * Deposit Money
	 * @param amount - double
	 */
	void deposit(double amount);

}
