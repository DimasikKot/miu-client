package org.example;

import org.example.model.ClientManifest;
import org.example.model.UpdateResponse;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("=== InstanceUpdater ===");

        if (args.length == 0) {
            System.err.println("Pack name not specified.");
            System.exit(1);
        }

        String pack = args[0];

        Path instance = Paths.get("").toAbsolutePath();

        System.out.println("Pack      : " + pack);
        System.out.println("Instance  : " + instance);

        ProgressWindow.show();

        try {
            ProgressWindow.setStatus("Сканирование сборки...");
            ClientManifest manifest = ManifestBuilder.build(instance);

            ProgressWindow.setStatus("Подготовка обновления...");
            UpdateResponse response = ApiClient.check(pack, manifest);

            ProgressWindow.setStatus("Начало обновления...");
            Updater.apply(instance, response);

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