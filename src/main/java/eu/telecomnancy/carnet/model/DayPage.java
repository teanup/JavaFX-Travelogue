package eu.telecomnancy.carnet.model;

import lombok.Getter;
import lombok.Setter;

import org.json.JSONObject;
import org.json.JSONException;

@Getter
@Setter
public class DayPage {
  private String title;
  private String description;
  private String photoPath;

  /**
   * Create a new day page
   * 
   * @param title       The title of the day page
   * @param description The description of the day page
   * @param photoPath   The path to the photo of the day page
   * @return A new day page
   */
  public DayPage(String title, String description, String photoPath) {
    this.title = title;
    this.description = description;
    this.photoPath = photoPath;
  }

  /**
   * Create a new day page from a JSON object
   * 
   * @param json The JSON object
   * @return A new day page
   */
  public DayPage(JSONObject json) throws JSONException {
    this.title = json.getString("title");
    this.description = json.getString("description");
    this.photoPath = json.getString("photoPath");
  }

  /**
   * Convert the day page to a JSON object
   * 
   * @return The JSON object
   */
  public JSONObject toJSON() throws JSONException {
    JSONObject json = new JSONObject();
    json.put("title", this.title);
    json.put("description", this.description);
    json.put("photoPath", this.photoPath);
    return json;
  }

  public String toString() {
    return this.toJSON().toString();
  }
}
