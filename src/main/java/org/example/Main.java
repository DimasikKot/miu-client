package org.example;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {

        System.out.println("=== InstanceUpdater ===");

        if (args.length == 0) {

            System.err.println("Pack name not specified.");

            System.exit(1);
        }

        String pack = args[0];

        Path instance = Paths.get("").toAbsolutePath();

        System.out.println("Pack      : " + pack);
        System.out.println("Instance  : " + instance);

    }

}