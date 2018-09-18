import javax.swing.*;
import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Gui extends JFrame{
	private board gameboard;
	private JMenuBar menubar;       //for all things in the menu
	private MenuHandler menuHandler;
	private JMenu start,quit;
	private FlowLayout layout;
	private JPanel[] rows;
	private playerpiece[][] soldiers;        //an array of all the pieces
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

		gameboard.setVisible(true);
		layout= new FlowLayout(FlowLayout.CENTER,3,3);
			
		xBoard=int(line);
		line=reader.readLine();
		yBoard=int(line);

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
		//can add background
	}

	private class playerpiece extends JButton
	{
		private int playernumb;
		private String ColorString;

		public playerpiece(int num)
		{
			playernumb=num;
			colorselector(num);	

		}
		private void colorselector(int color)
		{
		if(color==0)
			ColorString="empty";

		String directory="players/"+ColorString+".gif";

		Icon image= new ImageIcon(getClass().getResource(directory));
		this.setIcon(image);


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
	}	
}

