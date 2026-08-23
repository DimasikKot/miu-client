package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.example.model.UpdatePostRequest;
import org.example.model.UpdatePostResponse;

public class Main {
  public static void main(String[] args) throws Exception {
    System.out.println("=== InstanceUpdater ===");

    if (args.length == 0) {
      System.err.println("Pack name not specified.");
      System.exit(1);
    }

    String pack = Paths.get(args[1]).toString();
    Path instance = Paths.get(args[0]).toAbsolutePath();

    System.out.println("Pack      : " + pack);
    System.out.println("Instance  : " + instance);

    ProgressWindow.show();

    try {
      ProgressWindow.setStatus("Сканирование сборки...");
      UpdatePostRequest manifest = UpdatePostRequestBuilder.build(instance);

      ProgressWindow.setStatus("Подготовка обновления...");
      UpdatePostResponse response = ApiClient.check(pack, manifest, instance);

      ProgressWindow.setStatus("Начало обновления...");
      UpdaterApplyer.apply(instance, response);

      ProgressWindow.setStatus("Запуск Minecraft...");
      Thread.sleep(500);
      ProgressWindow.close();

    } catch (IOException e) {
      ProgressWindow.setStatus("Ошибка: " + e);
      Thread.sleep(2000);
      ProgressWindow.close();
      throw new RuntimeException(e);
    }
  }
}