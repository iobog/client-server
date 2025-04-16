package concurs.client;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProbaRestClient {

  private static final String BASE_URL = "http://localhost:8081/concurs/probe";

  public static void main(String[] args) throws IOException {
    System.out.println("=== GET ALL ===");
    sendGetRequest();

    System.out.println("\n=== POST NEW ===");
    String newProbaId = sendPostRequest();

    System.out.println("\n=== PUT UPDATE ===");
    sendPutRequest(newProbaId);

    System.out.println("\n=== DELETE ===");
    sendDeleteRequest(newProbaId);
  }

  private static void sendGetRequest() throws IOException {
    HttpURLConnection con = (HttpURLConnection) new URL(BASE_URL).openConnection();
    con.setRequestMethod("GET");

    int responseCode = con.getResponseCode();
    System.out.println("GET Response Code: " + responseCode);
    printResponse(con);
  }

  private static String sendPostRequest() throws IOException {
    HttpURLConnection con = (HttpURLConnection) new URL(BASE_URL).openConnection();
    con.setRequestMethod("POST");
    con.setRequestProperty("Content-Type", "application/json");
    con.setDoOutput(true);

    String jsonInput = """
            {
              "nume": "Altceva",
              "categorieVarsta": "1-25",
              "numarParticipanti": 2
            }
            """;

    try (OutputStream os = con.getOutputStream()) {
      os.write(jsonInput.getBytes());
      os.flush();
    }

    int responseCode = con.getResponseCode();
    System.out.println("POST Response Code: " + responseCode);
    String response = readStream(con.getInputStream());
    System.out.println(response);

    // Extract ID (assuming JSON response like {"id":1,...})
    int idStart = response.indexOf("\"id\":") + 5;
    int idEnd = response.indexOf(",", idStart);
    if (idEnd == -1) idEnd = response.indexOf("}", idStart);
    return response.substring(idStart, idEnd).trim();
  }

  private static void sendPutRequest(String id) throws IOException {
    HttpURLConnection con = (HttpURLConnection) new URL(BASE_URL + "/" + id).openConnection();
    con.setRequestMethod("PUT");
    con.setRequestProperty("Content-Type", "application/json");
    con.setDoOutput(true);

    String jsonUpdate = """
            {
              "nume": "Alergare 200m",
              "categorieVarsta": "18-25",
              "numarParticipanti": 12
            }
            """;

    try (OutputStream os = con.getOutputStream()) {
      os.write(jsonUpdate.getBytes());
      os.flush();
    }

    int responseCode = con.getResponseCode();
    System.out.println("PUT Response Code: " + responseCode);
    printResponse(con);
  }

  private static void sendDeleteRequest(String id) throws IOException {
    HttpURLConnection con = (HttpURLConnection) new URL(BASE_URL + "/" + id).openConnection();
    con.setRequestMethod("DELETE");

    int responseCode = con.getResponseCode();
    System.out.println("DELETE Response Code: " + responseCode);
  }

  private static void printResponse(HttpURLConnection con) throws IOException {
    InputStream is = con.getInputStream();
    System.out.println(readStream(is));
  }

  private static String readStream(InputStream stream) throws IOException {
    try (BufferedReader in = new BufferedReader(new InputStreamReader(stream))) {
      String inputLine;
      StringBuilder response = new StringBuilder();
      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      return response.toString();
    }
  }
}
