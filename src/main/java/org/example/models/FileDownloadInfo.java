package org.example.models;

public class FileDownloadInfo {
  private String url;
  private String sha256;
  private int size;

  public FileDownloadInfo() {
  }

  public FileDownloadInfo(String url, String sha256, int size) {
    this.url = url;
    this.sha256 = sha256;
    this.size = size;
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