/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.extflightdelays;

import java.net.URL;
import java.util.ResourceBundle;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

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

    @FXML
    void doAnalizzaAeroporti(ActionEvent event) {
    	//TODO
    	
    	this.txtResult.clear();
    	
    	String input = this.distanzaMinima.getText();
    	
    	if(!input.matches("[0-9]+")) {
    		this.txtResult.setText("Si prega di inserire un numero");
    		return;
    	}
    	
    	int distMin = Integer.parseInt(input);
    	
    	long start = System.currentTimeMillis();
    	Graph<Airport, DefaultWeightedEdge> grafo = this.model.creaGrafo(distMin);
    	long end = System.currentTimeMillis();
    	
    	int vertici = this.model.nVertici();
    	int archi = this.model.nArchi();
    	
    	if(archi == 0) {
    		this.txtResult.appendText("Nessuna tratta disponibile per la distanza minima inserita");
    		return;
    	}
    	
    	this.txtResult.appendText("Il numero di archi del grafo risulta essere: " + archi +" mentre il numero di vertici: " + vertici +"\n");
    	
    	
    	StringBuilder sb = new StringBuilder();
    	
    	for(DefaultWeightedEdge e : grafo.edgeSet()){
    		sb.append(String.format("%-15s ", "FROM " + grafo.getEdgeSource(e).getCity()));
			sb.append(String.format("%-20s ", " ---- > TO " + grafo.getEdgeTarget(e).getCity()));
			sb.append(String.format("%-15s ", "weight: " + grafo.getEdgeWeight(e)));
			sb.append("\n");
    	}
    	
    	this.txtResult.appendText(sb.toString());
    	double result = (end-start)/1000.0;
    	this.txtResult.appendText("COMPLETED IN: " + result + " seconds");
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert distanzaMinima != null : "fx:id=\"distanzaMinima\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'Scene.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
