package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;

import org.example.model.UpdatePostRequest;
import org.example.model.UpdatePostResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class ApiClient {
  private static final ObjectMapper MAPPER = new ObjectMapper();

  private static final HttpClient CLIENT = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();

  private ApiClient() {
  }

  public static UpdatePostResponse check(String pack, UpdatePostRequest manifest, Path instance) throws IOException, InterruptedException {

    String json = MAPPER.writeValueAsString(manifest);

    String encodedPack = URLEncoder.encode(pack, StandardCharsets.UTF_8).replace("+", "%20");

    IOException lastException = null;

    for (String server : Config.getServers(instance)) {
      try {
        System.out.println("[MIU] Trying server: " + server);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(server + "/api/v2/update/" + encodedPack))
            .timeout(Duration.ofSeconds(10))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json))
            .build();
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
          System.out.println("[MIU] Connected to: " + server);

          return MAPPER.readValue(response.body(), UpdatePostResponse.class);
        }

        System.out.println("[MIU] Server returned " + response.statusCode());

      } catch (IOException e) {
        lastException = e;

        System.out.println("[MIU] Server unavailable: " + server);
      }
    }

    throw new IOException("All MIU servers are unavailable", lastException);
  }
}