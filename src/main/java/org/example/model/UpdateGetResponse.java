package org.example.model;

import java.util.Set;

public class UpdateGetResponse {
  private Set<String> files_paths;
  private Set<String> dirs_paths;
  private Set<String> strict_files_paths;
  private Set<String> strict_dirs_paths;

  public UpdateGetResponse() {
  }

  public UpdateGetResponse(Set<String> files_paths, Set<String> dirs_paths, Set<String> strict_files_paths, Set<String> strict_dirs_paths) {
    this.files_paths = files_paths;
    this.dirs_paths = dirs_paths;
    this.strict_files_paths = strict_files_paths;
    this.strict_dirs_paths = strict_dirs_paths;
  }

  public Set<String> getFiles_paths() {
    return files_paths;
  }

  public void setFiles_paths(Set<String> files_paths) {
    this.files_paths = files_paths;
  }

  public Set<String> getDirs_paths() {
    return dirs_paths;
  }

  public void setDirs_paths(Set<String> dirs_paths) {
    this.dirs_paths = dirs_paths;
  }

  public Set<String> getStrict_files_paths() {
    return strict_files_paths;
  }

  public void setStrict_files_paths(Set<String> strict_files_paths) {
    this.strict_files_paths = strict_files_paths;
  }

  public Set<String> getStrict_dirs_paths() {
    return strict_dirs_paths;
  }

  public void setStrict_dirs_paths(Set<String> strict_dirs_paths) {
    this.strict_dirs_paths = strict_dirs_paths;
  }
}