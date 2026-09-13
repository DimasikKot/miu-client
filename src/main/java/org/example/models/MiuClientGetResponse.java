package org.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MiuClientGetResponse {
  @JsonProperty("pre_launch_command")
  private String preLaunchCommand;

  @JsonProperty("miu_client_path")
  private String miuClientPath;

  @JsonProperty("miu_client_file")
  private FileDownloadInfo miuClientFile;

  @JsonProperty("mmc_pack_path")
  private String mmcPackPath;

  @JsonProperty("mmc_pack_file")
  private FileDownloadInfo mmcPackFile;

  public MiuClientGetResponse() {
  }

  public MiuClientGetResponse(String preLaunchCommand, String miuClientPath, FileDownloadInfo miuClientFile,
                              String mmcPackPath, FileDownloadInfo mmcPackFile) {
    this.preLaunchCommand = preLaunchCommand;
    this.miuClientPath = miuClientPath;
    this.miuClientFile = miuClientFile;
    this.mmcPackPath = mmcPackPath;
    this.mmcPackFile = mmcPackFile;
  }

  public String getPreLaunchCommand() {
    return preLaunchCommand;
  }

  public void setPreLaunchCommand(String preLaunchCommand) {
    this.preLaunchCommand = preLaunchCommand;
  }

  public String getMiuClientPath() {
    return miuClientPath;
  }

  public void setMiuClientPath(String miuClientPath) {
    this.miuClientPath = miuClientPath;
  }

  public FileDownloadInfo getMiuClientFile() {
    return miuClientFile;
  }

  public void setMiuClientFile(FileDownloadInfo miuClientFile) {
    this.miuClientFile = miuClientFile;
  }

  public String getMmcPackPath() {
    return mmcPackPath;
  }

  public void setMmcPackPath(String mmcPackPath) {
    this.mmcPackPath = mmcPackPath;
  }

  public FileDownloadInfo getMmcPackFile() {
    return mmcPackFile;
  }

  public void setMmcPackFile(FileDownloadInfo mmcPackFile) {
    this.mmcPackFile = mmcPackFile;
  }
}