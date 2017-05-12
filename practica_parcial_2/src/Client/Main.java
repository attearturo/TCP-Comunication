package Client;

import processing.core.*;

public class Main extends PApplet {
	LogicaCliente logica;

	public static void main(String[] args) {
		PApplet.main("Client.Main");
	}

	@Override
	public void setup() {
		logica = new LogicaCliente(this);

		colorMode(HSB, 360, 80, 80);
		noStroke();
		smooth(4);
		textAlign(CENTER);
		textSize(10);
		size(400, 400);

	}
	

	public void draw(){
		background(345);
		logica.pintar();
	}


}
