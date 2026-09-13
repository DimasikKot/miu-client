package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.example.logic.UpdateApplyer;
import org.example.logic.UpdatePostRequestBuilder;
import org.example.models.MiuClientGetResponse;
import org.example.models.UpdateGetResponse;
import org.example.models.UpdatePostRequest;
import org.example.models.UpdatePostResponse;

public class Main {
  public static void main(String[] args) throws Exception {
    System.out.println("[MIU] PurMur Updater");

    if (args.length == 0) {
      System.err.println("[MIU] Pack name not specified.");
      System.exit(0);
    }

    String pack = Paths.get(args[1]).toString();
    Path instance = Paths.get(args[0]).toAbsolutePath();
    System.out.println("[MIU] Pack     : " + pack);
    System.out.println("[MIU] Instance : " + instance);

    ProgressWindow.show();
    try {
      ProgressWindow.setStatus("Смотрим что сканировать...");
      UpdateGetResponse scanPaths = null;
      try {
        scanPaths = ApiClient.getScanPaths(pack, instance);
      } catch (IOException e) {
        System.out.println("[MIU] Error getScanPaths" + e);
        System.exit(0);
      }

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

      MiuClientGetResponse version = null;
      try {
        version = ApiClient.getVersion(pack, instance);
      } catch (IOException e) {
        System.out.println("[MIU] Error getVersion" + e);
        System.exit(0);
      }

      int isNeed = UpdateApplyer.isNeedHelper(instance, version);

      if (isNeed > 0) {
        try {
          UpdateApplyer.startHelper(instance, pack);
        } catch (IOException e) {
          System.out.println("[MIU] Error startHelper" + e);
          System.exit(0);
        }
        System.out.println("[PAST] Update required. Stopping Minecraft launch.");
        if (isNeed == 2) {
          System.exit(1);
        }
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