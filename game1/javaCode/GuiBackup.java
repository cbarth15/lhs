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

public class Gui extends JFrame{
	private board gameboard;
	private JMenuBar menubar;       //for all things in the menu
	private MenuHandler menuHandler;
	private JMenu start,quit;
	private JMenuItem Begin;
	private FlowLayout layout;
	private JPanel[] rows;
	private playerpiece[][] soldiers;        //an array of all the pieces
	private int counter;
	BufferedReader reader;
	String line;
	private int xBoard;		//contains x dimension of field
	private int yBoard;		//contains y dimension of field

	public Gui()
	{

		super("WarSim");

		File Game= new File("../a.out");

		try{
		reader= new BufferedReader(new FileReader(Game));
		line= reader.readLine();	
		}
		catch(Exception ex)
		{
		line=null;
		}


		
		gameboard= new board();
		menubar= new JMenuBar();
		start= new JMenu("Start");
		quit= new JMenu("Quit");
		Begin= new JMenuItem("Begin");
		start.add(Begin);	

		gameboard.setVisible(true);
		layout= new FlowLayout(FlowLayout.CENTER,3,3);
			
		xBoard=Integer.parseInt(line);
		try{line=reader.readLine();}catch(Exception ex){}
		yBoard=Integer.parseInt(line);

		soldiers=new playerpiece[yBoard][xBoard];
		rows= new JPanel[yBoard];
		menuHandler= new MenuHandler();

		for(int y=0; y<yBoard;y++)
		{
			rows[y]= new Row();
			rows[y].setLayout(layout);

		}
		for(int y=0;y<yBoard;y++)
		{
			for(int x=0; x<xBoard;x++)
			{
			soldiers[y][x]=new playerpiece(0);

			}
		}

		menubar.add(start);
		menubar.add(quit);
		this.setJMenuBar(menubar);
		this.add(gameboard);

		gameboard.setLayout(new GridLayout(yBoard+1,0,0,0));
		for(int y=0;y<yBoard;y++)
		{
			gameboard.add(rows[y]);
		}
		
		for(int y=0;y<yBoard;y++)
		{
			for(int x=0;x<xBoard;x++)
			{
				rows[y].add(soldiers[y][x]);
			}
		}
		Begin.addActionListener(menuHandler);
	}	//end of function


	public void processing()
	{

		
		try{line=reader.readLine();}catch(Exception ex){}
		System.out.println(line);
		while(line!=null)
		{

		counter++;
		//Grabs --- barrier seperating each game 
		try{line=reader.readLine();}catch(Exception ex){}
	
		//clearboard each time
		//except for the last
		if(line.indexOf(':')==-1)
		{
			javax.swing.Timer timer=new javax.swing.Timer(1000, new ActionListener(){
			@Override
			public void actionPerformed(final ActionEvent e){
			clearBoard();
			}
			});
			timer.setRepeats(false);
			timer.start();
		
		}
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


			
	}
	}

	//public void 


	public void pieceprinter(int y)
	{
	
		StringBuilder lineSb= new StringBuilder(line);
		int temp=0;
		//blue base	
	if((temp=lineSb.indexOf("B!"))!=-1)
		{
			soldiers[y][temp/2].changePiece(3);
		lineSb.delete(temp,temp+1);

		}
		//red base
	if((temp=lineSb.indexOf("R!"))!=-1)
		{
			soldiers[y][temp/2].changePiece(4);
			lineSb.delete(temp,temp+1);

		}
	while(lineSb.indexOf("B")!=-1)
	{
		if((temp=lineSb.indexOf("B"))!=-1)
		{
			soldiers[y][temp/2].changePiece(1);
			lineSb.delete(temp,temp+1);


		}
	}	
	while(lineSb.indexOf("R")!=-1)
	{
		if((temp=lineSb.indexOf("R"))!=-1)
		{
			soldiers[y][temp/2].changePiece(2);
			lineSb.delete(temp,temp+1);
		}
	}


	}

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

	}

	private class playerpiece extends JButton
	{
		private int playernumb;
		private String ColorString;

		public playerpiece(int num)
		{
			this.setBorder(BorderFactory.createEmptyBorder());
			this.setContentAreaFilled(false);
			playernumb=num;
			colorselector(num);	

		}
		private void colorselector(int color)
		{
		if(color==0)
			ColorString="empty";
		if(color==1)
			ColorString="Blue2";
		if(color==2)
			ColorString="Red4";
		if(color==3)
			ColorString="BlueBase";
		if(color==4)
			ColorString="RedBase";

		String directory="players/"+ColorString+".gif";

		Icon image= new ImageIcon(getClass().getResource(directory));
		this.setIcon(image);

		}
		public void changePiece(int num)
		{
			colorselector(num);
		}

	}

	public class Row extends JPanel
	{
		public Row()
		{
		this.setBorder(BorderFactory.createEmptyBorder());
		this.setOpaque(false);

		}

	}

	public class MenuHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
		if(event.getSource() == Begin)
		{
			processing();

		}
		
		}
	}	
}

