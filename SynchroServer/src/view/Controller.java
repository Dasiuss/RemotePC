package view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

public class Controller implements Initializable {
	@FXML
	private TextArea textInput = new TextArea();

	@FXML
	private ListView<String> textMSG = new ListView<String>();
	ObservableList<String> textMSG_items = FXCollections.observableArrayList("Single", "Double", "Suite", "Family App");

	@FXML
	private ListView<String> textMSGIncoming = new ListView<String>();
	ObservableList<String> textMSGIncoming_items = FXCollections.observableArrayList("Single", "Double", "Suite", "Family App");

	
	@FXML
	private BorderPane mainLayout = new BorderPane();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		textMSG.setItems(textMSG_items);
		textMSGIncoming.setItems(textMSGIncoming_items);
		//handling enter key in textInput
		textInput.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (KeyCode.CONTROL.ENTER.equals(event.getCode()) && event.getTarget() instanceof TextArea)
					addTextToSendedList();
			}
		});	
	}

	final public void writeToTextArea() {
		textInput.appendText("hi!");
		textInput.setText("Witam");
		System.out.println(textInput.getText());
	}

	final public void addTextToSendedList() {
		System.out.println(textInput.getText());
		textMSG_items.add(textInput.getText());
		textInput.clear();

	}
	
	final public void addRecToSendedList(String msg) {
		System.out.println(textInput.getText());
		textMSGIncoming_items.add(msg);
		textMSGIncoming_items.add("");
		textInput.clear();

	}

}
