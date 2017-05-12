package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;

public class Comunicacion extends Observable implements Runnable {
	Socket s;
	private int id;
	private boolean conectado;

	public Comunicacion(Socket s, int id) {
		this.id = id;
		this.s = s;
		conectado = true;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		enviarMensaje("New client");

		while (conectado) {
			recibirMensaje();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void recibirMensaje() {
		// TODO Auto-generated method stub
		DataInputStream in = null;
		try {
			in = new DataInputStream(s.getInputStream());
			String mensaje = in.readUTF();
			System.out.println("Se recibio: " + mensaje + " del cliente " + id);

			setChanged();
			notifyObservers(mensaje);

		} catch (IOException e) {
			System.out.println("Se perdio conexión del cliente " + id);

			try {
				if (in != null) {
					in.close();
				}
				s.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			s = null;
			conectado = false;
			setChanged();
			notifyObservers("cliente desconectado");
		}

	}

	public void enviarMensaje(String mensaje) {
		// TODO Auto-generated method stub
		DataOutputStream out = null;

		try {
			out = new DataOutputStream(s.getOutputStream());
			out.writeUTF(mensaje);
			System.out.println("Se envio el mensaje al cliente: " + mensaje);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Se perdio la conexion con el cliente: " + id);
			
				try {
					if (out != null) {
					out.close();
					}
					s.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				s= null;
				setChanged();
				notifyObservers("cliente desconectado");
		}

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isConectado() {
		return conectado;
	}

	public void setConectado(boolean conectado) {
		this.conectado = conectado;
	}

}
