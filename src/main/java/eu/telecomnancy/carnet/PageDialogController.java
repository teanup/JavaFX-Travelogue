package eu.telecomnancy.carnet;

import lombok.Getter;
import lombok.Setter;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

import java.io.File;

import eu.telecomnancy.carnet.model.DayPage;

public class PageDialogController implements DialogController {
  @Getter
  private DayPage page;
  @Setter
  private MainController mainController;
  @FXML
  private TextField titleField, photoPathField;
  @FXML
  private TextArea descriptionField;

  public String getTitle() {
    return titleField.getText();
  }

  public void setTitle(String title) {
    titleField.setText(title);
  }

  public String getDescription() {
    return descriptionField.getText();
  }

  public void setDescription(String description) {
    descriptionField.setText(description);
  }

  public String getPhotoPath() {
    return photoPathField.getText();
  }

  public void setPhotoPath(String photoPath) {
    photoPathField.setText(photoPath);
  }

  public void setDayPage(DayPage page) {
    setTitle(page.getTitle());
    setDescription(page.getDescription());
    setPhotoPath(page.getPhotoPath());
  }

  @FXML
  private void onParcourir() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choisir une photo");
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.PNG", "*.jpg", "*.JPG",
        "*.jpeg", "*.JPEG", "*.gif", "*.GIF"));

    File file = fileChooser.showOpenDialog(mainController.getPrimaryStage());
    if (file != null) {
      try {
        setPhotoPath(file.toPath().toString());
      } catch (Exception e) {
        mainController.showAlert("Erreur", "Impossible de sélectionner cette image", AlertType.ERROR);
        e.printStackTrace();
        return;
      }
    }
  }

  @FXML
  private void onSupprimer() {
    setPhotoPath("");
  }
}
