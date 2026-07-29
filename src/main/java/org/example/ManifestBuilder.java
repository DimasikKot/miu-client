package org.example;

import org.example.model.ClientManifest;
import org.example.model.ManifestFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ManifestBuilder {
    private ManifestBuilder() {
    }

    public static ClientManifest build(Path instance) throws IOException {
        ClientManifest manifest = new ClientManifest();

        Path pack = instance.resolve("mmc-pack.json");
        if (Files.exists(pack)) {
            manifest.setPack(scanFile(pack, "mmc-pack.json"));
        }

        Path instanceCfg = instance.resolve("instance.cfg");
        if (Files.exists(instanceCfg)) {
            manifest.setInstance(scanFile(instanceCfg, "instance.cfg"));
        }

        scanFolder(instance.resolve("minecraft/config"), instance, manifest);
        scanFolder(instance.resolve("minecraft/mods"), instance, manifest);
        scanFolder(instance.resolve("minecraft/resourcepacks"), instance, manifest);
        scanFolder(instance.resolve("minecraft/xaero"), instance, manifest);

        // servers.dat позже TODO
        return manifest;
    }

    private static ManifestFile scanFile(Path file, String relative) {
        try {
            ManifestFile manifestFile = new ManifestFile();
            manifestFile.setName(file.getFileName().toString());
            manifestFile.setPath(relative.replace("\\", "/"));
            manifestFile.setSha256(HashUtil.sha256(file));
            manifestFile.setSize(Files.size(file));
            return manifestFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void scanFolder(Path folder, Path root, ClientManifest manifest) throws IOException {
        if (!Files.exists(folder)) return;
        try (var stream = Files.walk(folder)) {
            stream.filter(Files::isRegularFile).forEach(file -> {
                String relative = root.relativize(file).toString();
                ManifestFile manifestFile = scanFile(file, relative);
                manifest.getFiles().put(relative.replace("\\", "/"), manifestFile);
            });
        }
    }
}