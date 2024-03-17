import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
public class ImpostorOne extends MazeElement
{
	private boolean succeed;
	private static final String[] arrayImages = {"impostorALIVE.png", "impostorGHOST.png"};
	private BufferedImage[] images;
	private int killed;

	public ImpostorOne(Location loc, int size)
	{
		super(loc,size);
		succeed = false;
		killed = 0;
		images = new BufferedImage[arrayImages.length];
		System.out.print("here");
		for(int i = 0; i < arrayImages.length; i++)
		{
			try
			{
				images[i] = ImageIO.read(new File(arrayImages[i]));
			}
			catch(IOException e)
			{
				System.out.println("Image " + arrayImages[i] + " not loaded");
				e.printStackTrace();
			}
		}
	}


	//moving character
	public void moving(int key, char[][] maze)
	{
		int r = getL().getR();
		int c = getL().getC();
		
		boolean moved = false;
		
		while(!moved)
		{

			//direction possibilites
			int compass = (int)(Math.random()*4);
			if(compass==0)
			{
				if(r>0 && maze[r-1][c]==' ')//up
				{
					getL().incR(-1);
					moved=true;
				}
			}
			
			if(compass==1)//right
			{
				if(c<maze[0].length-1 && maze[r][c+1]==' ')
				{
					getL().incC(+1);
					moved=true;
				}
			}
			
			if(compass==2)//down
			{
				if(r<maze.length-1 && maze[r+1][c]==' ')
				{
					getL().incR(+1);
					moved=true;
				}
			}
			
			if(compass==3)//left
			{
				if(c>0 && maze[r][c-1]== ' ')
				{
					getL().incC(-1);
					moved=true;
				}
			}
		}
	}


	//loading images, kills, and success
	@Override
	public BufferedImage getImg()
	{
		return images[killed];
	}
	public void killed()
	{
		killed++;
		if(killed>1)
		{
			killed = 1;
		}
	}
	public void suceed()
	{
		succeed = true;
	}
	public boolean allow()
	{
		return succeed;
	}
	public static void main(String[] args)
	{
	}

}