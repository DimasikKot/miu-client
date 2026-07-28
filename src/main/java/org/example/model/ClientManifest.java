package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class ClientManifest {

    private MinecraftInfo minecraft;

    private List<ManifestFile> files = new ArrayList<>();

    private List<ServerInfo> servers = new ArrayList<>();

    public ClientManifest() {
    }

    public MinecraftInfo getMinecraft() {
        return minecraft;
    }

    public void setMinecraft(MinecraftInfo minecraft) {
        this.minecraft = minecraft;
    }

    public List<ManifestFile> getFiles() {
        return files;
    }

    public void setFiles(List<ManifestFile> files) {
        this.files = files;
    }

    public List<ServerInfo> getServers() {
        return servers;
    }

    public void setServers(List<ServerInfo> servers) {
        this.servers = servers;
    }

}
