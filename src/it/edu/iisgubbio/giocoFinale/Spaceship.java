package it.edu.iisgubbio.giocoFinale;

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

public class Spaceship extends Application{
	//SCHERMO
	Pane interfaccia = new Pane();
	final int WIDTH_SCHERMO = 1000;
	final int HEIGTH_SCHERMO = 600;

	//SFONDO
	final int WIDTH_SFONDO = 5000;
	final int HEIGTH_SFONDO = 800;
	Image immagineSfondo = new Image(getClass().getResourceAsStream("Sfondo.png"));
	ImageView sfondo = new ImageView(immagineSfondo);
	final int WIDTH_SFOCATURA=2000;
	final int HEIGTH_SFOCATURA = 1600;
	Image immagineSfocatura = new Image(getClass().getResourceAsStream("Sfocatura.png"));
	ImageView sfocatura = new ImageView(immagineSfocatura);
	//spostamento sfondo
	double tempoDiSpostamentoTOT=2; //se si vuole scegliere la durata del gioco standard 25s
	double posizioneSfondoX=0;
	double valoreSpostamentoSfondo=WIDTH_SFONDO*25/(tempoDiSpostamentoTOT*1000);
	Timeline muoviSfondo= new Timeline(new KeyFrame(
			Duration.millis(25), 
			x -> aggiornaPosizioneSfondo()));
	

	//OGGETTI
	final int DIMENSION_OGGETTI = 100; //gli oggetti sono quadrati
	int nOggetti=7;
	Image immagineUfo = new Image(getClass().getResourceAsStream("Ufo.png"));
	Image immagineMeteoriteBlu = new Image(getClass().getResourceAsStream("MeteoriteBlu.png"));
	Image immagineMeteoriteViola = new Image(getClass().getResourceAsStream("MeteoriteViola.png"));
	Image vettoreImmagini[]= {immagineUfo, immagineMeteoriteBlu, immagineMeteoriteViola};
	ImageView vettoreOggetti[]=new ImageView[nOggetti];
	//spostamento oggetti
	int distanzaDiAzione=200;
	int valoreSpostamentoOggetti=2;
	int numeroOggettoPrecedente=0;
	int numeroOggettoAttuale=0;
	Timeline muoviOggetti= new Timeline(new KeyFrame(
			Duration.millis(1), 
			x -> spostaOggetti()));

	
	//NAVICELLA
	Image immagineNavicella = new Image(getClass().getResourceAsStream("NavicellaSpaziale.png"));
	ImageView navicella = new ImageView(immagineNavicella);
	final int WIDTH_NAVICELLA = 125;
	final int HEIGTH_NAVICELLA = 150;
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

		//settaggi sfondo
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
		
		//settaggi oggetti
		int immagine, posizioneY, rotazione;
		for(int n=0; n<nOggetti; n++) {
			posizioneY=(int)(Math.random()*(HEIGTH_SCHERMO-DIMENSION_OGGETTI));
			immagine=(int)(Math.random()*vettoreImmagini.length);
			vettoreOggetti[n]=new ImageView(vettoreImmagini[immagine]);
			vettoreOggetti[n].setFitHeight(DIMENSION_OGGETTI);
			vettoreOggetti[n].setFitWidth(DIMENSION_OGGETTI);
			vettoreOggetti[n].setLayoutX(WIDTH_SCHERMO);
			vettoreOggetti[n].setLayoutY(posizioneY);
			if(immagine!=0) {
				rotazione=(int)(Math.random()*270);
				vettoreOggetti[n].setRotate(rotazione);
			}
			interfaccia.getChildren().add(vettoreOggetti[n]);
		}
		muoviOggetti.setCycleCount(Animation.INDEFINITE);
		muoviOggetti.play();

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

		interfaccia.getChildren().add(sfocatura);
		Scene scena=new Scene(interfaccia, WIDTH_SCHERMO, HEIGTH_SCHERMO);

		scena.setOnKeyPressed(e -> pigiato(e));
		scena.setOnKeyReleased(e -> rilasciato(e));

		finestra.setTitle("Spaceship");
		finestra.setScene(scena);
		finestra.resizableProperty().setValue(false); //blocca il ridimensionamento della finestra
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

	public void spostaOggetti() {
		ImageView oggettoPrecedente=vettoreOggetti[numeroOggettoPrecedente];
		ImageView oggettoAttuale=vettoreOggetti[numeroOggettoAttuale];
		int posizioneY;
		//Math.abs() restituisce il valore assoluto dell'espressione
		if(numeroOggettoAttuale==0 || Math.abs(oggettoPrecedente.getLayoutX()-oggettoAttuale.getLayoutX())>=distanzaDiAzione) {
			if(oggettoAttuale.getLayoutX()<=-DIMENSION_OGGETTI) {
				posizioneY=(int)(Math.random()*(HEIGTH_SCHERMO-DIMENSION_OGGETTI));
				oggettoAttuale.setLayoutX(WIDTH_SCHERMO);
				oggettoAttuale.setLayoutY(posizioneY);
			}else {
				oggettoAttuale.setLayoutX(oggettoAttuale.getLayoutX()-valoreSpostamentoOggetti);
			}
		}
		numeroOggettoAttuale++;
		if(numeroOggettoAttuale>=1) {
			numeroOggettoPrecedente=numeroOggettoAttuale-1;
		}
		if(numeroOggettoAttuale==nOggetti) {
			numeroOggettoAttuale=0;	
			numeroOggettoPrecedente=0;
		}
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
		for(int nM=0; nM<munizioniUtilizzate; nM++) { 
			munizioni[nM].setLayoutX(munizioni[nM].getLayoutX()+valoreSpostamentoNavicella+5);
			if(munizioni[nM].getLayoutX()>=WIDTH_SCHERMO) {
				rimuoviOggetto(10, munizioni[nM]);
			}
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
	public void rimuoviOggetto(int traQuantoTempo, ImageView oggetto) {
		Timeline eliminaOggetto;
		eliminaOggetto= new Timeline(new KeyFrame(
				Duration.millis(traQuantoTempo), 
				x -> eseguiRimozione(oggetto)));
		eliminaOggetto.setCycleCount(1);
		eliminaOggetto.play();
	}
	public void eseguiRimozione(ImageView oggetto) {
			interfaccia.getChildren().remove(oggetto);
	}
	public static void main(String[] args){
		launch(args);
	}
}
