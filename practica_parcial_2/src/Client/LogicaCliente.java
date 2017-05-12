package Client;
import java.util.Observable;
import java.util.Observer;

import processing.core.PApplet;

public class LogicaCliente implements Observer {
	private PApplet app;
	static Comunicacion com;
	private int colores, id, numClientes;
	private float x, y;
	private boolean movible;
	
	public LogicaCliente(PApplet app){
		this.app = app;
		com = new Comunicacion();
		new Thread(com).start();
		com.addObserver(this);
		
		movible = false;
	}
	


	public void pintar() {
		// TODO Auto-generated method stub
		app.fill(0);
		app.text("Cliente: "+id+" Total de clientes: "+numClientes, app.width/2, 50);
		if(movible){
			app.fill(colores, 80,80);
			app.ellipse(x, y, 50, 50);
			x += 8;
		}
		meta();
	}


	private void meta() {
		// TODO Auto-generated method stub
		if(x >= app.width + 25){
			com.enviarMensaje("llegue");
			movible = false;
			x = 0;
		}
	}
	


	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		if(arg instanceof String){
			String mensaje = (String) arg;
			System.out.println("Se recibio: "+mensaje);
			
			if(mensaje.contains("id")){
				String[] partes = mensaje.split(":");
				id = Integer.parseInt(partes[1]);
				numClientes = id;
				y = id * 100;
				System.out.println(id);
			}
			
			if(mensaje.contains("color")){
				String[] partes = mensaje.split(":");
				colores = Integer.parseInt(partes[1]);
				System.out.println(colores);
			}
			
			if(mensaje.contains("suma")){
				numClientes++;
			}
			
			if(mensaje.contains("mover")){
				movible = true;
			}
			
			if(mensaje.equals("New client")){
				com.enviarMensaje("values");
			}
		}
	}

}
