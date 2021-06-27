/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Distretto;
import it.polito.tdp.crimes.model.DistrettoAdiacente;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<Integer> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaReteCittadina"
    private Button btnCreaReteCittadina; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaReteCittadina(ActionEvent event) {
    	this.txtResult.clear();
    	Integer anno= this.boxAnno.getValue();
    	if (anno==null) {
    		this.txtResult.setText("Selezionare un anno");
    		return;
    	}
    	this.model.creaGrafo(anno);
    	this.txtResult.appendText("GRAFO CREATO:\n");
    	this.txtResult.appendText("# Vertici: "+model.getNumVertici() );
    	this.txtResult.appendText("\n# Archi: "+model.getNumArchi()+"\n" );
    	if (!model.isGrafoCreato()) {
    		this.txtResult.setText("Errore, creare prima il grafo");
    		return;
    	}
    	for (Distretto d : model.getVertici()) {
    		this.txtResult.appendText("\nInformazioni per il distretto: "+d.getId()+"\n");
    		List<DistrettoAdiacente> elenco = model.adiacenti(d);
    		for (DistrettoAdiacente da : elenco) {
    			this.txtResult.appendText("distanza dal distretto "+da.getDistretto().getId()+" è di "+da.getDistanza()+" Km\n");
    		}
    	}
    	this.boxMese.getItems().clear();
    	this.boxMese.getItems().addAll(model.getAllMonths(anno));
    	this.boxGiorno.getItems().clear();
    	this.boxGiorno.getItems().addAll(model.getAllDays(anno));
    }

    @FXML
    void doSimula(ActionEvent event) {
    	this.txtResult.clear();
    	Integer anno= this.boxAnno.getValue();
    	if (anno==null) {
    		this.txtResult.setText("Selezionare un anno");
    		return;
    	}
    	if (!model.isGrafoCreato()) { 
    		this.txtResult.setText("Prima crea il grafo!!!");
    		return;
    	}
    	Integer mese= this.boxMese.getValue();
    	if (mese==null) {
    		this.txtResult.setText("Selezionare un mese");
    		return;
    	}
    	Integer giorno= this.boxGiorno.getValue();
    	if (giorno==null) {
    		this.txtResult.setText("Selezionare un giorno");
    		return;
    	}
    	String nString = this.txtN.getText();
    	if (nString.isEmpty()) { //superfluo, basta il try e catch
    		this.txtResult.setText("Inserire un numero minimo di agenti");
    		return;
    	}
    	int n; 
    	try {
    		n=Integer.parseInt(nString);
    	} catch(NumberFormatException e) {
    		this.txtResult.setText("Inseire un valore numerico come numero minimo di agenti");
    		return;
    	}
    	if (n<0 || n>10) { //eventuali altri controlli
    		this.txtResult.setText("Inserire un numero compreso tra 0 e 10 come numero di agenti");
    		return;
    	}
    	this.model.Simula(n, anno, mese, giorno);
    	this.txtResult.appendText("Il numero di casi mal gestiti sono: \n");
    	this.txtResult.appendText(""+model.getNumCasiMalGestiti());
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaReteCittadina != null : "fx:id=\"btnCreaReteCittadina\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxAnno.getItems().addAll(model.getAllYears());
    }
}
