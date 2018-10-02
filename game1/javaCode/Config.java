//Christian Barth
//Provides an interface
//for the config file


import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;

public class Config extends JFrame
{
private FlowLayout layout;
private JTextField Title;
private JPanel rows[];
private JTextField sent[];		//contains sentences
private File configuration;
private BufferedReader reader;
private String line;
private JTextField input[];
private JButton submit;
private JPanel bottom;
private ButtonPress buttonPress;
	public Config()
	{

		super("Config");
		configuration= new File("../game.conf");


		try{
		reader=new BufferedReader(new FileReader(configuration));
		line=reader.readLine();
		}
		catch(Exception ex)
		{
		line=null;
		}
		
		this.getContentPane().setBackground(Color.WHITE);
		rows=new JPanel[13];
		sent= new JTextField[13];
		input= new JTextField[13];
		JTextField title=new JTextField("Config");
		submit=new JButton("Submit");
		bottom=new JPanel();
		buttonPress=new ButtonPress();
		JPanel JTitle=new JPanel();
		JTitle.setLayout( new FlowLayout(FlowLayout.CENTER));
		JTitle.add(title);
		this.add(JTitle);
		
		layout= new FlowLayout();
		this.setLayout(new GridLayout(15,0,0,0));

		
		for(int i=0;i<13;i++)
		{
			rows[i]= new JPanel();
			rows[i].setLayout(layout);
			
			sent[i]=new JTextField();
			sent[i].setEditable(false);
			sent[i].setOpaque(false);
			sent[i].setBorder(BorderFactory.createEmptyBorder());
		
			input[i]=new JTextField();	
			rows[i].add(sent[i]);
			rows[i].add(input[i]);
			this.add(rows[i]);
		}
		bottom.add(submit);
		this.add(bottom);
		Font font=new Font(title.getText(), Font.BOLD,12);
		title.setFont(font);
		title.setOpaque(false);
		title.setEditable(false);
		title.setBorder(BorderFactory.createEmptyBorder());


		sent[0].setText("Edit Board Dimensions:");
		sent[1].setText("Blue Base Location:");
		sent[2].setText("Number of Blue troops:");
		sent[3].setText("Accuracy of Blue troops:");
		sent[4].setText("Stealth of Blue troops:");
		sent[5].setText("Speed of Blue troops:");
		sent[6].setText("Fear of Blue troops:");
		
		
		sent[7].setText("Red Base Location:");
		sent[8].setText("Number of Red troops:");
		sent[9].setText("Accuracy of Red troops:");
		sent[10].setText("Stealth of Red troops:");
		sent[11].setText("Speed of Red troops:");
		sent[12].setText("Fear of Red troops:");

		input[0].setText("20,20");
		input[1].setText("1,1");
		input[2].setText("10");
		input[3].setText("0.7");
		input[4].setText("6");
		input[5].setText("3");
		input[6].setText("20");
		input[7].setText("20,20");
		input[8].setText("10");
		input[9].setText("0.7");
		input[10].setText("6");
		input[11].setText("3");
		input[12].setText("20");

		submit.addActionListener(buttonPress);

	}





	private class ButtonPress implements ActionListener
	{

		public void actionPerformed(ActionEvent event)
		{
			PrintStream o=null;
	
			try{o = new PrintStream(new File("A.txt"));}catch(Exception ex){} 	
		
			PrintStream console = System.out; 
			
			System.setOut(o); 
       			System.out.println("#Comments_only_with_no_spaces");
			System.out.println();
			System.out.println("$X=20"); 
			System.out.println("$Y=20");
			System.out.println("field="+input[0].getText());
			System.out.println("seed=8"); 
			System.out.println();
			System.out.println("#Number_of_repetitions");
			System.out.println("nrep=1"); 
			System.out.println();
			System.out.println("[blue]");
			System.out.println("base="+input[1].getText());
			System.out.println("N="+input[2].getText());

			System.out.println("accuracy="+input[3].getText()); 
			System.out.println("stealth="+input[4].getText());
			System.out.println("speed="+input[5].getText());
			System.out.println("fear="+input[6].getText());
			System.out.println();
			System.out.println("[red]");
			System.out.println("base="+input[7].getText());

			System.out.println("N="+input[8].getText()); 
			System.out.println("accuracy="+input[9].getText());
			System.out.println("stealth="+input[10].getText()); 
			System.out.println("speed="+input[11].getText()); 
			System.out.println("fear="+input[12].getText());

			System.out.println(); 
			System.out.println();
			System.out.println("prn_map=1"); 
			System.out.println("prn_result=1");
			System.out.println("prn_move=0");
			System.out.println("prn_shoot=0"); 
			System.out.println("prn_title=0");
	
			System.setOut(console); 
		}
	}


















}
