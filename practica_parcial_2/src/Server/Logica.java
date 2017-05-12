package Server;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

public class Logica implements Observer, Runnable {

	private final int PORT = 4001;
	private ServerSocket server;
	private boolean conectado;
	private ArrayList<Comunicacion> clientes = new ArrayList<>();
	private boolean moviendo;

	public Logica() {
		try {
			server = new ServerSocket(PORT);
			conectado = true;
			System.out.println("Servidor esperando en " + InetAddress.getLocalHost().getHostAddress().toString() + ": "
					+ this.PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (conectado) {
			Socket s;
			try {
				s = server.accept();
				Comunicacion com = new Comunicacion(s, clientes.size());
				com.addObserver(this);
				new Thread(com).start();

				clientes.add(com);
				System.out.println("Numero de clientes " + clientes.size() + " clientes");
				Thread.sleep(100);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		Comunicacion controlCliente = (Comunicacion) o;

		if (arg instanceof String) {
			String mensaje = (String) arg;

			System.out.println(mensaje);

			if (mensaje.equalsIgnoreCase("Cliente desconocido")) {
				clientes.remove(controlCliente);
				System.out.println("Numero de clientes " + clientes.size() + "clientes");
			}

			if (mensaje.contains("values")) {
				controlCliente.enviarMensaje("id:" + clientes.size());
				controlCliente.enviarMensaje("color: " + (int) (Math.random() * 250));
				reenviarMensaje("suma", controlCliente);
			}

			if (moviendo) {
				controlCliente.enviarMensaje("mover");
				moviendo = false;
			}

			if (mensaje.contains("llegue")) {
				if (controlCliente.getId() == clientes.size() - 1) {
					clientes.get(0).enviarMensaje("mover");

				} else {
					clientes.get(controlCliente.getId() + 1).enviarMensaje("mover");
				}
			}
		}
	}

	private void reenviarMensaje(String arg, Comunicacion controlCliente) {
		// TODO Auto-generated method stub
		int reenvio = 0;
		for (Iterator<Comunicacion> iterator = clientes.iterator(); iterator.hasNext();) {
			Comunicacion com = iterator.next();
			
			if(!com.equals(controlCliente)){
				com.enviarMensaje(arg);
				reenvio++;
			}
		}
		System.out.println("Se devolvio al cliente "+reenvio);
	}

}
