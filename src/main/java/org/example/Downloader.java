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

  public static void download(Path instance, FileDownloadInfo file) throws IOException, InterruptedException {
    Path destination = instance.resolve(file.getPath());
    Files.createDirectories(destination.getParent());

    if (Files.exists(destination)) {
      String hash = HashUtil.sha256(destination);
      if (hash.equalsIgnoreCase(file.getSha256())) {
        System.out.println("[SKIP] " + file.getPath());
        return;
      }
    }

    Path temp = destination.resolveSibling(destination.getFileName() + ".download");
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(file.getUrl())).GET().build();
    HttpResponse<InputStream> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());

    if (response.statusCode() != 200) {
      throw new IOException("Download failed: " + response.statusCode() + " " + file.getUrl());
    }

    try (InputStream input = response.body()) {
      Files.copy(input, temp, StandardCopyOption.REPLACE_EXISTING);
    }

    String hash = HashUtil.sha256(temp);

    if (!hash.equalsIgnoreCase(file.getSha256())) {
      Files.deleteIfExists(temp);
      throw new IOException("SHA256 mismatch for " + file.getPath());
    }

    Files.move(temp, destination, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    System.out.println("[OK] Downloaded: " + file.getPath());
  }
}