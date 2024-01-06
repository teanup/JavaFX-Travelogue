package eu.telecomnancy.carnet;

import lombok.Setter;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import eu.telecomnancy.carnet.model.Carnet;
import eu.telecomnancy.carnet.model.DayPage;

public class PageViewController {
  private DayPage page;
  private int index;
  @Setter
  private Carnet carnet;
  @Setter
  private MainController mainController;
  @FXML
  private MenuItem precedentMenuItem, suivantMenuItem, modifierPageMenuItem;
  @FXML
  private Label titleLabel, dateLabel;
  @FXML
  private Text descriptionText;
  @FXML
  private ImageView imageView;
  @FXML
  private Button previousButton, nextButton;

  @FXML
  private void initializeMenuItems() {
    precedentMenuItem.setOnAction(event -> onPrecedent());
    suivantMenuItem.setOnAction(event -> onSuivant());
    modifierPageMenuItem.setOnAction(event -> onModifier());
  }

  @FXML
  public void initialize() {
    if (carnet == null) {
      titleLabel.setText("Aucun carnet ouvert");
      dateLabel.setText("");
      descriptionText.setText("");
      imageView.setImage(new Image(Main.DEFAULT_PHOTO_PATH));
      return;
    }

    page = carnet.getPage(index);
    titleLabel.setText(page.getTitle());
    dateLabel.setText("Jour " + (index + 1) + " - " + carnet.getDayDate(index).toString());
    descriptionText.setText(page.getDescription());
    try {
      imageView.setImage(new Image(Main.photoPath(page.getPhotoPath())));
    } catch (Exception e) {
      mainController.showAlert("Erreur", "Impossible de charger l'image", AlertType.ERROR);
      e.printStackTrace();
    }

    if (index == 0) {
      previousButton.setDisable(true);
      precedentMenuItem.setDisable(true);
    } else {
      previousButton.setDisable(false);
      precedentMenuItem.setDisable(false);
    }

    if (index == carnet.getDays() - 1) {
      nextButton.setDisable(true);
      suivantMenuItem.setDisable(true);
    } else {
      nextButton.setDisable(false);
      suivantMenuItem.setDisable(false);
    }
  }

  public void setIndex(int index) {
    this.index = index;
    initialize();
  }

  public void setMenuItems(MenuItem precedentMenuItem, MenuItem suivantMenuItem, MenuItem modifierPageMenuItem) {
    this.precedentMenuItem = precedentMenuItem;
    this.suivantMenuItem = suivantMenuItem;
    this.modifierPageMenuItem = modifierPageMenuItem;
    initializeMenuItems();
  }

  @FXML
  private void onModifier() {
    PageDialogController controller = mainController.openPageDialog("Modifier la page " + (index + 1), index);
    if (controller == null) {
      return;
    }

    DayPage updatedPage = createPageFromDialog(controller);
    if (updatedPage == null) {
      return;
    }

    this.page = updatedPage;
    carnet.setPage(index, updatedPage);
    initialize();
  }

  private DayPage createPageFromDialog(PageDialogController controller) {
    String title, description, photoPath;
    DayPage newPage = null;

    try {
      title = controller.getTitle();
      description = controller.getDescription();
      photoPath = controller.getPhotoPath();
      newPage = new DayPage(title, description, photoPath);
    } catch (Exception e) {
      mainController.showAlert("Erreur", "Champs invalides ou manquants", AlertType.ERROR);
      e.printStackTrace();
    }

    return newPage;
  }

  @FXML
  private void onPrecedent() {
    setIndex(index - 1);
  }

  @FXML
  private void onSuivant() {
    setIndex(index + 1);
  }
}
