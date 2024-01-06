package eu.telecomnancy.carnet;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
  public static final String APP_NAME = "Carnet de Voyage";
  public static final String DEFAULT_PHOTO_PATH = Main.class.getResource("/eu/telecomnancy/carnet/images/default.png")
      .toExternalForm();

  public static String photoPath(String path) {
    if (path.isEmpty()) {
      return DEFAULT_PHOTO_PATH;
    }

    File photoFile = new File(path);
    if (!photoFile.exists()) {
      return DEFAULT_PHOTO_PATH;
    }

    return photoFile.toURI().toString();
  }

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    primaryStage.setTitle("Carnet de Voyage");

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/eu/telecomnancy/carnet/Main.fxml"));
    Parent root = loader.load();

    MainController controller = loader.getController();
    controller.setPrimaryStage(primaryStage);

    Scene scene = new Scene(root, 1000, 800);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
