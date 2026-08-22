package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class OptionsWriter {
  private OptionsWriter() {
  }

  public static void write(Path instance, List<String> resourcePacks) throws IOException {
    Path options = instance.resolve("minecraft/options.txt");
    List<String> lines = new ArrayList<>();
    if (Files.exists(options)) {
      lines.addAll(Files.readAllLines(options));
    }

    String newLine = "resourcePacks:" + serialize(resourcePacks);
    System.out.println("[OK] " + newLine);
    boolean found = false;

    for (int i = 0; i < lines.size(); i++) {
      if (lines.get(i).startsWith("resourcePacks:")) {
        lines.set(i, newLine);
        found = true;
        break;
      }
    }

    if (!found) {
      lines.add(newLine);
    }
    Files.write(options, lines);
  }

  private static String serialize(List<String> packs) {
    StringBuilder builder = new StringBuilder();
    builder.append("[");

    for (int i = 0; i < packs.size(); i++) {
      if (i != 0) {
        builder.append(",");
      }
      builder.append("\"").append(packs.get(i)).append("\"");

    }
    builder.append("]");
    return builder.toString();
  }
}