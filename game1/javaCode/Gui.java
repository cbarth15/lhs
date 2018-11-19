/* Christian Barth */

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
	private JMenuBar menubar;       //for all things in the menu

	private MenuHandler menuHandler;
	private ButtonPress buttonPress;//for the continue button,
					//is pressed every few minutes
					//by a timer for constant movement

	private JMenu start,quit;
	private JMenuItem Begin;
	private JMenuItem config;
	private JMenuItem stop;
	private FlowLayout layout;
	private JPanel[] rows;		//each row is a Jpanel that contains
					//all the buttons of a single row
					//of the playerpiece array

	private playerpiece[][] soldiers;       //an array of all the pieces
	private JButton Continue;	//the continue button that is 
					//linked with the buttonpress
					//handler

	private int counter;
	BufferedReader reader;
	String line;
	private int xBoard;		//contains x dimension of field
	private int yBoard;		//contains y dimension of field

	private int flag;

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


		//setting up the window itself
		gameboard= new board();
		menubar= new JMenuBar();
		start= new JMenu("Start");
		quit= new JMenu("Quit");
		Begin= new JMenuItem("Begin");
		config= new JMenuItem("Config");
		stop= new JMenuItem("Stop");
		Continue= new JButton("Continue");
		start.add(Begin);	
		start.add(config);
		quit.add(stop);
		gameboard.setVisible(true);
		layout= new FlowLayout(FlowLayout.CENTER,3,3);
		//take off max x coordinates of the board	
		xBoard=Integer.parseInt(line);
		try{line=reader.readLine();}catch(Exception ex){}
		//take off max y coordinates of the board
		yBoard=Integer.parseInt(line);
		
		//creates an array of all the pieces that can occur
		soldiers=new playerpiece[yBoard][xBoard];
		rows= new JPanel[yBoard+1];
		menuHandler= new MenuHandler();
		buttonPress= new ButtonPress();

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

		menubar.add(start);
		menubar.add(quit);
		rows[yBoard].add(Continue);
		this.setJMenuBar(menubar);
		this.add(gameboard);

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
		Begin.addActionListener(menuHandler);
		config.addActionListener(menuHandler);
		Continue.addActionListener(buttonPress);
		stop.addActionListener(menuHandler);
		try{line=reader.readLine();}catch(Exception ex){}
		flag=0;

		Continue.doClick();//this function simulates a person click
				//-ing a jbutton. I use it with this timer
				//to iterate through each occurance

		Timer timer = new Timer(5000, new ActionListener() {

		    @Override
		    public void actionPerformed(ActionEvent arg0) {            
			Continue.doClick();
		    }
		});
		timer.setRepeats(true);
		timer.start();

	}	//end of function

	//UNUSED LEGACY CODE
	public void processing()
	{

		//clearboard each time
		//except for the last
		if(flag==1)
		{
			return;
		}
		if(line.indexOf(':')!=-1)
		{
			flag=1;
			return;
		}
		clearBoard();
		try{line=reader.readLine();}catch(Exception ex){}
		//runs for the length of the board
		for(int y=0;y<yBoard;y++)
		{
			//if ending is reached
			if(line==null)
				return;
			//if the starting line is reached
			if(line.indexOf('C')==0)
			{
				return;
			}
			
		//this point of the code is where I
		//am working with the actual game
		System.out.println(line);
		pieceprinter(y);
		// Grabs next line
		try{line=reader.readLine();}catch(Exception ex){}
		}
		try{line=reader.readLine();}catch(Exception ex){}
		//return;
		overlapUpdate();
			
	}

	public void whoFired()
	{
		String[] POS;		//positions
		try{line=reader.readLine();}catch(Exception ex){};//blues
		try{line=reader.readLine();}catch(Exception ex){};//blist
			if(!line.isEmpty())
			{
			System.out.println("Running blue guns");
			int temp;
			//convert to stringbuilder for manipulation
			POS=line.split(",");

			for(int i=0;i<POS.length;i=i+2)
			{
			int temp0 = Integer.parseInt(POS[i]);
			int temp1 = Integer.parseInt(POS[i+1]);
			System.out.println(temp0+", "+temp1);
			soldiers[temp1-1][temp0-1].changePiece(10);
			}
			}
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
			}//isempty if statement
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
		if(line.indexOf('R')==-1)
		{
		whoFired();
		}
		//For Red pieces
		while(true)
		{
		try{line=reader.readLine();}catch(Exception ex){}

		if(line.indexOf('~')!=-1)//if at Blue~
		{
			break;
		}

		line=line.replaceAll("[^0-9]+"," ");
		String[] temp=line.trim().split(" ");

		int temp0 = Integer.parseInt(temp[0]);
		int temp1= Integer.parseInt(temp[1]);
		int temp2=Integer.parseInt(temp[2]);

		soldiers[temp0-1][temp1-1].setText(temp[2]);
		if(temp2==2)
		{
			soldiers[temp0-1][temp1-1].changePiece(5);

		}
		if(temp2>=3)
		{	
			soldiers[temp0-1][temp1-1].changePiece(6);
		}

		}

		//for blue pieces		
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
		{
			soldiers[temp0-1][temp1-1].changePiece(7);

		}
		else if(temp2>=3)
		{	
			soldiers[temp0-1][temp1-1].changePiece(8);
		}
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

	while(lineSb.indexOf("R")!=-1)
	{
			temp=lineSb.indexOf("R");
			soldiers[y][temp/2].changePiece(2);
			lineSb.setCharAt(temp,'+');
			lineSb.setCharAt(temp+1,'+');
	}
	while(lineSb.indexOf("B")!=-1)
	{
			temp=lineSb.indexOf("B");
			soldiers[y][temp/2].changePiece(1);	
			lineSb.setCharAt(temp,'+');
			lineSb.setCharAt(temp+1,'+');		
	}	

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
		{
			for(int x=0;x<xBoard;x++)
			{
			soldiers[y][x].changePiece(0);


			}
		}
		
		

	}
	//UNUSED
	public static void getInfo (String[] args)
 
	{
	File gameboard= new File("../a.out");

	BufferedReader reader;
	try{
	reader= new BufferedReader(new FileReader(gameboard));
	String line= reader.readLine();	
	}
	catch(Exception ex)
	{
	}



	}

	private class board extends JPanel
	{
		private Image background;
		public void paintComponent(Graphics g)
		{
			try{
			background=ImageIO.read(getClass().getResource("players/background.jpeg"));
		}catch(Exception ex){}
		super.paintComponent(g);
		g.drawImage(background.getScaledInstance(Gui.this.getWidth(), Gui.this.getHeight(), 1),0,0,this);



		}

	}


	//sets up for each soldier
	private class playerpiece extends JButton
	{
		private int playernumb;
		private String ColorString;
		private int colorHOLD;
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
		public boolean isRed()
			{
				if(colorHOLD==2 || colorHOLD==9 || colorHOLD==4 || colorHOLD==5 || colorHOLD==6)
					{
						return true;
					
					}	
			return false;

			}
		public boolean isBlue()
			{
				if(colorHOLD==1 || colorHOLD==3 || colorHOLD==7 || colorHOLD==8 || colorHOLD==10)
				{
					return true;
				}

			return false;
			}
		//this function changes the piece color
		//sets it to 1 since each soldier is at least one
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
		//sets up each row's defaults
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
			
	
		//clearboard each time
		//except for the last
		if(flag==1)
		{
			rerun();
			Screen.setVisible(false);
			Screen.dispose();
			return;
		}
		if(line.indexOf(':')!=-1)//runs at the end
		{
			flag=1;
			Screen= new WinScreen();
			Screen.setSize(400,300);
			Screen.setVisible(true);
			return;
		}
		clearBoard();
		try{line=reader.readLine();}catch(Exception ex){}
		//runs for the length of the board
		for(int y=0;y<yBoard;y++)
		{
			//if ending is reached
			if(line==null)
				return;
			//if the starting line is reached
			if(line.indexOf('C')==0)
			{
				return;
			}
			
		//this point of the code is where I
		//am working with the actual game
		System.out.println(line);
		pieceprinter(y);
		// Grabs next line
		try{line=reader.readLine();}catch(Exception ex){}
		}
		//return;

		overlapUpdate();


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
	public class MenuHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
		if(event.getSource() == Begin)
		{
			processing();

		}
		
		if(event.getSource() == stop)
		{
	 		System.exit(0);	
		}
		
		if(event.getSource()==config)
		{

			Config configuration = new Config();
			configuration.setSize(400,600);
			configuration.setVisible(true);
		}
	}	
	}

	//a popup that displays results
	public class WinScreen extends JFrame
	{
	FlowLayout layout;
	JTextField sent[];
	JPanel rows[];


	public WinScreen()
	{
		super("Win");
		this.getContentPane().setBackground(Color.WHITE);
		rows=new JPanel[13];
		sent=new JTextField[13];
		this.setLayout(new GridLayout(15,0,0,0));
		layout=new FlowLayout();
		for(int i=0;i<13;i++)
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
		//reads in what the c program outputs at the end (which is
		//results)
		//and sets each line to it
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

