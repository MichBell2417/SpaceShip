package it.edu.iisgubbio.partiGioco;

import java.util.Hashtable;

public class Dizionario {
	public static void main(String[] args) {
		Hashtable<Integer, Integer> oggettoPosizioneX = new Hashtable<Integer, Integer>();
		Hashtable<Integer, Integer> oggettoPosizioneY = new Hashtable<Integer, Integer>();
		Integer[] vettore= new Integer[10];
		for(int i=0; i<10; i++) {
			vettore[i]=i;
		}
		for(int i=0; i<10; i++) {
			int posX= (int)(Math.random()*9);
			int posY= (int)(Math.random()*6);
			oggettoPosizioneX.put(vettore[i], posX);
			oggettoPosizioneY.put(vettore[i], posY);
		}
		for(int i=0; i<10; i++) {
			System.out.println("------------------------------------");
			System.out.println("oggetto: "+vettore[i]);
			System.out.println("posizione x: "+oggettoPosizioneX.get(vettore[i]));
			System.out.println("posizioe y: "+oggettoPosizioneY.get(vettore[i]));
		}

	}
}
