package org.example.model;

public class ManifestFile {
    private String name;
    private String path;
    private String sha256;
    private long size;

    public ManifestFile() {
    }

    public ManifestFile(String name, String path, String sha256, long size) {
        this.name = name;
        this.path = path;
        this.sha256 = sha256;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSha256() {
        return sha256;
    }

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}