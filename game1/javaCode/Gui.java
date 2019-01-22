/* Christian Barth 
The following code is for a program that:
1. Grabs the text from a.out in the above directory (the game results)
2. Displays each turn for the game using swing
3. reruns the game (through re-execution of run.sh)

*/

import javax.swing.*;
import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;
import javax.imageio.*;
import java.util.Arrays;

public class Gui extends JFrame{
	private board gameboard;	//A JPanel
	private JMenuBar menubar;       //for all things in the menubar

	private MenuHandler menuHandler;
	private ButtonPress buttonPress;//for the continue button,
					//is pressed every few minutes
					//by a timer for constant movement

	private JMenu start,quit;	//menu options to be displayed
	private JMenuItem config;	//within JMenu start
	private JMenuItem stop;		//within JMenu quit
	private FlowLayout layout;
	private JPanel[] rows;		//each row is a Jpanel that contains
					//all the buttons of a single row
					//of the playerpiece array

	private playerpiece[][] soldiers;       //an array of all the pieces
	private JButton Continue;	//the continue button that is 
					//linked with the buttonpress
					//handler

	BufferedReader reader;		//used to grab each line
	String line;			//holds lines outputted from reader

	private int xBoard;		//contains x dimension of field
	private int yBoard;		//contains y dimension of field

	private int flag;		//can be set to 1 within push button
					//when flag=1, the function rerun is
					//ran, running a new game

//sets up Gui and also contains the timer that issues a button press
//on continue every ~5 seconds
	public Gui()
	{

		super("WarSim");
		//sets up reading the file
		File Game= new File("../a.out");

		try{
		reader= new BufferedReader(new FileReader(Game));
		line= reader.readLine();	
		}
		catch(Exception ex)
		{
		line=null;
		}
		//line now contains the x dimension of the board

		//setting up the window itself
		gameboard= new board();
		menubar= new JMenuBar();
		start= new JMenu("Start");
		quit= new JMenu("Quit");
		config= new JMenuItem("Config");
		stop= new JMenuItem("Stop");
		Continue= new JButton("Continue");

		//adding menu items
		start.add(config);
		quit.add(stop);
		menubar.add(start);
		menubar.add(quit);
		this.setJMenuBar(menubar);
		this.add(gameboard);

		gameboard.setVisible(true);
		layout= new FlowLayout(FlowLayout.CENTER,3,3);

		//xBoard now contains the x dimension of the board
		//yBoard holds y dimension	
		xBoard=Integer.parseInt(line);
		try{line=reader.readLine();}catch(Exception ex){}
		yBoard=Integer.parseInt(line);
		
		//creates a 2D array of all the pieces that can occur
		//rows holds all rows of the gameboard
		soldiers=new playerpiece[yBoard][xBoard];
		rows= new JPanel[yBoard+1];
		menuHandler= new MenuHandler();
		buttonPress= new ButtonPress();

		//these rows are lying on top of one another
		//and will eventually contain all the playing pieces
		//making a grid of soldiers
		for(int y=0; y<yBoard+1;y++)
		{
			rows[y]= new Row();
			rows[y].setLayout(layout);
		}

		//set each solider to be an empty piece
		for(int y=0;y<yBoard;y++)
		{
			for(int x=0; x<xBoard;x++)
			{
			soldiers[y][x]=new playerpiece(0);

			}
		}


		rows[yBoard].add(Continue);
		gameboard.setLayout(new GridLayout(yBoard+1,0,0,0));

		//start adding all the rows to the gameboard
		for(int y=0;y<yBoard+1;y++)
		{
			gameboard.add(rows[y]);
		}

		//go through x and y to add all of the pieces onto the rows
		for(int y=0;y<yBoard;y++)
		{
			for(int x=0;x<xBoard;x++)
			{
				rows[y].add(soldiers[y][x]);
			}
		}

		config.addActionListener(menuHandler);
		Continue.addActionListener(buttonPress);
		stop.addActionListener(menuHandler);

		try{line=reader.readLine();}catch(Exception ex){}
		flag=0;



		//these following lines drive the iteration of the
		//program. doClick() is ran on a timer which simulates
		//a click every 5 seconds
		Continue.doClick();
		Timer timer = new Timer(5000, new ActionListener() {

		    @Override
		    public void actionPerformed(ActionEvent arg0) {            
			Continue.doClick();
		    }
		});
		timer.setRepeats(true);
		timer.start();

	}	//end of function

	/*whoFired changes the icon of the game pieces to a gun,whether if
	they had fired or not. The soldiers who have fired is outputed in
	a.out before the gameboard is outputted. The position of the soldier
	is used to identify the pieces.	
	*/
	public void whoFired()
	{
		String[] POS;		//positions
		try{line=reader.readLine();}catch(Exception ex){};//blues
		
		try{line=reader.readLine();}catch(Exception ex){};//blist

		//at this point, line contains the positions of the soldiers
		// in the form x1,y1,x2,y2,.... two numbers are grabbed
		//(x1, y1 for instance) and used to reference the soldier
		//in the array by soldiers[y1-1][x1-1]

			if(!line.isEmpty())
			{
			System.out.println("Running blue guns");
			int temp;
			//convert to stringbuilder for manipulation
			POS=line.split(",");

			for(int i=0;i<POS.length;i=i+2)
			{
			int temp0 = Integer.parseInt(POS[i]);//grabs x
			int temp1 = Integer.parseInt(POS[i+1]);//grabs y

			System.out.println(temp0+", "+temp1);
			soldiers[temp1-1][temp0-1].changePiece(10);
			}
			}

			//the same as above, but for red pieces
		try{line=reader.readLine();}catch(Exception ex){}//reds
		try{line=reader.readLine();}catch(Exception ex){}//rlist
			if(!line.isEmpty())
			{
		
			System.out.println("Running red guns");
		
			int temp;
			//convert to stringbuilder for manipulation
			POS=line.split(",");

			for(int i=0;i<POS.length;i=i+2)
			{
			int temp0 = Integer.parseInt(POS[i]);
			int temp1 = Integer.parseInt(POS[i+1]);
			soldiers[temp1-1][temp0-1].changePiece(9);

			}
			}

		//grabbing lines from a.out to prepare for the remainder of
		//overlapUpdate
		try{line=reader.readLine();}catch(Exception ex){}//Killed/S
		try{line=reader.readLine();}catch(Exception ex){}//~
		try{line=reader.readLine();}catch(Exception ex){}//Reds

	}

	//I have the c program output all overlapping occurances
	//right above the gameboard. This function grabs those overlaps
	//and applies them to the necessary pieces, allowing for each piece
	//to show overlaps.
	public void overlapUpdate()
	{
		//grabs Red~	
		try{line=reader.readLine();}catch(Exception ex){}
		
		//if Red~ is not the lined grabbed, there must be a shooting
		//portion that needs to be parsed through. This function
		//deals with that portion
		if(line.indexOf('R')==-1)
		{
			whoFired();
		}

		//This portion of the function is where the overlap updating
		//happens. It loops through the code until Blue~ is found 
		//(signifying that there are no more red overlaps). And at 
		//each overlap updates the pieces correctly
		while(true)
		{
			try{line=reader.readLine();}catch(Exception ex){}
			if(line.indexOf('~')!=-1)//if at Blue~ 
						//(no more overlaps
			{
				break;
			}

			//temp array contains 3 int values 
			//(y1,x1, and overlap num)
			line=line.replaceAll("[^0-9]+"," ");
			String[] temp=line.trim().split(" ");

			int temp0 = Integer.parseInt(temp[0]);//y coordinate
			int temp1= Integer.parseInt(temp[1]);//x coordinate
			int temp2=Integer.parseInt(temp[2]);//overlap number

			soldiers[temp0-1][temp1-1].setText(temp[2]);

			//changes the icon of the gamepiece to either 2 or 3
			//to add addition visual elements
			if(temp2==2)
				soldiers[temp0-1][temp1-1].changePiece(5);

			
			else if(temp2>=3)
				soldiers[temp0-1][temp1-1].changePiece(6);

		}

		//does the same but for blue pieces		
		while(true)
		{
		try{line=reader.readLine();}catch(Exception ex){}

		if(line.indexOf('~')!=-1)//if at End~
		{
			break;
		}

		line=line.replaceAll("[^0-9]+"," ");
		String[] temp=line.trim().split(" ");

		int temp0 = Integer.parseInt(temp[0]);//get x
		int temp1= Integer.parseInt(temp[1]);//get y
		int temp2= Integer.parseInt(temp[2]);

		soldiers[temp0-1][temp1-1].setText(temp[2]);

		if(temp2==2)
			soldiers[temp0-1][temp1-1].changePiece(7);
		else if(temp2>=3)
			soldiers[temp0-1][temp1-1].changePiece(8);
		}

	}

	//grab pieces from the text file and apply them to the gui
	//I have the characters then changed in order to not interfer
	//with distance and to prevent a piece being counted twice
	public void pieceprinter(int y)
	{
	
		StringBuilder lineSb= new StringBuilder(line);
		int temp=0;
		//blue base	
	if((temp=lineSb.indexOf("B!"))!=-1)
		{
			soldiers[y][temp/2].changePiece(3);	
			lineSb.setCharAt(temp,'+');
			lineSb.setCharAt(temp+1,'+');

		}
		//red base
	if((temp=lineSb.indexOf("R!"))!=-1)
		{
			soldiers[y][temp/2].changePiece(4);
			lineSb.setCharAt(temp,'+');
			lineSb.setCharAt(temp+1,'+');

		}
	//finds red soldiers
	while(lineSb.indexOf("R")!=-1)
	{
			temp=lineSb.indexOf("R");
			soldiers[y][temp/2].changePiece(2);
			lineSb.setCharAt(temp,'+');
			lineSb.setCharAt(temp+1,'+');
	}
	//finds blue soldiers
	while(lineSb.indexOf("B")!=-1)
	{
			temp=lineSb.indexOf("B");
			soldiers[y][temp/2].changePiece(1);	
			lineSb.setCharAt(temp,'+');
			lineSb.setCharAt(temp+1,'+');		
	}	
	//finds walls
	while(lineSb.indexOf("l")!=-1)
	{
			temp=lineSb.indexOf("l");
			soldiers[y][temp/2].changePiece(11);	
			lineSb.setCharAt(temp,'+');
			lineSb.setCharAt(temp+1,'+');		
	}	

	}

	//change all pieces to blank
	public void clearBoard()
	{
		for(int y=0;y<yBoard;y++)
			for(int x=0;x<xBoard;x++)
				soldiers[y][x].changePiece(0);
	}


	//sets up the gameboard itself where all of the game rows rest upon
	private class board extends JPanel
	{
		private Image background;
		public void paintComponent(Graphics g)//get wooden 
							//background
		{
			try{
			background=ImageIO.read(getClass().getResource("players/background.jpeg"));
		}catch(Exception ex){}
		super.paintComponent(g);
		g.drawImage(background.getScaledInstance(Gui.this.getWidth(), Gui.this.getHeight(), 1),0,0,this);
		}
	}


	//This is the class that represents the soldiers of the game
	private class playerpiece extends JButton
	{
		private int playernumb;
		private String ColorString;
		private int colorHOLD;
		
		//makes the Jbutton invisible and chooses the icon (
		//color, 2 soldiers, whether is is a gun or not) for
		//the button based on the number provided
		public playerpiece(int num)
		{
			//gets rid of default button image
			this.setBorder(BorderFactory.createEmptyBorder());
			this.setContentAreaFilled(false);
			playernumb=num;
			colorselector(num);
			//assume to be no soldiers at position on creation
			this.setText("");
			//sets text position
			this.setHorizontalTextPosition(JButton.CENTER);
			this.setVerticalTextPosition(JButton.CENTER);	
			this.setForeground(Color.WHITE);
		}

		//this function changes the soldier's icon
		private void colorselector(int color)
		{
		colorHOLD=color;
		if(color==0)
		{
			ColorString="empty";
			this.setText("");
		}
		if(color==1)
		{
			ColorString="Blue2";
			this.setText("1");
		}
		if(color==2)
		{
			ColorString="Red4";
			this.setText("1");
		}
		if(color==3)
			ColorString="BlueBase";
		if(color==4)
			ColorString="RedBase";
		if(color==5)
			ColorString="RedMulti2";
		if(color==6)
			ColorString="RedMulti3";
		if(color==7)
			ColorString="BlueMulti2";
		if(color==8)
			ColorString="BlueMulti3";
		if(color==9)
			ColorString="RedGun";
		if(color==10)
			ColorString="BlueGun";
		if(color==11)
			ColorString="Wall";
		String directory="players/"+ColorString+".gif";

		Icon image= new ImageIcon(getClass().getResource(directory));
		this.setIcon(image);

		}

		//changepiece changes piece
		public void changePiece(int num)
		{
			colorselector(num);
		}

	}
		//creates an empty Jpanel. These Jpanels are stacked on top
		//of each other to give the game rows
	public class Row extends JPanel
	{
		public Row()
		{
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setOpaque(false);

		}

	}

	//This is where iterating through each turn happens
	public class ButtonPress implements ActionListener
	{
		WinScreen Screen;
		public void actionPerformed(ActionEvent event)
		{
			
	
		//Reruns the game and throws away the winscreen
		if(flag==1)
		{
			rerun();
			Screen.setVisible(false);
			Screen.dispose();
			return;
		}

		//if game is over, set the flag so that rerun is needed
		//and display the win screen
		if(line.indexOf(':')!=-1)
		{
			flag=1;
			Screen= new WinScreen();
			Screen.setSize(400,250);
			Screen.setVisible(true);
			return;
		}

		clearBoard();
		//grabs the ------ line of the gameboard
		try{line=reader.readLine();}catch(Exception ex){}

		//This portion of the code works with the actual game
		for(int y=0;y<yBoard;y++)
		{
			if(line==null)		//if end is reached
				return;
			if(line.indexOf('C')==0)//if game has ended
				return;
			
			System.out.println(line);
			pieceprinter(y);
			try{line=reader.readLine();}catch(Exception ex){}
		}

		overlapUpdate();

		//grabs the ----- line of the gameboard
		try{line=reader.readLine();}catch(Exception ex){}
		}
	}

	//runs the c program and then reruns the java code each time
	//this function is used for rerunning the program automatically
	public void rerun()
	{
		ProcessBuilder hold;
		File parent= new File("..");

		try{	
			hold=new ProcessBuilder("./run.sh", "");
			hold.directory(parent);
			Process p=hold.start();
			p.waitFor();
			flag=0;
			clearBoard();
		}
		catch(Exception ex)
		{
		}

		File Game= new File("../a.out");

		try{
			reader= new BufferedReader(new FileReader(Game));
			line= reader.readLine();	
		}
		catch(Exception ex)
			{
			line=null;
			}

		try{line=reader.readLine();}catch(Exception ex){}
		try{line=reader.readLine();}catch(Exception ex){}
		System.out.println(line);

	}

	//handles menubar activities
	public class MenuHandler implements ActionListener
	{
		//menu interacted with
		public void actionPerformed(ActionEvent event)
		{
		
			//exit if stop is pressed
			if(event.getSource() == stop)
	 			System.exit(0);	
		
			//launch config editer is config is pressed
			if(event.getSource()==config)
			{
				Config configuration = new Config();
				configuration.setSize(400,600);
				configuration.setVisible(true);
			}
		}	
	}

	//The win screen that displayes the results of the game
	public class WinScreen extends JFrame
	{
		FlowLayout layout;
		JTextField sent[];
		JPanel rows[];

	//creates Jpanel rows that simply display the win message from a.out
	//line by line
	public WinScreen()
	{
		super("Win");
		this.getContentPane().setBackground(Color.WHITE);
		rows=new JPanel[13];
		sent=new JTextField[13];
		this.setLayout(new GridLayout(8,0,0,0));
		layout=new FlowLayout();
		for(int i=0;i<8;i++)
		{
			rows[i]=new JPanel();
			sent[i]=new JTextField();
			rows[i].setLayout(layout);
			sent[i].setEditable(false);
			sent[i].setOpaque(false);
			sent[i].setBorder(BorderFactory.createEmptyBorder());
			rows[i].add(sent[i]);
			this.add(rows[i]);

		}
		sent[0].setText(line);	
		try{line=reader.readLine();}catch(Exception ex){}
		sent[1].setText(line);	
		try{line=reader.readLine();}catch(Exception ex){}
		sent[2].setText(line);	
		try{line=reader.readLine();}catch(Exception ex){}
		sent[3].setText(line);	
		}

	}
}

