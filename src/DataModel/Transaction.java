package DataModel;

import java.util.Date;

public class Transaction {
	public int tid;
	public User user;
	public Date date;
	public StockType type;
	private String ticker;
	
	public static enum StockType {
		BUY, SELL
	}
	
	public String getTicker()
	{ 
		return ticker.toUpperCase();
	}
	
	public void setTicker(String ticker) throws Exception
	{
		if (ticker.trim().toUpperCase().length() > 4) 
		{
			throw new Exception("Ticker must be less than or equal to 4 characters.");
		} else {
			this.ticker = ticker;
		}
	}
	
	@Override
	public String toString()
	{
		return date.toString() + "," + type.toString() + "," + ticker;
	}
	
	
}
