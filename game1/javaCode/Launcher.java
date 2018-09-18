//Christian Barth
//Code taken from Homework 5
import javax.swing.JFrame;

public class Launcher {
	
  public static void main(String[] args) {
    Gui dFrame = new Gui();
    dFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    dFrame.setSize( 800, 600 ); // set frame size
    dFrame.setVisible( true ); // display frame
  }
}
