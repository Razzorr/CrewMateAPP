import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
public class Explorer extends MazeElement
{
	private int compass;
	private int spaces;
	private boolean kills;
	private int lives;
	private static final String[] arrayImages = {"amongusUP.png","amongusRIGHT.png","amongusDOWN.png","amongusLEFT.png"};
	private BufferedImage[] images;

	public Explorer(Location loc, int size)
	{

		//setting location variables, directions, lives, kills
		super(loc,size);
		compass = 1;
		spaces = 0;
		lives = 0;
		kills = false;
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
				e.printStackTrace();
			}
		}
	}

	@Override
	public BufferedImage getImg()
	{
		return images[compass];
	}



	//Spaces
	public int getSpaces()
	{
		return spaces;
	}

	//Direction
	public int getCompass()
	{
		return compass;
	}


	//Kills
	public boolean kills()
	{
		return kills;
	}
	public void getKills()
	{
		kills = true;
	}

	//Lives
	public void addLives()
	{
		lives++;
	}
	public int getLives()
	{
		return lives;
	}
	


	

	@Override
	public void move(int key, char[][] maze) {


		//movement using int = wasd
		if(key == 37)
		{
			compass--;
			if(compass<0)
			{
				compass = 3;
			}
		}


		if(key == 39)
		{
			compass++;
			if(compass>3)
			{
				compass = 0;
			}
		}
		
		if(key == 87)
		{
			int r = getL().getR();
			int c = getL().getC();
			//differnt compass north south east west		
			if(compass == 1 && maze[r][c+1] == ' ')
			{
				spaces++;
				getL().incC(1);
			}
			if(compass == 2 && maze[r+1][c] == ' ')
			{
				spaces++;
				getL().incR(1);
			}
			if(compass == 3 && maze[r][c-1] == ' ')
			{
				spaces++;
				getL().incC(-1);
			}
			if(compass == 0 && maze[r-1][c] == ' ')
			{
				spaces++;
				getL().incR(-1);
			}
		}
		if(key == 83)
		{
			int r = getL().getR();
			int c = getL().getC();
			if(compass == 1 && maze[r][c-1] == ' ')
			{
				spaces++;
				getL().incC(-1);
			}
			if(compass == 2 && maze[r-1][c] == ' ')
			{
				spaces++;
				getL().incR(-1);
			}
			if(compass == 3 && maze[r][c+1] == ' ')
			{
				spaces++;
				getL().incC(1);
			}
			if(compass == 0 && maze[r+1][c] == ' ')
			{
				spaces++;
				getL().incR(1);
			}
		}
	} 

	public static void main(String[] args)
	{
	}

}