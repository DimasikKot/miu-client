package org.example;

import org.example.model.ClientManifest;
import org.example.model.UpdateResponse;

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
        ProgressWindow.setStatus("Сканирование сборки...");
        ClientManifest manifest = ManifestBuilder.build(instance);

        ProgressWindow.setStatus("Подготовка обновления...");
        UpdateResponse response = ApiClient.check(pack, manifest);

        ProgressWindow.setStatus("Начало обновления...");
        Updater.apply(instance, response);
    }
}