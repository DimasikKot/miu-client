package org.example.model;

public class MinecraftInfo {

    private String version;
    private String loader;
    private String loaderVersion;

    public MinecraftInfo() {
    }

    public MinecraftInfo(String version, String loader, String loaderVersion) {

        this.version = version;
        this.loader = loader;
        this.loaderVersion = loaderVersion;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLoader() {
        return loader;
    }

    public void setLoader(String loader) {
        this.loader = loader;
    }

    public String getLoaderVersion() {
        return loaderVersion;
    }

    public void setLoaderVersion(String loaderVersion) {
        this.loaderVersion = loaderVersion;
    }

}
