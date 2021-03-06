package controller;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.ConnectException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.Client;
import application.ErrorInfoDisplay;
import application.Main;
import application.Server;

public class Controler {

	private static String DEFAULT_IP_ADRESS = "127.0.0.1";
	private static String DEFAULT_PORT = "3343";
	private Main main;
	private String clientPort;
	private String serverPort;
	private String ipAdress;
	private String regexForPort = "^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$";
	private String regexForIP = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	private ErrorInfoDisplay errorInfoDisplay = new ErrorInfoDisplay();

	@FXML
	private Button clientButton;
	@FXML
	private Button serverButton;
	@FXML
	private TextField ipAdressClientTextField;
	@FXML
	private TextField portClientTextField;
	@FXML
	private TextField portServerTextField;

	public void setMain(Main main) {
		this.main = main;
		ipAdressClientTextField.setText(DEFAULT_IP_ADRESS);
		portClientTextField.setText(DEFAULT_PORT);
		portServerTextField.setText(DEFAULT_PORT);
	}

	@FXML
	public void loadClientButton() {
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/client.fxml"));

		try {
			Stage primaryStage = new Stage();
			AnchorPane pane = loader.load();
			primaryStage.setMinWidth(800.0);
			primaryStage.setMinHeight(800.0);
			Scene scene = new Scene(pane);
			Image im = new Image(getClass().getResourceAsStream("/view/background.jpg"), 750, 750, false, true);
			BackgroundImage bi = new BackgroundImage(im, BackgroundRepeat.ROUND, BackgroundRepeat.ROUND, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
			pane.setBackground(new Background(bi));
			ControlerClient cl = loader.getController();
			getConnectionParametersForClient();
			if (Client.sprKlient(ipAdress, Integer.parseInt(clientPort)) == true) {
				cl.setServerClient(false, clientPort, ipAdress);
				primaryStage.setScene(scene);
				primaryStage.setTitle("Klient");
				primaryStage.resizableProperty().setValue(Boolean.FALSE);
				primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					
			          public void handle(WindowEvent we) {
			              try
			              {
			            	  System.exit(0);
			            	  
								
			              }catch(Exception e)
			              {
			            	  
			              }
			          }
			      }); 
				
				primaryStage.show();
				
			    main.primaryStage.close();

			} else {
				Platform.runLater(() -> errorInfoDisplay.cannotConnectToServer());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void loadServerButton() {
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/client.fxml"));

		try {

			Stage primaryStage = new Stage();
			AnchorPane pane = loader.load();
			primaryStage.setMinWidth(800.0);
			primaryStage.setMinHeight(800.0);
			Scene scene = new Scene(pane);
			Image im = new Image(getClass().getResourceAsStream("/view/background.jpg"), 750, 750, false, true);
			BackgroundImage bi = new BackgroundImage(im, BackgroundRepeat.ROUND, BackgroundRepeat.ROUND, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
			pane.setBackground(new Background(bi));
			ControlerClient cl = loader.getController();
			getConnectionParametersForServer();
			if (Server.sprSvr(serverPort) == true) {
				cl.setServerClient(true, serverPort, null);
				primaryStage.setScene(scene);
				primaryStage.setTitle("Serwer");
				primaryStage.resizableProperty().setValue(Boolean.FALSE);
				primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					
			          public void handle(WindowEvent we) {
			              try
			              {
			            	  System.exit(0);
			            	  
								
			              }catch(Exception e)
			              {
			            	  
			              }
			          }
			      }); 
				
				primaryStage.show();

				main.primaryStage.close();
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getConnectionParametersForServer() throws ConnectException {
		serverPort = portServerTextField.getText();
		if (!validatePort(serverPort)) {
			errorInfoDisplay.wrongPort();
			throw new ConnectException();
		}
	}

	private void getConnectionParametersForClient() throws ConnectException {
		clientPort = portClientTextField.getText();
		ipAdress = ipAdressClientTextField.getText();
		if (!validatePort(clientPort) || !validateIp(ipAdress)) {
			errorInfoDisplay.wrongPortOrIpAdress();
			throw new ConnectException();
		}
	}

	private boolean validatePort(String port) {
		Pattern pattern = Pattern.compile(regexForPort);
		Matcher matcher = pattern.matcher(port);
		return matcher.matches();
	}

	private boolean validateIp(String adressIp) {
		Pattern pattern = Pattern.compile(regexForIP);
		Matcher matcher = pattern.matcher(adressIp);
		return matcher.matches();
	}
}
