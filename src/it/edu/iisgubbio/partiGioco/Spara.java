package it.edu.iisgubbio.partiGioco;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Spara extends Application{
	//SCHERMO
	Pane interfaccia = new Pane();
	final int WIDTH_SCHERMO = 1000;
	final int HEIGTH_SCHERMO = 600;

	//NAVICELLA
	Image immagineNavicella = new Image(getClass().getResourceAsStream("NavicellaSpaziale.png"));
	ImageView navicella = new ImageView(immagineNavicella);
	final int WIDTH_NAVICELLA = 200;
	final int HEIGTH_NAVICELLA = 225;

	//spostamento navicella
	boolean spostaSU=false;
	boolean spostaGIU=false;
	boolean spostaAVANTI=false;
	boolean spostaINDIETRO=false;
	int posizioneNaviciella[] = {0, (HEIGTH_SCHERMO-WIDTH_NAVICELLA)/2};
	int valoreSpostamentoNavicella=10;
	Timeline muoviNavicella= new Timeline(new KeyFrame(
			Duration.millis(25), 
			x -> aggiornaPosizioneNavicella()));

	//LASER
	final int WIDTH_LASER = 60;
	final int HEIGTH_LASER = 30;
	int numeroMunizioni=1000;
	int munizioniUtilizzate=0;
	ImageView[] munizioni= new ImageView[numeroMunizioni];
	//spawn missile
	int precedente=0;
	long tempoScorsoMissile;
	
	public void start(Stage finestra) {
		//riempimento munizioni
		for(int nM=0; nM<numeroMunizioni; nM++) {
			Image immagineMissile=new Image(getClass().getResourceAsStream("Missile.png"));
			munizioni[nM]=new ImageView(immagineMissile);
			munizioni[nM].setFitHeight(HEIGTH_LASER);
			munizioni[nM].setFitWidth(WIDTH_LASER);
			munizioni[nM].setLayoutX(WIDTH_SCHERMO);;
			interfaccia.getChildren().add(munizioni[nM]);
		}
		
		//settaggio navicella
		navicella.setFitWidth(WIDTH_NAVICELLA);
		navicella.setFitHeight(HEIGTH_NAVICELLA);
		navicella.setRotate(90);
		navicella.setLayoutX(posizioneNaviciella[0]);
		navicella.setLayoutY(posizioneNaviciella[1]);
		interfaccia.getChildren().add(navicella);
		muoviNavicella.setCycleCount(Animation.INDEFINITE);
		muoviNavicella.play();

		Scene scena=new Scene(interfaccia, WIDTH_SCHERMO, HEIGTH_SCHERMO);

		scena.setOnKeyPressed(e -> pigiato(e));
		scena.setOnKeyReleased(e -> rilasciato(e));

		finestra.setTitle("Spaceship");
		finestra.setScene(scena);
		finestra.show();		
	}
	public void spara() {
		if(System.currentTimeMillis()-tempoScorsoMissile>=100) {
			if(munizioniUtilizzate==numeroMunizioni) {
				
			}else {
				munizioni[munizioniUtilizzate].setLayoutY(navicella.getLayoutY()+(HEIGTH_NAVICELLA/2-HEIGTH_LASER/2));
				munizioni[munizioniUtilizzate].setLayoutX(navicella.getLayoutX()+WIDTH_NAVICELLA-WIDTH_LASER);
				munizioniUtilizzate++;
				precedente=munizioniUtilizzate-1;
				tempoScorsoMissile= System.currentTimeMillis();
			}	
		}
	}
	public void pigiato(KeyEvent pulsante) {
		switch(pulsante.getText().toLowerCase()) {
		case "w" :
			spostaSU=true;
			break;
		case "d" :
			spostaAVANTI=true;
			break;
		case "s" :
			spostaGIU=true;
			break;
		case "a" :
			spostaINDIETRO=true;
			break;
		case " ":
			spara();
		}
	}
	public void rilasciato(KeyEvent pulsante) {
		switch(pulsante.getText().toLowerCase()) {
		case "w" :
			spostaSU=false;
			break;
		case "d" :
			spostaAVANTI=false;
			break;
		case "s" :
			spostaGIU=false;
			break;
		case "a" :
			spostaINDIETRO=false;
			break;
		}
	}
	public void aggiornaPosizioneNavicella() {
		for(int nM=0; nM<munizioniUtilizzate; nM++) { 
			munizioni[nM].setLayoutX(munizioni[nM].getLayoutX()+valoreSpostamentoNavicella+5);
		}
		if(spostaSU && navicella.getLayoutY()>=-20) {
			posizioneNaviciella[1]-=valoreSpostamentoNavicella;
			navicella.setLayoutY(posizioneNaviciella[1]);
		}
		if(spostaGIU && navicella.getLayoutY()<=HEIGTH_SCHERMO-WIDTH_NAVICELLA) {
			posizioneNaviciella[1]+=valoreSpostamentoNavicella;
			navicella.setLayoutY(posizioneNaviciella[1]);
		}if(spostaAVANTI && navicella.getLayoutX()<=WIDTH_SCHERMO-HEIGTH_NAVICELLA) {
			posizioneNaviciella[0]+=valoreSpostamentoNavicella;
			navicella.setLayoutX(posizioneNaviciella[0]);
		}
		if(spostaINDIETRO && navicella.getLayoutX()>=0) {
			posizioneNaviciella[0]-=valoreSpostamentoNavicella;
			navicella.setLayoutX(posizioneNaviciella[0]);
		}
	}

	public static void main(String[] args){
		launch(args);
	}
}
