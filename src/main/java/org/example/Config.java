package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class Config {
  private static final ObjectMapper MAPPER = new ObjectMapper();

  private static final List<String> DEFAULT_SERVERS = List.of("http://localhost:10033", "http://fundata.mooo.com:10033",
      "http://85.15.190.8:10033");

  private Config() {
  }

  public static List<String> getServers(Path instance) throws IOException {
    Path configPath = instance.resolve("config.json");

    ensureConfigExists(configPath);

    JsonNode config = MAPPER.readTree(configPath.toFile());
    JsonNode serversNode = config.get("servers");

    if (serversNode == null || !serversNode.isArray()) {
      throw new IOException("'servers' must be an array in " + configPath);
    }

    List<String> servers = new ArrayList<>();

    for (JsonNode node : serversNode) {
      String server = node.asText().trim();

      if (!server.isBlank()) {
        servers.add(server.replaceAll("/+$", ""));
      }
    }

    if (servers.isEmpty()) {
      throw new IOException("No servers configured");
    }

    return servers;
  }

  private static void ensureConfigExists(Path configPath) throws IOException {
    // Проверяем существование файла
    if (Files.exists(configPath)) {
      return;
    }

    // СОЗДАЕМ РОДИТЕЛЬСКИЕ ДИРЕКТОРИИ
    Path parent = configPath.getParent();
    if (parent != null && !Files.exists(parent)) {
      Files.createDirectories(parent);
      System.out.println("[MIU] Created directories: " + parent);
    }

    // Создаем конфиг с дефолтными серверами
    ObjectNode config = MAPPER.createObjectNode();
    ArrayNode servers = config.putArray("servers");

    for (String server : DEFAULT_SERVERS) {
      servers.add(server);
    }

    MAPPER.writerWithDefaultPrettyPrinter().writeValue(configPath.toFile(), config);

    System.out.println("[MIU] Created config: " + configPath);
  }
}