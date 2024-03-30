package it.edu.iisgubbio.partiGioco;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class VisualizzaEsplosione extends Application{
	//SCHERMO
	final int WIDTH_SCHERMO = 1000;
	final int HEIGTH_SCHERMO = 600;

	//esplosione

	final int WIDTH_ESPLOSIONE = 100;
	final int HEIGTH_ESPLOSIONE = 100;
	Image animazioneEsplosione = new Image(getClass().getResourceAsStream("animazione-esplosione2.gif"));
	
	public void start(Stage finestra) {
		Pane interfaccia = new Pane();
		
		int posizioneXMissile, posizioneXMeteorie, posizioneYMissile;
		posizioneXMissile=100;
		posizioneXMeteorie=80;
		posizioneYMissile=423;
		ImageView esplosione=new ImageView(animazioneEsplosione);
		esplosione.setFitHeight(HEIGTH_ESPLOSIONE);
		esplosione.setFitWidth(WIDTH_ESPLOSIONE);
		interfaccia.getChildren().add(esplosione);
		esplosione.setLayoutX((posizioneXMissile+posizioneXMeteorie)/2);
		esplosione.setLayoutY(posizioneYMissile-HEIGTH_ESPLOSIONE/2);
		
		Scene scena=new Scene(interfaccia, WIDTH_SCHERMO, HEIGTH_SCHERMO);
		finestra.setTitle("Spaceship");
		finestra.setScene(scena);
		finestra.show();		
	}

	public static void main(String[] args){
		launch(args);
	}
}
