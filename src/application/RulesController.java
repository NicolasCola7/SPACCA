package application;

import java.io.File;
import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;

public class RulesController implements Initializable{
	private Scene scene;
	private Stage stage;
	private Parent root;
	@FXML private TabPane tabPane;
    @FXML private Tab tabAzione, tabStatiche, tabEvento, tabArmi, tabPersonaggi;
	@FXML private Button homeButton; 
	@FXML private TextArea TGenerale;
	@FXML private TextArea TAttacco, TThanos, TPozione, TSauron, TArrembaggio, TPioggia;
	@FXML private ImageView CAttacco, CThanos, CPozione, CSauron, CArrembaggio, CPioggia;
	@FXML private TextArea TAnello, TSpecchio, TVeleno,TMaledizione,TScudo,TOlogramma;
	@FXML private ImageView CAnello, CSpecchio, CVeleno,CMaledizione,CScudo,COlogramma;
	@FXML private TextArea TFurto, TGiorno, TMiracolo;
	@FXML private ImageView CFurto, CGiorno, CMiracolo;
	@FXML private TextArea TBacchetta, TSpadaLaser, TSpadaRe,TSciabola,TPistola,TMjolnir,TSpadaSemplice,TBacchettaPrinc,TSpadaOscura,TScettro;
	@FXML private ImageView CBacchetta, CSpadaLaser, CSpadaRe,CSciabola,CPistola,CMjolnir,CSpadaSemplice,CBacchettaPrinc,CSpadaOscura,CScettro;
	@FXML private TextArea THarry, TVoldemort, TDarthVader,TYoda,TFrodo,TSauronp,TThor,TCThanos,TJack,TDavy;
	@FXML private ImageView CHarry, CVoldemort, CDarthVader,CYoda,CFrodo,CSauronp,CThor,CCThanos,CJack,CDavy;

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//load main tab immediately, others tabs only in case of click  
		TGenerale.setText("Spacca è un gioco stand-alone di carte fantasy, utilizzabile in modalità multiplayer con giocatori umani e bot. \n\n"
				+ "Si basa su personaggi principali di differenti saghe (Marvel, Harry Potter, Signore degli Anelli, Star Wars e Pirati dei Caraibi) che rappresentano i semi delle carte. Tali semi non sono vincolanti per il loro utilizzo. Alcune carte non hanno un seme perchè generali e non rappresentative delle saghe. \n\n"
				+ "L'obiettivo del gioco è eliminare gli avversari e resistere come ultimo giocatore rimasto e vincere. \n\n"
				+ "E' possiile giocare in modalità \"Partita classica\" ovvero 1 contro tutti, con un minimo di 2 e un massimo di 5 giocatori. Oppure \"Torneo\", composto da 8 giocatori che si sfidano in partite 1 vs 1."
				+ "\n\n"
				+ "-----------------------------------------------------------------------------------------------\n"
				+ "COME SI GIOCA?"
				+ "\n\n"
				+ "All'inizio della partita verrà assegnato casualmente un personaggio ad ogni giocatore. Informazioni sugli avversari possono essere visionate tramite i tasti in alto. \n"
				+ "Il giocatore troverà nella parte inferiore una serie di carte utilizzabili tramite i bottoni presenti sulla destra. \n"
				+ "Dopo aver selezionato una carta sarà possibile utilizzarla (eventulalmente su uno specifico giocatore) oppure scartarla. Una volta terminate le azioni il giocatore deve passare il turno tramite l'apposito bottone. \n"
				+ "E' possibile pescare una nuova carta oppure scartarla solamente una volta per turno. \n"
				+ "Nel primo turno di gioco non è possibile utilizzare carte azione ed evento. Le carte attacco possono essere utlizzate solo una volta per turno. \n"
				+ "In caso di vittoria di una partita classica o torneo da parte di un bot non verrà assegnato nessun punto alla leaderboard. \n"
				+ "Sulla sinistra è possibile visualizzare le informazioni sul proprio personaggio, l'arma equipaggiata e la board. \n"
				+ "La board è necessaria per il posizionamento di carte statiche. \n"
				+ "-1^ posizione: carte che evtano un attacco(scudo, ologramma, specchio incantato), se usate vengono scartate in automatico.\n"
				+ "-2^ posizione: carte che permettono di aumentare/diminuire un attributo(anello, maledizione azteca e veleno di vedova nera), la loro gestione spetta al giocatore.\n"
				+ "Attraverso il menu in alto a sinstra è possibile uscire, salvare, visualizzare la leaderboard o il regolamento."
				+ "\n\n"
				+ "-----------------------------------------------------------------------------------------------\n"
				+ "PARTITA CLASSICA\n\n"
				+ "E' composta da un minimo di 2 fino ad un massimo di 5 giocatori, che si scontreranno in modalità uno vs tutti. \n\n"
				+ "Vince l'ultimo giocatore che rimane, a cui verrà assegnato un punto.\n\n"
				+ "Se gli ultimi giocatori si eliminano a vicenda la partita termina in pareggio e non viene assegnato nessun punto nella leaderboard. "
				+ "\n\n"
				+ "-----------------------------------------------------------------------------------------------\n"
				+ "TORNEO\n\n"
				+ "E' obbligatoriamente composto da 8 giocatori, che si scontreranno in modalità 1 vs 1, per raggiungere la vetta del torneo.\n\n"
				+ "Si compone da quarti di finale (4 partite da 2 giocatori ad eliminazione diretta), semifinali e finale. \n\n"
				+ "In una singola partita il vincitore rimarrà in gioco per scalare la vetta, il perdente verrà escluso. In caso di eliminazione a vicenda viene lanciata una moneta per decretare vincitore.\n \n"
				+ "Attraverso il tasto bracket possiamo visualizzare l'andamento del torneo.");
				
		tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
	        if (newTab != null && newTab.equals(tabAzione)) {
	            setActions();
	            System.out.println("entrato");
	        }
	    });
		tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
	        if (newTab != null && newTab.equals(tabStatiche)) {
	            setStatics();
	            System.out.println("entrato stat");
	        }
	    });
		tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
	        if (newTab != null && newTab.equals(tabEvento)) {
	            setEvento();
	            System.out.println("entrato ev");
	        }
	    });
		tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
	        if (newTab != null && newTab.equals(tabArmi)) {
	            setWeapons();
	            System.out.println("entrato arm");
	        }
	    });
		tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
	        if (newTab != null && newTab.equals(tabPersonaggi)) {
	            setCharacters();
	            System.out.println("entrato pers");
	        }
	    });
	 }
	
	//set action cards textArea and images 
	public void setActions() {
		TAttacco.setText("Carta che permette di compiere un'attacco\nsemplice contro altri avversari.\nAll'interno di un mazzo sono presenti 27 copie\nNon possiede alcun seme.");
		TThanos.setText("Carta che permette di scartare una carta\ncasuale nella mano dell'avversario.\nAll'interno di un mazzo sono presenti 5 copie.\nFa parte del seme Marvel (MV)");
		TPozione.setText("Carta che permette di rigenerare 15 punti vita\npersi.\nAll'interno di un mazzo sono presenti 6 copi.\nFa parte del seme Harry Potter (HP).\n");
		TSauron.setText("Carta che permette di infliggere 20 danni a\ntutti i giocatori che non è possibile  evitare.\nAll'interno di un mazzo sono presenti 5 copie.\nFa parte del seme Signore degli Anelli (SA).\n");
		TArrembaggio.setText("Carta che permette di rubare una carta casuale\ndalla mano dell'avversario.\nAll'interno di un mazzo sono presenti 4 copie.\nFa parte del seme Pirati dei Caraibi (PC).\n");
		TPioggia.setText("Carta che permette di distruggere tutte le\ncarte statiche presenti nella board dell'\navversario scelto.\nAll'interno di un mazzo sono presenti 4 copie.\nFa parte del seme Star Wars (SW).\n");
		CAttacco.setImage(new Image(getClass().getResourceAsStream("game_playing/CardsImages/Attacco.png")));
		CThanos.setImage(new Image(getClass().getResourceAsStream("game_playing/CardsImages/GuantoDiThanos.png")));
		CPozione.setImage(new Image(getClass().getResourceAsStream("game_playing/CardsImages/PozioneCurativa.png")));
		CSauron.setImage(new Image(getClass().getResourceAsStream("game_playing/CardsImages/OcchioDiSauron.png")));
		CArrembaggio.setImage(new Image(getClass().getResourceAsStream("game_playing/CardsImages/Arrembaggio.png")));
		CPioggia.setImage(new Image(getClass().getResourceAsStream("game_playing/CardsImages/PioggiaDiMeteore.png")));

	}
	
	//set static cards textArea and images 
	public void setStatics() {
		TAnello.setText("Carta che permette di aumentare la precisione\nquando si attacca.\nAll'interno di un mazzo sono presenti 5 copie\nFa parte del seme Signore degli Anelli (SA).\n");
		TSpecchio.setText("Carta che permette se attaccati di far subire \nall'avversario i danni al posto nostro.\nAll'interno di un mazzo sono presenti 5 copie.\nFa parte del seme Harry Potter (HP)\n");
		TVeleno.setText("Carta che permette di far perdere all'\navversario 5 punti vita se attaccato.\nAll'interno di un mazzo sono presenti 5 copie.\nFa parte del seme Marvel (MV).\n");
		TMaledizione.setText("Carta che permette di ridurre la precisione \ndell'attaccante.\nAll'interno di un mazzo sono presenti 5 copie.\nFa parte del seme Pirati dei Caraibi (PC).\n");
		TScudo.setText("Carta che permette di evitare il colpo di un\navversario.\nAll'interno di un mazzo sono presenti 12 copie\nNon possiede alcun seme.\n");
		TOlogramma.setText("Carta che funge da scudo se la carta estratta\nè dello stesso seme del personaggio.\nAll'interno di un mazzo sono presenti 5 copie.\nFa parte del seme Star Wars (SW).\n");
		CAnello.setImage(new Image(getClass().getResourceAsStream("game_playing/CardsImages/Anello.png")));
		CSpecchio.setImage(new Image(getClass().getResourceAsStream("game_playing/CardsImages/SpecchioIncantato.png")));
		CVeleno.setImage(new Image(getClass().getResourceAsStream("game_playing/CardsImages/VelenoDiVedovaNera.png")));
		CMaledizione.setImage(new Image(getClass().getResourceAsStream("game_playing/CardsImages/MaledizioneAzteca.png")));
		CScudo.setImage(new Image(getClass().getResourceAsStream("game_playing/CardsImages/Scudo.png")));
		COlogramma.setImage(new Image(getClass().getResourceAsStream("game_playing/CardsImages/Ologramma.png")));
	}
	
	//set event cards textArea and images 
	public void setEvento() {
		TFurto.setText("Furto d'identità:\nCarta che permette di scambiare il\npersonaggio con quello di un altro giocatore.\nAll'interno di un mazzo ne è presente 1 copia\nNon possiede alcun seme.\n");
		TGiorno.setText("Giorno del giudizio:\nCarta che permette di eliminare un giocatore\na scelta.\nAll'interno di un mazzo ne è presente 1 copia\nNon possiede alcun seme.\n");
		TMiracolo.setText("Miracolo:\nCarta che permette recupera tutti i punti vita\npersi.\nAll'interno di un mazzo ne è presente 1 copia\nNon possiede alcun seme.\n");
		CFurto.setImage(new Image(getClass().getResourceAsStream("game_playing/CardsImages/FurtoDiIdentità.png")));
		CGiorno.setImage(new Image(getClass().getResourceAsStream("game_playing/CardsImages/GiornoDelGiudizio.png")));
		CMiracolo.setImage(new Image(getClass().getResourceAsStream("game_playing/CardsImages/Miracolo.png")));
	}
	
	//set weapons cards textArea and images 
	public void setWeapons() {
		TBacchetta.setText("Arma che ha una potenza di 15 punti.\nAll'interno di un mazzo sono presenti 2 copie\nFa parte del seme Harry Potter (HP).\n");
		TSpadaLaser.setText("Arma che ha una potenza di 13 punti.\nAll'interno di un mazzo sono presenti 2 copie\nFa parte del seme Star Wars (SW).\n");
		TSpadaRe.setText("Arma che ha una potenza di 12 punti.\nAll'interno di un mazzo sono presenti 2 copie\nFa parte del seme Signore degli Anelli (SA).\n");
		TSciabola.setText("Arma che ha una potenza di 10 punti.\nAll'interno di un mazzo sono presenti 2 copie\nFa parte del seme Pirati dei Caraibi (SA).\n");
		TPistola.setText("Arma che ha una potenza di 5 punti.\nAll'interno di un mazzo sono presenti 2 copie\nFa parte del seme Pirati dei Caraibi (PC).\n");
		TMjolnir.setText("Arma che ha una potenza di 14 punti.\nAll'interno di un mazzo sono presenti 2 copie\nFa parte del seme Marvel (MV).\n");
		TSpadaSemplice.setText("Arma che ha una potenza di 7 punti.\nAll'interno di un mazzo sono presenti 2 copie\nFa parte del seme Signore degli Anelli (SA).\n");
		TBacchettaPrinc.setText("Arma che ha una potenza di 9 punti.\nAll'interno di un mazzo sono presenti 2 copie\nFa parte del seme Harry Potter (HP).\n");
		TSpadaOscura.setText("Arma che ha una potenza di 10 punti.\nAll'interno di un mazzo sono presenti 2 copie\nFa parte del seme Star Wars (SW).\n");
		TScettro.setText("Arma che ha una potenza di 8 punti.\nAll'interno di un mazzo sono presenti 2 copie\nFa parte del seme Marvel (MV).\n");
		CBacchetta.setImage(new Image(getClass().getResourceAsStream("game_playing/CardsImages/BacchettaDiSambuco.png")));
		CSpadaLaser.setImage(new Image(getClass().getResourceAsStream("game_playing/CardsImages/SpadaLaser.png")));
		CSpadaRe.setImage(new Image(getClass().getResourceAsStream("game_playing/CardsImages/SpadaDelRe.png")));
		CSciabola.setImage(new Image(getClass().getResourceAsStream("game_playing/CardsImages/SciabolaDelPirata.png")));
		CPistola.setImage(new Image(getClass().getResourceAsStream("game_playing/CardsImages/Pistola.png")));
		CMjolnir.setImage(new Image(getClass().getResourceAsStream("game_playing/CardsImages/Mjolnir.png")));
		CSpadaSemplice.setImage(new Image(getClass().getResourceAsStream("game_playing/CardsImages/SpadaComune.png")));
		CBacchettaPrinc.setImage(new Image(getClass().getResourceAsStream("game_playing/CardsImages/BacchettaDelPrincipiante.png")));
		CSpadaOscura.setImage(new Image(getClass().getResourceAsStream("game_playing/CardsImages/SpadaLaserOscura.png")));
		CScettro.setImage(new Image(getClass().getResourceAsStream("game_playing/CardsImages/ScettroDiLoki.png")));

	}
	
	//set characters cards textArea and images 
	public void setCharacters() {
		THarry.setText("Personaggio di Harry Potter.\nPosiede 8 punti attacco, 85 punti vita, \n6 punti precisione.\nFa parte del seme Harry Potter (HP).\n");
		TVoldemort.setText("Personaggio di Voldemort.\nPosiede 9 punti attacco, 100 punti vita, \n7 punti precisione.\nFa parte del seme Harry Potter (HP).\n");
		TDarthVader.setText("Personaggio di Darth Vader.\nPosiede 8 punti attacco, 100 punti vita, \n8 punti precisione.\nFa parte del seme Star Wars (SW).\n");
		TYoda.setText("Personaggio di Yoda.\nPosiede 9 punti attacco, 90 punti vita, \n8 punti precisione.\nFa parte del seme Star Wars (SW).\n");
		TFrodo.setText("Personaggio di Frodo Baggins.\nPosiede 5 punti attacco, 80 punti vita, \n4 punti precisione.\nFa parte del seme Signori degli Anelli (SA).\n");
		TSauronp.setText("Personaggio di Sauron.\nPosiede 10 punti attacco, 110 punti vita, \n7 punti precisione.\nFa parte del seme Signore degli Anelli (SA).\n");
		TThor.setText("Personaggio di Thor.\nPosiede 8 punti attacco, 110 punti vita, \n7 punti precisione.\nFa parte del seme Marvel (MV).\n");
		TCThanos.setText("Personaggio di Thanos.\nPosiede 10 punti attacco, 80 punti vita, \n6 punti precisione.\nFa parte del seme Marvel (MV).\n");
		TJack.setText("Personaggio di Jack Sparrow.\nPosiede 6 punti attacco, 90 punti vita, \n9 punti precisione.\nFa parte del seme Pirati dei Caraibi (PC).\n");
		TDavy.setText("Personaggio di Davy Jones.\nPosiede 8 punti attacco, 90 punti vita, \n9 punti precisione.\nFa parte del seme Pirati dei Caraibi (PC).\n");
		CHarry.setImage(new Image(getClass().getResourceAsStream("game_playing/CharactersCardsImages/HarryPotterCh.png")));
		CVoldemort.setImage(new Image(getClass().getResourceAsStream("game_playing/CharactersCardsImages/VoldemortCh.png")));
		CDarthVader.setImage(new Image(getClass().getResourceAsStream("game_playing/CharactersCardsImages/DarthVaderCh.png")));
		CYoda.setImage(new Image(getClass().getResourceAsStream("game_playing/CharactersCardsImages/YodaCh.png")));
		CFrodo.setImage(new Image(getClass().getResourceAsStream("game_playing/CharactersCardsImages/FrodoCh.png")));
		CSauronp.setImage(new Image(getClass().getResourceAsStream("game_playing/CharactersCardsImages/SauronCh.png")));
		CThor.setImage(new Image(getClass().getResourceAsStream("game_playing/CharactersCardsImages/ThorCh.png")));
		CCThanos.setImage(new Image(getClass().getResourceAsStream("game_playing/CharactersCardsImages/ThanosCh.png")));
		CJack.setImage(new Image(getClass().getResourceAsStream("game_playing/CharactersCardsImages/JackSparrowCh.png")));
		CDavy.setImage(new Image(getClass().getResourceAsStream("game_playing/CharactersCardsImages/DavyJonesCh.png")));
		
	}
	
	public void goToHome(ActionEvent event)  {
		try {
			root = FXMLLoader.load((new File("FXML/home.fxml").toURI().toURL()));
			stage=(Stage)((Node)event.getSource()).getScene().getWindow();
			scene=new Scene(root);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			Alert errorAlert=new Alert(AlertType.ERROR);
			errorAlert.setHeaderText("Si è verificato un errore:");
			errorAlert.setContentText("Riprova più tardi!");
			errorAlert.showAndWait();
			e.printStackTrace();
		}
		
	}

	public void hideHomeButton() {
		homeButton.setVisible(false);
		
	}
	
	
	
}