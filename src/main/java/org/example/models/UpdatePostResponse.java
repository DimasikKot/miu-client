package org.example.models;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class UpdatePostResponse {
  private List<String> new_resourcepacks;
  private List<String> new_incompatible_resourcepacks;
  private List<ServerInfo> new_servers;
  private Set<String> need_delete;
  private Map<String, FileDownloadInfo> need_download;

  public UpdatePostResponse() {
  }

  public UpdatePostResponse(List<String> new_resourcepacks, List<String> new_incompatible_resourcepacks,
      List<ServerInfo> new_servers, Set<String> need_delete, Map<String, FileDownloadInfo> need_download) {
    this.new_resourcepacks = new_resourcepacks;
    this.new_incompatible_resourcepacks = new_incompatible_resourcepacks;
    this.new_servers = new_servers;
    this.need_delete = need_delete;
    this.need_download = need_download;
  }

  public List<String> getNew_resourcepacks() {
    return new_resourcepacks;
  }

  public void setNew_resourcepacks(List<String> new_resourcepacks) {
    this.new_resourcepacks = new_resourcepacks;
  }

  public List<String> getNew_incompatible_resourcepacks() {
    return new_incompatible_resourcepacks;
  }

  public void setNew_incompatible_resourcepacks(List<String> new_incompatible_resourcepacks) {
    this.new_incompatible_resourcepacks = new_incompatible_resourcepacks;
  }

  public List<ServerInfo> getNew_servers() {
    return new_servers;
  }

  public void setNew_servers(List<ServerInfo> new_servers) {
    this.new_servers = new_servers;
  }

  public Set<String> getNeed_delete() {
    return need_delete;
  }

  public void setNeed_delete(Set<String> need_delete) {
    this.need_delete = need_delete;
  }

  public Map<String, FileDownloadInfo> getNeed_download() {
    return need_download;
  }

  public void setNeed_download(Map<String, FileDownloadInfo> need_download) {
    this.need_download = need_download;
  }
}