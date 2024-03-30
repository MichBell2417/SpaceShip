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
		int nOggetti=10;
		Image immagineUfo = new Image(getClass().getResourceAsStream("Ufo.png"));
		Image immagineMeteoriteBlu = new Image(getClass().getResourceAsStream("MeteoriteBlu.png"));
		Image immagineMeteoriteViola = new Image(getClass().getResourceAsStream("MeteoriteViola.png"));
		Image vettoreImmagini[]= {immagineUfo, immagineMeteoriteBlu, immagineMeteoriteViola};
		ImageView vettoreOggetti[]=new ImageView[nOggetti];
		
		//spostamento oggetti
		int numeroOggettoPrecedente=0;
		int numeroOggettoAttuale=0;
		Timeline muoviOggetti= new Timeline(new KeyFrame(
				Duration.millis(2), 
		      x -> spostaOggetti()));
		
		public void start(Stage finestra) {
			Pane interfaccia = new Pane();
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
			
			Scene scena=new Scene(interfaccia, WIDTH_SCHERMO, HEIGTH_SCHERMO);
			finestra.setTitle("Spaceship");
			finestra.setScene(scena);
			finestra.show();		
		}
		public void spostaOggetti() {
			ImageView oggettoPrecedente=vettoreOggetti[numeroOggettoPrecedente];
			ImageView oggettoAttuale=vettoreOggetti[numeroOggettoAttuale];
			int posizioneY;
			//Math.abs() restituisce il valore assoluto dell'espressione
			if(numeroOggettoAttuale==0 || Math.abs(oggettoPrecedente.getLayoutX()-oggettoAttuale.getLayoutX())>=200) {
				if(oggettoAttuale.getLayoutX()<=-DIMENSION_OGGETTI) {
					posizioneY=(int)(Math.random()*(HEIGTH_SCHERMO-DIMENSION_OGGETTI));
					oggettoAttuale.setLayoutX(WIDTH_SCHERMO);
					oggettoAttuale.setLayoutY(posizioneY);
				}else {
					oggettoAttuale.setLayoutX(oggettoAttuale.getLayoutX()-3);
				}
			}
			numeroOggettoAttuale++;
			if(numeroOggettoAttuale>=1) {
				numeroOggettoPrecedente=numeroOggettoAttuale-1;
			}
			if(numeroOggettoAttuale==nOggetti) {
				//ho messo -1 perche alla riga 67 sommo di uno e se mettessi 0 l'oggetto a quella posizione non si muoverebbe mai
				numeroOggettoAttuale=0;	
				numeroOggettoPrecedente=0;
			}
		}
		
		public static void main(String[] args){
			launch(args);
		}
}
