package it.edu.iisgubbio.partiGioco;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class interfacciaGraficaBase extends Application{
	//SCHERMO
	final int WIDTH_SCHERMO = 1000;
	final int HEIGTH_SCHERMO = 600;

	public void start(Stage finestra) {
		Pane interfaccia = new Pane();

		Scene scena=new Scene(interfaccia, WIDTH_SCHERMO, HEIGTH_SCHERMO);
		finestra.setTitle("Spaceship");
		finestra.setScene(scena);
		finestra.show();		
	}

	public static void main(String[] args){
		launch(args);
	}
}
