package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.example.logic.ServersDat;
import org.example.model.FileDownloadInfo;
import org.example.model.MiuClientGetResponse;
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

  public static void apply(Path instance, UpdatePostResponse response) throws Exception {
    System.out.println();
    System.out.println("=== Applying update ===");
    System.out.println();

    deleteFiles(instance, response);
    downloadFiles(instance, response);

    OptionsWriter.writeResourcepacks(instance, response.getNew_resourcepacks());
    OptionsWriter.writeIncompatibleResourcepacks(instance, response.getNew_incompatible_resourcepacks());

    ServersDat.replaceServers(instance.resolve("minecraft/servers.dat"), response.getNew_servers());

    System.out.println("[PAST] new_servers:");
    for (ServerInfo server : response.getNew_servers()) {
      System.out.printf("  - %s (%s)%n", server.getName(), server.getIp());
    }

    System.out.println();
    System.out.println("[MIU] Update completed successfully.");
    System.out.println();
  }

  private static void deleteFiles(Path instance, UpdatePostResponse response) throws IOException {
    if (response.getNeed_delete().isEmpty()) {
      System.out.println("[PAST] Nothing to delete.");
      return;
    }

    System.out.println("[PAST] Deleting files...");
    ProgressWindow.setStatus("Удаление старых файлов...");

    for (String relative : response.getNeed_delete()) {
      Path file = instance.resolve(relative);

      if (!Files.exists(file)) continue;

      Files.delete(file);
      cleanupEmptyParents(instance, file.getParent());

      System.out.println("[PAST] Deleted: " + relative);
    }
  }

  private static void downloadFiles(Path instance, UpdatePostResponse response) throws Exception {
    Map<String, FileDownloadInfo> needDownload = response.getNeed_download();

    if (needDownload.isEmpty()) {
      System.out.println("[PAST] Nothing to download.");
      return;
    }

    System.out.println("[PAST] Downloading files...");
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

  public static boolean isFileChanged(Path instance, String path, FileDownloadInfo fileInfo) throws IOException {
    Path file = instance.resolve(path);
    if (!Files.exists(file)) {
      return true;
    }

    String localSha256 = HashUtil.sha256(file);

    return !localSha256.equalsIgnoreCase(fileInfo.getSha256());
  }

  public static boolean isMmcFileChanged(Path instance, String path, FileDownloadInfo fileInfo) throws IOException {
    Path file = instance.resolve(path);
    if (!Files.exists(file)) {
      return true;
    }

    String localSha256;
    try {
      localSha256 = HashUtil.normalizedSha256(file);
    } catch (NoSuchAlgorithmException e) {
      throw new IOException(e);
    }

    return !localSha256.equalsIgnoreCase(fileInfo.getSha256());
  }

  public static boolean isPreLaunchCommandOld(Path instance, String filePath) throws IOException {
    Path config = instance.resolve("instance.cfg");

    if (!Files.exists(config)) {
      return true;
    }

    for (String line : Files.readAllLines(config)) {
      if (!line.startsWith("PreLaunchCommand=")) {
        continue;
      }

      String actual = line.substring("PreLaunchCommand=".length());
      return !isThirdTokenContains(actual, filePath);
    }

    // Параметра нет в конфиге
    return true;
  }

  /**
   * Проверяет, что 3-й токен команды содержит указанную подстроку (например, имя jar-файла)
   * в любой позиции. Токены разделяются пробелами (с учётом кавычек).
   */
  private static boolean isThirdTokenContains(String command, String expected) {
    if (command == null || expected == null || expected.isEmpty()) {
      return false;
    }

    List<String> tokens = tokenize(command);
    if (tokens.size() < 3) {
      return false;
    }

    String third = tokens.get(2);
    return third != null && third.contains(expected);
  }

  /**
   * Простой токенизатор с поддержкой кавычек ("...") — пробелы внутри кавычек не разделяют.
   */
  private static List<String> tokenize(String command) {
    List<String> tokens = new ArrayList<>();
    StringBuilder current = new StringBuilder();
    boolean inQuotes = false;

    for (int i = 0; i < command.length(); i++) {
      char c = command.charAt(i);
      if (c == '"') {
        inQuotes = !inQuotes;
        current.append(c);
      } else if (Character.isWhitespace(c) && !inQuotes) {
        if (!current.isEmpty()) {
          tokens.add(current.toString());
          current.setLength(0);
        }
      } else {
        current.append(c);
      }
    }
    if (!current.isEmpty()) {
      tokens.add(current.toString());
    }
    return tokens;
  }

  public static boolean isNeedHelper(Path instance, MiuClientGetResponse response) throws IOException {
    boolean miuClientChanged = isFileChanged(instance, response.getMiuClientPath(), response.getMiuClientFile());
    if (miuClientChanged) {
      System.out.println("[HELPER] Miu need update");
    }

    boolean mmcPackChanged = isMmcFileChanged(instance, response.getMmcPackPath(), response.getMmcPackFile());
    if (mmcPackChanged) {
      System.out.println("[HELPER] Mmc-pack need update");
    }

    boolean preLaunchCommandOld = isPreLaunchCommandOld(instance, response.getMiuClientPath());
    if (preLaunchCommandOld) {
      System.out.println("[HELPER] PreLaunchCommand need update");
    }

    return miuClientChanged || mmcPackChanged || preLaunchCommandOld;
  }

  public static void startHelper(Path instance, String pack) throws IOException {
    Path helperJar = instance.resolve("miu-client-helper.jar");

    if (!Files.exists(helperJar)) {
      System.out.println("[PAST] Helper JAR not found: " + helperJar);
      throw new IOException("[PAST] Helper JAR not found: " + helperJar);
    }

    System.out.println("[PAST] Starting helper...");

    new ProcessBuilder("java", "-jar", helperJar.toString(), instance.toString(), pack).directory(instance.toFile()).inheritIO().start();

    System.out.println("[PAST] Helper started.");
  }
}