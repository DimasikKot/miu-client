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

        scanFolder(instance.resolve("mods"), instance, manifest);
        scanFolder(instance.resolve("resourcepacks"), instance, manifest);
        // Xaero позже
        // servers.dat позже TODO
        return manifest;
    }

    private static void scanFolder(Path folder, Path root, ClientManifest manifest) throws IOException {
        if (!Files.exists(folder)) return;

        try (var stream = Files.walk(folder)) {

            stream.filter(Files::isRegularFile).forEach(file -> {

                try {

                    String relative = root.relativize(file).toString().replace("\\", "/");
                    ManifestFile manifestFile = new ManifestFile();
                    manifestFile.setName(file.getFileName().toString());
                    manifestFile.setPath(relative);
                    manifestFile.setSha256(HashUtil.sha256(file));
                    manifestFile.setSize(Files.size(file));
                    manifest.getFiles().put(relative, manifestFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}