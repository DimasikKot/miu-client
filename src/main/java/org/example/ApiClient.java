package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.ClientManifest;
import org.example.model.UpdateResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class ApiClient {

    private static final String SERVER =
            "http://fundata.mooo.com:10033";

    private static final ObjectMapper MAPPER =
            new ObjectMapper();

    private static final HttpClient CLIENT =
            HttpClient.newHttpClient();

    private ApiClient() {
    }

    public static UpdateResponse check(
            String pack,
            ClientManifest manifest
    ) throws IOException, InterruptedException {

        String json =
                MAPPER.writeValueAsString(manifest);

        HttpRequest request =
                HttpRequest.newBuilder()

                        .uri(
                                URI.create(
                                        SERVER +
                                                "/update/" +
                                                pack
                                )
                        )

                        .header(
                                "Content-Type",
                                "application/json"
                        )

                        .POST(
                                HttpRequest.BodyPublishers
                                        .ofString(json)
                        )

                        .build();

        HttpResponse<String> response =
                CLIENT.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() != 200) {

            throw new IOException(
                    "Server returned "
                            + response.statusCode()
            );

        }

        return MAPPER.readValue(
                response.body(),
                UpdateResponse.class
        );

    }

}