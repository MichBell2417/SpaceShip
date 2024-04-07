package it.edu.iisgubbio.giocoFinale;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Spaceship extends Application {
	// SCHERMO
	Pane interfaccia = new Pane();
	final int WIDTH_SCHERMO = 1000;
	final int HEIGTH_SCHERMO = 600;

	// SFONDO
	final int WIDTH_SFONDO = 5000;
	final int HEIGTH_SFONDO = 800;
	Image immagineSfondo = new Image(getClass().getResourceAsStream("Sfondo.png"));
	ImageView sfondo = new ImageView(immagineSfondo);
	final int WIDTH_SFOCATURA = 2000;
	final int HEIGTH_SFOCATURA = 1600;
	Image immagineSfocatura = new Image(getClass().getResourceAsStream("Sfocatura.png"));
	ImageView sfocatura = new ImageView(immagineSfocatura);
	// spostamento sfondo
	double tempoDiSpostamentoTOT = 25; // se si vuole scegliere la durata del gioco standard 25s
	double posizioneSfondoX = 0;
	double valoreSpostamentoSfondo = WIDTH_SFONDO * 25 / (tempoDiSpostamentoTOT * 1000);
	Timeline muoviSfondo = new Timeline(new KeyFrame(Duration.millis(25), x -> aggiornaPosizioneSfondo()));

	// OGGETTI
	final int DIMENSION_OGGETTI = 100; // gli oggetti sono quadrati
	int nOggetti = 10;
	Image immagineUfo = new Image(getClass().getResourceAsStream("Ufo.png"));
	Image immagineMeteoriteBlu = new Image(getClass().getResourceAsStream("MeteoriteBlu.png"));
	Image immagineMeteoriteViola = new Image(getClass().getResourceAsStream("MeteoriteViola.png"));
	Image vettoreImmagini[] = { immagineUfo, immagineMeteoriteBlu, immagineMeteoriteViola };
	ImageView vettoreOggetti[] = new ImageView[nOggetti];
	// spostamento oggetti
	ImageView oggettoAttuale;
	int indiceOggetto = 0;
	Timeline muoviOggetti = new Timeline(new KeyFrame(Duration.millis(1), x -> spostaOggetti()));

	// NAVICELLA
	Image immagineNavicella = new Image(getClass().getResourceAsStream("NavicellaSpaziale.png"));
	ImageView navicella = new ImageView(immagineNavicella);
	final int WIDTH_NAVICELLA = 125;
	final int HEIGTH_NAVICELLA = 150;
	// spostamento navicella
	boolean spostaSU = false;
	boolean spostaGIU = false;
	boolean spostaAVANTI = false;
	boolean spostaINDIETRO = false;
	int posizioneNaviciella[] = { 0, (HEIGTH_SCHERMO - WIDTH_NAVICELLA) / 2 };
	int valoreSpostamentoNavicella = 10;
	Timeline muoviNavicella = new Timeline(new KeyFrame(Duration.millis(25), x -> aggiornaPosizioneNavicella()));

	// MUNIZIONI
	final int WIDTH_MISSILE = 60;
	final int HEIGTH_MISSILE = 30;
	int numeroMunizioni = 1000;
	int munizioniUtilizzate = 0;
	ImageView[] munizioni = new ImageView[numeroMunizioni];
	// spawn missile
	int precedente = 0;
	long tempoScorsoMissile;

	// colisioni oggetto misile
	int numeroOggettoBound = 0;
	int numeroMissileBound = 0;
	int conta = 0;
	boolean[] numeriMunizioniEsaurite = new boolean[numeroMunizioni]; // vettore che contiene lo stato di ogni missile
	Bounds boundOggetti;
	Bounds boundMissile;

	// controlla intersezione
	Timeline controllaCollisione = new Timeline(new KeyFrame(Duration.millis(2), x -> metodoControllaCollisione()));
	// esplosione
	final int WIDTH_ESPLOSIONE = 200;
	final int HEIGTH_ESPLOSIONE = 200;
	Image animazioneEsplosione = new Image(getClass().getResourceAsStream("animazione-esplosione1.gif"));	
	
	public void start(Stage finestra) {

		// settaggi sfondo
		sfondo.setFitWidth(WIDTH_SFONDO);
		sfondo.setFitHeight(HEIGTH_SFONDO);
		sfondo.setLayoutX(0);
		sfondo.setLayoutY((HEIGTH_SCHERMO - HEIGTH_SFONDO) / 2);
		interfaccia.getChildren().add(sfondo);
		sfocatura.setFitWidth(WIDTH_SFOCATURA);
		sfocatura.setFitHeight(HEIGTH_SFOCATURA);
		sfocatura.setLayoutX(0);
		sfocatura.setLayoutY((HEIGTH_SCHERMO - HEIGTH_SFOCATURA) / 2);

		// settaggi oggetti
		int immagine, posizioneY, rotazione, posizioneX;
		for (int n = 0; n < nOggetti; n++) {
			posizioneY = (int) (Math.random() * (HEIGTH_SCHERMO - DIMENSION_OGGETTI));
			posizioneX = (int) (Math.random() * WIDTH_SCHERMO);
			immagine = (int) (Math.random() * vettoreImmagini.length);
			vettoreOggetti[n] = new ImageView(vettoreImmagini[immagine]);
			vettoreOggetti[n].setFitHeight(DIMENSION_OGGETTI);
			vettoreOggetti[n].setFitWidth(DIMENSION_OGGETTI);
			vettoreOggetti[n].setLayoutX(WIDTH_SCHERMO + posizioneX);
			vettoreOggetti[n].setLayoutY(posizioneY);
			if (immagine != 0) {
				rotazione = (int) (Math.random() * 270);
				vettoreOggetti[n].setRotate(rotazione);
			}
			interfaccia.getChildren().add(vettoreOggetti[n]);
		}

		// riempimento munizioni
		for (int nM = 0; nM < numeroMunizioni; nM++) {
			Image immagineMissile = new Image(getClass().getResourceAsStream("Missile.png"));
			munizioni[nM] = new ImageView(immagineMissile);
			munizioni[nM].setFitHeight(HEIGTH_MISSILE);
			munizioni[nM].setFitWidth(WIDTH_MISSILE);
			munizioni[nM].setLayoutX(WIDTH_SCHERMO);
			;
			interfaccia.getChildren().add(munizioni[nM]);
		}

		// settaggio navicella
		navicella.setFitWidth(WIDTH_NAVICELLA);
		navicella.setFitHeight(HEIGTH_NAVICELLA);
		navicella.setRotate(90);
		navicella.setLayoutX(posizioneNaviciella[0]);
		navicella.setLayoutY(posizioneNaviciella[1]);
		interfaccia.getChildren().add(navicella);

		interfaccia.getChildren().add(sfocatura);
		Scene scena = new Scene(interfaccia, WIDTH_SCHERMO, HEIGTH_SCHERMO);

		scena.setOnKeyPressed(e -> pigiato(e));
		scena.setOnKeyReleased(e -> rilasciato(e));

		finestra.setTitle("Spaceship");
		finestra.setScene(scena);
		finestra.resizableProperty().setValue(false); // blocca il ridimensionamento della finestra
		finestra.show();

		// movimento sfondo
		muoviSfondo.setCycleCount(Animation.INDEFINITE);
		muoviSfondo.play();
		// controlla collisioni
		controllaCollisione.setCycleCount(Animation.INDEFINITE);
		controllaCollisione.play();
		// movimento navicella
		muoviNavicella.setCycleCount(Animation.INDEFINITE);
		muoviNavicella.play();
		// movimento oggetti
		muoviOggetti.setCycleCount(Animation.INDEFINITE);
		muoviOggetti.play();
	}

	public void aggiornaPosizioneSfondo() {
		posizioneSfondoX -= valoreSpostamentoSfondo;
		sfondo.setLayoutX(posizioneSfondoX);
		double posizioneFinaleSfondo = posizioneSfondoX + WIDTH_SFONDO;
		sfocatura.setLayoutX(posizioneFinaleSfondo - WIDTH_SFOCATURA / 2);
		if (posizioneFinaleSfondo > WIDTH_SCHERMO && posizioneFinaleSfondo < WIDTH_SCHERMO + 20) {
			muoviSfondo.stop();
		}
	}

	public void spostaOggetti() {
		// scegliamo l'oggetto
		indiceOggetto++;
		if (indiceOggetto == nOggetti) {
			indiceOggetto = 0;
		}
		oggettoAttuale = vettoreOggetti[indiceOggetto];
		oggettoAttuale.setLayoutX(oggettoAttuale.getLayoutX() - 3);
		// spostiamo o risposizioniamo l'oggetto
		if (oggettoAttuale.getLayoutX() <= -DIMENSION_OGGETTI) {
			riposizionaOggetto(oggettoAttuale);
		}
	}

	public void riposizionaOggetto(ImageView oggetto) {
		int posizioneY;
		int posizioneX;
		posizioneY = (int) (Math.random() * (HEIGTH_SCHERMO - DIMENSION_OGGETTI));
		posizioneX = (int) (Math.random() * WIDTH_SCHERMO);
		oggetto.setLayoutX(WIDTH_SCHERMO + posizioneX);
		oggetto.setLayoutY(posizioneY);
	}

	public void spara() {
		if (System.currentTimeMillis() - tempoScorsoMissile >= 100) {
			if (munizioniUtilizzate == numeroMunizioni) {
				
			} else {
				munizioni[munizioniUtilizzate]
						.setLayoutY(navicella.getLayoutY() + (HEIGTH_NAVICELLA / 2 - HEIGTH_MISSILE / 2));
				munizioni[munizioniUtilizzate].setLayoutX(navicella.getLayoutX() + WIDTH_NAVICELLA - WIDTH_MISSILE);
				munizioniUtilizzate++;
				precedente = munizioniUtilizzate - 1;
				tempoScorsoMissile = System.currentTimeMillis();
			}
		}
	}

	public void pigiato(KeyEvent pulsante) {
		switch (pulsante.getText().toLowerCase()) {
		case "w":
			spostaSU = true;
			break;
		case "d":
			spostaAVANTI = true;
			break;
		case "s":
			spostaGIU = true;
			break;
		case "a":
			spostaINDIETRO = true;
			break;
		case " ":
			spara();
			break;
		}
	}

	public void rilasciato(KeyEvent pulsante) {
		switch (pulsante.getText().toLowerCase()) {
		case "w":
			spostaSU = false;
			break;
		case "d":
			spostaAVANTI = false;
			break;
		case "s":
			spostaGIU = false;
			break;
		case "a":
			spostaINDIETRO = false;
			break;
		}
	}

	public void aggiornaPosizioneNavicella() {
		for (int nM = 0; nM < munizioniUtilizzate; nM++) {
			munizioni[nM].setLayoutX(munizioni[nM].getLayoutX() + valoreSpostamentoNavicella + 3);
		}
		if (spostaSU && navicella.getLayoutY() >= -20) {
			posizioneNaviciella[1] -= valoreSpostamentoNavicella;
			navicella.setLayoutY(posizioneNaviciella[1]);
		}
		if (spostaGIU && navicella.getLayoutY() <= HEIGTH_SCHERMO - WIDTH_NAVICELLA) {
			posizioneNaviciella[1] += valoreSpostamentoNavicella;
			navicella.setLayoutY(posizioneNaviciella[1]);
		}
		if (spostaAVANTI && navicella.getLayoutX() <= WIDTH_SCHERMO - HEIGTH_NAVICELLA) {
			posizioneNaviciella[0] += valoreSpostamentoNavicella;
			navicella.setLayoutX(posizioneNaviciella[0]);
		}
		if (spostaINDIETRO && navicella.getLayoutX() >= 0) {
			posizioneNaviciella[0] -= valoreSpostamentoNavicella;
			navicella.setLayoutX(posizioneNaviciella[0]);
		}
	}

	public void metodoControllaCollisione() {
		ImageView oggettoSottopostoBound;
		numeroOggettoBound++;
		if (numeroOggettoBound == nOggetti) {
			numeroOggettoBound = 0;
		}
		oggettoSottopostoBound = vettoreOggetti[numeroOggettoBound];
		boundOggetti = oggettoSottopostoBound.getBoundsInParent();

		ImageView missileSottopostoBound;
		// long start = System.nanoTime();
		for (int i = 0; i < munizioniUtilizzate; i++) {
			if (!numeriMunizioniEsaurite[i]) { // controlliamo se il missile è esploso o scomparso
				missileSottopostoBound = munizioni[i];
				boundMissile = missileSottopostoBound.getBoundsInParent();
				if (missileSottopostoBound.getLayoutX() > WIDTH_SCHERMO) {
					rimuoviOggetto(10, missileSottopostoBound);
					numeriMunizioniEsaurite[i] = true;
				}
				if (boundMissile.intersects(boundOggetti)) {
					int posizioneXMissile, posizioneXMeteorie, posizioneYMissile;
					// prendiamo le posizione del missile e gli oggetti
					posizioneXMissile = (int) (missileSottopostoBound.getLayoutX() + WIDTH_MISSILE);
					posizioneXMeteorie = (int) (oggettoSottopostoBound.getLayoutX());
					posizioneYMissile = (int) (missileSottopostoBound.getLayoutY());
					// spostiamo il missile eli oggetti
					riposizionaOggetto(oggettoSottopostoBound);
					rimuoviOggetto(10, missileSottopostoBound); // spostiamo il missile ad una posizione fuori dalla
																// portata degli oggetti
					numeriMunizioniEsaurite[i] = true;
					// conta++;
					/*
					 * System.out.println(posizioneXMissile+"+"+posizioneXMeteorie+"/2"+" = "+(
					 * posizioneXMissile+posizioneXMeteorie)/2);
					 * System.out.println(posizioneYMissile+"+"+HEIGTH_ESPLOSIONE+"/2"+" = "+(
					 * posizioneYMissile+HEIGTH_ESPLOSIONE/2));
					 * System.out.println("questa è la "+conta+" volta che compare l'esplosione");
					 */
					// posizioniamo l'esplosione
					ImageView esplosione = new ImageView(animazioneEsplosione);
					esplosione.setFitHeight(HEIGTH_ESPLOSIONE);
					esplosione.setFitWidth(WIDTH_ESPLOSIONE);
					interfaccia.getChildren().add(esplosione);
					esplosione.setLayoutX(((posizioneXMissile + posizioneXMeteorie) / 2) - WIDTH_ESPLOSIONE / 2);
					esplosione.setLayoutY(posizioneYMissile - HEIGTH_ESPLOSIONE / 2);
					rimuoviOggetto(500, esplosione);
				}
			}
		}
		// long end = System.nanoTime();
		// System.out.println("c'ho messo "+(end-start));
	}

	public void rimuoviOggetto(int traQuantoTempo, ImageView oggetto) {
		Timeline eliminaOggetto;
		eliminaOggetto = new Timeline(new KeyFrame(Duration.millis(traQuantoTempo), x -> eseguiRimozione(oggetto)));
		eliminaOggetto.setCycleCount(1);
		eliminaOggetto.play();
	}

	public void eseguiRimozione(ImageView oggetto) {
		interfaccia.getChildren().remove(oggetto);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
