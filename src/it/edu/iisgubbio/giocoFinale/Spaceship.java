package it.edu.iisgubbio.giocoFinale;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.Event;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Spaceship extends Application {

	// SCHERMO
	Pane schermo = new Pane();
	final int WIDTH_SCHERMO = 1000;
	final int HEIGTH_SCHERMO = 600;
	GridPane grigliaImpostazioni= new GridPane();

	// SFONDO
	final int WIDTH_SFONDO = 5000;
	final int HEIGTH_SFONDO = 800;
	Image immagineSfondo = new Image(getClass().getResourceAsStream("Sfondo.png"));
	ImageView sfondo = new ImageView(immagineSfondo);
	Image immagineSfondo2 = new Image(getClass().getResourceAsStream("Sfondo2.png"));
	ImageView sfondo2 = new ImageView(immagineSfondo2);
	final int WIDTH_SFOCATURA = 2000;
	final int HEIGTH_SFOCATURA = 1600;
	Image immagineSfocatura = new Image(getClass().getResourceAsStream("Sfocatura.png"));
	ImageView sfocatura = new ImageView(immagineSfocatura);
	// spostamento sfondo
	double tempoDiSpostamentoTOT = 25; //durata del gioco standard 25s
	double posizioneSfondoX = 0;
	double posizioneSfondo2X = WIDTH_SFONDO;
	double valoreSpostamentoSfondo = (WIDTH_SFONDO-1000)/(tempoDiSpostamentoTOT*1000)*25;
	Timeline muoviSfondo = new Timeline(new KeyFrame(Duration.millis(35), x -> aggiornaPosizioneSfondo()));

	// OGGETTI
	final int DIMENSION_OGGETTI = 100;
	int nOggetti = 7;
	Image immagineUfo = new Image(getClass().getResourceAsStream("Ufo.png"));
	Image immagineMeteoriteBlu = new Image(getClass().getResourceAsStream("MeteoriteBlu.png"));
	Image immagineMeteoriteViola = new Image(getClass().getResourceAsStream("MeteoriteViola.png"));
	Image vettoreImmagini[] = { immagineMeteoriteBlu, immagineMeteoriteViola, immagineUfo }; 
	ImageView vettoreOggetti[] = new ImageView[nOggetti];
	int viteRimasteOggetti[]= new int[nOggetti]; //le vite in un indice corrispondo alle vite dell'oggetto che si trova nello stesso indice nel vettore vettoreOggetti
	int oggettoNellaPosizione[]= new int[nOggetti]; // che immagine (del vettore immagine) si trova in quella posizione
	int viteMeteorite=2;
	int viteUFO=3;
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
	Timeline spostaAllaFineNavicella = new Timeline(new KeyFrame(Duration.millis(25), x -> metodoSpostaAllaFineNavicella()));
	//animazioni navicella
	boolean lampeggiamento=false;
	Timeline lampeggiaNavicella = new Timeline(new KeyFrame(Duration.millis(250), x -> metodoLampeggiaNavicella()));

	// MUNIZIONI
	final int WIDTH_MISSILE = 60;
	final int HEIGTH_MISSILE = 30;
	int numeroMunizioni = 100;
	int numeroMunizioniAttuali= numeroMunizioni;
	int munizioniUtilizzate = 0;
	ImageView[] munizioni = new ImageView[numeroMunizioni];
	// spawn missile
	long tempoScorsoMissile;

	//PUNTEGGIO
	//viene incrementato nel metodo "metodoControllaCollisioni"
	int punteggioAttuale=0;

	// colisioni oggetto misile
	int numeroOggettoBound = 0;
	int numeroMissileBound = 0;
	int conta = 0;
	boolean[] statoMunizione = new boolean[numeroMunizioni]; // vettore che contiene lo stato di ogni missile
	Bounds boundOggetti;
	Bounds boundMissile;

	// controlla intersezione
	Timeline controllaCollisione = new Timeline(new KeyFrame(Duration.millis(2), x -> metodoControllaCollisione()));
	final int WIDTH_ESPLOSIONE = 200;
	final int HEIGTH_ESPLOSIONE = 200;
	Image animazioneEsplosione = new Image(getClass().getResourceAsStream("animazione-esplosione1.gif"));	

	//INTERFACCIA GRAFICA
	int statoInterfaccia=0; //0=home, 1=gioco, 2=impostazioni, 3=schermata finale

	//home
	final int WIDTH_SFONDO_HOME=1000;
	final int HEIGTH_SFONDO_HOME=600;
	final int OFFSET_Y_OGGETTI_MENU=20;
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
	Image immagineSfondoHome=new Image(getClass().getResourceAsStream("VideoSfondoHome.gif"));

	ImageView sfondoHomeFirstOpen=new ImageView(immagineSfondoHome);
	Rectangle sfondoHomeTrasperente= new Rectangle(WIDTH_SCHERMO, HEIGTH_SCHERMO);
	Region menu= new Region();

	//GIOCO
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
	boolean bianco=true; //per alternare il colore bianco o rosso delle munizioni
	boolean giocoIniziato=false;

	//oggetti informazioni
	Region informazioni= new Region();
	Label ePunti= new Label("PUNTI");
	Label eVite= new Label("VITE");
	Label eNumeroPunti= new Label(""+punteggioAttuale);
	Label eMunizioni= new Label("MUNIZIONI");
	Label eNumeroMunizioni= new Label(""+numeroMunizioniAttuali);
	long tempoPassato=0;
	Button bHomeGioco= new Button("HOME");
	Line separaInformazioni1= new Line(POSIZIONE_X_SEPARATORE1,POSIZIONAMENTO_ASSE_Y_MENU, POSIZIONE_X_SEPARATORE1, 100+POSIZIONAMENTO_ASSE_Y_MENU);
	Line separaInformazioni2= new Line(POSIZIONE_X_SEPARATORE2,POSIZIONAMENTO_ASSE_Y_MENU, POSIZIONE_X_SEPARATORE2, 100+POSIZIONAMENTO_ASSE_Y_MENU);
	Line separaInformazioni3= new Line(POSIZIONE_X_SEPARATORE3,POSIZIONAMENTO_ASSE_Y_MENU, POSIZIONE_X_SEPARATORE3, 100+POSIZIONAMENTO_ASSE_Y_MENU);
	int numeroVite=3; //devono essere 3
	int numeroViteRimaste=numeroVite;
	Image imamgineCuoreVita=new Image(getClass().getResourceAsStream("ImmagineCuroePerVite.png"));
	ImageView vettoreCuori[]= new ImageView[numeroVite];

	//IMPOSTAZIONI 
	boolean suonoSottofondoAttivo=false;
	//interfaccia
	final int DIMENSIONI_X_GRIDPANE=500;
	final int DIMENSIONI_COLONNA_GRIDPANE=DIMENSIONI_X_GRIDPANE/3;
	final int DIMENSIONI_Y_GRIDPANE=400;
	final int DIMENSIONI_X_SFONDO_IMPOSTAZIONI= 600;
	final int DIMENSIONI_Y_SFONDO_IMPOSTAZIONI= 520;

	int numeroLivelli=3;
	boolean modalitaGiocoIllimitato=false; //modalità: true=illimitato false=limitato
	long tempoStart;
	long tempoSovpravvissuto=0;
	Button bHomeSettings= new Button("HOME");
	Button bSave= new Button("Save");
	Region impostazioni= new Region(); 

	Rectangle dimensioneGrigliaX= new Rectangle(DIMENSIONI_X_GRIDPANE,0);
	Rectangle dimensioneGrigliaY= new Rectangle(0,DIMENSIONI_Y_GRIDPANE);
	Rectangle dimensioneGrigliaColonna1= new Rectangle(DIMENSIONI_COLONNA_GRIDPANE,0);
	Rectangle dimensioneGrigliaColonna2= new Rectangle(DIMENSIONI_COLONNA_GRIDPANE,0);
	Rectangle dimensioneGrigliaColonna3= new Rectangle(DIMENSIONI_COLONNA_GRIDPANE,0);
	Label eImpostazioni=new Label("IMPOSTAZIONI");
	Label eModalitaGioco=new Label("Modalità di gioco:");
	ToggleGroup modalitaDiGioco = new ToggleGroup();
	RadioButton rbSenzaLimiti = new RadioButton("illimitato");
	RadioButton rbConLimiti = new RadioButton("limitato");
	Label eNumeroMuinizioniImpostazioni = new Label("numero munizioni: ");
	TextField cNumeroMunizioni= new TextField("100");
	Label eDurataGioco = new Label("durata (Secondi): ");
	TextField durataGioco= new TextField("25");
	Label eSuono= new Label("Suono: ");
	Label eVolumeSuono= new Label("volume: ");
	Slider volume= new Slider(1, 10 , 5);
	CheckBox ckSottofondo= new CheckBox("Sottofondo");
	CheckBox ckMissile= new CheckBox("Sparatoria");
	CheckBox ckEspolosione= new CheckBox("Esplosione");
	CheckBox ckMunizioni= new CheckBox("Munizioni");
	Label eViteOggetto= new Label("vite Oggetti: ");
	Label eMeteorite= new Label("Meteorite: ");
	Label eUfo= new Label("UFO: ");
	TextField cViteMeteorite= new TextField(""+viteMeteorite);
	TextField cViteUfo= new TextField(""+viteUFO);

	//griglia finale
	final int DIMENSIONI_X_SFONDO_FINALE= 350;
	final int DIMENSIONI_Y_SFONDO_FINALE= 300;
	final int WIDTH_GRIGLIA_FINALE= 150*2;
	final int HEIGTH_GRIGLIA_FINALE= 40*5+10;
	GridPane grigliaGiocoFinale = new GridPane();
	Label eTitoloFinale=new Label("YOU WIN!");
	Label ePuntiFinale= new Label("punti totali:");
	Label ePunteggioTOT= new Label("----");
	Label eProporzioneFinale= new Label("---------------------------");
	Label eVitaFinale= new Label("vita:");
	Label ePunteggioDaVita=new Label("-");
	Label eBonusFinale= new Label("bonus:");
	Label ePunteggioDaBonus=new Label("-");
	Label ePunteggioFinale= new Label("punteggio:");
	Label ePunteggioDaPunteggio=new Label("-");
	Region sfondoFinale=new Region();
	Button bHomeFinale= new Button("HOME");

	//utili per cambiare le interfaccie nel modo corretto
	boolean resetGame=true;
	boolean firstOpen=true;
	boolean fineGioco=false;
	boolean avvenutaCollisioneOggNav=false;
	Timeline schermataPunteggio= new Timeline(new KeyFrame(
			Duration.millis(1000), 
			x -> costruisciInterfaccia(3)));

	//MUSICA DI SOTTOFONDO
	AudioClip musicaSottofondo= new AudioClip(getClass().getResource("musicaEpicaSottofondo.mp3").toExternalForm());
	AudioClip suonoEsplosione= new AudioClip(getClass().getResource("Esplosione.mp3").toExternalForm());
	AudioClip suonoMunizioniFinite= new AudioClip(getClass().getResource("MunizioniFinite.mp3").toExternalForm());
	AudioClip suonoSparoMissile= new AudioClip(getClass().getResource("SparoMissile.mp3").toExternalForm());
	AudioClip suonoGameOver= new AudioClip(getClass().getResource("GameOver.mp3").toExternalForm());
	AudioClip suonoVittoria= new AudioClip(getClass().getResource("Victory.mp3").toExternalForm());

	//ELLISSI E CERCHI PER COLLISIONI
	final int RADIUS_WIDTH_ELLISSE_VERT = 20;
	final int RADIUS_HEIGTH_ELLISSE_VERT = WIDTH_NAVICELLA/2;
	final int POS_X_ELLISSE_VERTICALE = posizioneNaviciella[0]+WIDTH_NAVICELLA/2-10;
	final int POS_Y_ELLISSE_VERTICALE = posizioneNaviciella[1]+HEIGTH_NAVICELLA/2;
	Ellipse ellisseVert = new Ellipse (POS_X_ELLISSE_VERTICALE,POS_Y_ELLISSE_VERTICALE,RADIUS_WIDTH_ELLISSE_VERT,RADIUS_HEIGTH_ELLISSE_VERT);

	final int RADIUS_WIDTH_ELLISSE_ORIZ = HEIGTH_NAVICELLA/2;
	final int RADIUS_HEIGTH_ELLISSE_ORIZ = 20;
	final int POS_X_ELLISSE_ORIZZONTALE = posizioneNaviciella[0]+WIDTH_NAVICELLA/2-10;
	final int POS_Y_ELLISSE_ORIZZONTALE = posizioneNaviciella[1]+HEIGTH_NAVICELLA/2;
	Ellipse ellisseOriz = new Ellipse (POS_X_ELLISSE_ORIZZONTALE,POS_Y_ELLISSE_ORIZZONTALE,RADIUS_WIDTH_ELLISSE_ORIZ,RADIUS_HEIGTH_ELLISSE_ORIZ);

	final int WIDTH_ELLISSE_UFO = 95;
	final int HEIGTH_ELLISSE_UFO = 30;

	final int WIDTH_ELLISSE_ASTEROIDI = 95;

	Timeline collisioniOggettiNavicella= new Timeline(new KeyFrame(
			Duration.millis(10), 
			x -> metodoCollisioniOggettiNavicella()));

	Ellipse vettoreEllissiCollisione[]=new Ellipse[nOggetti]; //vettore contenente i cerchi e le ellissi degli oggetti

	//effetti universali
	DropShadow dropShadow1 = new DropShadow();
	DropShadow effettoGameOver = new DropShadow();
	DropShadow dropShadow = new DropShadow();

	public void start(Stage finestra) {
		//definiamo degli effetti
		dropShadow.setBlurType(BlurType.THREE_PASS_BOX);
		dropShadow.setRadius(15); //raggio di sfocatura
		dropShadow.setSpread(0.5); //densità della sfocatura 
		dropShadow.setColor(Color.GRAY);
		dropShadow1.setBlurType(BlurType.TWO_PASS_BOX);
		dropShadow1.setRadius(30); //raggio di sfocatura
		dropShadow1.setSpread(0.7); //densità della sfocatura 
		dropShadow1.setColor(Color.color(0.4,0,0.8));
		dropShadow1.setBlurType(BlurType.GAUSSIAN);
		effettoGameOver.setRadius(30); //raggio di sfocatura
		effettoGameOver.setSpread(0.7); //densità della sfocatura 
		effettoGameOver.setColor(Color.INDIANRED);
		//--------------------------------------------------------------------------------------------------------------------------------
		//CONFIGURAZIONE GENERALE SCHERMATA DI GIOCO
		// settaggi sfondo
		sfondo.setFitWidth(WIDTH_SFONDO);
		sfondo.setFitHeight(HEIGTH_SFONDO);
		sfondo.setLayoutX(posizioneSfondoX);
		sfondo.setLayoutY((HEIGTH_SCHERMO - HEIGTH_SFONDO) / 2);
		sfondo2.setFitWidth(WIDTH_SFONDO);
		sfondo2.setFitHeight(HEIGTH_SFONDO);
		sfondo2.setLayoutX(posizioneSfondo2X);
		sfondo2.setLayoutY((HEIGTH_SCHERMO - HEIGTH_SFONDO) / 2);
		sfocatura.setFitWidth(WIDTH_SFOCATURA);
		sfocatura.setFitHeight(HEIGTH_SFOCATURA);
		sfocatura.setLayoutX(0);
		sfocatura.setLayoutY((HEIGTH_SCHERMO - HEIGTH_SFOCATURA) / 2);
		// settaggi oggetti
		int immagine, rotazione;
		for(int n=0; n<nOggetti; n++) {
			immagine=(int)(Math.random()*vettoreImmagini.length);
			vettoreOggetti[n]=new ImageView(vettoreImmagini[immagine]);
			vettoreOggetti[n].setFitHeight(DIMENSION_OGGETTI);
			vettoreOggetti[n].setFitWidth(DIMENSION_OGGETTI);
			oggettoNellaPosizione[n]=immagine;
			if(immagine!=2) {
				Ellipse EllisseAsteroidiColl = new Ellipse(WIDTH_ELLISSE_ASTEROIDI/2,WIDTH_ELLISSE_ASTEROIDI/2);
				vettoreEllissiCollisione[n]=EllisseAsteroidiColl;
				rotazione=(int)(Math.random()*270);
				vettoreOggetti[n].setRotate(rotazione);
			}else {
				Ellipse EllisseUfoColl = new Ellipse(WIDTH_ELLISSE_UFO/2, HEIGTH_ELLISSE_UFO/2);
				vettoreEllissiCollisione[n]=EllisseUfoColl;
			}
			vettoreEllissiCollisione[n].setVisible(false);
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
		ePunti.getStyleClass().add("testoMenuInformazioni");
		eNumeroPunti.getStyleClass().add("testoMenuInformazioni");
		eNumeroPunti.getStyleClass().add("testoMenuInformazioni");
		eVite.getStyleClass().add("testoMenuInformazioni");
		eMunizioni.getStyleClass().add("testoMenuInformazioni");
		eNumeroMunizioni.getStyleClass().add("testoMenuInformazioni");
		bHomeGioco.getStyleClass().add("pulsanteMenuInformazioni");
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
		bHomeGioco.setLayoutX(OFFSET_ELEMENTI_MENU*3+POSIZIONAMENTO_ASSE_X_MENU+(POSIZIONE_X_SEPARATORE1-POSIZIONAMENTO_ASSE_X_MENU-WIDTH_PULSANTE_INFORMAZIONI)/2);
		bHomeGioco.setLayoutY((HEIGTH_MENU_INFORMAZIONI+POSIZIONAMENTO_ASSE_Y_MENU-HEIGTH_PULSANTE_INFORMAZIONI)/2);
		bHomeGioco.setEffect(dropShadow);
		//-------------------------------------------------------------------------------------------------------------------------------
		//settaggi griglia a fine gioco
		grigliaGiocoFinale.add(ePuntiFinale, 0, 0);
		grigliaGiocoFinale.add(ePunteggioTOT, 1, 0);
		grigliaGiocoFinale.add(eProporzioneFinale, 0, 1, 2, 1);
		grigliaGiocoFinale.add(ePunteggioFinale, 0, 2);
		grigliaGiocoFinale.add(ePunteggioDaPunteggio, 1, 2);
		grigliaGiocoFinale.add(eVitaFinale, 0, 3);
		grigliaGiocoFinale.add(ePunteggioDaVita, 1, 3);
		grigliaGiocoFinale.add(eBonusFinale, 0, 4);
		grigliaGiocoFinale.add(ePunteggioDaBonus, 1, 4);
		grigliaGiocoFinale.add(bHomeFinale, 0, 5, 2, 1);
		ePuntiFinale.getStyleClass().add("testoGiocoFinale");
		ePunteggioTOT.getStyleClass().add("testoGiocoFinale");
		eProporzioneFinale.getStyleClass().add("testoGiocoFinaleSottotitolo");
		ePunteggioFinale.getStyleClass().add("testoGiocoFinale");
		ePunteggioDaPunteggio.getStyleClass().add("testoGiocoFinale");
		eVitaFinale.getStyleClass().add("testoGiocoFinale");
		ePunteggioDaVita.getStyleClass().add("testoGiocoFinale");
		eBonusFinale.getStyleClass().add("testoGiocoFinale");
		ePunteggioDaBonus.getStyleClass().add("testoGiocoFinale");
		bHomeFinale.getStyleClass().add("pulsanteImpostazioni");

		bHomeFinale.setMaxWidth(WIDTH_GRIGLIA_FINALE);
		bHomeFinale.setEffect(dropShadow);

		eProporzioneFinale.setEffect(dropShadow);
		ePuntiFinale.setEffect(dropShadow);

		grigliaGiocoFinale.setLayoutX((WIDTH_SCHERMO-WIDTH_GRIGLIA_FINALE)/2);
		grigliaGiocoFinale.setLayoutY((HEIGTH_SCHERMO-HEIGTH_GRIGLIA_FINALE)/2-20);

		grigliaGiocoFinale.setVgap(10);

		//settaggio Titolo
		eTitoloFinale.getStyleClass().add("titleFinale");
		eTitoloFinale.setLayoutX((WIDTH_SCHERMO-WIDTH_TITOLO_HOME)/2);
		eTitoloFinale.setLayoutY(50);
		eTitoloFinale.setEffect(dropShadow1);
		//sfondo
		sfondoFinale.getStyleClass().add("sfondoFinale");
		sfondoFinale.setLayoutX((WIDTH_SCHERMO-DIMENSIONI_X_SFONDO_FINALE)/2);
		sfondoFinale.setLayoutY((HEIGTH_SCHERMO-DIMENSIONI_Y_SFONDO_FINALE)/2);
		sfondoFinale.setEffect(dropShadow1);
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
		rbConLimiti.setSelected(true);
		volume.setShowTickMarks(true);
		volume.setShowTickLabels(true);
		volume.setMajorTickUnit(1);
		volume.setMinorTickCount(0);
		volume.setSnapToTicks(true);
		ckSottofondo.setSelected(suonoSottofondoAttivo);
		ckMissile.setSelected(true);
		ckEspolosione.setSelected(true);
		ckMunizioni.setSelected(true);
		//diamo le classi e gli effetti agli oggetti
		impostazioni.getStyleClass().add("sfondoImpostazioni");
		bHomeSettings.getStyleClass().add("pulsanteImpostazioni");
		bSave.getStyleClass().add("pulsanteImpostazioni");
		eImpostazioni.getStyleClass().add("titleImpostazioni");
		eModalitaGioco.getStyleClass().add("testoImpostazioni");
		eNumeroMuinizioniImpostazioni.getStyleClass().add("testoImpostazioni");
		eNumeroMuinizioniImpostazioni.getStyleClass().add("allinemantoCentratoImpostazioni");
		eDurataGioco.getStyleClass().add("testoImpostazioni");
		eDurataGioco.getStyleClass().add("allinemantoCentratoImpostazioni");
		eSuono.getStyleClass().add("testoImpostazioni");
		eVolumeSuono.getStyleClass().add("testoImpostazioni");
		eVolumeSuono.getStyleClass().add("allinemantoCentratoImpostazioni");
		eViteOggetto.getStyleClass().add("testoImpostazioni");
		eMeteorite.getStyleClass().add("testoImpostazioni");
		eMeteorite.getStyleClass().add("allinemantoCentratoImpostazioni");
		eUfo.getStyleClass().add("testoImpostazioni");
		eUfo.getStyleClass().add("allinemantoCentratoImpostazioni");
		ckSottofondo.getStyleClass().add("testoImpostazioniDettaglio");
		ckMissile.getStyleClass().add("testoImpostazioniDettaglio");
		ckEspolosione.getStyleClass().add("testoImpostazioniDettaglio");
		ckMunizioni.getStyleClass().add("testoImpostazioniDettaglio");
		rbSenzaLimiti.getStyleClass().add("testoImpostazioniDettaglio");
		rbConLimiti.getStyleClass().add("testoImpostazioniDettaglio");
		bHomeSettings.setEffect(dropShadow1);
		bSave.setEffect(dropShadow);
		impostazioni.setEffect(dropShadow1);
		eImpostazioni.setEffect(dropShadow1);
		eModalitaGioco.setEffect(dropShadow);
		eNumeroMuinizioniImpostazioni.setEffect(dropShadow);
		eDurataGioco.setEffect(dropShadow);
		eSuono.setEffect(dropShadow);
		eVolumeSuono.setEffect(dropShadow);
		eViteOggetto.setEffect(dropShadow);
		eMeteorite.setEffect(dropShadow);
		eUfo.setEffect(dropShadow);
		ckSottofondo.setEffect(dropShadow);
		ckMissile.setEffect(dropShadow);
		ckEspolosione.setEffect(dropShadow);
		ckMunizioni.setEffect(dropShadow);
		rbSenzaLimiti.setEffect(dropShadow);
		rbConLimiti.setEffect(dropShadow);
		//Aggiungiamo gli elementi alla griglia
		grigliaImpostazioni.add(eImpostazioni, 0, 0, 2, 1);//(0,0)
		grigliaImpostazioni.add(bHomeSettings, 2, 0);//(0,0)
		grigliaImpostazioni.add(eModalitaGioco, 0, 1, 2, 1);//(0,1)
		grigliaImpostazioni.add(rbSenzaLimiti, 0, 2);
		grigliaImpostazioni.add(eNumeroMuinizioniImpostazioni, 1, 2);
		grigliaImpostazioni.add(cNumeroMunizioni, 2, 2);
		grigliaImpostazioni.add(rbConLimiti, 0, 3);
		grigliaImpostazioni.add(eDurataGioco, 1, 3);
		grigliaImpostazioni.add(durataGioco, 2, 3);
		grigliaImpostazioni.add(eSuono, 0, 4, 3, 1);
		grigliaImpostazioni.add(eVolumeSuono, 0, 5);
		grigliaImpostazioni.add(volume, 1, 5, 2, 1);
		grigliaImpostazioni.add(ckSottofondo, 0, 6);
		grigliaImpostazioni.add(ckMissile, 1, 6);
		grigliaImpostazioni.add(ckEspolosione, 2, 6);
		grigliaImpostazioni.add(ckMunizioni, 1, 7);
		grigliaImpostazioni.add(eViteOggetto, 0, 8, 3, 1);
		grigliaImpostazioni.add(eMeteorite, 0, 9);
		grigliaImpostazioni.add(cViteMeteorite, 1, 9,2,1);
		grigliaImpostazioni.add(eUfo, 0, 10);
		grigliaImpostazioni.add(cViteUfo, 1, 10,2,1);
		grigliaImpostazioni.add(bSave, 0, 11,4,1);
		//do delle dimensioni al GridPane
		grigliaImpostazioni.setPadding(new Insets(10,10,10,10));
		grigliaImpostazioni.setHgap(10);
		grigliaImpostazioni.setVgap(10);
		grigliaImpostazioni.setPrefWidth(1000);
		grigliaImpostazioni.getStyleClass().add("grigliaImpstazioni");
		grigliaImpostazioni.setLayoutX((WIDTH_SCHERMO-DIMENSIONI_X_GRIDPANE-40)/2);
		grigliaImpostazioni.setLayoutY((HEIGTH_SCHERMO-DIMENSIONI_Y_GRIDPANE-90)/2);
		impostazioni.setLayoutX((WIDTH_SCHERMO-DIMENSIONI_X_SFONDO_IMPOSTAZIONI)/2);
		impostazioni.setLayoutY((HEIGTH_SCHERMO-DIMENSIONI_Y_SFONDO_IMPOSTAZIONI)/2);
		bSave.setMaxWidth(DIMENSIONI_X_GRIDPANE);
		//aggiungiamo i due radioButton alla lista delle modalità del gioco
		rbConLimiti.setToggleGroup(modalitaDiGioco);
		rbSenzaLimiti.setToggleGroup(modalitaDiGioco);

		Scene scena = new Scene(schermo, WIDTH_SCHERMO, HEIGTH_SCHERMO);
		scena.getStylesheets().add("it/edu/iisgubbio/giocoFinale/StyleSpaceShip.css");
		//settiamo le variabili per il gioco
		controllaImpostazioni();
		costruisciInterfaccia(0); //costruiamo la schermata home
		bStartGioco.setOnAction(e->gestisciInterfaccia(e));
		bResetGioco.setOnAction(e->gestisciInterfaccia(e));
		bSettings.setOnAction(e->gestisciInterfaccia(e));
		bHomeGioco.setOnAction(e->gestisciInterfaccia(e));
		bHomeSettings.setOnAction(e->gestisciInterfaccia(e));
		bSave.setOnAction(e->controllaImpostazioni());
		bHomeFinale.setOnAction(e->gestisciInterfaccia(e));
		scena.setOnKeyPressed(e -> pigiato(e));
		scena.setOnKeyReleased(e -> rilasciato(e));
		finestra.resizableProperty().setValue(false); // blocca il ridimensionamento della finestra
		finestra.setTitle("Spaceship");
		finestra.setScene(scena);
		finestra.show();
	}

	public void metodoLampeggiaNavicella() {
		if(navicella.getOpacity()==0.5) {
			navicella.setOpacity(1);
		}else {
			navicella.setOpacity(0.5);
		}
	}

	/**
	 * metodo che all'inizio del gioco o quando si pigia save, aggiorna i valori utilizzati per i vari casi
	 */
	public void controllaImpostazioni() {
		//VITE OGGETTI
		int nuovaVitaMeteorite= Integer.parseInt(cViteMeteorite.getText());
		int nuovaVitaUFO= Integer.parseInt(cViteUfo.getText());
		for (int n = 0; n < nOggetti; n++) {
			if(oggettoNellaPosizione[n]!=2) {
				viteRimasteOggetti[n]=nuovaVitaMeteorite;
			}else{
				viteRimasteOggetti[n]=nuovaVitaUFO;
			}
		}
		viteMeteorite=nuovaVitaMeteorite;
		viteUFO=nuovaVitaUFO;
		//SUONO
		boolean volumeCambiato=false;
		double volumeSuoni= volume.getValue()/10;
		if(volumeSuoni!=musicaSottofondo.getVolume()) {
			musicaSottofondo.stop();
			volumeCambiato=true;
		}
		musicaSottofondo.setVolume(volumeSuoni);
		suonoEsplosione.setVolume(volumeSuoni);
		suonoMunizioniFinite.setVolume(volumeSuoni);
		suonoSparoMissile.setVolume(volumeSuoni);
		suonoGameOver.setVolume(volumeSuoni);
		suonoVittoria.setVolume(volumeSuoni);
		if(ckSottofondo.isSelected() && !suonoSottofondoAttivo || volumeCambiato && ckSottofondo.isSelected()) {
			musicaSottofondo.play();
		}else if(!ckSottofondo.isSelected() && suonoSottofondoAttivo){
			musicaSottofondo.stop();
		}
		suonoSottofondoAttivo=ckSottofondo.isSelected();
		//MODALITA DI GIOCO
		if(Integer.parseInt(durataGioco.getText())<10) {
			tempoDiSpostamentoTOT=10;
			durataGioco.setText(""+10);
		}else if(Integer.parseInt(durataGioco.getText())>300) {
			tempoDiSpostamentoTOT=300;
			durataGioco.setText(""+300);
		}else {
			tempoDiSpostamentoTOT=Integer.parseInt(durataGioco.getText());
		}
		if(rbConLimiti.isSelected()) {
			valoreSpostamentoSfondo = (WIDTH_SFONDO-1000)/(tempoDiSpostamentoTOT*1000)*25;
			modalitaGiocoIllimitato=false;
		}else if(rbSenzaLimiti.isSelected()) {
			valoreSpostamentoSfondo=(WIDTH_SFONDO-1000)/(25.0*1000)*25;
			modalitaGiocoIllimitato=true;
		}
		int nuovoNumeroMunizioni=Integer.parseInt(cNumeroMunizioni.getText());
		if(numeroMunizioni!=nuovoNumeroMunizioni) {
			if(nuovoNumeroMunizioni<0) {
				nuovoNumeroMunizioni=0;
				cNumeroMunizioni.setText(""+nuovoNumeroMunizioni);
			}else if(nuovoNumeroMunizioni>1500) {
				nuovoNumeroMunizioni=1500;
				cNumeroMunizioni.setText(""+nuovoNumeroMunizioni);
			}
			numeroMunizioni=nuovoNumeroMunizioni;
			munizioni = new ImageView[numeroMunizioni];
			statoMunizione = new boolean[numeroMunizioni];
			riempiMunizioni();
		}
		bStartGioco.setDisable(true);
		bResetGioco.setDisable(false);
	}

	/**
	 * cambia interfaccia in base al pulsante che pigi
	 * @param pulsante è l'evento 
	 */
	public void gestisciInterfaccia(Event pulsante) {
		int interfaccia=-1;
		String evento=pulsante.getSource().toString();
		if(evento.equals(bStartGioco.toString())) {
			if(fineGioco) {
				interfaccia=3;
			}else {
				interfaccia=1;
			}
		}else if(evento.equals(bResetGioco.toString())) {
			resetGame=true;
			interfaccia=1;
		}else if(evento.equals(bSettings.toString())) {
			interfaccia=2; 
		}else if(evento.equals(bHomeGioco.toString())) {
			bStartGioco.setDisable(false);
			bResetGioco.setDisable(false);
			interfaccia=0; 
		}else if(evento.equals(bHomeSettings.toString())) {
			interfaccia=0; 
		}else if(evento.equals(bHomeFinale.toString())) {
			bStartGioco.setDisable(false);
			bResetGioco.setDisable(false);
			interfaccia=0; 
		}
		costruisciInterfaccia(interfaccia);
	}

	/**
	 * crea l'interfaccia
	 * @param interfaccia che interfaccia costruire
	 */
	public void costruisciInterfaccia(int interfaccia) {
		schermo.getChildren().clear();
		switch(interfaccia) {
		case 0:
			if(modalitaGiocoIllimitato) {
				tempoSovpravvissuto+=System.currentTimeMillis()-tempoStart;
			}
			giocoIniziato=false;
			//costruiamo la schermata home
			schermo.getStyleClass().add("pain");
			muoviNavicella.stop();
			controllaCollisione.stop();
			muoviSfondo.stop();
			muoviOggetti.stop();
			collisioniOggettiNavicella.stop();
			if(firstOpen) {
				bStartGioco.setDisable(false);
				bResetGioco.setDisable(true);
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
			giocoIniziato=true;
			//costruiamo la schermata per il gioco
			schermo.getChildren().add(sfondo);
			schermo.getChildren().add(sfondo2);
			posizioneNaviciella[0]=0;
			posizioneNaviciella[1]=(HEIGTH_SCHERMO - WIDTH_NAVICELLA) / 2;
			navicella.setLayoutX(posizioneNaviciella[0]);
			navicella.setLayoutY(posizioneNaviciella[1]);
			ellisseOriz.setCenterX(posizioneNaviciella[0]+WIDTH_NAVICELLA/2);
			ellisseVert.setCenterX(posizioneNaviciella[0]+WIDTH_NAVICELLA/2);
			ellisseOriz.setCenterY(posizioneNaviciella[1]+HEIGTH_NAVICELLA/2);
			ellisseVert.setCenterY(posizioneNaviciella[1]+HEIGTH_NAVICELLA/2);
			ellisseOriz.setVisible(false);	
			ellisseVert.setVisible(false);
			schermo.getChildren().add(navicella);
			schermo.getChildren().add(ellisseOriz);
			schermo.getChildren().add(ellisseVert);
			if(resetGame) {
				//nel caso si sia in modalità di reset
				if(modalitaGiocoIllimitato) {
					tempoSovpravvissuto=0;
					tempoStart=System.currentTimeMillis();
				}
				fineGioco=false;
				posizioneSfondoX = 0;
				posizioneSfondo2X = WIDTH_SFONDO;
				munizioniUtilizzate=0;
				punteggioAttuale=0;
				numeroViteRimaste=numeroVite;
				eNumeroPunti.setText(""+punteggioAttuale);
				// riempimento munizioni
				riempiMunizioni();
				numeroMunizioniAttuali=numeroMunizioni;
				eNumeroMunizioni.setText(""+numeroMunizioniAttuali);
				for (int n = 0; n < nOggetti; n++) {
					riposizionaOggetto(n);
					schermo.getChildren().add(vettoreOggetti[n]);
					schermo.getChildren().add(vettoreEllissiCollisione[n]);
				}
				sfocatura.setLayoutX(WIDTH_SFONDO);
				resetGame=false;
			}else{
				//nel caso si sia in modalità normale
				if(modalitaGiocoIllimitato) {
					tempoStart=System.currentTimeMillis();
				}
				for (int n = 0; n < nOggetti; n++) {
					schermo.getChildren().add(vettoreOggetti[n]);
					schermo.getChildren().add(vettoreEllissiCollisione[n]);
				}
				for (int nM = munizioniUtilizzate; nM < numeroMunizioni; nM++) {
					schermo.getChildren().add(munizioni[nM]);
				}
				sfocatura.setLayoutX(WIDTH_SFONDO); //posizioniamo la sfocatura fuori dallo schermo
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
			schermo.getChildren().add(bHomeGioco);
			// movimento navicella
			muoviNavicella.setCycleCount(Animation.INDEFINITE);
			muoviNavicella.play();
			// controlla collisioni missile oggetto
			controllaCollisione.setCycleCount(Animation.INDEFINITE);
			controllaCollisione.play();
			//controlla collisioni oggetto navicella
			collisioniOggettiNavicella.setCycleCount(Animation.INDEFINITE);
			collisioniOggettiNavicella.play();
			// movimento sfondo
			muoviSfondo.setCycleCount(Animation.INDEFINITE);
			muoviSfondo.play();
			// movimento oggetti
			muoviOggetti.setCycleCount(Animation.INDEFINITE);
			muoviOggetti.play();
			break;

		case 2:
			//costruiamo la schermata delle impostazioni
			if(modalitaGiocoIllimitato) {
				rbSenzaLimiti.setSelected(true);
			}else {
				rbConLimiti.setSelected(true);
			}
			cNumeroMunizioni.setText(""+numeroMunizioni);
			durataGioco.setText(""+(int)(tempoDiSpostamentoTOT));
			volume.setValue(musicaSottofondo.getVolume()*10);
			if(ckEspolosione.isSelected()) {
				ckEspolosione.setSelected(true);
			}else {
				ckEspolosione.setSelected(false);
			}
			if(ckMissile.isSelected()) {
				ckMissile.setSelected(true);
			}else {
				ckMissile.setSelected(false);
			}
			if(ckMunizioni.isSelected()) {
				ckMunizioni.setSelected(true);
			}else {
				ckMunizioni.setSelected(false);
			}
			if(suonoSottofondoAttivo) {
				ckSottofondo.setSelected(true);
			}else {
				ckSottofondo.setSelected(false);
			}
			cViteMeteorite.setText(""+viteMeteorite);
			cViteUfo.setText(""+viteUFO);
			schermo.getChildren().add(sfondoHomeFirstOpen);
			schermo.getChildren().add(sfondoHomeTrasperente);
			schermo.getChildren().add(impostazioni);
			schermo.getChildren().add(grigliaImpostazioni);
			break;

		case 3:
			muoviSfondo.stop();
			muoviNavicella.stop();
			controllaCollisione.stop();
			muoviOggetti.stop();
			controllaCollisione.stop();
			navicella.setOpacity(1);
			giocoIniziato=false;
			fineGioco=true;
			int punteggioBonus=0;//le munizioni rimaste moltiplicate per 5
			int punteggioVite=0; //le vite rimanenti moltiplicate per 100
			if(modalitaGiocoIllimitato) {
				eTitoloFinale.setText("THE END");
				eTitoloFinale.setTextFill(Color.WHITE);
				eTitoloFinale.setEffect(dropShadow1);
				tempoSovpravvissuto+=System.currentTimeMillis()-tempoStart;
				punteggioBonus=(int)(tempoSovpravvissuto)/1000*10; //tempo sopravvissuto in Sec per 10
				ePunteggioDaVita.setText("non contato");
			}else {
				if(avvenutaCollisioneOggNav) {
					eTitoloFinale.setText("GAME OVER");
					eTitoloFinale.setTextFill(Color.DARKRED);
					eTitoloFinale.setEffect(effettoGameOver);
					punteggioBonus=0;
				}else {
					eTitoloFinale.setText("YOU WIN");
					eTitoloFinale.setTextFill(Color.WHITE);
					eTitoloFinale.setEffect(dropShadow1);
					punteggioBonus=numeroMunizioniAttuali*5;
				}
				punteggioVite=numeroViteRimaste*100;
				ePunteggioDaVita.setText(""+punteggioVite);
			}
			int punteggioTotale=punteggioAttuale+punteggioBonus+punteggioVite;

			ePunteggioDaBonus.setText(""+punteggioBonus);
			ePunteggioDaPunteggio.setText(""+punteggioAttuale);
			ePunteggioTOT.setText(""+punteggioTotale);

			sfondo.setLayoutX(-WIDTH_SFONDO+WIDTH_SCHERMO);

			schermo.getChildren().add(sfondo);
			schermo.getChildren().add(sfondoHomeTrasperente);
			schermo.getChildren().add(sfondoFinale);
			schermo.getChildren().add(eTitoloFinale);
			schermo.getChildren().add(grigliaGiocoFinale);
			break;
		}
	}

	/**
	 * metodo utile per aggiornare la vita agli oggetti
	 * @param posizione la posizione dell'oggetto che si esamina nel vettore "oggettoNellaPosizione"
	 * @return ritorna la vita dell'oggetto richiesto
	 */
	public int refreshVitaOggetto(int posizione){
		if(oggettoNellaPosizione[posizione]!=2) {
			viteRimasteOggetti[posizione]=viteMeteorite;
			return viteMeteorite;
		}else {
			viteRimasteOggetti[posizione]=viteUFO;
			return viteUFO;
		}
	}

	/**
	 * riempie il numero di munizioni rendendole nuovamente disponibili tutte
	 */
	public void riempiMunizioni() {
		for (int nM = 0; nM < numeroMunizioni; nM++) {
			Image immagineMissile = new Image(getClass().getResourceAsStream("Missile.png"));
			statoMunizione[nM]=false;
			munizioni[nM] = new ImageView(immagineMissile);
			munizioni[nM].setFitHeight(HEIGTH_MISSILE);
			munizioni[nM].setFitWidth(WIDTH_MISSILE);
			munizioni[nM].setLayoutX(WIDTH_SCHERMO);
			schermo.getChildren().add(munizioni[nM]);
		}
	}
	public void metodoSpostaAllaFineNavicella(){
		valoreSpostamentoNavicella+=1;
		navicella.setLayoutX(navicella.getLayoutX()+valoreSpostamentoNavicella);
		if(navicella.getLayoutX()>=WIDTH_SCHERMO-WIDTH_NAVICELLA) {
			fineGioco=true;
			spostaAllaFineNavicella.stop();
			//riportiamo la variabile al suo valore predefinito
			valoreSpostamentoNavicella=10;
			avvenutaCollisioneOggNav=false;
			suonoVittoria.play();
			costruisciInterfaccia(3);
		}
	}
	/**
	 * valore che sposta lo sfondo
	 */
	public void aggiornaPosizioneSfondo() {
		posizioneSfondoX -= valoreSpostamentoSfondo;
		posizioneSfondo2X -= valoreSpostamentoSfondo;
		double posizioneFinaleSfondo = posizioneSfondoX + WIDTH_SFONDO;
		double posizioneFinaleSfondo2 = posizioneSfondo2X + WIDTH_SFONDO;
		if(!modalitaGiocoIllimitato) {
			if (posizioneFinaleSfondo <= WIDTH_SCHERMO) {
				giocoIniziato=false;
				for(int i=0; i<vettoreOggetti.length; i++) {
					schermo.getChildren().remove(vettoreOggetti[i]);
				}
				for (int i = 0; i < munizioniUtilizzate; i++) {
					if (!statoMunizione[i]) { // controlliamo se il missile è esploso o scomparso
						schermo.getChildren().remove(munizioni[i]);
					}
				}
				muoviSfondo.stop();
				muoviNavicella.stop();
				controllaCollisione.stop();
				muoviOggetti.stop();
				controllaCollisione.stop();
				spostaAllaFineNavicella.setCycleCount(Animation.INDEFINITE);
				spostaAllaFineNavicella.play();
			}
			sfondo.setLayoutX(posizioneSfondoX);
			sfocatura.setLayoutX(posizioneFinaleSfondo - WIDTH_SFOCATURA / 2);
		}else {
			if(posizioneFinaleSfondo<=0) {
				posizioneSfondoX=posizioneFinaleSfondo2-1000; //sotraiamo mille cosi da nascondere la parte dello sfondo più scura
			}
			if(posizioneFinaleSfondo2<=0) {
				posizioneSfondo2X=posizioneFinaleSfondo-2;
			}
			sfondo.setLayoutX(posizioneSfondoX);
			sfondo2.setLayoutX(posizioneSfondo2X);
		}
		//approfittiamo dello spostamento dello sfondo per far anche cambiare colore al numero di munizioni quando è zero
		if(System.currentTimeMillis() - tempoPassato >= 500 && numeroMunizioniAttuali==0) {
			if(bianco) {
				eNumeroMunizioni.setTextFill(Color.WHITE);
				bianco=false;
			}else {
				eNumeroMunizioni.setTextFill(Color.RED);
				bianco=true;
			}
			tempoPassato = System.currentTimeMillis();
		}
	}

	/**
	 * metodo che sposta in sequenza gli oggetti
	 */
	public void spostaOggetti() {
		indiceOggetto++;
		if(indiceOggetto==nOggetti) {
			indiceOggetto=0;
		}
		oggettoAttuale=vettoreOggetti[indiceOggetto];
		if(oggettoAttuale.getLayoutX()<=-DIMENSION_OGGETTI) {
			riposizionaOggetto(indiceOggetto);
		}else {
			oggettoAttuale.setLayoutX(oggettoAttuale.getLayoutX()-3);
			vettoreEllissiCollisione[indiceOggetto].setCenterX((oggettoAttuale.getLayoutX()+DIMENSION_OGGETTI/2)-3);
		}
	}

	/**
	 * serve per riposizionare gli oggetti prima dellla parte visibile dello schermo
	 * @param posizione la posizione dell'oggetto nel "vettoreOggetti"
	 */
	public void riposizionaOggetto(int posizione) {
		int posizioneY;
		int posizioneX;
		posizioneY=(int)(Math.random()*(HEIGTH_SCHERMO-DIMENSION_OGGETTI));
		posizioneX=(int)(Math.random()*WIDTH_SCHERMO);
		vettoreOggetti[posizione].setLayoutX(WIDTH_SCHERMO+posizioneX);
		vettoreOggetti[posizione].setLayoutY(posizioneY);
		vettoreEllissiCollisione[posizione].setCenterX(WIDTH_SCHERMO+posizioneX);
		vettoreEllissiCollisione[posizione].setCenterY(posizioneY+DIMENSION_OGGETTI/2);
		refreshVitaOggetto(posizione); //aggiorniamo la vita all'oggetto
	}
	/**
	 * fa comparire il missile
	 */
	public void spara() {
		//aspetto che pasino 100 millisecondi
		if (System.currentTimeMillis() - tempoScorsoMissile >= 100 && numeroMunizioniAttuali!=0) {
			if (numeroMunizioniAttuali<=10 && numeroMunizioniAttuali>5) {
				eNumeroMunizioni.setTextFill(Color.ORANGE);
			}else if(numeroMunizioniAttuali<=5) {
				eNumeroMunizioni.setTextFill(Color.RED);
			}
			if(ckMissile.isSelected()) {
				suonoSparoMissile.play();
			}
			munizioni[munizioniUtilizzate].setLayoutY(navicella.getLayoutY() + (HEIGTH_NAVICELLA / 2 - HEIGTH_MISSILE / 2));
			munizioni[munizioniUtilizzate].setLayoutX(navicella.getLayoutX() + WIDTH_NAVICELLA - WIDTH_MISSILE);
			munizioniUtilizzate++;
			numeroMunizioniAttuali=numeroMunizioni-munizioniUtilizzate;
			tempoScorsoMissile = System.currentTimeMillis();
			eNumeroMunizioni.setText(""+numeroMunizioniAttuali);
		}else if(System.currentTimeMillis() - tempoScorsoMissile >= 600 && numeroMunizioniAttuali==0) {
			if(ckMunizioni.isSelected()) {
				suonoMunizioniFinite.play();
			}
			tempoScorsoMissile = System.currentTimeMillis();
		}
	}

	public void pigiato(KeyEvent pulsante) {
		if(giocoIniziato) {
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
		}
		if(spostaAVANTI && navicella.getLayoutX()<=WIDTH_SCHERMO-HEIGTH_NAVICELLA) {
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

	/**
	 * controlla le collisioni tra il missile e l'oggetto
	 */
	public void metodoControllaCollisione() {
		ImageView oggettoSottopostoBound;
		numeroOggettoBound++;
		if (numeroOggettoBound == nOggetti) {
			numeroOggettoBound = 0;
		}
		oggettoSottopostoBound = vettoreOggetti[numeroOggettoBound];
		boundOggetti = oggettoSottopostoBound.getBoundsInParent();

		ImageView missileSottopostoBound;
		for (int i = 0; i < munizioniUtilizzate; i++) {
			if (!statoMunizione[i]) { // controlliamo se il missile non è nella schermata
				missileSottopostoBound = munizioni[i];
				boundMissile = missileSottopostoBound.getBoundsInParent();
				if (missileSottopostoBound.getLayoutX() > WIDTH_SCHERMO) {
					rimuoviOggetto(10, missileSottopostoBound);
					statoMunizione[i] = true;
				}
				if (boundMissile.intersects(boundOggetti)) {
					int posizioneXMissile, posizioneXMeteorie, posizioneYMissile;
					posizioneXMissile = (int) (missileSottopostoBound.getLayoutX() + WIDTH_MISSILE);
					posizioneYMissile = (int) (missileSottopostoBound.getLayoutY());
					posizioneXMeteorie = (int) (oggettoSottopostoBound.getLayoutX());
					viteRimasteOggetti[numeroOggettoBound]--;
					if(viteRimasteOggetti[numeroOggettoBound]==0) {
						riposizionaOggetto(numeroOggettoBound);
						punteggioAttuale+=refreshVitaOggetto(numeroOggettoBound)*10;
						eNumeroPunti.setText(""+punteggioAttuale);
						ImageView esplosione = new ImageView(animazioneEsplosione);
						esplosione.setFitHeight(HEIGTH_ESPLOSIONE);
						esplosione.setFitWidth(WIDTH_ESPLOSIONE);
						schermo.getChildren().add(esplosione);
						esplosione.setLayoutX(((posizioneXMissile + posizioneXMeteorie) / 2) - WIDTH_ESPLOSIONE / 2);
						esplosione.setLayoutY(posizioneYMissile - HEIGTH_ESPLOSIONE / 2);
						if(ckEspolosione.isSelected()) {
							suonoEsplosione.play();
						}
						rimuoviOggetto(500, esplosione);
					}
					rimuoviOggetto(10, missileSottopostoBound);
					statoMunizione[i] = true;
				}
			}
		}
	}

	/**
	 * controlla la collisione tra la navicella e gli oggetti
	 */
	int posizioneCollisioneOggetto=0;
	public void metodoCollisioniOggettiNavicella() {
		posizioneCollisioneOggetto++;
		if(posizioneCollisioneOggetto==nOggetti) {
			posizioneCollisioneOggetto=0;
		}
		boolean intersezione=false;
		Shape intersectPunta = Shape.intersect(ellisseOriz, vettoreEllissiCollisione[posizioneCollisioneOggetto]);
		if (intersectPunta.getBoundsInLocal().getWidth() >= 30){
			intersezione=true;
		}
		Shape intersectAli = Shape.intersect(ellisseVert, vettoreEllissiCollisione[posizioneCollisioneOggetto]);
		if (intersectAli.getBoundsInLocal().getWidth() >= 15){
			intersezione=true;
		}
		if(intersezione) {
			int posizioneXMeteorite=(int) (vettoreOggetti[posizioneCollisioneOggetto].getLayoutX());
			int posizioneYMeteorite=(int) (vettoreOggetti[posizioneCollisioneOggetto].getLayoutY());
			riposizionaOggetto(posizioneCollisioneOggetto);
			rimuoviOggetto(10, vettoreCuori[numeroViteRimaste-1]);
			numeroViteRimaste--;
			ImageView esplosione=new ImageView(animazioneEsplosione);
			schermo.getChildren().add(esplosione);
			if(numeroViteRimaste == 0) {
				giocoIniziato=false;
				lampeggiamento=false;
				avvenutaCollisioneOggNav=true;
				collisioniOggettiNavicella.stop();
				muoviNavicella.stop();
				for (int i = 0; i < munizioniUtilizzate; i++) {
					if (!statoMunizione[i]) { // controlliamo se il missile è esploso o scomparso
						schermo.getChildren().remove(munizioni[i]);
					}
				}
				controllaCollisione.stop();
				lampeggiaNavicella.stop();
				esplosione.setFitHeight(WIDTH_NAVICELLA*2);
				esplosione.setFitWidth(WIDTH_NAVICELLA*2);    
				esplosione.setLayoutX(posizioneNaviciella[0]-HEIGTH_NAVICELLA/2);
				esplosione.setLayoutY(posizioneNaviciella[1]-WIDTH_NAVICELLA/2);
				rimuoviOggetto(250, navicella);
				suonoGameOver.play();
				schermataPunteggio.setCycleCount(1);
				schermataPunteggio.play();
			}else {
				esplosione.setFitHeight(HEIGTH_ESPLOSIONE);
				esplosione.setFitWidth(WIDTH_ESPLOSIONE);
				esplosione.setLayoutX(posizioneXMeteorite-DIMENSION_OGGETTI/2);
				esplosione.setLayoutY(posizioneYMeteorite-DIMENSION_OGGETTI/2);
				rimuoviOggetto(500, esplosione);
				if(lampeggiamento) {
					lampeggiaNavicella.stop();
					navicella.setOpacity(1);
				}else {
					lampeggiamento=true;
				}
				lampeggiaNavicella.setCycleCount(4);
				lampeggiaNavicella.play();
			}
		}
	}

	/**
	 * metodo utile per rimuovere gli oggetti dallo schermo
	 * @param traQuantoTempo tempo dopo il quel si vuole rimuovere l'immagine
	 * @param oggetto l'immagine che si vuole rimuovere
	 */
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