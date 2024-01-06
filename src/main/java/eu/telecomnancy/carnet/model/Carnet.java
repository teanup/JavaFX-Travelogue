package eu.telecomnancy.carnet.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

@Getter
@Setter
public class Carnet {
  private String title;
  private String author;
  private LocalDate startDate;
  private LocalDate endDate;
  private final int days;
  private List<String> participants;
  private List<DayPage> pages;

  /**
   * Create a new carnet
   * 
   * @param title     The title of the carnet
   * @param author    The author of the carnet
   * @param startDate The start LocalDate of the carnet
   * @param endDate   The end LocalDate of the carnet
   * @return A new carnet
   */
  public Carnet(String title, String author, LocalDate startDate, LocalDate endDate) {
    this.title = title;
    this.author = author;
    this.startDate = startDate;
    this.endDate = endDate;
    this.participants = new ArrayList<String>();

    this.days = (int) (endDate.toEpochDay() - startDate.toEpochDay()) + 1;
    this.pages = new ArrayList<DayPage>();
    for (int i = 0; i < days; i++) {
      this.pages.add(new DayPage("", "", ""));
    }
  }

  /**
   * Create a new carnet from a JSON object
   * 
   * @param json The JSON object
   * @return A new carnet
   */
  public static Carnet fromJSON(JSONObject json) throws JSONException {
    Carnet carnet = new Carnet(json.getString("title"), json.getString("author"),
        LocalDate.ofEpochDay(json.getLong("startDate")), LocalDate.ofEpochDay(json.getLong("endDate")));
    JSONArray participants = json.getJSONArray("participants");
    for (int i = 0; i < participants.length(); i++) {
      carnet.addParticipant(participants.getString(i));
    }
    JSONArray pages = json.getJSONArray("pages");
    for (int i = 0; i < pages.length(); i++) {
      carnet.setPage(i, new DayPage(pages.getJSONObject(i)));
    }
    return carnet;
  }

  /**
   * Create a new carnet from a JSON file
   * 
   * @param path The path to the JSON file
   * @return A new carnet
   */
  public static Carnet loadFromJSON(String path) throws IOException, JSONException {
    File file = new File(path);
    String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
    JSONObject json = new JSONObject(content);
    return Carnet.fromJSON(json);
  }

  /**
   * Add a participant to the carnet
   * 
   * @param participant The participant to add
   */
  public void addParticipant(String participant) {
    this.participants.add(participant);
  }

  /**
   * Remove a participant from the carnet
   * 
   * @param participant The participant to remove
   */
  public void removeParticipant(String participant) {
    this.participants.remove(participant);
  }

  /**
   * Get a page from the carnet
   * 
   * @param index The index of the page
   */
  public DayPage getPage(int index) {
    return this.pages.get(index);
  }

  /**
   * Set a page of the carnet
   * 
   * @param index The index of the page
   * @param page  The page to add
   */
  public void setPage(int index, DayPage page) {
    this.pages.set(index, page);
  }

  /**
   * Set the pages of the carnet
   * 
   * @param pages the pages to set
  */
  public void setPages(List<DayPage> pages) {
    for (int i = 0; i < days && i < pages.size(); i++) {
      DayPage page = pages.get(i);
      if (page != null) {
        this.setPage(i, page);
      }
    }
  }

  /**
   * Get the LocalDate of a day of the carnet
   * 
   * @param index The index of the day
   * @return The LocalDate of the day
   */
  public LocalDate getDayDate(int index) {
    return this.startDate.plusDays(index);
  }

  /**
   * Get the JSON representation of the carnet
   * 
   * @return The JSON representation of the carnet
   */
  public JSONObject toJSON() throws JSONException {
    JSONObject json = new JSONObject();
    json.put("title", this.title);
    json.put("author", this.author);
    json.put("startDate", this.startDate.toEpochDay());
    json.put("endDate", this.endDate.toEpochDay());

    JSONArray participants = new JSONArray();
    for (String participant : this.participants) {
      participants.put(participant);
    }
    json.put("participants", participants);

    JSONArray pages = new JSONArray();
    for (int i = 0; i < this.days; i++) {
      DayPage page = this.pages.get(i);
      if (page == null) {
        page = new DayPage("", "", "");
      }
      pages.put(page.toJSON());
    }
    json.put("pages", pages);
    return json;
  }

  /**
   * Save the carnet as a JSON file
   * 
   * @param path The path to the JSON file
   */
  public void saveAsJSON(String path) throws IOException {
    File file = new File(path);
    File parent = file.getParentFile();
    if (parent != null) {
      parent.mkdirs();
    }
    file.createNewFile();

    FileWriter writer = new FileWriter(file);
    writer.write(this.toJSON().toString());
    writer.flush();
    writer.close();
  }

  public String toString() {
    return this.toJSON().toString();
  }
}
