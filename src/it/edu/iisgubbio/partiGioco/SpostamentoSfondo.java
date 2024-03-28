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

public class SpostamentoSfondo extends Application{
	//SCHERMO
	final int WIDTH_SCHERMO = 1000;
	final int HEIGTH_SCHERMO = 600;
	
	//SFONDO
	final int WIDTH_SFONDO = 5000;
	final int HEIGTH_SFONDO = 800;
	int posizioneFinaleSfondo;
	Image immagineSfondo = new Image(getClass().getResourceAsStream("Sfondo.png"));
	ImageView sfondo = new ImageView(immagineSfondo);
	final int WIDTH_SFOCATURA=2000;
	final int HEIGTH_SFOCATURA = 1600;
	Image immagineSfocatura = new Image(getClass().getResourceAsStream("Sfocatura.png"));
	ImageView sfocatura = new ImageView(immagineSfocatura);
	
	//spostamento sfondo
	double tempoDiSpostamentoTOT=10; //se si vuole scegliere la durata del gioco standard 25s
	double posizioneSfondoX=0;
	double valoreSpostamentoSfondo=WIDTH_SFONDO*25/(tempoDiSpostamentoTOT*1000);
	Timeline muoviSfondo= new Timeline(new KeyFrame(
		      Duration.millis(25), 
		      x -> aggiornaPosizioneSfondo()));
	
	public void start(Stage finestra) {
		Pane interfaccia = new Pane();
		
		sfondo.setFitWidth(WIDTH_SFONDO);
		sfondo.setFitHeight(HEIGTH_SFONDO);
		sfondo.setLayoutX(0);
		sfondo.setLayoutY((HEIGTH_SCHERMO-HEIGTH_SFONDO)/2);
		muoviSfondo.setCycleCount(Animation.INDEFINITE);
		muoviSfondo.play();
		interfaccia.getChildren().add(sfondo);

		sfocatura.setFitWidth(WIDTH_SFOCATURA);
		sfocatura.setFitHeight(HEIGTH_SFOCATURA);
		sfocatura.setLayoutX(0);
		sfocatura.setLayoutY((HEIGTH_SCHERMO-HEIGTH_SFOCATURA)/2);
		interfaccia.getChildren().add(sfocatura);
		
		Scene scena=new Scene(interfaccia, WIDTH_SCHERMO, HEIGTH_SCHERMO);
		finestra.setTitle("Spaceship");
		finestra.setScene(scena);
		finestra.show();		
	}
	public void aggiornaPosizioneSfondo() {
		posizioneSfondoX-=valoreSpostamentoSfondo;
		sfondo.setLayoutX(posizioneSfondoX);
		double posizioneFinaleSfondo=posizioneSfondoX+WIDTH_SFONDO;
		sfocatura.setLayoutX(posizioneFinaleSfondo-WIDTH_SFOCATURA/2);
		if(posizioneFinaleSfondo>WIDTH_SCHERMO && posizioneFinaleSfondo<WIDTH_SCHERMO+20) {
			muoviSfondo.stop();
		}
	}
	public static void main(String[] args){
		launch(args);
	}
}
