package it.edu.iisgubbio.partiGioco;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SfondoConOggetti extends Application{
	
	public void start(Stage finestra) {
		Pane interfaccia = new Pane();
		
		Scene scena=new Scene(interfaccia, 1000, 800);
		finestra.setTitle("Spaceship");
		finestra.setScene(scena);
		finestra.show();
	}
	
	public static void main(String[] args){
		launch(args);
	}
}
