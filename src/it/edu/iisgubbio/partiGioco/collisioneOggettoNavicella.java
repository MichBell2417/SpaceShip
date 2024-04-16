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
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;


public class collisioneOggettoNavicella extends Application{
	//SCHERMO
	Pane interfaccia = new Pane();
	final int WIDTH_SCHERMO = 1000;
	final int HEIGTH_SCHERMO = 600;
	
	//NAVICELLA
	Image immagineNavicella = new Image(getClass().getResourceAsStream("NavicellaSpaziale.png"));
	ImageView navicella = new ImageView(immagineNavicella);
	final int WIDTH_NAVICELLA = 200; //le dimensioni nel codice sono richiamate al contrario
	final int HEIGTH_NAVICELLA = 225;//la WIDTH serve al posto della HEIGTH e viceversa
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
								 //a causa della rotazione di novanta gradi
	
	//ELLISSI E CERCHI PER COLLISIONI
	final int RADIUS_WIDTH_ELLISSE_VERT = 30;
	final int RADIUS_HEIGTH_ELLISSE_VERT = 101;
	final int POS_X_ELLISSE_VERTICALE = posizioneNaviciella[0]+WIDTH_NAVICELLA/2-10;
	final int POS_Y_ELLISSE_VERTICALE = posizioneNaviciella[1]+HEIGTH_NAVICELLA/2;
	Ellipse ellisseVert = new Ellipse (POS_X_ELLISSE_VERTICALE,POS_Y_ELLISSE_VERTICALE,RADIUS_WIDTH_ELLISSE_VERT,RADIUS_HEIGTH_ELLISSE_VERT);
	
	final int RADIUS_WIDTH_ELLISSE_ORIZ = 101;
	final int RADIUS_HEIGTH_ELLISSE_ORIZ = 25;
	final int POS_X_ELLISSE_ORIZZONTALE = posizioneNaviciella[0]+WIDTH_NAVICELLA/2-10;
	final int POS_Y_ELLISSE_ORIZZONTALE = posizioneNaviciella[1]+HEIGTH_NAVICELLA/2;
	Ellipse ellisseOriz = new Ellipse (POS_X_ELLISSE_ORIZZONTALE,POS_Y_ELLISSE_ORIZZONTALE,RADIUS_WIDTH_ELLISSE_ORIZ,RADIUS_HEIGTH_ELLISSE_ORIZ);
	
	final int WIDTH_ELLISSE_UFO = 95;
	final int HEIGTH_ELLISSE_UFO = 30;
	
	final int WIDTH_ELLISSE_ASTEROIDI = 95;
	
	Timeline collisioniOggettiNavicella= new Timeline(new KeyFrame(
		      Duration.millis(5), 
		      x -> metodoCollisioniOggettiNavicella()));
	
	//OGGETTI
		final int DIMENSION_OGGETTI = 100; //gli oggetti sono quadrati
		int nOggetti=15;
		Image immagineUfo = new Image(getClass().getResourceAsStream("Ufo.png"));
		Image immagineMeteoriteBlu = new Image(getClass().getResourceAsStream("MeteoriteBlu.png"));
		Image immagineMeteoriteViola = new Image(getClass().getResourceAsStream("MeteoriteViola.png"));
		Image vettoreImmagini[]= {immagineUfo, immagineMeteoriteBlu, immagineMeteoriteViola};
		ImageView vettoreOggetti[]=new ImageView[nOggetti];
		Ellipse vettoreEllissiCollisione[]=new Ellipse[nOggetti];

		//spostamento oggetti
		ImageView oggettoAttuale;
		int numeroOggettoPrecedente=0; //ver prec
		int numeroOggettoAttuale=0; //ver prec
		int indiceOggetto=0;
		Timeline muoviOggetti= new Timeline(new KeyFrame(
				Duration.millis(2), 
				x -> spostaOggetti()));
		
		
		final int WIDTH_ESPLOSIONE = 200;
		final int HEIGTH_ESPLOSIONE = 200;
		Image animazioneEsplosione = new Image(getClass().getResourceAsStream("animazione-esplosione2.gif"));
		
	public void start(Stage finestra) {
		
		//settaggio navicella
        navicella.setFitWidth(WIDTH_NAVICELLA);
        navicella.setFitHeight(HEIGTH_NAVICELLA);
		navicella.setRotate(90);
		navicella.setLayoutX(posizioneNaviciella[0]);
		navicella.setLayoutY(posizioneNaviciella[1]);
        interfaccia.getChildren().add(navicella);
        muoviNavicella.setCycleCount(Animation.INDEFINITE);
        muoviNavicella.play();
        
        //settaggio oggetti
        int immagine, rotazione;
		for(int n=0; n<nOggetti; n++) {
			immagine=(int)(Math.random()*vettoreImmagini.length);
			vettoreOggetti[n]=new ImageView(vettoreImmagini[immagine]);
			vettoreOggetti[n].setFitHeight(DIMENSION_OGGETTI);
			vettoreOggetti[n].setFitWidth(DIMENSION_OGGETTI);
			if(immagine!=0) {
				Ellipse EllisseAsteroidiColl = new Ellipse(WIDTH_ELLISSE_ASTEROIDI/2,WIDTH_ELLISSE_ASTEROIDI/2);
				vettoreEllissiCollisione[n]=EllisseAsteroidiColl;
				rotazione=(int)(Math.random()*270);
				vettoreOggetti[n].setRotate(rotazione);
			}else {
				Ellipse EllisseUfoColl = new Ellipse(WIDTH_ELLISSE_UFO/2, HEIGTH_ELLISSE_UFO/2);
				vettoreEllissiCollisione[n]=EllisseUfoColl;
			}
			riposizionaOggetto(n);
			interfaccia.getChildren().add(vettoreOggetti[n]);
			interfaccia.getChildren().add(vettoreEllissiCollisione[n]);
		}
		
		//settaggio oggetto navicella collisioni
		
		interfaccia.getChildren().add(ellisseVert);
		interfaccia.getChildren().add(ellisseOriz);
		
		
		System.out.println("posizionamento oggetti start okay");
		muoviOggetti.setCycleCount(Animation.INDEFINITE);
		muoviOggetti.play();

		collisioniOggettiNavicella.setCycleCount(Animation.INDEFINITE);
		collisioniOggettiNavicella.play();
		
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
			ellisseOriz.setCenterY(posizioneNaviciella[1]+HEIGTH_NAVICELLA/2);
			ellisseVert.setCenterY(posizioneNaviciella[1]+HEIGTH_NAVICELLA/2);
			
		}
		if(spostaGIU && navicella.getLayoutY()<=HEIGTH_SCHERMO-WIDTH_NAVICELLA) {
			posizioneNaviciella[1]+=valoreSpostamentoNavicella;
			navicella.setLayoutY(posizioneNaviciella[1]);
			ellisseOriz.setCenterY(posizioneNaviciella[1]+HEIGTH_NAVICELLA/2);
			ellisseVert.setCenterY(posizioneNaviciella[1]+HEIGTH_NAVICELLA/2);
			
		}if(spostaAVANTI && navicella.getLayoutX()<=WIDTH_SCHERMO-HEIGTH_NAVICELLA) {
			posizioneNaviciella[0]+=valoreSpostamentoNavicella;
			navicella.setLayoutX(posizioneNaviciella[0]);
			ellisseOriz.setCenterX(posizioneNaviciella[0]+WIDTH_NAVICELLA/2-10);
			ellisseVert.setCenterX(posizioneNaviciella[0]+WIDTH_NAVICELLA/2-10);
		}
		if(spostaINDIETRO && navicella.getLayoutX()>=0) {
			posizioneNaviciella[0]-=valoreSpostamentoNavicella;
			navicella.setLayoutX(posizioneNaviciella[0]);
			ellisseOriz.setCenterX(posizioneNaviciella[0]+WIDTH_NAVICELLA/2-10);
			ellisseVert.setCenterX(posizioneNaviciella[0]+WIDTH_NAVICELLA/2-10);
		}
	}
	public void spostaOggetti() {
		//scegliamo l'oggetto
		indiceOggetto++;
		if(indiceOggetto==nOggetti) {
			indiceOggetto=0;
		}
		oggettoAttuale=vettoreOggetti[indiceOggetto];
		//spostiamo o risposizioniamo l'oggetto
		if(oggettoAttuale.getLayoutX()<=-DIMENSION_OGGETTI) {
			riposizionaOggetto(indiceOggetto);
		}else {
			oggettoAttuale.setLayoutX(oggettoAttuale.getLayoutX()-3);

			vettoreEllissiCollisione[indiceOggetto].setCenterX((oggettoAttuale.getLayoutX()+DIMENSION_OGGETTI/2)-3);
		}
	}
	public void riposizionaOggetto(int posizione) {
		int posizioneY;
		int posizioneX;
		posizioneY=(int)(Math.random()*(HEIGTH_SCHERMO-DIMENSION_OGGETTI));
		posizioneX=(int)(Math.random()*WIDTH_SCHERMO);
		vettoreOggetti[posizione].setLayoutX(WIDTH_SCHERMO+posizioneX);
		vettoreOggetti[posizione].setLayoutY(posizioneY);
		vettoreEllissiCollisione[posizione].setCenterX(WIDTH_SCHERMO+posizioneX);
		vettoreEllissiCollisione[posizione].setCenterY(posizioneY+DIMENSION_OGGETTI/2);

	}
	int posizioneCollisioneOggetto=0;
	public void metodoCollisioniOggettiNavicella() {
		posizioneCollisioneOggetto++;
		if(posizioneCollisioneOggetto==nOggetti) {
			posizioneCollisioneOggetto=0;
		}
		boolean intersezione=false;
		Shape intersectPunta = Shape.intersect(ellisseOriz, vettoreEllissiCollisione[posizioneCollisioneOggetto]);
        if (intersectPunta.getBoundsInLocal().getWidth() != -1){
            System.out.println("collisione punta");
            intersezione=true;
        }
        Shape intersectAli = Shape.intersect(ellisseVert, vettoreEllissiCollisione[posizioneCollisioneOggetto]);
        if (intersectAli.getBoundsInLocal().getWidth() != -1){
            System.out.println("collisione Ali");
            intersezione=true;
        }
        
        if(intersezione) {
			int posizioneXMeteorie=(int) (vettoreOggetti[posizioneCollisioneOggetto].getLayoutX());
            riposizionaOggetto(posizioneCollisioneOggetto);
            /*ImageView esplosione=new ImageView(animazioneEsplosione);
			esplosione.setFitHeight(HEIGTH_ESPLOSIONE);
			esplosione.setFitWidth(WIDTH_ESPLOSIONE);    
			interfaccia.getChildren().add(esplosione);
			esplosione.setLayoutX(+WIDTH_ESPLOSIONE/2);
			esplosione.setLayoutY(posizioneYMissile-HEIGTH_ESPLOSIONE/2);
			rimuoviOggetto(500, esplosione);*/
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
			oggetto.setLayoutX(WIDTH_SCHERMO*2);
			interfaccia.getChildren().remove(oggetto);
	}
	public static void main(String[] args){
		launch(args);
	}
}
