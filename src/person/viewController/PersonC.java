package person.viewController;


import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import person.model.Geschlecht;
import person.model.Person;
import serial.Catalog;

import java.io.IOException;
import java.text.NumberFormat;


public class PersonC {
  @FXML
  public GridPane gpKey;
  @FXML
  public VBox vBox;
  @FXML
  public TextField tfSvnr;
  @FXML
  public GridPane gpNonKey;
  @FXML
  public TextField tfNname;
  @FXML
  public TextField tfVname;
  @FXML
  public ChoiceBox cbGeschlecht;
  @FXML
  public DatePicker dpGebDat;
  @FXML
  public TextField tfGroesse;
  @FXML
  public Button btCancel;
  @FXML
  public Button btSave;
  @FXML
  public HBox hbButtons;
  @FXML
  public TextField tfMsg;
  private Person model;

  private static final NumberFormat NUMBERFORMAT_2DECIMALS;
  
  
  static {
    NUMBERFORMAT_2DECIMALS = NumberFormat.getNumberInstance();
    NUMBERFORMAT_2DECIMALS.setMaximumFractionDigits(2);
    NUMBERFORMAT_2DECIMALS.setMinimumFractionDigits(2);
  }

  public PersonC() {
    this.model = new Person();
  }
  
  
  public static void show(Stage stage, Catalog catalog) {
    try {
      Person.setCatalog(catalog);

      PersonC personC = new PersonC();

      FXMLLoader loader = new FXMLLoader(
              PersonC.class.getResource("PersonV.fxml")
      );

      Parent root = (Parent) loader.load();


      Scene scene = new Scene(root);
      stage.setTitle("Personenwartung");
      stage.setScene(scene);
      stage.show();
    } catch(Exception e){
      e.printStackTrace();
    }

  }
  
  @FXML
  public void initialize() {
    tfSvnr.focusedProperty().addListener(
        (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
          if (oldValue && tfSvnr.getText() != null && tfSvnr.getText().trim().length() != 0) {
            select();
          }
        });

    cbGeschlecht.setItems(Geschlecht.valuesAsObservableList());

    showSuccessMessage("SVNR eingeben!");

    reset_display();
  }
  

  public void reset_display() {
    setModel(new Person());
    fullDisplay(false);
  }
  

  public void setModel(Person person) {
    model = person;

    if (model != null) {
      tfSvnr.setText(model.getSvnr());
      tfNname.setText(model.getNname());
      tfVname.setText(model.getVname());
      dpGebDat.setValue(model.getGebDat());
      tfGroesse.setText(model.getGroesse() != null ?
          NUMBERFORMAT_2DECIMALS.format(model.getGroesse()) :
          "");
      cbGeschlecht.setValue(model.getGeschlecht());
    }
  }
  

  public void fullDisplay(boolean full) {
    tfSvnr.setEditable(!full);
    gpNonKey.setVisible(full);
    btSave.setVisible(full);
    btCancel.setVisible(full);
    
    if (full) {
      tfNname.requestFocus();
    }
    else {
      tfSvnr.requestFocus();
    }
  }
  

  public void select() {
    try {
      Person person = Person.selectBySvnr(tfSvnr.getText());
      
      if (person != null) {
        setModel(person);
        showSuccessMessage("Daten selektiert!");
      }
      else {
        showSuccessMessage("Daten erfassen!");
      }

      fullDisplay(true);
    }
    catch (Exception ex) {
      showErrorMessage(ex.toString());
    }
  }

  @FXML
  public void save() {
    try {
      model.setSvnr(tfSvnr.getText());
      model.setNname(tfNname.getText());
      model.setVname(tfVname.getText());
      model.setGroesse(NUMBERFORMAT_2DECIMALS.parse(tfGroesse.getText()).doubleValue());
      model.setGebDat(dpGebDat.getValue());
      model.setGeschlecht((Geschlecht) cbGeschlecht.getValue());

      model.save();
      
      showSuccessMessage("Daten gesichert!");

      reset_display();
    }
    catch (Exception ex) {
      showErrorMessage(ex.getMessage());
    }
  }
  

  @FXML
  public void cancel() {
    reset_display();
    showSuccessMessage("Vorgang abgebrochen!");
  }
  

  public void showErrorMessage(String message) {
    tfMsg.setText(message);
    tfMsg.setStyle("-fx-text-inner-color: red;");
  }
  

  public void showSuccessMessage(String message) {
    tfMsg.setText(message);
    tfMsg.setStyle("-fx-text-inner-color: green;");
  }
}
