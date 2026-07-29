package org.example;

import org.example.model.DownloadFile;
import org.example.model.UpdateResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Updater {
    private Updater() {
    }

    public static void apply(Path instance, UpdateResponse response) throws Exception {
        System.out.println();
        System.out.println("=== Applying update ===");
        System.out.println();

        deleteFiles(instance, response);

        downloadFiles(instance, response);

        OptionsWriter.write(instance, response.getResourcePacks());

        System.out.println();
        System.out.println("Update completed successfully.");
        System.out.println();
    }

    private static void deleteFiles(Path instance, UpdateResponse response) throws IOException {
        if (response.getDelete().isEmpty()) {
            System.out.println("Nothing to delete.");
            return;
        }

        System.out.println("Deleting files...");
        ProgressWindow.setStatus("Удаление старых файлов...");
        for (String relative : response.getDelete()) {
            Path file = instance.resolve(relative);
            if (!Files.exists(file)) continue;
            Files.delete(file);
            cleanupEmptyParents(instance, file.getParent());
            System.out.println("[OK] Deleted: " + relative);
        }
    }

    private static void downloadFiles(Path instance, UpdateResponse response) throws Exception {
        if (response.getDownload().isEmpty()) {
            System.out.println("Nothing to download.");
            return;
        }

        System.out.println("Downloading files...");
        ProgressWindow.setStatus("Загрузка файлов...");

        int current_count = 0;
        int all_count = response.getDownload().size();

        long totalBytes = response.getDownload().stream().mapToLong(DownloadFile::getSize).sum();
        long downloadedBytes = 0;
        long start = System.nanoTime();

        for (DownloadFile file : response.getDownload()) {
            Downloader.download(instance, file);

            current_count++;
            ProgressWindow.setFile(file.getPath());
            ProgressWindow.setProgress(current_count, all_count);

            downloadedBytes += file.getSize();

            ProgressWindow.setDownloadedBytes(downloadedBytes, totalBytes);
            long elapsed = System.nanoTime() - start;
            double seconds = elapsed / 1_000_000_000.0;
            double speed = downloadedBytes / 1024d / 1024d / seconds;
            ProgressWindow.setSpeed(speed);
        }
    }

    private static void cleanupEmptyParents(Path root, Path current) throws IOException {
        while (current != null && !current.equals(root)) {
            try (var stream = Files.list(current)) {
                if (stream.findAny().isPresent()) return;
            }

            Files.delete(current);

            current = current.getParent();
        }
    }
}