package it.edu.iisgubbio.partiGioco;

import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PrendiInformazioniEvento extends Application{
	final int WIDTH_SCHERMO = 1000;
	final int HEIGTH_SCHERMO = 600;

	Image immagineSfondoHome=new Image(getClass().getResourceAsStream("immagine-sfondo-home.png"));
	ImageView sfondoHome=new ImageView(immagineSfondoHome);
	
		Button click= new Button("", sfondoHome);
		Button click2= new Button("pigiami2");
	public void start(Stage finestra) {
		Pane interfaccia = new Pane();

		
		click.setOnAction(e->leggiInfo(e));
		click2.setOnAction(e->leggiInfo(e));
		
		interfaccia.getChildren().add(click);
		interfaccia.getChildren().add(click2);
		click2.setLayoutX(600);
		
		Scene scena=new Scene(interfaccia, WIDTH_SCHERMO, HEIGTH_SCHERMO);
		finestra.setTitle("Spaceship");
		finestra.setScene(scena);
		finestra.show();		
	}
	public void leggiInfo(Event info) {
		System.out.println(info.getTarget().toString());
		System.out.println(click.toString());
	}
	public static void main(String[] args){
		launch(args);
	}
}
