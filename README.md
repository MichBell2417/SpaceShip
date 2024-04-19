# spaceship
## Gioco:
Questo gioco è stato programmato in gruppo da Michele Bellucci e Giacomo Lillacci e si trova nel pacchetto [it.edu.iisgubbio.giocoFinale](src/it/edu/iisgubbio/giocoFinale) sotto il nome di SpaceShip.java

Nel percorso si possono notare molti file, MP3 per effetti sonori, CSS per lo stie, gif per animare il gioco, png per gli oggetti particolari (navicella, meteoriti, ufo, sfondo...) e in fine un file JAVA che contiene il codice del gioco.
## In cosa consiste:
Questa è una repository contenente un gioco che consiste in una navicella spaziale che si ritrova nello spazio e deve evitare missili detriti ed altri oggetti spaziali. Per fare ciò ha a disposizione dei missili per distruggere gli oggetti che gli impediscono il passaggio e si può muovere per tutto lo schermo.
#### PUNTEGGIO:
In questo gioco esistono tre tipi di punteggio che vanno a costituire il punteggio finale. 

Questi tre punteggi sono: 

-il punteggio preso dalle uccisioni (che viene visualizzato durante il gioco);

-il punteggio dato dal bonus (calcolato, nella modalita limitata prendendo le munizioni rimaste e moltiplicandole per 5, nella modalita illimitata si prende il tempo che si è sopravvissuti in secondi e si moltiplica per 10) nella modalita limitata in caso di GameOver varrà 0;

-il punteggio dato dalle vite (calcolato prendendo le vite rimaste e moltiplicandole per 100, non viene calcolato nella modalità illimitata);
#### VITE:
si parte avendo tre vite e si perde una vita ogni volta che si colpisce un qualsiasi oggetto, per rendere la cosa più visibile la navicella lampeggia quando colpita.

Alla fine delle vite il gioco scriverà game over presentando una schermata con il riassunto dei punti e con un pulsante attraverso il quale si può tornare alla home.
#### IMPOSTAZIONI:
dalle impostazioni si possono fare varie cose:

1-scegliere la modalita del gioco: limitata (dopo un tempo scelto il gioco arriva alla fine), illimitata (il gioco non finisce mai)

2-si possono scegliere il numero di munizioni disponibili (min: 0, max:1500)

3-si può scelgiere qual'è la durata della partita nella modalità limitata, il valore è espresso in Secondi. (min:10 S, max:300 S (5min))

4-si può scegliere il volume generale dei suoni

5-si possono attivare e disattivare i vari suoni

6-si può scegliere la vita degli oggetti che si muovono

Per salvare le impostazioni, una volta modificati i valori bisognerà salvare attraverso il pulsante "Save", se non viene fatto ciò e si torna alla home tutti i valori non salvati torneranno come prima, alcune CheckBox, non si resetteranno dato che non hanno bisogno di essere salvate.
## comandi
i comandi utili per giocare sono i seguenti (non fa differenza se si ha il Block Maiusc attivo o no):
#### spostamento
W: sposta la navicella verso l'alto

S: sposta la navicella verso il basso

A: sposta la navicella indietro

D: sposta la navicella avanti
#### spara
P: sparare un missile, se si tiene premuto, vengono sparati missili in successione fino a quando non si ricomincia lo spostamento
