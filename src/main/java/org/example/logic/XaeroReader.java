package org.example.logic;

import org.example.models.Waypoint;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class XaeroReader {

  private XaeroReader() {
  }

  private static final Map<String, String> DIMENSION_MAP = Map.of(
      "dim%0", "overworld",
      "dim%-1", "nether",
      "dim%1", "end"
  );

  private static final Map<String, String> REVERSE_DIMENSION_MAP = Map.of(
      "overworld", "dim%0",
      "nether", "dim%-1",
      "end", "dim%1"
  );

  public static Map<String, String> getDimensionMap() {
    return DIMENSION_MAP;
  }

  public static Map<String, String> getReverseDimensionMap() {
    return REVERSE_DIMENSION_MAP;
  }

  private static boolean parseBool(String value) {
    return value.trim().equalsIgnoreCase("true");
  }

  /** Аналог parse_waypoint_line. */
  public static Waypoint parseWaypointLine(String line) {
    if (line == null) {
      return null;
    }
    line = line.trim();
    if (line.isEmpty() || line.startsWith("#")) {
      return null;
    }

    String[] parts = line.split(":", -1);
    if (parts.length < 14 || !"waypoint".equals(parts[0])) {
      return null;
    }

    // Имя может содержать ':' — берём всё между "waypoint" и последними 12 полями.
    String[] tail = Arrays.copyOfRange(parts, parts.length - 12, parts.length);
    String name = String.join(":", Arrays.copyOfRange(parts, 1, parts.length - 12));

    Waypoint w = new Waypoint();
    w.setName(name);
    w.setInitials(tail[0]);
    w.setX(Integer.parseInt(tail[1]));
    w.setY(Integer.parseInt(tail[2]));
    w.setZ(Integer.parseInt(tail[3]));
    w.setColor(Integer.parseInt(tail[4]));
    w.setDisabled(parseBool(tail[5]));
    w.setType(Integer.parseInt(tail[6]));
    w.setSet(tail[7]);
    w.setRotateOnTp(parseBool(tail[8]));
    w.setTpYaw(Integer.parseInt(tail[9]));
    w.setVisibilityType(Integer.parseInt(tail[10]));
    w.setDestination(parseBool(tail[11]));
    return w;
  }

  /**
   * Читает метки из: minecraft/xaero/minimap/&lt;server&gt;/dim%&lt;N&gt;/mw$*.txt
   *
   * Возвращает словарь с ключом "&lt;server&gt;/&lt;dimension&gt;",
   * например {"Multiplayer_purmur.exaroton.me/overworld": [...]}.
   */
  public static Map<String, List<Waypoint>> getWaypoints(Path instancePath) throws IOException {
    Map<String, List<Waypoint>> result = new LinkedHashMap<>();

    Path minimapPath = instancePath.resolve("minecraft/xaero/minimap");
    if (!Files.exists(minimapPath)) {
      return result;
    }

    try (DirectoryStream<Path> serverDirs = Files.newDirectoryStream(minimapPath)) {
      for (Path serverDir : serverDirs) {
        if (!Files.isDirectory(serverDir)) {
          continue;
        }
        String serverName = serverDir.getFileName().toString();

        try (DirectoryStream<Path> dimDirs = Files.newDirectoryStream(serverDir)) {
          for (Path dimDir : dimDirs) {
            if (!Files.isDirectory(dimDir)) {
              continue;
            }
            String dimName = dimDir.getFileName().toString();
            if (!dimName.startsWith("dim%")) {
              continue;
            }

            String dimension = DIMENSION_MAP.getOrDefault(dimName, dimName);
            String key = serverName + "/" + dimension;
            List<Waypoint> bucket = result.computeIfAbsent(key, k -> new ArrayList<>());

            // Читаем все файлы mw$*.txt в директории измерения
            try (DirectoryStream<Path> files = Files.newDirectoryStream(dimDir, "mw$*.txt")) {
              for (Path file : files) {
                for (String line : Files.readAllLines(file)) {
                  Waypoint wp = parseWaypointLine(line);
                  if (wp != null) {
                    bucket.add(wp);
                  }
                }
              }
            }
          }
        }
      }
    }

    return result;
  }
}