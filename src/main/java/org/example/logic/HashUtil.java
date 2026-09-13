package org.example.logic;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class HashUtil {
  private static final int BUFFER_SIZE = 1024 * 1024;

  private HashUtil() {
  }

  public static String sha256(Path file) throws IOException {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      try (InputStream input = Files.newInputStream(file)) {
        byte[] buffer = new byte[BUFFER_SIZE];
        int read;
        while ((read = input.read(buffer)) != -1) {
          digest.update(buffer, 0, read);
        }
      }
      return bytesToHex(digest.digest());

    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  private static String bytesToHex(byte[] hash) {
    StringBuilder builder = new StringBuilder(hash.length * 2);
    for (byte b : hash) {
      builder.append(String.format("%02x", b));
    }
    return builder.toString();
  }

  private static final ObjectMapper MAPPER = new ObjectMapper();

  public static String normalizedSha256(Path file) throws IOException, NoSuchAlgorithmException {
    JsonNode root = MAPPER.readTree(Files.readAllBytes(file));
    removeCachedFields(root);

    // Каноническая сериализация: сортировка ключей, без отступов
    ObjectMapper canonical = new ObjectMapper();
    canonical.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
    byte[] bytes = canonical.writeValueAsBytes(root);

    System.out.println(Arrays.toString(bytes));

    return sha256bytes(bytes);
  }

  private static void removeCachedFields(JsonNode node) {
    if (node.isObject()) {
      ObjectNode obj = (ObjectNode) node;
      List<String> toRemove = new ArrayList<>();
      Iterator<String> it = obj.fieldNames();
      while (it.hasNext()) {
        String name = it.next();
        if (name.startsWith("cached")) {
          toRemove.add(name);
        }
      }
      toRemove.forEach(obj::remove);
      obj.elements().forEachRemaining(HashUtil::removeCachedFields);
    } else if (node.isArray()) {
      node.elements().forEachRemaining(HashUtil::removeCachedFields);
    }
  }

  public static String sha256bytes(byte[] data) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] digest = md.digest(data);
    StringBuilder sb = new StringBuilder();
    for (byte b : digest) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }
}