package main;

import javafx.application.Application;
import javafx.stage.Stage;
import person.viewController.PersonC;
import serial.Catalog;
import java.io.IOException;

public class TheMain extends Application {
  Catalog catalog;
  
  @Override
  public void init() {
    catalog = new Catalog();
    catalog.restore();
  }
  
  @Override
  public void start(Stage stage) throws IOException {
    PersonC.show(stage, catalog);
  }
  
  @Override
  public void stop() throws Exception {
    super.stop();
    catalog.persist();
  }
  
  public static void main(String[] args) {
    launch(args);
  }
}
