import java.io.File;

public class Test{

public static void main(String[] args)
{

ProcessBuilder excel;
File parent= new File("..");
try{


excel=new ProcessBuilder("touch", "penis");

excel.directory(parent);

Process p=excel.start();
}
catch(Exception ex)
{
System.out.println("Hello!");
}
}





}
