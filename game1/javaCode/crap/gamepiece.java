//Christian Barth
//Parent class to player piece
//originally used to represent a blank piece but
//was changed in final version
import javax.swing.*;
import java.awt.*;


public class gamepiece extends JButton
{
	int row;
	int column;
	public gamepiece()
	{
	super("");			//assign image of empty space
	Icon image= new ImageIcon( getClass().getResource("empty.gif"));
	this.setIcon(image);					
	this.setBorder(BorderFactory.createEmptyBorder());
	this.setContentAreaFilled(false);	
	}
	public gamepiece(int num, int x, int y)
	{
		this();
		row=x;
		column=y;

	}
	public gamepiece(String s)
	{
		super(s);
	}
	public int getRow()
	{
	return row;
	}
	public int getColumn()
	{
	return column;
	}

	public boolean isEmpty()
	{

		return true;

	}

}
