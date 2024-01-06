package eu.telecomnancy.carnet;

import lombok.Getter;
import lombok.Setter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import eu.telecomnancy.carnet.model.*;

public class MainController {
  @Getter
  @Setter
  private Stage primaryStage;
  private Carnet carnet;

  @FXML
  private MenuBar menuBar;
  @FXML
  private Menu nomCarnetMenu, vueMenu, pageMenu;
  @FXML
  private MenuItem modifierMenuItem, exporterMenuItem, modeGlobalMenuItem, modePageMenuItem, precedentMenuItem,
      suivantMenuItem, modifierPageMenuItem;
  @FXML
  private AnchorPane root;

  @FXML
  public void initialize() {
    loadGlobalView();
  }

  @FXML
  private void onNouveau() {
    InfoDialogController controller = openInfoDialog("Nouveau carnet");
    if (controller == null) {
      return;
    }

    Carnet newCarnet = createCarnetFromDialog(controller);
    if (newCarnet == null) {
      return;
    }

    setCarnet(newCarnet);
  }

  @FXML
  private void onOuvrir() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Ouvrir un carnet");
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers JSON", "*.json", "*.JSON"));

    File file = fileChooser.showOpenDialog(primaryStage);
    if (file != null) {
      try {
        carnet = Carnet.loadFromJSON(file.toPath().toString());
        setCarnet(carnet);
      } catch (Exception e) {
        showAlert("Erreur", "Impossible de charger le carnet", AlertType.ERROR);
        e.printStackTrace();
        return;
      }
    }
  }

  @FXML
  private void onQuitter() {
    primaryStage.close();
  }

  @FXML
  private void onModifier() {
    InfoDialogController controller = openInfoDialog("Modifier le carnet");
    if (controller == null) {
      return;
    }

    Carnet updatedCarnet = createCarnetFromDialog(controller);
    if (updatedCarnet == null) {
      return;
    }

    updatedCarnet.setPages(carnet.getPages());
    setCarnet(updatedCarnet);
  }

  @FXML
  private void onExporter() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Exporter un carnet");
    fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    fileChooser.setInitialFileName(carnet.getTitle() + ".json");
    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers JSON", "*.json", "*.JSON"));

    File file = fileChooser.showSaveDialog(primaryStage);
    if (file != null) {
      try {
        carnet.saveAsJSON(file.toPath().toString());
        showAlert("Succès", "Le carnet a été exporté vers " + file.toPath().toString(), AlertType.INFORMATION);
      } catch (Exception e) {
        showAlert("Erreur", "Impossible d'exporter le carnet", AlertType.ERROR);
        e.printStackTrace();
        return;
      }
    }
  }

  @FXML
  private void onModeGlobal() {
    loadGlobalView();
  }

  @FXML
  private void onModePage() {
    loadPageView();
  }

  @FXML
  public void loadGlobalView() {
    pageMenu.setDisable(true);
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/carnet/GlobalView.fxml"));
      Parent globalView = loader.load();

      GlobalViewController controller = loader.getController();
      controller.setMainController(this);
      controller.setCarnet(carnet);

      root.getChildren().clear();
      root.getChildren().add(globalView);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  public void loadPageView() {
    loadPageView(0);
  }

  @FXML
  public void loadPageView(int index) {
    pageMenu.setDisable(false);
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/carnet/PageView.fxml"));
      Parent pageView = loader.load();

      PageViewController controller = loader.getController();
      controller.setMainController(this);
      controller.setMenuItems(precedentMenuItem, suivantMenuItem, modifierPageMenuItem);
      controller.setCarnet(carnet);
      controller.setIndex(index);

      root.getChildren().clear();
      root.getChildren().add(pageView);
    } catch (IOException e) {
      showAlert("Erreur", "Impossible de charger la vue", AlertType.ERROR);
      e.printStackTrace();
      return;
    }
  }

  public void showAlert(String title, String content, AlertType type) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }

  private boolean showAndWaitForDialog(String title, DialogPane dialogPane) {
    Dialog<ButtonType> dialog = new Dialog<>();
    dialog.setDialogPane(dialogPane);
    dialog.setTitle(title);

    Optional<ButtonType> result = dialog.showAndWait();
    return result.isPresent() && result.get().getButtonData().equals(ButtonType.OK.getButtonData());
  }

  public InfoDialogController openInfoDialog(String title) {
    FXMLLoader dialogLoader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/carnet/InfoDialog.fxml"));

    DialogPane dialogPane;
    try {
      dialogPane = dialogLoader.load();
    } catch (IOException e) {
      showAlert("Erreur", "Impossible de charger la fenêtre de dialogue", AlertType.ERROR);
      e.printStackTrace();
      return null;
    }

    InfoDialogController infoController = dialogLoader.getController();
    if (carnet != null) {
      infoController.setCarnet(carnet);
    }

    if (!showAndWaitForDialog(title, dialogPane)) {
      return null;
    }

    return infoController;
  }

  public PageDialogController openPageDialog(String title, int index) {
    FXMLLoader dialogLoader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/carnet/PageDialog.fxml"));

    DialogPane dialogPane;
    try {
      dialogPane = dialogLoader.load();
    } catch (IOException e) {
      showAlert("Erreur", "Impossible de charger la fenêtre de dialogue", AlertType.ERROR);
      e.printStackTrace();
      return null;
    }

    PageDialogController pageController = dialogLoader.getController();
    pageController.setMainController(this);
    if (carnet != null) {
      pageController.setDayPage(carnet.getPage(index));
    }

    if (!showAndWaitForDialog(title, dialogPane)) {
      return null;
    }

    return pageController;
  }

  private Carnet createCarnetFromDialog(InfoDialogController controller) {
    String title, author;
    LocalDate startDate, endDate;
    List<String> participants;
    Carnet newCarnet = null;

    try {
      title = controller.getTitle();
      author = controller.getAuthor();
      startDate = controller.getStartDate();
      endDate = controller.getEndDate();
      participants = controller.getParticipants();
      newCarnet = new Carnet(title, author, startDate, endDate);
      newCarnet.setParticipants(participants);
    } catch (Exception e) {
      showAlert("Erreur", "Champs invalides ou manquants", AlertType.ERROR);
      e.printStackTrace();
    }

    return newCarnet;
  }

  private void setCarnet(Carnet carnet) {
    if (carnet.getDays() <= 0) {
      showAlert("Erreur", "Le carnet doit contenir au moins un jour", AlertType.ERROR);
      return;
    }
    if (carnet.getTitle().isEmpty()) {
      showAlert("Erreur", "Le carnet doit avoir un titre", AlertType.ERROR);
      return;
    }
    if (carnet.getAuthor().isEmpty()) {
      showAlert("Erreur", "Le carnet doit avoir un auteur", AlertType.ERROR);
      return;
    }

    this.carnet = carnet;

    nomCarnetMenu.setText(carnet.getTitle());
    nomCarnetMenu.setDisable(false);
    modifierMenuItem.setDisable(false);
    exporterMenuItem.setDisable(false);
    vueMenu.setDisable(false);
    modeGlobalMenuItem.setDisable(false);
    modePageMenuItem.setDisable(false);

    loadGlobalView();
  }
}
