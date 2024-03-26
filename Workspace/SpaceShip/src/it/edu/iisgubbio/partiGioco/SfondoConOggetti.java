package it.edu.iisgubbio.partiGioco;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SfondoConOggetti extends Application{
	//SCHERMO
	final int WIDTH_SCHERMO = 1000;
	final int HEIGTH_SCHERMO = 600;
	
	//SFONDO
	final int WIDTH_SFONDO = 5000;
	final int HEIGTH_SFONDO = 800;
	Image immagineSfondo = new Image(getClass().getResourceAsStream("Sfondo.png"));
	ImageView sfondo = new ImageView(immagineSfondo);
	
	//spostamento sfondo
	double posizioneSfondoX=0;
	double valoreSpostamentoSfondo=6;
	Timeline muoviSfondo= new Timeline(new KeyFrame(
		      Duration.millis(25), 
		      x -> aggiornaPosizioneSfondo()));
	
	public void start(Stage finestra) {
		Pane interfaccia = new Pane();
		
		sfondo.setFitWidth(WIDTH_SFONDO);
		sfondo.setFitHeight(HEIGTH_SFONDO);
		sfondo.setLayoutX(0);
		sfondo.setLayoutY(0);
		muoviSfondo.setCycleCount(Animation.INDEFINITE);
		muoviSfondo.play();
		interfaccia.getChildren().add(sfondo);
		
		Scene scena=new Scene(interfaccia, WIDTH_SCHERMO, HEIGTH_SCHERMO);
		finestra.setTitle("Spaceship");
		finestra.setScene(scena);
		finestra.show();		
	}
	public void aggiornaPosizioneSfondo() {
		posizioneSfondoX-=valoreSpostamentoSfondo;
		sfondo.setLayoutX(posizioneSfondoX);
	}
	public static void main(String[] args){
		launch(args);
	}
}
