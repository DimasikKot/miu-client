package org.example.model;

public class DownloadFile {
    private String path;
    private String sha256;
    private long size;
    private String url;

    public DownloadFile() {
    }

    public String getPath() {
        return path;
    }

    public String getSha256() {
        return sha256;
    }

    public long getSize() {
        return size;
    }

    public String getUrl() {
        return url;
    }
}
