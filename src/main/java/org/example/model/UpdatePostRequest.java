package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdatePostRequest {
  private List<String> resourcepacks;
  @JsonProperty("incompatible_resourcepacks")
  private List<String> incompatible_resourcepacks;
  private List<ServerInfo> servers;
  private Map<String, FileInfo> files; // path: FileInfo

  public UpdatePostRequest() {
    this.resourcepacks = new ArrayList<>();
    this.incompatible_resourcepacks = new ArrayList<>();
    this.servers = new ArrayList<>();
    this.files = new HashMap<>();
  }

  public UpdatePostRequest(List<String> resourcepacks, List<String> incompatible_resourcepacks,
      List<ServerInfo> servers, Map<String, FileInfo> files) {
    this.resourcepacks = resourcepacks;
    this.incompatible_resourcepacks = incompatible_resourcepacks;
    this.servers = servers;
    this.files = files;
  }

  public List<String> getResourcepacks() {
    return resourcepacks;
  }

  public void setResourcepacks(List<String> resourcepacks) {
    this.resourcepacks = resourcepacks;
  }

  public List<String> getIncompatibleResourcepacks() {
    return incompatible_resourcepacks;
  }

  public void setIncompatibleResourcepacks(List<String> incompatible_resourcepacks) {
    this.incompatible_resourcepacks = incompatible_resourcepacks;
  }

  public List<ServerInfo> getServers() {
    return servers;
  }

  public void setServers(List<ServerInfo> servers) {
    this.servers = servers;
  }

  public Map<String, FileInfo> getFiles() {
    return files;
  }

  public void setFiles(Map<String, FileInfo> files) {
    this.files = files;
  }
}