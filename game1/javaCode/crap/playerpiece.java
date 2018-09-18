//Christian Barth
//The class for all pieces used
import javax.swing.*;
import java.awt.*;

public class playerpiece extends gamepiece
{
	private int playernumb;
	private String ColorString;
	private int row;
	private int column;

	private int rowBehind;			//used specifically to calculate if a player piece
	private int columnBehind;		//was trying to jump this one, where it would go
	public playerpiece()
	{

	}
	//Get x (row) and y (column) so the piece knows where it is located
	//on the board
	public playerpiece(int num,int x, int y)		
	{	
		playernumb=num;
		colorselector(num);
		row=x;
		column=y;
	}
	//used to change the piece
	//i.e. if the piece where to be claimed as empty assign
	//the playernumb to 0
	public void setPlayer(int num)
	{
		playernumb=num;
		colorselector(num);
	}
	public int getRow()
	{
	return row;
	}
	public int getColumn()
	{
	return column;
	}
	//used to identify the piece
	private void colorselector(int color)
	{
		if(color==0)
			ColorString="empty";
		if(color==1)
			ColorString="Green1";
		if(color==2)
			ColorString="Blue2";
		if(color==3)
			ColorString="Orange3";
		if(color==4)
			ColorString="Red4";
		if(color==5)
			ColorString="Pink5";
		if (color==6)
			ColorString="Yellow6";

		String directory="players/"+ColorString+".gif";
		Icon image= new ImageIcon(getClass().getResource(directory));
		this.setIcon(image);

	}
	public int playernum()
	{
	return playernumb;
	}
	//used to change the piece in its entirety if
	//it needed to be moved
	public void change(int num,int x, int y)
	{	
		playernumb=num;
		colorselector(num);
		row=x;
		column=y;
	}
	public String Color()
	{
	return ColorString;
	}

	public boolean isEmpty()
	{
	return false;
	}



}
