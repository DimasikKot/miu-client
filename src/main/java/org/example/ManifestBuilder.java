package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.example.model.UpdatePostRequest;
import org.example.model.FileInfo;

public final class ManifestBuilder {
  private ManifestBuilder() {
  }

  public static UpdatePostRequest build(Path instance) throws IOException {
    UpdatePostRequest request = new UpdatePostRequest();

    scanFolder(instance.resolve("minecraft/config"), instance, request);
    scanFolder(instance.resolve("minecraft/mods"), instance, request);
    scanFolder(instance.resolve("minecraft/resourcepacks"), instance, request);
    scanFolder(instance.resolve("minecraft/xaero"), instance, request);

    request.setResourcepacks(
        OptionsReader.read(instance));

    // servers.dat позже TODO
    return request;
  }

  private static FileInfo scanFile(Path file, String relative) {
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
        FileInfo fileInfo = scanFile(file, relative);
        manifest.getFiles().put(relative.replace("\\", "/"), fileInfo);
      });
    }
  }
}