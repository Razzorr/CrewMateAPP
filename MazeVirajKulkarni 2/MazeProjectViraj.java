import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
public class MazeProjectViraj extends JPanel implements KeyListener, ActionListener
{
	private JFrame frame;
	private int size = 30, width = 1500, height = 1000;
	private char[][] maze;
	private Timer t;
	private MazeElement finish;
	private Explorer explorer;
	private ImpostorOne ImpostorOne;
	boolean nextMaze = false;
	private Knife knife;
	int numMazes = 0;


	public MazeProjectViraj(){
		//Maze variables
		setBoard("maze0.txt");
		frame=new JFrame("AMONGUS MAZE PROGRAM");
		frame.setSize(width,height);
		frame.add(this);
		frame.addKeyListener(this);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		t = new Timer(500, this);  // will trigger actionPerformed every 500 ms
		t.start();
	}

	// All Graphics handled in this method.  Don't do calculations here
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2=(Graphics2D)g;
		g2.setColor(Color.BLACK);
		g2.fillRect(0,0,frame.getWidth(),frame.getHeight());

		for(int r=0;r<maze.length;r++){
			for(int c=0;c<maze[0].length;c++){

				g2.setColor(Color.BLUE);
				if (maze[r][c]=='#')
					g2.fillRect(c*size+size,r*size+size,size,size); //Wall
				else{
					g2.setColor(Color.RED);
					g2.drawRect(c*size+size,r*size+size,size,size);  //Open

			     }

				//paint MazeElements
				Location here = new Location(r,c);
				if (here.equals(finish.getL())){
					g2.drawImage(finish.getImg(), c*size+size,r*size+size,size,size,null, this);
				}
				if (here.equals(knife.getL())){
					g2.drawImage(knife.getImg(), c*size+size,r*size+size,size,size,null, this);
				}
				if(here.equals(explorer.getL()))
				{
					g2.drawImage(explorer.getImg(), c*size+size,r*size+size,size,size,null, this);
				}
				if(here.equals(ImpostorOne.getL()))
				{
					g2.drawImage(ImpostorOne.getImg(), c*size+size,r*size+size,size,size,null, this);
				}
			}
		}

		// Display at bottom of page
		int hor = size;
		int vert = maze.length*size+ 2*size;
		int kills0 = 0;
		g2.setFont(new Font("Arial",Font.BOLD,20));
		g2.setColor(Color.PINK);
		g2.drawString(explorer.getSpaces() + " Spaces Moved",hor,vert);
		
		
		if(!explorer.kills()  &&  ImpostorOne.interacts(explorer)){
			g2.drawString("\n\n\nDIED! SPAWNING BACK!",hor,(vert+70));
		} else if(ImpostorOne.interacts(explorer)) {
			kills0++;
			g2.drawString("KILLED IMPOSTOR! KILLS:" + kills0 ,hor,(vert+70));
			g2.setColor(Color.GREEN);
		}

		if(nextMaze&&(numMazes==0||numMazes==1))
		{
			g2.setColor(Color.RED);
			g2.drawString("\n\nCOMPLETED SELECTED MAZE --> PRESS ANY BUTTON TO CONTINUE",hor,(vert+70));
			
			g2.setColor(Color.WHITE);
		}
		if(nextMaze&&numMazes==2)
		{
			g2.setColor(Color.GREEN);
			g2.drawString("\nFINISHED MAZE!",hor,(vert+70));
			
			g2.setColor(Color.PINK);
		}
	}


	public void keyPressed(KeyEvent e){

		//set next mazes and movements
		if(!nextMaze)
		{
			explorer.move(e.getKeyCode(),maze);
		}

		//switching from mazes
		if(nextMaze && numMazes==0)
		{
			setBoard("maze1.txt");
			nextMaze = false;
			numMazes++;
		}
		else if(nextMaze&&numMazes==1)
		{
			setBoard("maze2.txt");
			nextMaze = false;
			numMazes++;
		}
		else if(nextMaze&&numMazes==2)
		{
			setBoard("amongusVictoryScreen.webp");
			nextMaze = false;
			numMazes++;
		}


		repaint();
		
		if(explorer.interacts(finish))
		{
			nextMaze = true;
		}

		//interaction with knife
		if(knife.interacts(explorer))
		{
			explorer.getKills();
			knife.taken();
		}

		//Interaction with enemy
		if(ImpostorOne.interacts(explorer))
		{


			
			ImpostorOne.killed();
			if(!explorer.kills())
			{
				explorer.addLives();
				if(numMazes == 0)
				{
					setBoard("maze0.txt");
				}
				if(numMazes == 1)
				{
					setBoard("maze1.txt");
				}
				if(numMazes == 2)
				{
					setBoard("maze2.txt");
				}
			}
		}
	}


	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}

	public void actionPerformed(ActionEvent e) 
	{
	
		for(int i = 0; i < maze.length; i++){
			
			if(!(ImpostorOne.interacts(explorer)) ) 
			{
				ImpostorOne.moving(0, maze);
				repaint();
			}
			
		}
				
	}

	public void setBoard(String fileName){
		//file input
		File file = new File(fileName);
		try{
			ArrayList<String[]> array = new ArrayList<String[]>();
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String text = "";
			while ((text = reader.readLine()) != null){
				array.add(text.split(""));
			}
			int row = array.size();
			int col = array.get(0).length;
			reader.close();

			//setting array size
			for(int i = 1; i < array.size(); i++)
			{
				if(col != array.get(i).length)
				{
					throw new IOException("--\n");
				}
			}

			//temp array
			char[][] array2 = new char[row][col];
			for(int x = 0; x<row; x++)
			{
				for(int y = 0; y<col; y++)
				{
					if((array.get(x)[y]).charAt(0) != ' ' && (array.get(x)[y]).charAt(0) != '#')
					{
					
						//Starting
        		  		if ((array.get(x)[y]).charAt(0) == 'S') {
							explorer = new Explorer(new Location(x,y),size);
							array2[x][y] = ' ';
						}
        		  			
						//Knife

        		  		if ((array.get(x)[y]).charAt(0) == 'K')
						{
							knife = new Knife(new Location(x,y),size);
							array2[x][y] = ' ';
						}


						//Impostor
        		  		if ((array.get(x)[y]).charAt(0) == 'I') {

							ImpostorOne = new ImpostorOne(new Location(x,y),size);
							array2[x][y] = ' ';
						}

						//Victory

						if ((array.get(x)[y]).charAt(0) == 'V') {
							finish = new MazeElement(new Location(x,y),size,"finishline.png");
							array2[x][y] = ' ';
						}
        		  			
							
					}
					else
					{
						array2[x][y] = (array.get(x)[y]).charAt(0);
					}
				}
			}
			maze = array2;
		}catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void main(String[] args){
		MazeProjectViraj maze  =new MazeProjectViraj();
	}
}