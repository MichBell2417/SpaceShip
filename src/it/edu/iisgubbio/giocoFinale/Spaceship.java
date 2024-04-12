package it.edu.iisgubbio.giocoFinale;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.Event;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Spaceship extends Application {
	/* TODO: aggiungere vettore per gli sfondi di ciascun livello del gioco
	 * TODO: inserire la vita agli oggetti
	 * TODO: fare collisione navicella oggetti
	 * TODO: costruire e far funzionare l'interfaccia delle impostazioni
	 * TODO: creare interfaccia gioco
	 * TODO: aggiungere musica epica
	 * TODO: aggiungere suoni al gioco
	 */
	// SCHERMO
	Pane schermo = new Pane();
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
	int vettoreViteOggettiRimaste[]= new int[nOggetti]; //le vite in un indice corrispondo alle vite dell'oggetto che si trova nello stesso indice nel vettore vettoreOggetti
	int vettoreViteOggettiStandard[]= new int[nOggetti];
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
	int numeroMunizioni = 20;
	int numeroMunizioniAttuali= numeroMunizioni;
	int munizioniUtilizzate = 0;
	ImageView[] munizioni = new ImageView[numeroMunizioni];
	// spawn missile
	int precedente = 0;
	long tempoScorsoMissile;
	
	//PUNTEGGIO
	int punteggioAttuale=0;

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

	//INTERFACCIA GRAFICA
	int statoInterfaccia=0; //0=home, 1=gioco, 2=impostazioni
	//in comune:
	Button bHome= new Button("HOME");
	
	//home
	final int WIDTH_SFONDO_HOME=1000;
	final int HEIGTH_SFONDO_HOME=600;
	final int OFFSET_Y_OGGETTI_MENU=20; //valore che indica la posizione degli oggetti (titolo, pulsanti) del menu
	final int WIDTH_PULSANTI_HOME=200;
	final int HEIGTH_PULSANTI_HOME=40;
	final int WIDTH_RECTANGLE_HOME=275;
	final int HEIGTH_RECTANGLE_HOME=300;
	final int WIDTH_TITOLO_HOME=250;
	final int HEIGTH_TITOLO_HOME=50;

	Label eTitle=new Label("Space Ship");
	Button bStartGioco= new Button("Start");
	Button bResetGioco= new Button("Reset");
	Button bSettings= new Button("Settings");
	Image immagineSfondoHome=new Image(getClass().getResourceAsStream("videoSfondoHome.gif"));
	ImageView sfondoHomeFirstOpen=new ImageView(immagineSfondoHome);
	/*
	 * sostituito dallo sfondo
	Image immagineSfondoHomeTrasparente=new Image(getClass().getResourceAsStream("immagineSfondoHomeTrasparente.png"));
	ImageView sfondoHomeTrasperente=new ImageView(immagineSfondoHomeTrasparente);
	*/
	Rectangle sfondoHomeTrasperente= new Rectangle(WIDTH_SCHERMO, HEIGTH_SCHERMO);
	Region menu= new Region(); //come u rettangolo ma controllabile maggiormente da CSS
	
	//gioco
	final int WIDTH_MENU_INFORMAZIONI=500;
	final int HEIGTH_MENU_INFORMAZIONI=100;
	final int WIDTH_TESTO_INFORMAZIONI=100;
	final int HEIGTH_TESTO_INFORMAZIONI=30;
	final int WIDTH_PULSANTE_INFORMAZIONI=100;
	final int HEIGTH_PULSANTE_INFORMAZIONI=40;
	final int DIMENSIONI_IMMAGINE_CUORE_INFORMAZIONI=35;
	
	final int POSIZIONAMENTO_ASSE_Y_MENU=-20;
	final int POSIZIONAMENTO_ASSE_X_MENU=(WIDTH_SCHERMO-WIDTH_MENU_INFORMAZIONI)/2;
	final int OFFSET_ELEMENTI_MENU=WIDTH_MENU_INFORMAZIONI/4;
	final int POSIZIONE_X_SEPARATORE1=OFFSET_ELEMENTI_MENU+(WIDTH_SCHERMO-WIDTH_MENU_INFORMAZIONI)/2;
	final int POSIZIONE_X_SEPARATORE2=OFFSET_ELEMENTI_MENU*2+(WIDTH_SCHERMO-WIDTH_MENU_INFORMAZIONI)/2;
	final int POSIZIONE_X_SEPARATORE3=OFFSET_ELEMENTI_MENU*3+(WIDTH_SCHERMO-WIDTH_MENU_INFORMAZIONI)/2;
	
	boolean bianco=true;
	
	int numeroVite=3;
	int viteRimaste=numeroVite;
	
	Region informazioni= new Region();
	Label ePunti= new Label("PUNTI");
	Label eVite= new Label("VITE");
	Label eNumeroPunti= new Label(""+punteggioAttuale);
	Label eMunizioni= new Label("MUNIZIONI");
	Label eNumeroMunizioni= new Label(""+numeroMunizioniAttuali);
	//Button bHome= new Button("HOME"); fatto prima
	Line separaInformazioni1= new Line(POSIZIONE_X_SEPARATORE1,POSIZIONAMENTO_ASSE_Y_MENU, POSIZIONE_X_SEPARATORE1, 100+POSIZIONAMENTO_ASSE_Y_MENU);
	Line separaInformazioni2= new Line(POSIZIONE_X_SEPARATORE2,POSIZIONAMENTO_ASSE_Y_MENU, POSIZIONE_X_SEPARATORE2, 100+POSIZIONAMENTO_ASSE_Y_MENU);
	Line separaInformazioni3= new Line(POSIZIONE_X_SEPARATORE3,POSIZIONAMENTO_ASSE_Y_MENU, POSIZIONE_X_SEPARATORE3, 100+POSIZIONAMENTO_ASSE_Y_MENU);
	//immagini per vita
	Image imamgineCuoreVita=new Image(getClass().getResourceAsStream("immagineCuroePerVite.png"));
	ImageView vettoreCuori[]= new ImageView[numeroVite]; //3 vite
	
	//impostazioni
	
	
	//utili per cambiare le interfaccie nel modo corretto
	boolean resetGame=true;
	boolean firstOpen=true;
	
	
	public void start(Stage finestra) {
		//definiamo degli effetti
		DropShadow dropShadow = new DropShadow();
		dropShadow.setBlurType(BlurType.THREE_PASS_BOX);
		dropShadow.setRadius(15); //raggio di sfocatura
		dropShadow.setSpread(0.5); //densità della sfocatura 
		dropShadow.setColor(Color.GRAY);
		DropShadow dropShadow1 = new DropShadow();
		dropShadow1.setBlurType(BlurType.TWO_PASS_BOX);
		dropShadow1.setRadius(30); //raggio di sfocatura
		dropShadow1.setSpread(0.7); //densità della sfocatura 
		dropShadow1.setColor(Color.color(0.4,0,0.8));
	//--------------------------------------------------------------------------------------------------------------------------------
		//CONFIGURAZIONE SCHERMATA DI GIOCO
		// settaggi sfondo
		sfondo.setFitWidth(WIDTH_SFONDO);
		sfondo.setFitHeight(HEIGTH_SFONDO);
		sfondo.setLayoutX(0);
		sfondo.setLayoutY((HEIGTH_SCHERMO - HEIGTH_SFONDO) / 2);
		sfocatura.setFitWidth(WIDTH_SFOCATURA);
		sfocatura.setFitHeight(HEIGTH_SFOCATURA);
		sfocatura.setLayoutX(0);
		sfocatura.setLayoutY((HEIGTH_SCHERMO - HEIGTH_SFOCATURA) / 2);
		// settaggi oggetti
		int immagine, rotazione;
		for (int n = 0; n < nOggetti; n++) {
			immagine = (int) (Math.random() * vettoreImmagini.length);
			vettoreOggetti[n] = new ImageView(vettoreImmagini[immagine]);
			vettoreOggetti[n].setFitHeight(DIMENSION_OGGETTI);
			vettoreOggetti[n].setFitWidth(DIMENSION_OGGETTI);
			riposizionaOggetto(vettoreOggetti[n]);
			if (immagine != 0) {
				rotazione = (int) (Math.random() * 270);
				vettoreOggetti[n].setRotate(rotazione);
				vettoreViteOggettiRimaste[n]=2;
				vettoreViteOggettiStandard[n]=2;
			}else { //è un ufo
				vettoreViteOggettiRimaste[n]=3;
				vettoreViteOggettiStandard[n]=3;
			}
		}
		// settaggio navicella
		navicella.setFitWidth(WIDTH_NAVICELLA);
		navicella.setFitHeight(HEIGTH_NAVICELLA);
		navicella.setRotate(90);
		navicella.setLayoutX(posizioneNaviciella[0]);
		navicella.setLayoutY(posizioneNaviciella[1]);
		//settaggio menu informazioni
		informazioni.getStyleClass().add("menuInformazioniGioco");
		informazioni.setEffect(dropShadow1);
		ePunti.getStyleClass().add("testoMenuInformazioni");
		eNumeroPunti.getStyleClass().add("testoMenuInformazioni");
		eVite.getStyleClass().add("testoMenuInformazioni");
		eMunizioni.getStyleClass().add("testoMenuInformazioni");
		eNumeroMunizioni.getStyleClass().add("testoMenuInformazioni");
		bHome.getStyleClass().add("pulsanteMenuInformazioni");
		informazioni.setLayoutX(POSIZIONAMENTO_ASSE_X_MENU);
		informazioni.setLayoutY(POSIZIONAMENTO_ASSE_Y_MENU);
		ePunti.setLayoutX(POSIZIONAMENTO_ASSE_X_MENU+(POSIZIONE_X_SEPARATORE1-POSIZIONAMENTO_ASSE_X_MENU-WIDTH_TESTO_INFORMAZIONI)/2);
		ePunti.setLayoutY(5);
		eNumeroPunti.setLayoutX(POSIZIONAMENTO_ASSE_X_MENU+(POSIZIONE_X_SEPARATORE1-POSIZIONAMENTO_ASSE_X_MENU-WIDTH_TESTO_INFORMAZIONI)/2);
		eNumeroPunti.setLayoutY(35);
		eVite.setLayoutX(OFFSET_ELEMENTI_MENU+POSIZIONAMENTO_ASSE_X_MENU+(POSIZIONE_X_SEPARATORE1-POSIZIONAMENTO_ASSE_X_MENU-WIDTH_TESTO_INFORMAZIONI)/2);
		eVite.setLayoutY(5);
		for(int i=0; i<vettoreCuori.length; i++) {
			vettoreCuori[i]=new ImageView(imamgineCuoreVita);
			vettoreCuori[i].setLayoutX(35*i+OFFSET_ELEMENTI_MENU+POSIZIONAMENTO_ASSE_X_MENU+(POSIZIONE_X_SEPARATORE1-POSIZIONAMENTO_ASSE_X_MENU-WIDTH_TESTO_INFORMAZIONI)/2);
			vettoreCuori[i].setLayoutY(35);
			vettoreCuori[i].setFitHeight(DIMENSIONI_IMMAGINE_CUORE_INFORMAZIONI);
			vettoreCuori[i].setFitWidth(DIMENSIONI_IMMAGINE_CUORE_INFORMAZIONI);
		}
		eMunizioni.setLayoutX(OFFSET_ELEMENTI_MENU*2+POSIZIONAMENTO_ASSE_X_MENU+(POSIZIONE_X_SEPARATORE1-POSIZIONAMENTO_ASSE_X_MENU-WIDTH_TESTO_INFORMAZIONI)/2);
		eMunizioni.setLayoutY(5);
		eNumeroMunizioni.setLayoutX(OFFSET_ELEMENTI_MENU*2+POSIZIONAMENTO_ASSE_X_MENU+(POSIZIONE_X_SEPARATORE1-POSIZIONAMENTO_ASSE_X_MENU-WIDTH_TESTO_INFORMAZIONI)/2);
		eNumeroMunizioni.setLayoutY(35);
		bHome.setLayoutX(OFFSET_ELEMENTI_MENU*3+POSIZIONAMENTO_ASSE_X_MENU+(POSIZIONE_X_SEPARATORE1-POSIZIONAMENTO_ASSE_X_MENU-WIDTH_PULSANTE_INFORMAZIONI)/2);
		bHome.setLayoutY((HEIGTH_MENU_INFORMAZIONI+POSIZIONAMENTO_ASSE_Y_MENU-HEIGTH_PULSANTE_INFORMAZIONI)/2);
		bHome.setEffect(dropShadow);
		
//------------------------------------------------------------------------------------------------------------------------------------------------
		//CONFIGURAZIONE SCHERMATA HOME
		//settaggio sfondo
		sfondoHomeFirstOpen.setFitWidth(WIDTH_SFONDO_HOME);
		sfondoHomeFirstOpen.setFitHeight(HEIGTH_SFONDO_HOME);
		sfondoHomeFirstOpen.setLayoutY((HEIGTH_SCHERMO-HEIGTH_SFONDO_HOME)/2);
		sfondoHomeFirstOpen.setLayoutX((WIDTH_SCHERMO-WIDTH_SFONDO_HOME)/2);
		sfondoHomeTrasperente.getStyleClass().add("sfondoHomeTrasparente");
		sfondoHomeTrasperente.setLayoutY((HEIGTH_SCHERMO-HEIGTH_SFONDO_HOME)/2);
		sfondoHomeTrasperente.setLayoutX((WIDTH_SCHERMO-WIDTH_SFONDO_HOME)/2);
		//posizionamento pulsanti e menu
		// il numero aggiunto alla posizione X serve per riallineare i pulsanti con lo sfondo
		menu.setLayoutX((WIDTH_SCHERMO-WIDTH_RECTANGLE_HOME)/2);
		menu.setLayoutY((HEIGTH_SCHERMO-HEIGTH_RECTANGLE_HOME)/2);
		bStartGioco.setLayoutX((WIDTH_SCHERMO-WIDTH_PULSANTI_HOME)/2);
		bStartGioco.setLayoutY((HEIGTH_SCHERMO-HEIGTH_PULSANTI_HOME)/2-70+OFFSET_Y_OGGETTI_MENU);
		bSettings.setLayoutX((WIDTH_SCHERMO-WIDTH_PULSANTI_HOME)/2);
		bSettings.setLayoutY((HEIGTH_SCHERMO-HEIGTH_PULSANTI_HOME)/2+OFFSET_Y_OGGETTI_MENU);
		bResetGioco.setLayoutX((WIDTH_SCHERMO-WIDTH_PULSANTI_HOME)/2);
		bResetGioco.setLayoutY((HEIGTH_SCHERMO-HEIGTH_PULSANTI_HOME)/2+70+OFFSET_Y_OGGETTI_MENU);
		//stile pulsanti
		bStartGioco.getStyleClass().add("buttonHome");
		bResetGioco.getStyleClass().add("buttonHome");
		bSettings.getStyleClass().add("buttonHome");
		menu.getStyleClass().add("menuHome");
		bStartGioco.setEffect(dropShadow);
		bResetGioco.setEffect(dropShadow);
		bSettings.setEffect(dropShadow);
		menu.setEffect(dropShadow1);
		
		//settaggio e stile Titolo
		eTitle.setLayoutX((WIDTH_SCHERMO-WIDTH_TITOLO_HOME)/2);
		eTitle.setLayoutY((HEIGTH_SCHERMO-HEIGTH_TITOLO_HOME)/2-130+OFFSET_Y_OGGETTI_MENU);
		eTitle.getStyleClass().add("titleHome");
		eTitle.setEffect(dropShadow);
		
//------------------------------------------------------------------------------------------------------------------------------------------------
		//CONFIGURAZIONE SCHERMATA IMPOSTAZIONI
		
		
		
		Scene scena = new Scene(schermo, WIDTH_SCHERMO, HEIGTH_SCHERMO);
		scena.getStylesheets().add("it/edu/iisgubbio/giocoFinale/StyleSpaceShip.css");
		
		costruisciInterfaccia(0); //entriamo nella schermata home
		
		bStartGioco.setOnAction(e->gestisciInterfaccia(e));
		bResetGioco.setOnAction(e->gestisciInterfaccia(e));
		bSettings.setOnAction(e->gestisciInterfaccia(e));
		bHome.setOnAction(e->gestisciInterfaccia(e));
		
		scena.setOnKeyPressed(e -> pigiato(e));
		scena.setOnKeyReleased(e -> rilasciato(e));

		
		finestra.resizableProperty().setValue(false); // blocca il ridimensionamento della finestra
		finestra.setTitle("Spaceship");
		finestra.setScene(scena);
		finestra.show();

	}
	
	public void gestisciInterfaccia(Event pulsante) {
		int interfaccia=-1;
		String evento=pulsante.getSource().toString();
		if(evento.equals(bStartGioco.toString())) {
			interfaccia=1;
		}else if(evento.equals(bResetGioco.toString())) {
			resetGame=true;
			interfaccia=1;
		}else if(evento.equals(bSettings.toString())) {
			interfaccia=2; 
		}else if(evento.equals(bHome.toString())) {
			interfaccia=0; 
		}
		costruisciInterfaccia(interfaccia);
	}
	
	public void costruisciInterfaccia(int interfaccia) {
		schermo.getChildren().clear();
		switch(interfaccia) {
		case 0:
			//costruiamo la schermata home
			schermo.getStyleClass().add("pain");
			muoviNavicella.stop();
			controllaCollisione.stop();
			muoviSfondo.stop();
			muoviOggetti.stop();
			
			if(firstOpen) {
				schermo.getChildren().add(sfondoHomeFirstOpen);
				schermo.getChildren().add(sfondoHomeTrasperente);
				schermo.getChildren().add(menu);
				schermo.getChildren().add(eTitle);
				schermo.getChildren().add(bStartGioco);
				schermo.getChildren().add(bSettings);
				schermo.getChildren().add(bResetGioco);
				firstOpen=false;
			}else {
				schermo.getChildren().add(sfondoHomeFirstOpen);
				schermo.getChildren().add(sfondoHomeTrasperente);
				schermo.getChildren().add(menu);
				schermo.getChildren().add(eTitle);
				schermo.getChildren().add(bStartGioco);
				schermo.getChildren().add(bResetGioco);
				schermo.getChildren().add(bSettings);
			}
			break;
		case 1:
			//costruiamo la schermata per il gioco
			schermo.getChildren().add(sfondo);
			schermo.getChildren().add(navicella);
			if(resetGame) {
				//nel caso si sia in modalità di reset
				posizioneSfondoX = 0;
				munizioniUtilizzate=0;
				punteggioAttuale=0;
				eNumeroPunti.setText(""+punteggioAttuale);
				numeroMunizioniAttuali=numeroMunizioni;
				eNumeroMunizioni.setText(""+numeroMunizioniAttuali);
				// riempimento munizioni
				for (int nM = 0; nM < numeroMunizioni; nM++) {
					Image immagineMissile = new Image(getClass().getResourceAsStream("Missile.png"));
					numeriMunizioniEsaurite[nM]=false;
					munizioni[nM] = new ImageView(immagineMissile);
					munizioni[nM].setFitHeight(HEIGTH_MISSILE);
					munizioni[nM].setFitWidth(WIDTH_MISSILE);
					munizioni[nM].setLayoutX(WIDTH_SCHERMO);
					schermo.getChildren().add(munizioni[nM]);
				}
				for (int n = 0; n < nOggetti; n++) {
					riposizionaOggetto(vettoreOggetti[n]);
					vettoreViteOggettiRimaste[n]=vettoreViteOggettiStandard[n];
					schermo.getChildren().add(vettoreOggetti[n]);
				}
				resetGame=false;
			}else {
				//nel caso si sia in modalità normale
				for (int n = 0; n < nOggetti; n++) {
					schermo.getChildren().add(vettoreOggetti[n]);
				}
				for (int nM = munizioniUtilizzate; nM < numeroMunizioni; nM++) {
					schermo.getChildren().add(munizioni[nM]);
				}
			}
			schermo.getChildren().add(sfocatura);
			
			//costruiamo il menu delle informazioni
			schermo.getChildren().add(informazioni);
			schermo.getChildren().add(separaInformazioni1);
			schermo.getChildren().add(separaInformazioni2);
			schermo.getChildren().add(separaInformazioni3);
			schermo.getChildren().add(ePunti);
			schermo.getChildren().add(eNumeroPunti);
			schermo.getChildren().add(eVite);
			for(int i=0; i<vettoreCuori.length; i++) {
				schermo.getChildren().add(vettoreCuori[i]);
			}
			schermo.getChildren().add(eMunizioni);
			schermo.getChildren().add(eNumeroMunizioni);
			schermo.getChildren().add(bHome);
			
			// movimento navicella
			muoviNavicella.setCycleCount(Animation.INDEFINITE);
			muoviNavicella.play();
			// controlla collisioni
			controllaCollisione.setCycleCount(Animation.INDEFINITE);
			controllaCollisione.play();
			// movimento sfondo
			muoviSfondo.setCycleCount(Animation.INDEFINITE);
			muoviSfondo.play();
			// movimento oggetti
			muoviOggetti.setCycleCount(Animation.INDEFINITE);
			muoviOggetti.play();
			break;
			
		case 2:
			//costruiamo la schermata delle impostazioni
			schermo.getChildren().add(bHome);
			break;
		}
	}
	
	public void aggiornaPosizioneSfondo() {
		double posizioneFinaleSfondo = posizioneSfondoX + WIDTH_SFONDO;
		if (posizioneFinaleSfondo < WIDTH_SCHERMO + 20) {
			muoviSfondo.stop();
		}
		posizioneSfondoX -= valoreSpostamentoSfondo;
		sfondo.setLayoutX(posizioneSfondoX);
		sfocatura.setLayoutX(posizioneFinaleSfondo - WIDTH_SFOCATURA / 2);
		
		//approfittamo dello spostamento dello sfondo per far anche cambiare colore al numero di munizioni quando è zero
		if(System.currentTimeMillis() - tempoScorsoMissile >= 500 && numeroMunizioniAttuali==0) {
			if(bianco) {
				eNumeroMunizioni.setTextFill(Color.WHITE);
				bianco=false;
			}else {
				eNumeroMunizioni.setTextFill(Color.RED);
				bianco=true;
			}
			tempoScorsoMissile = System.currentTimeMillis();
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
		if (System.currentTimeMillis() - tempoScorsoMissile >= 100 && numeroMunizioniAttuali!=0) {
			if (numeroMunizioniAttuali<=10 && numeroMunizioniAttuali>5) {
				eNumeroMunizioni.setTextFill(Color.ORANGE);
			}else if(numeroMunizioniAttuali<=5) {
				eNumeroMunizioni.setTextFill(Color.RED);
			}
			munizioni[munizioniUtilizzate].setLayoutY(navicella.getLayoutY() + (HEIGTH_NAVICELLA / 2 - HEIGTH_MISSILE / 2));
			munizioni[munizioniUtilizzate].setLayoutX(navicella.getLayoutX() + WIDTH_NAVICELLA - WIDTH_MISSILE);
			munizioniUtilizzate++;
			numeroMunizioniAttuali=numeroMunizioni-munizioniUtilizzate;
			precedente = munizioniUtilizzate - 1;
			tempoScorsoMissile = System.currentTimeMillis();
			eNumeroMunizioni.setText(""+numeroMunizioniAttuali);
			
		}
		//System.out.println("sparato");
	}

	public void pigiato(KeyEvent pulsante) {
		//System.out.println(pulsante.getText());
		switch (pulsante.getText().toLowerCase()) {
		case "p":
			spara();
			break;
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
		if (spostaINDIETRO && navicella.getLayoutX() >= 0) {
			posizioneNaviciella[0] -= valoreSpostamentoNavicella;
			navicella.setLayoutX(posizioneNaviciella[0]);
		}
		if (spostaAVANTI && navicella.getLayoutX() <= WIDTH_SCHERMO - HEIGTH_NAVICELLA) {
			posizioneNaviciella[0] += valoreSpostamentoNavicella;
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
					posizioneYMissile = (int) (missileSottopostoBound.getLayoutY());
					posizioneXMeteorie = (int) (oggettoSottopostoBound.getLayoutX());
					// spostiamo il missile e gli oggetti
					vettoreViteOggettiRimaste[numeroOggettoBound]--; //scaliamo la vita all'oggetto colpito
					if(vettoreViteOggettiRimaste[numeroOggettoBound]==0) {
						riposizionaOggetto(oggettoSottopostoBound);
						vettoreViteOggettiRimaste[numeroOggettoBound]=vettoreViteOggettiStandard[numeroOggettoBound];
						punteggioAttuale+=vettoreViteOggettiStandard[numeroOggettoBound]*10;
						eNumeroPunti.setText(""+punteggioAttuale);
						// posizioniamo l'esplosione
						ImageView esplosione = new ImageView(animazioneEsplosione);
						esplosione.setFitHeight(HEIGTH_ESPLOSIONE);
						esplosione.setFitWidth(WIDTH_ESPLOSIONE);
						schermo.getChildren().add(esplosione);
						esplosione.setLayoutX(((posizioneXMissile + posizioneXMeteorie) / 2) - WIDTH_ESPLOSIONE / 2);
						esplosione.setLayoutY(posizioneYMissile - HEIGTH_ESPLOSIONE / 2);
						rimuoviOggetto(500, esplosione);
					}
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
		schermo.getChildren().remove(oggetto);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
