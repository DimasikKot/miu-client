package org.example.logic;

import net.querz.nbt.io.NBTDeserializer;
import net.querz.nbt.io.NBTSerializer;
import net.querz.nbt.io.NamedTag;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;

import org.example.model.ServerInfo;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class ServersDat {

  private ServersDat() {
  }

  /**
   * Читает servers.dat.
   *
   * @param path путь к servers.dat
   * @return список серверов
   */
  public static List<ServerInfo> read(Path path) throws IOException {
    if (!Files.exists(path)) {
      return new ArrayList<>();
    }

    CompoundTag root = readNbt(path);
    ListTag<CompoundTag> serversTag = root.getListTag("servers").asCompoundTagList();
    List<ServerInfo> servers = new ArrayList<>();

    for (CompoundTag serverTag : serversTag) {
      String name = serverTag.getString("name");
      String ip = serverTag.getString("ip");

      servers.add(new ServerInfo(name, ip));
    }

    return servers;
  }

  /**
   * Полностью записывает список серверов в servers.dat.
   * <p>
   * Если файл не существует — он будет создан.
   */
  public static void write(Path path, List<ServerInfo> servers) throws IOException {

    Path parent = path.getParent();

    if (parent != null) {
      Files.createDirectories(parent);
    }

    CompoundTag root = new CompoundTag();

    ListTag<CompoundTag> serversTag = new ListTag<>(CompoundTag.class);

    for (ServerInfo server : servers) {
      CompoundTag serverTag = new CompoundTag();

      serverTag.putString("name", server.getName());
      serverTag.putString("ip", server.getIp());

      serversTag.add(serverTag);
    }

    root.put("servers", serversTag);

    writeNbt(path, root);
  }

  /**
   * Добавляет сервер в существующий servers.dat.
   * <p>
   * Если файл не существует — он будет создан.
   */
  public static void addServer(Path path, ServerInfo server) throws IOException {

    List<ServerInfo> servers = read(path);

    servers.add(server);

    write(path, servers);
  }

  /**
   * Удаляет все серверы с указанным IP.
   *
   * @return true, если хотя бы один сервер был удалён
   */
  public static boolean removeServer(Path path, String ip) throws IOException {

    List<ServerInfo> servers = read(path);

    boolean removed = servers.removeIf(server -> ip.equals(server.getIp()));

    if (removed) {
      write(path, servers);
    }

    return removed;
  }

  /**
   * Обновляет сервер с указанным IP.
   *
   * @return true, если сервер был найден и обновлён
   */
  public static boolean updateServer(Path path, String ip, String newName, String newIp) throws IOException {

    List<ServerInfo> servers = read(path);

    for (ServerInfo server : servers) {
      if (ip.equals(server.getIp())) {
        server.setName(newName);
        server.setIp(newIp);

        write(path, servers);

        return true;
      }
    }

    return false;
  }

  /**
   * Полностью заменяет список серверов в servers.dat.
   *
   * @param path    путь к servers.dat
   * @param servers новый список серверов
   */
  public static void replaceServers(
      Path path,
      List<ServerInfo> servers
  ) throws IOException {
    write(path, servers);
  }

  private static CompoundTag readNbt(Path path) throws IOException {
    try (InputStream file = Files.newInputStream(path)) {
      NBTDeserializer deserializer = new NBTDeserializer(false);
      NamedTag namedTag = deserializer.fromStream(file);
      return (CompoundTag) namedTag.getTag();
    }
  }

  /**
   * Записывает корневой NBT CompoundTag.
   */
  private static void writeNbt(
      Path path,
      CompoundTag root
  ) throws IOException {
    try (OutputStream file = Files.newOutputStream(path)) {
      NBTSerializer serializer = new NBTSerializer(false);
      serializer.toStream(
          new NamedTag("", root),
          file
      );
    }
  }
}