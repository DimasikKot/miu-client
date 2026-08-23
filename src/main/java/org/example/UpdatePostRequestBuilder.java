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
import org.example.model.UpdatePostRequest;

public final class UpdatePostRequestBuilder {
  private UpdatePostRequestBuilder() {
  }

  public static UpdatePostRequest build(Path instance) throws IOException {
    UpdatePostRequest request = new UpdatePostRequest();

    // TODO сделать получение сканирования с сервера

    request.setResourcepacks(new ArrayList<>());
    request.setIncompatibleResourcepacks(new ArrayList<>());
    request.setServers(new ArrayList<>());
    request.setFiles(new HashMap<>());

    scanFolder(instance.resolve("minecraft/config"), instance, request);
    scanFolder(instance.resolve("minecraft/mods"), instance, request);
    scanFolder(instance.resolve("minecraft/resourcepacks"), instance, request);
    scanFolder(instance.resolve("minecraft/xaero"), instance, request);

    request.setResourcepacks(OptionsReader.readResourcepacks(instance));
    request.setIncompatibleResourcepacks(OptionsReader.readIncompatibleResourcepacks(instance));

    List<ServerInfo> servers = ServersDat.read(instance.resolve("minecraft/servers.dat"));
    request.setServers(servers);
    System.out.println("[OK] old_servers:");

    for (ServerInfo server : servers) {
      System.out.printf(
          "  - %s (%s)%n",
          server.getName(),
          server.getIp()
      );
    }

    return request;
  }

  private static FileInfo scanFile(Path file) {
    try {
      FileInfo fileInfo = new FileInfo();
      fileInfo.setName(file.getFileName().toString());
      fileInfo.setSha256(HashUtil.sha256(file));
      fileInfo.setSize((int) Files.size(file));
      return fileInfo;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void scanFolder(Path folder, Path root, UpdatePostRequest manifest) throws IOException {
    if (!Files.exists(folder))
      return;

    try (var stream = Files.walk(folder)) {
      stream.filter(Files::isRegularFile).forEach(file -> {
        String relative = root.relativize(file).toString();
        FileInfo fileInfo = scanFile(file);
        manifest.getFiles().put(relative.replace("\\", "/"), fileInfo);
      });
    }
  }
}