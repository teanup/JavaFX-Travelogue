module eu.telecomnancy.carnet {
  requires org.json;
  requires static lombok;
  requires transitive javafx.controls;
  requires transitive javafx.fxml;

  opens eu.telecomnancy.carnet to javafx.fxml;

  exports eu.telecomnancy.carnet;
  exports eu.telecomnancy.carnet.model;
}
