package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class OptionsReader {
    private OptionsReader() {
    }

    public static List<String> read(Path minecraftDir) throws IOException {
        Path options = minecraftDir.resolve("options.txt");
        if (!Files.exists(options)) {
            return new ArrayList<>();
        }

        for (String line : Files.readAllLines(options)) {
            if (!line.startsWith("resourcePacks:")) {
                continue;
            }
            return parse(line.substring("resourcePacks:".length()));
        }
        return new ArrayList<>();
    }

    private static List<String> parse(String value) {
        List<String> result = new ArrayList<>();
        value = value.trim();

        if (value.startsWith("["))
            value = value.substring(1);
        if (value.endsWith("]"))
            value = value.substring(0, value.length() - 1);
        if (value.isBlank())
            return result;

        String[] split = value.split(",");
        for (String part : split) {
            part = part.trim();

            if (part.startsWith("\""))
                part = part.substring(1);
            if (part.endsWith("\""))
                part = part.substring(0, part.length() - 1);
            result.add(part);
        }
        return result;
    }
}