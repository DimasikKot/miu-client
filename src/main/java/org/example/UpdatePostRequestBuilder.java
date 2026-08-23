package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.example.logic.OptionsReader;
import org.example.logic.ServersDat;
import org.example.model.FileInfo;
import org.example.model.ServerInfo;
import org.example.model.UpdateGetResponse;
import org.example.model.UpdatePostRequest;

public final class UpdatePostRequestBuilder {
  private UpdatePostRequestBuilder() {
  }

  public static UpdatePostRequest build(String pack, Path instance) throws IOException, InterruptedException {
    UpdatePostRequest request = new UpdatePostRequest();

    request.setResourcepacks(new ArrayList<>());
    request.setIncompatibleResourcepacks(new ArrayList<>());
    request.setServers(new ArrayList<>());
    request.setFiles(new HashMap<>());

    // Получаем с сервера список того, что необходимо сканировать
    UpdateGetResponse scanPaths = ApiClient.getScanPaths(pack, instance);

    System.out.println("[MIU] Scan configuration:");

    System.out.println("  files:");
    for (String file : scanPaths.getFiles_paths()) {
      System.out.println("    - " + file);
    }

    System.out.println("  dirs:");
    for (String dir : scanPaths.getDirs_paths()) {
      System.out.println("    - " + dir);
    }

    // Сканируем отдельные файлы
    for (String file : scanPaths.getFiles_paths()) {
      scanFile(instance.resolve(file), instance, request);
    }

    // Сканируем директории рекурсивно
    for (String dir : scanPaths.getDirs_paths()) {
      scanFolder(instance.resolve(dir), instance, request);
    }

    request.setResourcepacks(OptionsReader.readResourcepacks(instance));
    request.setIncompatibleResourcepacks(OptionsReader.readIncompatibleResourcepacks(instance));

    List<ServerInfo> servers = ServersDat.read(instance.resolve("minecraft/servers.dat"));
    request.setServers(servers);
    System.out.println("[OK] old_servers:");

    for (ServerInfo server : servers) {
      System.out.printf("  - %s (%s)%n", server.getName(), server.getIp());
    }

    return request;
  }

  private static void scanFile(Path file, Path root, UpdatePostRequest manifest) throws IOException {
    if (!Files.exists(file)) {
      System.out.println("[MIU] File not found: " + root.relativize(file));
      return;
    }

    if (!Files.isRegularFile(file)) {
      System.out.println("[MIU] Not a regular file: " + root.relativize(file));
      return;
    }

    FileInfo fileInfo = createFileInfo(file);
    String relative = root.relativize(file).toString().replace("\\", "/");
    manifest.getFiles().put(relative, fileInfo);
  }

  private static void scanFolder(Path folder, Path root, UpdatePostRequest manifest) throws IOException {
    if (!Files.exists(folder)) {
      System.out.println("[MIU] Directory not found: " + root.relativize(folder));
      return;
    }

    if (!Files.isDirectory(folder)) {
      System.out.println("[MIU] Not a directory: " + root.relativize(folder));
      return;
    }

    try (var stream = Files.walk(folder)) {
      stream.filter(Files::isRegularFile).forEach(file -> {
        try {
          FileInfo fileInfo = createFileInfo(file);
          String relative = root.relativize(file).toString().replace("\\", "/");
          manifest.getFiles().put(relative, fileInfo);
        } catch (IOException e) {
          throw new RuntimeException("Failed to scan file: " + file, e);
        }
      });
    }
  }

  private static FileInfo createFileInfo(Path file) throws IOException {
    FileInfo fileInfo = new FileInfo();
    fileInfo.setName(file.getFileName().toString());
    fileInfo.setSha256(HashUtil.sha256(file));
    fileInfo.setSize((int) Files.size(file));

    return fileInfo;
  }
}