package eu.telecomnancy.carnet.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class CarnetTest {
  private String currentTestName;
  private Carnet carnet;
  private LocalDate startDate;
  private LocalDate endDate;

  @BeforeEach
  public void setUp() {
    startDate = LocalDate.of(2023, 12, 12);
    endDate = LocalDate.of(2023, 12, 23);
    carnet = new Carnet("title", "author", startDate, endDate);
  }

  @AfterEach
  public void print() {
    System.out.println("-- " + this.getClass().getSimpleName() + "." + currentTestName + " --");
    System.out.println(carnet);
  }

  @Test
  public void testConstructor() {
    currentTestName = "testConstructor";
    assertEquals("title", carnet.getTitle());
    assertEquals("author", carnet.getAuthor());
    assertEquals(startDate, carnet.getStartDate());
    assertEquals(endDate, carnet.getEndDate());
    assertTrue(carnet.getParticipants().isEmpty());
    assertEquals(12, carnet.getDays());
  }

  @Test
  public void testAddParticipant() {
    currentTestName = "testAddParticipant";
    carnet.addParticipant("participant1");
    List<String> participants = carnet.getParticipants();
    assertEquals(1, participants.size());
    assertEquals("participant1", participants.get(0));
  }

  @Test
  public void testRemoveParticipant() {
    currentTestName = "testRemoveParticipant";
    carnet.addParticipant("participant1");
    carnet.removeParticipant("participant1");
    assertTrue(carnet.getParticipants().isEmpty());
  }

  @Test
  public void testSetPage() {
    currentTestName = "testSetPage";
    DayPage page = new DayPage("title", "description", "photoPath");
    carnet.setPage(3, page);
    List<DayPage> pages = carnet.getPages();
    assertEquals(carnet.getDays(), pages.size());
    assertEquals(page, pages.get(3));
  }

  @Test
  public void testJsonConstructor() throws JSONException {
    currentTestName = "testJsonConstructor";
    JSONObject json = new JSONObject();
    json.put("title", "title");
    json.put("author", "author");
    json.put("startDate", startDate.toEpochDay());
    json.put("endDate", endDate.toEpochDay());
    json.put("participants", new JSONArray());
    json.put("pages", new JSONArray());

    Carnet carnetFromJson = Carnet.fromJSON(json);
    assertEquals("title", carnetFromJson.getTitle());
    assertEquals("author", carnetFromJson.getAuthor());
    assertEquals(startDate, carnetFromJson.getStartDate());
    assertEquals(endDate, carnetFromJson.getEndDate());
    assertTrue(carnetFromJson.getParticipants().isEmpty());
  }

  @Test
  public void testAddRealParticipant() {
    currentTestName = "testAddRealParticipant";
    carnet.addParticipant("John Doe");
    List<String> participants = carnet.getParticipants();
    assertEquals(1, participants.size());
    assertEquals("John Doe", participants.get(0));
  }

  @Test
  public void testAddRealPage() {
    currentTestName = "testAddRealPage";
    DayPage page = new DayPage("A Day in the Life", "This is a description of a day in the life of John Doe.",
        "/path/to/photo.jpg");
    carnet.setPage(1, page);
    List<DayPage> pages = carnet.getPages();
    assertEquals(carnet.getDays(), pages.size());
    assertEquals(page, pages.get(1));
  }

  @Test
  public void testJsonConstructorWithRealContent() throws JSONException {
    currentTestName = "testJsonConstructorWithRealContent";
    JSONObject json = new JSONObject();
    json.put("title", "A Day in the Life");
    json.put("author", "John Doe");
    json.put("startDate", startDate.toEpochDay());
    json.put("endDate", endDate.toEpochDay());
    JSONArray participants = new JSONArray();
    participants.put("John Doe");
    json.put("participants", participants);
    JSONObject pageJson = new JSONObject();
    pageJson.put("title", "A Day in the Life");
    pageJson.put("description", "This is a description of a day in the life of John Doe.");
    pageJson.put("photoPath", "/path/to/photo.jpg");
    JSONArray pages = new JSONArray();
    pages.put(pageJson);
    json.put("pages", pages);

    Carnet carnetFromJson = Carnet.fromJSON(json);
    assertEquals("A Day in the Life", carnetFromJson.getTitle());
    assertEquals("John Doe", carnetFromJson.getAuthor());
    assertEquals(startDate, carnetFromJson.getStartDate());
    assertEquals(endDate, carnetFromJson.getEndDate());
    assertEquals(1, carnetFromJson.getParticipants().size());
    assertEquals("John Doe", carnetFromJson.getParticipants().get(0));
    assertEquals(carnet.getDays(), carnetFromJson.getPages().size());
    assertEquals(pageJson.toString(), carnetFromJson.getPages().get(0).toString());
  }

  @Test
  public void testSaveAsJson() throws JSONException {
    currentTestName = "testSaveAsJson";
    carnet.addParticipant("John Doe");
    DayPage page = new DayPage("A Day in the Life", "This is a description of a day in the life of John Doe.",
        "/path/to/photo.jpg");
    carnet.setPage(1, page);
    try {
      carnet.saveAsJSON("src/test/json/carnet.json");
    } catch (IOException e) {
      fail("IOException: " + e.getMessage());
    }

    Carnet carnetFromJson = null;
    try {
      carnetFromJson = Carnet.loadFromJSON("src/test/json/carnet.json");
    } catch (IOException e) {
      fail("IOException: " + e.getMessage());
    }
    assertEquals("title", carnetFromJson.getTitle());
    assertEquals("author", carnetFromJson.getAuthor());
    assertEquals(startDate, carnetFromJson.getStartDate());
    assertEquals(endDate, carnetFromJson.getEndDate());
    assertEquals(1, carnetFromJson.getParticipants().size());
    assertEquals("John Doe", carnetFromJson.getParticipants().get(0));
    assertEquals(carnet.getDays(), carnetFromJson.getPages().size());
    assertEquals("{\"photoPath\":\"\",\"description\":\"\",\"title\":\"\"}",
        carnetFromJson.getPages().get(0).toString());
    assertEquals(page.toString(), carnetFromJson.getPages().get(1).toString());
  }

}
