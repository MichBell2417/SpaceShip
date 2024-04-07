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

public class SpostamentoAutonomoDegliOggetti extends Application{
	//SCHERMO
	final int WIDTH_SCHERMO = 1000;
	final int HEIGTH_SCHERMO = 600;

	//OGGETTI
	final int DIMENSION_OGGETTI = 100; //gli oggetti sono quadrati
	int nOggetti=15;
	Image immagineUfo = new Image(getClass().getResourceAsStream("Ufo.png"));
	Image immagineMeteoriteBlu = new Image(getClass().getResourceAsStream("MeteoriteBlu.png"));
	Image immagineMeteoriteViola = new Image(getClass().getResourceAsStream("MeteoriteViola.png"));
	Image vettoreImmagini[]= {immagineUfo, immagineMeteoriteBlu, immagineMeteoriteViola};
	ImageView vettoreOggetti[]=new ImageView[nOggetti];

	//spostamento oggetti
	ImageView oggettoAttuale;
	int numeroOggettoPrecedente=0; //ver prec
	int numeroOggettoAttuale=0; //ver prec
	int indiceOggetto=0;
	Timeline muoviOggetti= new Timeline(new KeyFrame(
			Duration.millis(2), 
			x -> spostaOggetti()));

	public void start(Stage finestra) {
		Pane interfaccia = new Pane();
		int immagine, rotazione;
		for(int n=0; n<nOggetti; n++) {
			immagine=(int)(Math.random()*vettoreImmagini.length);
			vettoreOggetti[n]=new ImageView(vettoreImmagini[immagine]);
			vettoreOggetti[n].setFitHeight(DIMENSION_OGGETTI);
			vettoreOggetti[n].setFitWidth(DIMENSION_OGGETTI);
			riposizionaOggetto(vettoreOggetti[n]);
			if(immagine!=0) {
				rotazione=(int)(Math.random()*270);
				vettoreOggetti[n].setRotate(rotazione);
			}
			interfaccia.getChildren().add(vettoreOggetti[n]);
		}
		System.out.println("posizionamento oggetti start okay");
		muoviOggetti.setCycleCount(Animation.INDEFINITE);
		muoviOggetti.play();
		Scene scena=new Scene(interfaccia, WIDTH_SCHERMO, HEIGTH_SCHERMO);
		finestra.setTitle("Spaceship");
		finestra.setScene(scena);
		finestra.show();		
	}
	/*
		public void spostaOggetti() {
			ImageView oggettoPrecedente=vettoreOggetti[numeroOggettoPrecedente];
			oggettoAttuale=vettoreOggetti[numeroOggettoAttuale];
			boundOggetti=oggettoAttuale.getBoundsInParent();

			//Math.abs() restituisce il valore assoluto dell'espressione
			//numeroOggettoAttuale==0 ||  Math.abs(oggettoPrecedente.getLayoutX()-oggettoAttuale.getLayoutX())>=200
			if(numeroOggettoAttuale==0 ||  Math.abs(oggettoPrecedente.getLayoutX()-oggettoAttuale.getLayoutX())>=200) {
				if(oggettoAttuale.getLayoutX()<=-DIMENSION_OGGETTI) {
					riposizionaOggetto(oggettoAttuale);
				}else {
					oggettoAttuale.setLayoutX(oggettoAttuale.getLayoutX()-3);
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
	 */
	public void spostaOggetti() {
		//scegliamo l'oggetto
		indiceOggetto++;
		if(indiceOggetto==nOggetti) {
			indiceOggetto=0;
		}
		oggettoAttuale=vettoreOggetti[indiceOggetto];
		//spostiamo o risposizioniamo l'oggetto
		if(oggettoAttuale.getLayoutX()<=-DIMENSION_OGGETTI) {
			riposizionaOggetto(oggettoAttuale);
		}else {
			oggettoAttuale.setLayoutX(oggettoAttuale.getLayoutX()-3);
		}
	}
	public void riposizionaOggetto(ImageView oggetto) {
		int posizioneY;
		int posizioneX;
		posizioneY=(int)(Math.random()*(HEIGTH_SCHERMO-DIMENSION_OGGETTI));
		posizioneX=(int)(Math.random()*WIDTH_SCHERMO);
		oggetto.setLayoutX(WIDTH_SCHERMO+posizioneX);
		oggetto.setLayoutY(posizioneY);

	}
	public static void main(String[] args){
		launch(args);
	}
}
