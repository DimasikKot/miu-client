package org.example.model;

public class MiuClientManifest {
  private String MiuClientFile;
  private String PreLaunchCommand;
  private String path;
  private String url;
  private String sha256;
  private int size;

  public MiuClientManifest() {
  }

  public MiuClientManifest(String MiuClientFile, String PreLaunchCommand, String path, String url, String sha256, int size) {
    this.MiuClientFile = MiuClientFile;
    this.PreLaunchCommand = PreLaunchCommand;
    this.path = path;
    this.url = url;
    this.sha256 = sha256;
    this.size = size;
  }

  public String getMiuClientFile() {
    return MiuClientFile;
  }

  public void setMiuClientFile(String MiuClientFile) {
    this.MiuClientFile = MiuClientFile;
  }

  public String getPreLaunchCommand() {
    return PreLaunchCommand;
  }

  public void setPreLaunchCommand(String PreLaunchCommand) {
    this.PreLaunchCommand = PreLaunchCommand;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getSha256() {
    return sha256;
  }

  public void setSha256(String sha256) {
    this.sha256 = sha256;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }
}