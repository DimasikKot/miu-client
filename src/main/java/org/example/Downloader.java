package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.example.model.FileDownloadInfo;

public final class Downloader {
  private static final HttpClient CLIENT = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();

  private Downloader() {
  }

  public static void downloadFile(Path instance, String path, FileDownloadInfo fileInfo)
      throws IOException, InterruptedException {
    Path destination = instance.resolve(path);
    Files.createDirectories(destination.getParent());

    if (Files.exists(destination)) {
      String hash = HashUtil.sha256(destination);
      if (hash.equalsIgnoreCase(fileInfo.getSha256())) {
        System.out.println("[PAST] " + path);
        return;
      }
    }

    Path temp = destination.resolveSibling(destination.getFileName() + ".download");
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(fileInfo.getUrl())).GET().build();
    HttpResponse<InputStream> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());

    if (response.statusCode() != 200) {
      throw new IOException("[PAST] Download failed: " + response.statusCode() + " " + fileInfo.getUrl());
    }

    try (InputStream input = response.body()) {
      Files.copy(input, temp, StandardCopyOption.REPLACE_EXISTING);
    }

    String hash = HashUtil.sha256(temp);

    if (!hash.equalsIgnoreCase(fileInfo.getSha256())) {
      Files.deleteIfExists(temp);
      throw new IOException("[PAST] SHA256 mismatch for " + path);
    }

    Files.move(temp, destination, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    System.out.println("[PAST] Downloaded: " + path);
  }
}