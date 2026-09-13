package org.example.models;

public class FileInfo {
  private String name;
  private String sha256;
  private int size;

  public FileInfo() {
  }

  public FileInfo(String name, String sha256, int size) {
    this.name = name;
    this.sha256 = sha256;
    this.size = size;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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