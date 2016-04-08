package DataModel;

import java.util.Date;

public class Transaction {
	public Date date;
	public StockType type;
	private String ticker;
	
	public Transaction(Date date, String type, String ticker)
	{
		this.date = date;
		this.type = type.trim().toUpperCase() == StockType.BUY.name() ? StockType.BUY : StockType.SELL;
		this.ticker = ticker;
	}
	
	public static enum StockType {
		BUY, SELL
	}
	
	public String getTicker()
	{ 
		return ticker.trim().toUpperCase();
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
