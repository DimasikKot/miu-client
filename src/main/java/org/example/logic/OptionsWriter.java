package org.example.logic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class OptionsWriter {
  private OptionsWriter() {
  }

  public static void writeResourcepacks(Path instance, List<String> resourcepacks) throws IOException {
    Path options = instance.resolve("minecraft/options.txt");
    List<String> lines = new ArrayList<>();
    if (Files.exists(options)) {
      lines.addAll(Files.readAllLines(options));
    }

    String newLine = "resourcePacks:" + serialize(resourcepacks);
    System.out.println("[PAST] " + newLine);
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

  public static void writeIncompatibleResourcepacks(Path instance, List<String> incompatible_resourcepacks)
      throws IOException {
    Path options = instance.resolve("minecraft/options.txt");
    List<String> lines = new ArrayList<>();
    if (Files.exists(options)) {
      lines.addAll(Files.readAllLines(options));
    }

    String newLine = "incompatibleResourcePacks:" + serialize(incompatible_resourcepacks);
    System.out.println("[PAST] " + newLine);
    boolean found = false;

    for (int i = 0; i < lines.size(); i++) {
      if (lines.get(i).startsWith("incompatibleResourcePacks:")) {
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