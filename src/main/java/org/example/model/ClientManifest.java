package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientManifest {
    private ManifestFile pack;
    private ManifestFile instance;
    private Map<String, ManifestFile> files = new HashMap<>();
    private List<ServerInfo> servers = new ArrayList<>();
    @JsonProperty("resource_packs")
    private List<String> resourcePacks = new ArrayList<>();

    public ClientManifest() {
    }

    public void setPack(ManifestFile pack) {
        this.pack = pack;
    }

    public void setInstance(ManifestFile instance) {
        this.instance = instance;
    }

    public Map<String, ManifestFile> getFiles() {
        return files;
    }

    public void setServers(List<ServerInfo> servers) {
        this.servers = servers;
    }

    public void setResourcePacks(List<String> resourcePacks) {
        this.resourcePacks = resourcePacks;
    }
}
