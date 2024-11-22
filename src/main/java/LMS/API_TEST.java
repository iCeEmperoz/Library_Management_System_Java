package LMS;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class API_TEST {

  // Phương thức để gửi yêu cầu HTTP và nhận phản hồi
  public String getHttpResponse(String url) {
    try (HttpClient client = HttpClient.newHttpClient()) {
      System.out.println("Getting " + url);
      HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      int status = response.statusCode();
      if (status != 200) {
        throw new RuntimeException("Failed with HTTP error code : " + status);
      }
      return response.body();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  // Phương thức để chuyển đổi JSON thành danh sách sách
  public ArrayList<Book> getBooksFromJson(String json) {
    ArrayList<Book> books = new ArrayList<>();
    try {
      JsonNode node = new ObjectMapper().readTree(json);
      if (node.has("items")) {
        for (JsonNode item : node.get("items")) {
          JsonNode volumeInfo = item.get("volumeInfo");
          Book book = new Book();
          book.setTitle(volumeInfo.has("title") ? volumeInfo.get("title").asText() : "Unknown");
          book.setAuthor(
              volumeInfo.has("authors") ? volumeInfo.get("authors").get(0).asText() : "Unknown");
          book.setIsbn(volumeInfo.has("industryIdentifiers")
              && volumeInfo.get("industryIdentifiers").size() > 0 ? volumeInfo.get(
              "industryIdentifiers").get(0).get("identifier").asText() : "Unknown");
          book.setDescription(volumeInfo.has("description") ? volumeInfo.get("description").asText()
              : "No description available"); // Thay đổi sang description
          book.setPreviewLink(volumeInfo.has("previewLink") ? volumeInfo.get("previewLink").asText()
              : "No preview link available");
          JsonNode imageLinksNode = volumeInfo.get("imageLinks");
          book.setImageLink(
              imageLinksNode != null && imageLinksNode.has("smallThumbnail") ? imageLinksNode.get(
                  "smallThumbnail").asText() : "No image link available");
          books.add(book);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return books; // Trả về danh sách sách
  }

}
