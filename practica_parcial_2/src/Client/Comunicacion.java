package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

public class Comunicacion extends Observable implements Runnable {
	private Socket puerta;
	private boolean conectado;

	public Comunicacion() {
		try {
			puerta = new Socket(InetAddress.getByName("127.0.0.1"), 4001);
			conectado = true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
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
			in = new DataInputStream(puerta.getInputStream());
			String mensaje = in.readUTF();
			setChanged();
			notifyObservers();
			clearChanged();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			puerta = null;
			conectado = false;
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void enviarMensaje(String arg) {
		// TODO Auto-generated method stub
		DataOutputStream out = null;

		try {
			out = new DataOutputStream(puerta.getOutputStream());
			out.writeUTF(arg);
		} catch (IOException e) {
			// TODO Auto-generated catch block

			try {
				if (out != null) {
					out.close();
				}
				puerta.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			puerta = null;
			conectado = false;
			e.printStackTrace();
		}
	}

}
