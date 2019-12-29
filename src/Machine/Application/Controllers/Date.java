package Machine.Application.Controllers;

import java.text.SimpleDateFormat;

public class Date
{

	public String getDate()
	{
		return calcDate();
	}
	private String calcDate()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date date = new java.util.Date();
		return dateFormat.format(date);
	}
	public int getHour()
	{
		SimpleDateFormat hourformat = new SimpleDateFormat("HH");
		java.util.Date date = new java.util.Date();
		return Integer.parseInt(hourformat.format(date));
	}
}
