package eu.telecomnancy.carnet;

import lombok.Getter;
import lombok.Setter;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import eu.telecomnancy.carnet.model.*;

public class GlobalViewController {
  @Getter
  private Carnet carnet;
  @Setter
  private MainController mainController;
  @FXML
  private Label titleLabel, authorLabel, datesLabel, participantsLabel;
  @FXML
  private GridPane pagesGridPane;

  @FXML
  public void initialize() {
    if (carnet == null) {
      titleLabel.setText("Aucun carnet ouvert");
      authorLabel.setText("");
      datesLabel.setText("");
      participantsLabel.setText("");
      return;
    }

    titleLabel.setText(carnet.getTitle());
    authorLabel.setText("Par : " + carnet.getAuthor());
    datesLabel.setText(carnet.getStartDate().toString() + " - " + carnet.getEndDate().toString());
    participantsLabel.setText("Avec : " + String.join(", ", carnet.getParticipants()));

    for (int i = 0; i < carnet.getDays(); i++) {
      final int index = i;
      DayPage page = carnet.getPage(index);
      BackgroundImage photo = new BackgroundImage(new Image(Main.photoPath(page.getPhotoPath())),
          BackgroundRepeat.NO_REPEAT,
          BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
          new BackgroundSize(180, 180, false, false, false, false));
      Background background = new Background(photo);

      InnerShadow is = new InnerShadow(0.0f, 0.0f, 2.0f, Color.web("#222c"));
      Label label = new Label("Jour " + (index + 1));
      label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #dddc;");
      label.setEffect(is);

      Button button = new Button();
      button.setBackground(background);
      button.setGraphic(label);
      button.setPrefSize(200, 160);
      button.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
      button.setOnAction(event -> mainController.loadPageView(index));

      VBox buttonSpace = new VBox(button);
      buttonSpace.setPrefSize(240, 180);
      buttonSpace.setAlignment(javafx.geometry.Pos.BOTTOM_CENTER);

      pagesGridPane.add(buttonSpace, i % 4, i / 4);
    }
  }

  public void setCarnet(Carnet carnet) {
    this.carnet = carnet;
    initialize();
  }
}
