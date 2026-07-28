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

        ClientManifest manifest = ManifestBuilder.build(instance);

        UpdateResponse response = ApiClient.check(pack, manifest);

        Updater.apply(instance, response);

    }

}