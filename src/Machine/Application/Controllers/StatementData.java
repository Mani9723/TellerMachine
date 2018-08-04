package Machine.Application.Controllers;

import javafx.beans.property.SimpleStringProperty;

/**
 * @author Mani Shah
 * @version 1.0
 * @since 8/1/2018  23:03
 */
public class StatementData
{
	SimpleStringProperty date,type,amount,prevBal,currBal;

	public StatementData(String date, String type, String amount, String prevBal, String currBal)
	{
		this.date = new SimpleStringProperty(date);
		this.type = new SimpleStringProperty(type);
		this.amount = new SimpleStringProperty(amount);
		this.prevBal = new SimpleStringProperty(prevBal);
		this.currBal = new SimpleStringProperty(currBal);
	}

	public void setDate(String date)
	{
		this.date = new SimpleStringProperty(date);
	}

	public void setType(String type)
	{
		this.type = new SimpleStringProperty(type);
	}

	public void setAmount(String amount)
	{
		this.amount = new SimpleStringProperty(amount);
	}

	public void setPrevBal(String prevBal)
	{
		this.prevBal = new SimpleStringProperty(prevBal);
	}

	public void setCurrBal(String currBal)
	{
		this.currBal = new SimpleStringProperty(currBal);
	}

	public String getType()
	{
		return type.get();
	}

	public String getAmount()
	{
		return amount.get();
	}

	public String getPrevBal()
	{
		return prevBal.get();
	}

	public String getCurrBal()
	{
		return currBal.get();
	}

	public String getDate()
	{
		return date.get();
	}
}
