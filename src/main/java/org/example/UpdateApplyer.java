package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.example.logic.ServersDat;
import org.example.model.FileDownloadInfo;
import org.example.model.ServerInfo;
import org.example.model.UpdatePostResponse;

public final class UpdateApplyer {
  private UpdateApplyer() {
  }

  private static void cleanupEmptyParents(Path root, Path current) throws IOException {
    while (current != null && !current.equals(root)) {
      try (var stream = Files.list(current)) {
        if (stream.findAny().isPresent()) {
          return;
        }
      }

      Files.delete(current);
      current = current.getParent();
    }
  }

  public static void apply(Path instance, String pack, UpdatePostResponse response) throws Exception {
    System.out.println();
    System.out.println("=== Applying update ===");
    System.out.println();

    deleteFiles(instance, response);
    downloadFiles(instance, pack, response);

    // TODO Особая обработка mmc-pack.json

    OptionsWriter.writeResourcepacks(instance, response.getNew_resourcepacks());
    OptionsWriter.writeIncompatibleResourcepacks(instance, response.getNew_incompatible_resourcepacks());

    ServersDat.replaceServers(instance.resolve("minecraft/servers.dat"), response.getNew_servers());

    System.out.println("[OK] new_servers:");
    for (ServerInfo server : response.getNew_servers()) {
      System.out.printf("  - %s (%s)%n", server.getName(), server.getIp());
    }

    System.out.println();
    System.out.println("Update completed successfully.");
    System.out.println();
  }

  private static void deleteFiles(Path instance, UpdatePostResponse response) throws IOException {
    if (response.getNeed_delete().isEmpty()) {
      System.out.println("Nothing to delete.");
      return;
    }

    System.out.println("Deleting files...");
    ProgressWindow.setStatus("Удаление старых файлов...");

    for (String relative : response.getNeed_delete()) {
      Path file = instance.resolve(relative);

      if (!Files.exists(file)) continue;

      Files.delete(file);
      cleanupEmptyParents(instance, file.getParent());

      System.out.println("[OK] Deleted: " + relative);
    }
  }

  private static void downloadFiles(Path instance, String pack, UpdatePostResponse response) throws Exception {
    Map<String, FileDownloadInfo> needDownload = response.getNeed_download();

    if (needDownload.isEmpty()) {
      System.out.println("Nothing to download.");
      return;
    } else {
      startHelper(instance, pack);
      System.out.println(
          "[UPDATE] Update required. Stopping Minecraft launch."
      );
      System.exit(1);
    }

    System.out.println("Downloading files...");
    ProgressWindow.setStatus("Загрузка файлов...");

    int currentCount = 0;
    int allCount = needDownload.size();

    long totalBytes = needDownload.values().stream().mapToLong(FileDownloadInfo::getSize).sum();
    long downloadedBytes = 0;
    long start = System.nanoTime();

    for (Map.Entry<String, FileDownloadInfo> entry : needDownload.entrySet()) {
      String path = entry.getKey();
      FileDownloadInfo fileInfo = entry.getValue();

      Downloader.downloadFile(instance, path, fileInfo);

      currentCount++;
      ProgressWindow.setFile(path);
      ProgressWindow.setProgress(currentCount, allCount);

      downloadedBytes += fileInfo.getSize();

      ProgressWindow.setDownloadedBytes(downloadedBytes, totalBytes);

      long elapsed = System.nanoTime() - start;
      double seconds = elapsed / 1_000_000_000.0;
      double speed = downloadedBytes / 1024d / 1024d / seconds;

      ProgressWindow.setSpeed(speed);
    }
  }

  private static void startHelper(Path instance, String pack) throws IOException {
    Path helperJar = instance.resolve("miu-client-helper.jar");

    if (!Files.exists(helperJar)) {
      throw new IOException(
          "Helper JAR not found: " + helperJar
      );
    }

    System.out.println("[UPDATE] Starting helper...");

    new ProcessBuilder(
        "java",
        "-jar",
        helperJar.toString(),
        instance.toString(),
        pack
    )
        .directory(instance.toFile())
        .inheritIO()
        .start();

    System.out.println("[UPDATE] Helper started.");
  }
}