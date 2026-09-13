package org.example.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Waypoint {
  private String name;
  private String initials;
  private int x;
  private int y;
  private int z;
  private int color;
  private boolean disabled = false;
  private int type = 0;
  private String set = "gui.xaero_default";

  @JsonProperty("rotate_on_tp")
  private boolean rotateOnTp = false;

  @JsonProperty("tp_yaw")
  private int tpYaw = 0;

  @JsonProperty("visibility_type")
  private int visibilityType = 0;

  private boolean destination = false;

  public Waypoint() {
  }

  public Waypoint(String name, String initials, int x, int y, int z, int color,
      boolean disabled, int type, String set, boolean rotateOnTp, int tpYaw,
      int visibilityType, boolean destination) {
    this.name = name;
    this.initials = initials;
    this.x = x;
    this.y = y;
    this.z = z;
    this.color = color;
    this.disabled = disabled;
    this.type = type;
    this.set = set;
    this.rotateOnTp = rotateOnTp;
    this.tpYaw = tpYaw;
    this.visibilityType = visibilityType;
    this.destination = destination;
  }

  /** Сериализация в формат строки Xaero (аналог Waypoint.to_line). */
  public String toLine() {
    return String.join(":",
        "waypoint",
        name,
        initials,
        String.valueOf(x),
        String.valueOf(y),
        String.valueOf(z),
        String.valueOf(color),
        String.valueOf(disabled),
        String.valueOf(type),
        set,
        String.valueOf(rotateOnTp),
        String.valueOf(tpYaw),
        String.valueOf(visibilityType),
        String.valueOf(destination));
  }

  // --- getters / setters ---

  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public String getInitials() { return initials; }
  public void setInitials(String initials) { this.initials = initials; }

  public int getX() { return x; }
  public void setX(int x) { this.x = x; }

  public int getY() { return y; }
  public void setY(int y) { this.y = y; }

  public int getZ() { return z; }
  public void setZ(int z) { this.z = z; }

  public int getColor() { return color; }
  public void setColor(int color) { this.color = color; }

  public boolean isDisabled() { return disabled; }
  public void setDisabled(boolean disabled) { this.disabled = disabled; }

  public int getType() { return type; }
  public void setType(int type) { this.type = type; }

  public String getSet() { return set; }
  public void setSet(String set) { this.set = set; }

  public boolean isRotateOnTp() { return rotateOnTp; }
  public void setRotateOnTp(boolean rotateOnTp) { this.rotateOnTp = rotateOnTp; }

  public int getTpYaw() { return tpYaw; }
  public void setTpYaw(int tpYaw) { this.tpYaw = tpYaw; }

  public int getVisibilityType() { return visibilityType; }
  public void setVisibilityType(int visibilityType) { this.visibilityType = visibilityType; }

  public boolean isDestination() { return destination; }
  public void setDestination(boolean destination) { this.destination = destination; }
}