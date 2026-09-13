package org.example.logic;

import org.example.models.Waypoint;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public final class XaeroWriter {

  private XaeroWriter() {
  }

  /**
   * Заголовок файла меток Xaero.
   * Совпадает с форматом, который генерирует сам мод.
   */
  private static final String HEADER =
      "#\n"
          + "#waypoint:name:initials:x:y:z:color:disabled:type:set:rotate_on_tp:tp_yaw:visibility_type:destination\n"
          + "#\n";

  /** Имя файла по умолчанию, если в директории измерения ещё нет ни одного mw$*.txt. */
  public static final String DEFAULT_FILE_NAME = "mw$default.txt";

  /**
   * Записывает метки в файлы Xaero.
   *
   * <p>Ключ словаря — "&lt;server&gt;/&lt;dimension&gt;" (например,
   * "Multiplayer_purmur.exaroton.me/overworld"). Значение — список меток.
   *
   * <p>Файлы сохраняются по пути:
   * {@code minecraft/xaero/minimap/<server>/dim%<N>/mw$*.txt}
   *
   * <p>Если в директории измерения уже есть файл(ы) {@code mw$*.txt}, метки пишутся в первый
   * найденный (в алфавитном порядке), иначе создаётся {@link #DEFAULT_FILE_NAME}.
   */
  public static void writeWaypoints(Path instancePath,
      Map<String, List<Waypoint>> waypoints) throws IOException {
    if (waypoints == null || waypoints.isEmpty()) {
      return;
    }

    for (Map.Entry<String, List<Waypoint>> entry : waypoints.entrySet()) {
      String key = entry.getKey();
      List<Waypoint> list = entry.getValue();
      if (key == null || list == null) {
        continue;
      }

      int slash = key.lastIndexOf('/');
      if (slash <= 0 || slash == key.length() - 1) {
        // Невалидный ключ — пропускаем.
        continue;
      }

      String serverName = key.substring(0, slash);
      String dimension = key.substring(slash + 1);

      String dimFolder = XaeroReader.getReverseDimensionMap()
          .getOrDefault(dimension, dimension);

      Path dimDir = instancePath
          .resolve("minecraft/xaero/minimap")
          .resolve(serverName)
          .resolve(dimFolder);

      Files.createDirectories(dimDir);

      Path targetFile = resolveTargetFile(dimDir);
      writeFile(targetFile, list);
    }
  }

  /**
   * Возвращает путь к файлу, в который нужно писать:
   * либо существующий mw$*.txt, либо новый mw$default.txt.
   */
  private static Path resolveTargetFile(Path dimDir) throws IOException {
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dimDir, "mw$*.txt")) {
      Path first = null;
      for (Path p : stream) {
        if (first == null || p.getFileName().toString()
            .compareTo(first.getFileName().toString()) < 0) {
          first = p;
        }
      }
      if (first != null) {
        return first;
      }
    }
    return dimDir.resolve(DEFAULT_FILE_NAME);
  }

  private static void writeFile(Path file, List<Waypoint> waypoints) throws IOException {
    try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
      writer.write(HEADER);
      for (Waypoint wp : waypoints) {
        if (wp == null) {
          continue;
        }
        writer.write(wp.toLine());
        writer.newLine();
      }
    }
  }
}