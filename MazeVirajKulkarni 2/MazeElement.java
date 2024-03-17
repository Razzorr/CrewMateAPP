
import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
public class MazeElement
{
	private Location l;
	private int s;
	private BufferedImage img;


	//l = location
	//s = size
	public MazeElement(Location l, int s)
	{

		//setting location and size
		this.l=l;
		this.s=s;

		
	}

	public MazeElement(Location loc, int size,String imgString)
	{

		//loading size and location for file
		//creates function to use in  main class
		this.l=loc;
		this.s=size;
		try {
			img = ImageIO.read(new File(imgString));
		} catch (IOException e) {
				System.out.println("--\n");
		}
	}

	public BufferedImage getImg()
	{
		return img;
	}
	public Location getL()
	{
		return l;
	}

	public int getS()
	{
		return s;
	}

	//interaction function
	public boolean interacts(MazeElement other){

		return this.l.equals(other.l);
	}

	public void move(int key, char[][] maze) {} // DOES NOTHING BY DEFAULT COMPLETE FOR MOVING ELEMENTS


}