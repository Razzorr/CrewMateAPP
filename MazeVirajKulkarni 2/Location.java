public class Location
{
	private int row;
	private int col;

	public Location(int r, int c)//location class
	{
		row = r;
		col = c;
	}

	public int getR()
	{
		return row;
	}

	public int getC()
	{
		return col;
	}

	public void incR(int x)
	{
		row+=x;
	}

	public void incC(int x)
	{
		col+=x;
	}

	public void set(int newR, int newC)
	{
		row = newR;
		col = newC;
	}

	public boolean equals(Location other)
	{
		if(row == other.getR() && col == other.getC())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static void main(String []args)
	{

	}
}