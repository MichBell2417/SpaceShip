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
import javafx.stage.Stage;
import javafx.util.Duration;


public class muoviAereoplano extends Application{
	//SCHERMO
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
	
	public void start(Stage finestra) {
		Pane interfaccia = new Pane();
		
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
