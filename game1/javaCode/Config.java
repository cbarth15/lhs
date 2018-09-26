//Christian Barth
//Provides an interface
//for the config file


import javax.swing.*;
import java.awt.*;
import java.io.*;
public class Config extends JFrame
{
private FlowLayout layout;
private JTextField Title;
private JPanel rows[];
private JTextField sent[];		//contains sentences
private File configuration;
private BufferedReader reader;
private String line;
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
		rows=new JPanel[10];
		sent= new JTextField[10];
		JTextField title=new JTextField("Config");

		JPanel JTitle=new JPanel();
		JTitle.setLayout( new FlowLayout(FlowLayout.CENTER));
		JTitle.add(title);
		this.add(JTitle);
		
		layout= new FlowLayout();
		this.setLayout(new GridLayout(12,0,0,0));

		
		for(int i=0;i<10;i++)
		{
			rows[i]= new JPanel();
			rows[i].setLayout(layout);
			
			sent[i]=new JTextField();
			sent[i].setEditable(false);
			sent[i].setOpaque(false);
			sent[i].setBorder(BorderFactory.createEmptyBorder());
			
			rows[i].add(sent[i]);
			this.add(rows[i]);
		}

		Font font=new Font(title.getText(), Font.BOLD,12);
		title.setFont(font);
		title.setOpaque(false);
		title.setEditable(false);
		title.setBorder(BorderFactory.createEmptyBorder());












	}
























}
