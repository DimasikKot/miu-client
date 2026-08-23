package org.example.model;

import java.util.List;
import java.util.Map;

public class UpdatePostRequest {
  private List<String> resourcepacks;
  private List<ServerInfo> servers;
  private Map<String, FileInfo> files; // path: FileInfo

  public UpdatePostRequest() {
  }

  public UpdatePostRequest(List<String> resourcepacks, List<ServerInfo> servers, Map<String, FileInfo> files) {
    this.resourcepacks = resourcepacks;
    this.servers = servers;
    this.files = files;
  }

  public List<String> getResourcepacks() {
    return resourcepacks;
  }

  public void setResourcepacks(List<String> resourcepacks) {
    this.resourcepacks = resourcepacks;
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