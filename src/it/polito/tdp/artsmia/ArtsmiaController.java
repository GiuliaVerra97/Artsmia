/**
 * Sample Skeleton for 'Artsmia.fxml' Controller Class
 */

package it.polito.tdp.artsmia;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


import it.polito.tdp.artsmia.model.ArtObject;
import it.polito.tdp.artsmia.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ArtsmiaController {

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="boxLUN"
	private ChoiceBox<Integer> boxLUN; // Value injected by FXMLLoader

	@FXML // fx:id="btnCalcolaComponenteConnessa"
	private Button btnCalcolaComponenteConnessa; // Value injected by FXMLLoader

	@FXML // fx:id="btnCercaOggetti"
	private Button btnCercaOggetti; // Value injected by FXMLLoader

	@FXML // fx:id="btnAnalizzaOggetti"
	private Button btnAnalizzaOggetti; // Value injected by FXMLLoader

	@FXML // fx:id="txtObjectId"
	private TextField txtObjectId; // Value injected by FXMLLoader

	@FXML // fx:id="txtResult"
	private TextArea txtResult; // Value injected by FXMLLoader

	private Model model;
	
	@FXML
	void doAnalizzaOggetti(ActionEvent event) {
		
		txtResult.clear();
		
		this.model.creaGrafo();
		txtResult.setText("grafo creato "+model.getNumVertici()+" vertici "+model.NumArchi()+" archi");
		
	}

	@FXML
	void doCalcolaComponenteConnessa(ActionEvent event) {

		if(txtObjectId.getText()==null) {
			txtResult.setText("inserire numero oggetto ");
			return;
		}
	
	
		int oggetto=0;
		try {
			oggetto=Integer.parseInt(txtObjectId.getText());
		}catch(NumberFormatException e) {
			txtResult.appendText("Inserire intero\n");
			return;
		}
		
		if(model.getIdMap().containsKey(oggetto)) {
			txtResult.appendText("l'oggetto esiste\n");
		
		}else {
			txtResult.appendText("IMpossibile calcolare componenti connesse, l'oggetto non esiste\n");
			return;
		}
		
		int num=model.calcolatComponentiConnesse(oggetto);
		txtResult.appendText("\nle componenti connesse sono "+num);
		
		for(int i=1; i<num; i++) {
			boxLUN.getItems().add(i);
		}
	
	}

	@FXML
	void doCercaOggetti(ActionEvent event) {
		
		if(boxLUN.getValue()==null) {
			txtResult.appendText("Selezioneare LUN");
			return;
		}
		
		if(txtObjectId.getText()==null) {
			txtResult.appendText("inserire oggetto");
		}
		
		int oggetto=0;
		int num=0;
		try{
			 oggetto=Integer.parseInt(txtObjectId.getText());
			num=boxLUN.getValue();
		}catch(NumberFormatException e) {
			txtResult.setText("Inserire intero");
			return;
		}
		
		List<ArtObject> lista=model.cerca(oggetto, num);
		for(ArtObject a: lista) {
			txtResult.appendText(a.getName()+"\n");
		}
		txtResult.appendText("Peso massimo: "+model.pesoCamminoMassimo(lista));
		
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert boxLUN != null : "fx:id=\"boxLUN\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnCalcolaComponenteConnessa != null : "fx:id=\"btnCalcolaComponenteConnessa\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnCercaOggetti != null : "fx:id=\"btnCercaOggetti\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert btnAnalizzaOggetti != null : "fx:id=\"btnAnalizzaOggetti\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert txtObjectId != null : "fx:id=\"txtObjectId\" was not injected: check your FXML file 'Artsmia.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Artsmia.fxml'.";

	}

	public void setModel(Model model) {
		this.model=model;		
	}
}
