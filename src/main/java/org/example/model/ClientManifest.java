package org.example.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientManifest {
  private ManifestFile pack;
  private ManifestFile instance;
  private Map<String, ManifestFile> files = new HashMap<>();
  private List<ServerInfo> servers = new ArrayList<>();
  @JsonProperty("resource_packs")
  private List<String> resourcePacks = new ArrayList<>();

  public ClientManifest() {
  }

  public ManifestFile getPack() {
    return pack;
  }

  public void setPack(ManifestFile pack) {
    this.pack = pack;
  }

  public ManifestFile getInstance() {
    return instance;
  }

  public void setInstance(ManifestFile instance) {
    this.instance = instance;
  }

  public Map<String, ManifestFile> getFiles() {
    return files;
  }

  public void setFiles(Map<String, ManifestFile> files) {
    this.files = files;
  }

  public List<ServerInfo> getServers() {
    return servers;
  }

  public void setServers(List<ServerInfo> servers) {
    this.servers = servers;
  }

  public List<String> getResourcePacks() {
    return resourcePacks;
  }

  public void setResourcePacks(List<String> resourcePacks) {
    this.resourcePacks = resourcePacks;
  }
}
