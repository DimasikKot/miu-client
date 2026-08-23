package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.example.model.FileDownloadInfo;
import org.example.model.UpdatePostResponse;

public final class Updater {
  private Updater() {
  }

  public static void apply(Path instance, UpdatePostResponse response) throws Exception {
    System.out.println();
    System.out.println("=== Applying update ===");
    System.out.println();

    deleteFiles(instance, response);

    downloadFiles(instance, response);

    OptionsWriter.write(instance, response.getNew_resourcepacks());

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
      if (!Files.exists(file))
        continue;
      Files.delete(file);
      cleanupEmptyParents(instance, file.getParent());
      System.out.println("[OK] Deleted: " + relative);
    }
  }

  private static void downloadFiles(Path instance, UpdatePostResponse response) throws Exception {
    if (response.getNeed_download().isEmpty()) {
      System.out.println("Nothing to download.");
      return;
    }

    System.out.println("Downloading files...");
    ProgressWindow.setStatus("Загрузка файлов...");

    int current_count = 0;
    int all_count = response.getNeed_download().size();

    long totalBytes = response.getNeed_download().stream().mapToLong(FileDownloadInfo::getSize).sum();
    long downloadedBytes = 0;
    long start = System.nanoTime();

    for (FileDownloadInfo file : response.getNeed_download()) {
      Downloader.download(instance, file);

      current_count++;
      ProgressWindow.setFile(file.getPath());
      ProgressWindow.setProgress(current_count, all_count);

      downloadedBytes += file.getSize();

      ProgressWindow.setDownloadedBytes(downloadedBytes, totalBytes);
      long elapsed = System.nanoTime() - start;
      double seconds = elapsed / 1_000_000_000.0;
      double speed = downloadedBytes / 1024d / 1024d / seconds;
      ProgressWindow.setSpeed(speed);
    }
  }

  private static void cleanupEmptyParents(Path root, Path current) throws IOException {
    while (current != null && !current.equals(root)) {
      try (var stream = Files.list(current)) {
        if (stream.findAny().isPresent())
          return;
      }

      Files.delete(current);

      current = current.getParent();
    }
  }
}