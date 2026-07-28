package org.example.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientManifest {

    private MinecraftInfo minecraft;

    private Map<String, ManifestFile> files = new HashMap<>();

    private List<ServerInfo> servers = new ArrayList<>();

    public ClientManifest() {
    }

    public MinecraftInfo getMinecraft() {
        return minecraft;
    }

    public void setMinecraft(MinecraftInfo minecraft) {
        this.minecraft = minecraft;
    }

    public Map<String, ManifestFile> getFiles() {
        return files;
    }

    public void setFiles(Map<String, ManifestFile> files) {
        this.files = files;
    }

    public List<ServerInfo> getServers() {
        return servers;
    }

    public void setServers(List<ServerInfo> servers) {
        this.servers = servers;
    }

}
