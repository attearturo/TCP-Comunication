package Server;
import processing.core.*;
public class Main extends PApplet{
private static Logica logica;

public static void main(String[] arg){
	logica = new Logica();
	new Thread(logica).start();
}
	
}
