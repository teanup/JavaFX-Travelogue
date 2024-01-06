package eu.telecomnancy.carnet;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import eu.telecomnancy.carnet.model.Carnet;

public class InfoDialogController implements DialogController {
  @FXML
  private TextField titleField, authorField, newParticipantField;
  @FXML
  private DatePicker startDatePicker, endDatePicker;
  @FXML
  private ListView<String> participantsList;

  public String getTitle() {
    return titleField.getText();
  }

  @FXML
  public void setTitle(String title) {
    titleField.setText(title);
  }

  public String getAuthor() {
    return authorField.getText();
  }

  public void setAuthor(String author) {
    authorField.setText(author);
  }

  public LocalDate getStartDate() {
    return startDatePicker.getValue();
  }

  public void setStartDate(LocalDate startDate) {
    startDatePicker.setValue(startDate);
  }

  public LocalDate getEndDate() {
    return endDatePicker.getValue();
  }

  public void setEndDate(LocalDate endDate) {
    endDatePicker.setValue(endDate);
  }

  public List<String> getParticipants() {
    return new ArrayList<String>(participantsList.getItems());
  }

  public void setParticipants(List<String> participants) {
    participantsList.getItems().clear();
    participantsList.getItems().addAll(participants);
  }

  public void setCarnet(Carnet carnet) {
    setTitle(carnet.getTitle());
    setAuthor(carnet.getAuthor());
    setStartDate(carnet.getStartDate());
    setEndDate(carnet.getEndDate());
    setParticipants(carnet.getParticipants());
  }

  @FXML
  private void addParticipant() {
    String newParticipant = newParticipantField.getText();
    if (!newParticipant.isEmpty() && !participantsList.getItems().contains(newParticipant)) {
      participantsList.getItems().add(newParticipant);
      newParticipantField.clear();
    }
  }

  @FXML
  private void removeParticipant() {
    String selectedParticipant = participantsList.getSelectionModel().getSelectedItem();
    if (selectedParticipant != null) {
      participantsList.getItems().remove(selectedParticipant);
    }
  }
}
