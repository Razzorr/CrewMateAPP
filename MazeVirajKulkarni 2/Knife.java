
import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
public class Knife extends MazeElement
{
	private int taken;
	private static final String[] IMG_NAMES = {"knife.png", "blood.png"};
	private BufferedImage[] images;

	public Knife(Location loc, int size)
	{
		super(loc,size);
		taken = 0;//start untaken
		images = new BufferedImage[IMG_NAMES.length];
		System.out.print("here");
		for(int i = 0; i < IMG_NAMES.length; i++)
		{
			try
			{
				images[i] = ImageIO.read(new File(IMG_NAMES[i]));
			}
			catch(IOException e)
			{
				System.out.println("Image " + IMG_NAMES[i] + " not loaded");
				e.printStackTrace();
			}
		}
	}

	@Override
	public BufferedImage getImg()
	{
		return images[taken];
	}
	public void taken()
	{
		taken++;
		if(taken>1)
		{
			taken = 1;
		}
	}
	public static void main(String[] args)
	{
	}

}