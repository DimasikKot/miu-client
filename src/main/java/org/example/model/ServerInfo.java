package org.example.model;

public class ServerInfo {
  private String name;
  private String ip;

  public ServerInfo() {
  }

  public ServerInfo(String name, String ip) {
    this.name = name;
    this.ip = ip;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }
}