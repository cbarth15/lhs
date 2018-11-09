//Christian Barth
//creates a window that
//describes the instructions 
//of the game

import javax.swing.*;
import java.awt.*;

public class Instructions extends JFrame
{
	private FlowLayout layout;
	private JTextField Title;
	private JPanel Textfield1, Textfield2, Textfield3, Textfield4, Textfield5;
	public Instructions()
	{

		super("Instructions");
		this.getContentPane().setBackground(Color.WHITE);
		//used for seperate rows
		Textfield1= new JPanel();
		Textfield2= new JPanel();
		Textfield3= new JPanel();
		Textfield4=new JPanel();
		Textfield5=new JPanel();

		//text being outputted
		JTextField title=new JTextField("Instructions");
		JTextField sent1=new JTextField("Attempt to reach the opposite traingle with your pieces.");
		JTextField sent2=new JTextField("You may hop any pieces as many times as you would like");
		JTextField sent3=new JTextField("as long as there is a space in between.");
		JTextField sent4= new JTextField("Can be played with 2,3,4 or 6 players.");

		layout= new FlowLayout(FlowLayout.CENTER);
		this.setLayout(new GridLayout(7,0,0,0));
		//setting things to be see through for the white background
		Textfield1.setOpaque(false);
		Textfield2.setOpaque(false);
		Textfield3.setOpaque(false);
		Textfield4.setOpaque(false);
		Textfield5.setOpaque(false);

		title.setBorder(BorderFactory.createEmptyBorder());
		sent1.setBorder(BorderFactory.createEmptyBorder());
		sent2.setBorder(BorderFactory.createEmptyBorder());
		sent3.setBorder(BorderFactory.createEmptyBorder());
		sent4.setBorder(BorderFactory.createEmptyBorder());


		this.add(Textfield1);
		this.add(Textfield2);
		this.add(Textfield3);
		this.add(Textfield4);
		this.add(Textfield5);
		//change title to bold
		Font font=new Font(title.getText(), Font.BOLD,12);
		title.setFont(font);
		
		title.setOpaque(false);
		title.setEditable(false);
		sent1.setOpaque(false);
		sent1.setEditable(false);
		sent2.setOpaque(false);
		sent2.setEditable(false);
		sent3.setOpaque(false);
		sent3.setEditable(false);
		sent4.setEditable(false);
		sent4.setOpaque(false);

		Textfield1.add(title);
		Textfield2.add(sent1);
		Textfield3.add (sent2);
		Textfield4.add(sent3);
		Textfield5.add(sent4);


	}






}
