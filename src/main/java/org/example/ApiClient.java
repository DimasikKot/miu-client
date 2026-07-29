package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.ClientManifest;
import org.example.model.UpdateResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public final class ApiClient {
    private static final String SERVER = "http://fundata.mooo.com:10033";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    private ApiClient() {
    }

    public static UpdateResponse check(String pack, ClientManifest manifest) throws IOException, InterruptedException {
        String json = MAPPER.writeValueAsString(manifest);
        System.out.println(json);
        String encodedPack =
                URLEncoder.encode(
                                pack,
                                StandardCharsets.UTF_8
                        )
                        .replace("+", "%20");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(SERVER + "/update/" + encodedPack))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        if (response.statusCode() != 200) {
            throw new IOException("Server returned " + response.statusCode());
        }

        return MAPPER.readValue(response.body(), UpdateResponse.class);
    }
}