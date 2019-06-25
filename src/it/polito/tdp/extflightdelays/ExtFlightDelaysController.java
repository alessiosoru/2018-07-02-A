

/**
 * Sample Skeleton for 'ExtFlightDelays.fxml' Controller Class
 */

package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ExtFlightDelaysController {

	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="distanzaMinima"
    private TextField distanzaMinima; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalizza"
    private Button btnAnalizza; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxAeroportoPartenza"
    private ComboBox<Airport> cmbBoxAeroportoPartenza; // Value injected by FXMLLoader

    @FXML // fx:id="btnAeroportiConnessi"
    private Button btnAeroportiConnessi; // Value injected by FXMLLoader

    @FXML // fx:id="numeroVoliTxtInput"
    private TextField migliaDispTxtInput; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaItinerario"
    private Button btnCercaItinerario; // Value injected by FXMLLoader

    @FXML
    void doAnalizzaAeroporti(ActionEvent event) {
    	Integer x = 0;
    	this.txtResult.clear();
    	
    	if(this.distanzaMinima.getText().isEmpty()) {
    		this.txtResult.appendText("Devi inserire distanza minima in cifre\n");
    		return;
    	}
    	try{
    		x = Integer.parseInt(this.distanzaMinima.getText());
    	}catch(NumberFormatException e) {
    		this.txtResult.appendText("Devi inserire distanza minima in cifre\n");
    		return;
    	}
    	
    	model.creaGrafo(x);
    	
    	this.txtResult.appendText("GRAFO CREATO !!\nVERTICI "+model.getNumVertici()+
    			"\nARCHI "+model.getNumArchi()+"\n");
    	
    	this.cmbBoxAeroportoPartenza.getItems().clear();
    	this.cmbBoxAeroportoPartenza.getItems().addAll(model.getAeroporti());

    }

    @FXML
    void doCalcolaAeroportiConnessi(ActionEvent event) {
    	Airport partenza = this.cmbBoxAeroportoPartenza.getValue();
    	if(partenza==null) {
    		this.txtResult.appendText("Devi selezionare un aeroporto\n");
    		return;
    	}
    	
    	List<Airport>  adiacenti = model.getAdiacentiDi(partenza);
    	
    	this.txtResult.clear();
		this.txtResult.appendText("Aeroporti collegati a "+partenza.toString()+"\n");
    	for(Airport a : adiacenti) {
    		this.txtResult.appendText(a.toString()+"\n");
    	}
    	
    }

    @FXML
    void doCercaItinerario(ActionEvent event) {
    	Double miglia = 0.0;
    	Integer x = 0;
    	
    	this.txtResult.clear();
    	
    	if(this.distanzaMinima.getText().isEmpty()) {
    		this.txtResult.appendText("Devi inserire distanza minima in cifre\n");
    		return;
    	}
    	try{
    		x = Integer.parseInt(this.distanzaMinima.getText());
    	}catch(NumberFormatException e) {
    		this.txtResult.appendText("Devi inserire distanza minima in cifre\n");
    		return;
    	}
    	Airport partenza = this.cmbBoxAeroportoPartenza.getValue();
    	if(partenza==null) {
    		this.txtResult.appendText("Devi selezionare un aeroporto\n");
    		return;
    	}
    	
    	if(this.migliaDispTxtInput.getText()==null){
    		this.txtResult.appendText("Devi inserire le miglia in cifre\n");
    		return;
    	}
    	try{
    		miglia = Double.parseDouble(this.migliaDispTxtInput.getText());
    		if(miglia<x) {
        		this.txtResult.appendText("Devi inserire le miglia in cifre maggiori"
        				+ "o uguali alla distanza minima\n");
        		return;
    			
    		}
    	}catch(NumberFormatException e) {
    		this.txtResult.appendText("Devi inserire le miglia in cifre\n");
    		return;
    	}
    	
    	System.out.println(miglia+" "+ partenza);
    	List<Airport> percorsoBest = model.cercaItinerario(miglia, partenza);
    	
    	this.txtResult.appendText("TROAVTO PERCORSO BEST !!\nMIGLIA DISPONIBILI: "+
    			miglia+"\nMIGLIA PERCORSE: "+model.getMigliaBest()+"\n");
    	
    	for(Airport a : percorsoBest) {
    		this.txtResult.appendText(a.getAirportName()+" - "+a.getCity()+"\n");
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert distanzaMinima != null : "fx:id=\"distanzaMinima\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert cmbBoxAeroportoPartenza != null : "fx:id=\"cmbBoxAeroportoPartenza\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnAeroportiConnessi != null : "fx:id=\"btnAeroportiConnessi\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert migliaDispTxtInput != null : "fx:id=\"numeroVoliTxtInput\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";
        assert btnCercaItinerario != null : "fx:id=\"btnCercaItinerario\" was not injected: check your FXML file 'ExtFlightDelays.fxml'.";

    }
    
    public void setModel(Model model) {
		this.model = model;
		
	}
}

