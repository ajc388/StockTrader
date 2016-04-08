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
			return rank+",SELL,"+ticker;
		else if ( rank == 0)
			return "NEUTRAL,"+ticker;
		else
			return (rank*-1)+",SELL,"+ticker;
	}
}
