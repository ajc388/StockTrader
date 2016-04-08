package DataModel;

public class Rank {
	public int rank;
	public String ticker;
	
	public Rank(int rank, String ticker)
	{
		this.rank = rank;
		this.ticker = ticker;
	}
	
	@Override
	public String toString()
	{
		if (rank > 0)
			return "Buy Alert,"+rank+","+ticker;
		else if ( rank == 0)
			return "Stagnant Trade "+ticker;
		else
			return "Sell Alert,"+rank+","+ticker;
	}
}
