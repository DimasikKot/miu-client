package org.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class UpdatePostResponse {
  @JsonProperty("new_resourcepacks")
  private List<String> new_resourcepacks;

  @JsonProperty("new_incompatible_resourcepacks")
  private List<String> new_incompatible_resourcepacks;

  @JsonProperty("new_servers")
  private List<ServerInfo> new_servers;

  // "<server>/<dimension>" -> список меток
  @JsonProperty("new_waypoints")
  private Map<String, List<Waypoint>> new_waypoints;

  @JsonProperty("need_delete")
  private Set<String> need_delete;

  @JsonProperty("need_download")
  private Map<String, FileDownloadInfo> need_download;

  public UpdatePostResponse() {
  }

  public UpdatePostResponse(List<String> new_resourcepacks,
      List<String> new_incompatible_resourcepacks,
      List<ServerInfo> new_servers,
      Map<String, List<Waypoint>> new_waypoints,
      Set<String> need_delete,
      Map<String, FileDownloadInfo> need_download) {
    this.new_resourcepacks = new_resourcepacks;
    this.new_incompatible_resourcepacks = new_incompatible_resourcepacks;
    this.new_servers = new_servers;
    this.new_waypoints = new_waypoints;
    this.need_delete = need_delete;
    this.need_download = need_download;
  }

  public List<String> getNew_resourcepacks() { return new_resourcepacks; }
  public void setNew_resourcepacks(List<String> new_resourcepacks) {
    this.new_resourcepacks = new_resourcepacks;
  }

  public List<String> getNew_incompatible_resourcepacks() { return new_incompatible_resourcepacks; }
  public void setNew_incompatible_resourcepacks(List<String> v) {
    this.new_incompatible_resourcepacks = v;
  }

  public List<ServerInfo> getNew_servers() { return new_servers; }
  public void setNew_servers(List<ServerInfo> new_servers) { this.new_servers = new_servers; }

  public Map<String, List<Waypoint>> getNew_waypoints() { return new_waypoints; }
  public void setNew_waypoints(Map<String, List<Waypoint>> new_waypoints) {
    this.new_waypoints = new_waypoints;
  }

  public Set<String> getNeed_delete() { return need_delete; }
  public void setNeed_delete(Set<String> need_delete) { this.need_delete = need_delete; }

  public Map<String, FileDownloadInfo> getNeed_download() { return need_download; }
  public void setNeed_download(Map<String, FileDownloadInfo> need_download) {
    this.need_download = need_download;
  }
}