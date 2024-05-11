package application;

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
	@FXML private TextArea THarry, TVoldemort, TDarthVader,TYoda,TFrodo,TSauronp,TThor,TTahnos,TJack,TDavy;
	@FXML private ImageView CHarry, CVoldemort, CDarthVader,CYoda,CFrodo,CSauronp,CThor,CTahnos,CJack,CDavy;

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		TGenerale.setText("a\na\na\na\naaa\naaa\na\na");
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
	
	public void setActions() {
		TAttacco.setText("Carta che permette di compiere un'attacco\nsemplice contro altri avversari.\nAll'interno di un mazzo sono presenti 27 copie\nNon possiede alcun seme.");
		TThanos.setText("Carta che permette di scartare una carta\ncasuale nella mano dell'avversario.\nAll'interno di un mazzo sono presenti 5 copie.\nFa parte del seme Marvel (MV)");
		TPozione.setText("Carta che permette di rigenerare 15 punti vita\npersi.\nAll'interno di un mazzo sono presenti 6 copi.\nFa parte del seme Harry Potter (HP).\n");
		TSauron.setText("Carta che permette di infliggere 20 danni a\ntutti i giocatori che non è possibile  evitare.\nAll'interno di un mazzo sono presenti 5 copie.\nFa parte del seme Signore degli Anelli (SA).\n");
		TArrembaggio.setText("Carta che permette di rubare una carta casuale\ndalla mano dell'avversario.\nAll'interno di un mazzo sono presenti 4 copie.\nFa parte del seme Pirati dei Caraibi (PC).\n");
		TPioggia.setText("Carta che permette di distruggere tutte le\ncarte statiche presenti nella board dell'\navversario scelto.\nAll'interno di un mazzo sono presenti 4 copie.\nFa parte del seme Star Wars (SW).\n");
		CAttacco.setImage(new Image(getClass().getResourceAsStream("./game_playing/CardsImages/Attacco.png")));
		CThanos.setImage(new Image(getClass().getResourceAsStream("./game_playing/CardsImages/GuantoDiThanos.png")));
		CPozione.setImage(new Image(getClass().getResourceAsStream("./game_playing/CardsImages/PozioneCurativa.png")));
		CSauron.setImage(new Image(getClass().getResourceAsStream("./game_playing/CardsImages/OcchioDiSauron.png")));
		CArrembaggio.setImage(new Image(getClass().getResourceAsStream("./game_playing/CardsImages/Arrembaggio.png")));
		CPioggia.setImage(new Image(getClass().getResourceAsStream("./game_playing/CardsImages/PioggiaDiMeteore.png")));

	}
	
	public void setStatics() {
		TAnello.setText("Carta che permette di aumentare la precisione\nquando si attacca.\nAll'interno di un mazzo sono presenti 5 copie\nFa parte del seme Signore degli Anelli (SA).\n");
		TSpecchio.setText("Carta che permette se attaccati di far subire \nall'avversario i danni al posto nostro.\nAll'interno di un mazzo sono presenti 5 copie.\nFa parte del seme Harry Potter (HP)\n");
		TVeleno.setText("Carta che permette di far perdere all'\navversario 5 punti vita se attaccato.\nAll'interno di un mazzo sono presenti 5 copie.\nFa parte del seme Marvel (MV).\n");
		TMaledizione.setText("Carta che permette di ridurre la precisione \ndell'attaccante.\nAll'interno di un mazzo sono presenti 5 copie.\nFa parte del seme Pirati dei Caraibi (PC).\n");
		TScudo.setText("Carta che permette di evitare il colpo di un\navversario.\nAll'interno di un mazzo sono presenti 12 copie\nNon possiede alcun seme.\n");
		TOlogramma.setText("Carta che funge da scudo se la carta estratta\nè dello stesso seme del personaggio.\nAll'interno di un mazzo sono presenti 5 copie.\nFa parte del seme Star Wars (SW).\n");
		CAnello.setImage(new Image(getClass().getResourceAsStream("./game_playing/CardsImages/Anello.png")));
		CSpecchio.setImage(new Image(getClass().getResourceAsStream("./game_playing/CardsImages/SpecchioIncantato.png")));
		CVeleno.setImage(new Image(getClass().getResourceAsStream("./game_playing/CardsImages/VelenoDiVedovaNera.png")));
		CMaledizione.setImage(new Image(getClass().getResourceAsStream("./game_playing/CardsImages/MaledizioneAzteca.png")));
		CScudo.setImage(new Image(getClass().getResourceAsStream("./game_playing/CardsImages/Scudo.png")));
		COlogramma.setImage(new Image(getClass().getResourceAsStream("./game_playing/CardsImages/Ologramma.png")));
	}
	
	public void setEvento() {
		TFurto.setText("Furto d'identità:\nCarta che permette di scambiare il\npersonaggio con quello di un altro giocatore.\nAll'interno di un mazzo sono presenti 5 copie\nNon possiede alcun seme.\n");
		TGiorno.setText("Giorno del giudizio:\nCarta che permette di eliminare un giocatore\na scelta.\nAll'interno di un mazzo sono presenti 5 copie\nNon possiede alcun seme.\n");
		TMiracolo.setText("Miracolo:\nCarta che permette recupera tutti i punti vita\npersi.\nAll'interno di un mazzo sono presenti 5 copie\nNon possiede alcun seme.\n");
		CFurto.setImage(new Image(getClass().getResourceAsStream("./game_playing/CardsImages/FurtoDiIdentità.png")));
		CGiorno.setImage(new Image(getClass().getResourceAsStream("./game_playing/CardsImages/GiornoDelGiudizio.png")));
		CMiracolo.setImage(new Image(getClass().getResourceAsStream("./game_playing/CardsImages/Miracolo.png")));
	}
	
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
		CBacchetta.setImage(new Image(getClass().getResourceAsStream("./game_playing/CardsImages/BacchettaDiSambuco.png")));
		CSpadaLaser.setImage(new Image(getClass().getResourceAsStream("./game_playing/CardsImages/SpadaLaser.png")));
		CSpadaRe.setImage(new Image(getClass().getResourceAsStream("./game_playing/CardsImages/SpadaDelRe.png")));
		CSciabola.setImage(new Image(getClass().getResourceAsStream("./game_playing/CardsImages/SciabolaDelPirata.png")));
		CPistola.setImage(new Image(getClass().getResourceAsStream("./game_playing/CardsImages/Pistola.png")));
		CMjolnir.setImage(new Image(getClass().getResourceAsStream("./game_playing/CardsImages/Mjolnir.png")));
		CSpadaSemplice.setImage(new Image(getClass().getResourceAsStream("./game_playing/CardsImages/SpadaComune.png")));
		CBacchettaPrinc.setImage(new Image(getClass().getResourceAsStream("./game_playing/CardsImages/BacchettaDelPrincipiante.png")));
		CSpadaOscura.setImage(new Image(getClass().getResourceAsStream("./game_playing/CardsImages/SpadaLaserOscura.png")));
		CScettro.setImage(new Image(getClass().getResourceAsStream("./game_playing/CardsImages/ScettroDiLoki.png")));

	}
	
	public void setCharacters() {
		THarry.setText("Personaggio di Harry Potter.\nPosiede 8 punti attacco, 85 punti vita, \n6 punti precisione.\nFa parte del seme Harry Potter (HP).\n");
		TVoldemort.setText("Personaggio di Voldemort.\nPosiede 9 punti attacco, 100 punti vita, \n7 punti precisione.\nFa parte del seme Harry Potter (HP).\n");
		TDarthVader.setText("Personaggio di Darth Vader.\nPosiede 8 punti attacco, 100 punti vita, \n8 punti precisione.\nFa parte del seme Star Wars (SW).\n");
		TYoda.setText("Personaggio di Yoda.\nPosiede 9 punti attacco, 90 punti vita, \n8 punti precisione.\nFa parte del seme Star Wars (SW).\n");
		TFrodo.setText("Personaggio di Frodo Baggins.\nPosiede 5 punti attacco, 80 punti vita, \n4 punti precisione.\nFa parte del seme Signori degli Anelli (SA).\n");
		TSauronp.setText("Personaggio di Sauron.\nPosiede 10 punti attacco, 110 punti vita, \n7 punti precisione.\nFa parte del seme Signore degli Anelli (SA).\n");
		TThor.setText("Personaggio di Thor.\nPosiede 8 punti attacco, 110 punti vita, \n7 punti precisione.\nFa parte del seme Marvel (MV).\n");
		TThanos.setText("Personaggio di Thanos.\nPosiede 10 punti attacco, 80 punti vita, \n6 punti precisione.\nFa parte del seme Marvel (MV).\n");
		TJack.setText("Personaggio di Jack Sparrow.\nPosiede 6 punti attacco, 90 punti vita, \n9 punti precisione.\nFa parte del seme Pirati dei Caraibi (PC).\n");
		TDavy.setText("Personaggio di Davy Jones.\nPosiede 8 punti attacco, 90 punti vita, \n9 punti precisione.\nFa parte del seme Pirati dei Caraibi (PC).\n");
		CHarry.setImage(new Image(getClass().getResourceAsStream("./game_playing/CharactersCardsImages/HarryPotterCh.png")));
		CVoldemort.setImage(new Image(getClass().getResourceAsStream("./game_playing/CharactersCardsImages/VoldemortCh.png")));
		CDarthVader.setImage(new Image(getClass().getResourceAsStream("./game_playing/CharactersCardsImages/DarthVaderCh.png")));
		CYoda.setImage(new Image(getClass().getResourceAsStream("./game_playing/CharactersCardsImages/YodaCh.png")));
		CFrodo.setImage(new Image(getClass().getResourceAsStream("./game_playing/CharactersCardsImages/FrodoCh.png")));
		CSauronp.setImage(new Image(getClass().getResourceAsStream("./game_playing/CharactersCardsImages/SauronCh.png")));
		CThor.setImage(new Image(getClass().getResourceAsStream("./game_playing/CharactersCardsImages/ThorCh.png")));
		CThanos.setImage(new Image(getClass().getResourceAsStream("./game_playing/CharactersCardsImages/ThanosCh.png")));
		CJack.setImage(new Image(getClass().getResourceAsStream("./game_playing/CharactersCardsImages/JackSparrowCh.png")));
		CDavy.setImage(new Image(getClass().getResourceAsStream("./game_playing/CharactersCardsImages/DavyJonesCh.png")));
		
	}
	
	public void goToHome(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("home.fxml"));
		stage=(Stage)((Node)event.getSource()).getScene().getWindow();
		scene=new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	
	
}