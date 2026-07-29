package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class UpdateResponse {
    private int version;
    private List<DownloadFile> download = new ArrayList<>();
    private List<String> delete = new ArrayList<>();
    private List<ServerInfo> servers = new ArrayList<>();
    @JsonProperty("resource_packs")
    private List<String> resourcePacks = new ArrayList<>();

    public UpdateResponse() {
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<DownloadFile> getDownload() {
        return download;
    }

    public void setDownload(List<DownloadFile> download) {
        this.download = download;
    }

    public List<String> getDelete() {
        return delete;
    }

    public void setDelete(List<String> delete) {
        this.delete = delete;
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