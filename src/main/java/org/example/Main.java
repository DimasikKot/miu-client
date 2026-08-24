package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.example.model.MiuClientGetResponse;
import org.example.model.UpdateGetResponse;
import org.example.model.UpdatePostRequest;
import org.example.model.UpdatePostResponse;

public class Main {
  public static void main(String[] args) throws Exception {
    System.out.println("[MIU] PurMur Updater");

    if (args.length == 0) {
      System.err.println("[MIU] Pack name not specified.");
      System.exit(1);
    }

    String pack = Paths.get(args[1]).toString();
    Path instance = Paths.get(args[0]).toAbsolutePath();
    System.out.println("[MIU] Pack     : " + pack);
    System.out.println("[MIU] Instance : " + instance);

    ProgressWindow.show();
    try {
      ProgressWindow.setStatus("Смотрим что сканировать...");
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

      ProgressWindow.setStatus("Сканирование сборки...");
      Thread.sleep(500);
      UpdatePostRequest manifest = UpdatePostRequestBuilder.build(scanPaths, instance);

      ProgressWindow.setStatus("Подготовка обновления...");
      Thread.sleep(500);
      UpdatePostResponse response = ApiClient.check(pack, manifest, instance);

      ProgressWindow.setStatus("Начало обновления...");
      Thread.sleep(500);
      UpdateApplyer.apply(instance, response);

      MiuClientGetResponse serverVersion = ApiClient.getVersion(pack, instance);
      System.out.println(serverVersion);

      boolean updaterChanged = UpdateApplyer.isFileChanged(instance, serverVersion.getMiuClientPath(), serverVersion.getMiuClientFile()) || UpdateApplyer.isFileChanged(instance, serverVersion.getMmcPackPath(), serverVersion.getMmcPackFile());

      if (updaterChanged) {
        UpdateApplyer.startHelper(instance, pack);
        System.out.println("[PAST] Update required. Stopping Minecraft launch.");
        System.exit(1);
      }

      ProgressWindow.setStatus("Запуск Minecraft...");
      Thread.sleep(500);
      ProgressWindow.close();

    } catch (IOException e) {
      ProgressWindow.setStatus("Ошибка: " + e);
      Thread.sleep(5000);
      ProgressWindow.close();
      throw new RuntimeException(e);
    }
  }
}