package me.nchitty.bc.entities.embeddable;

import jakarta.persistence.Embeddable;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Objects;

@Embeddable
public class Location {
  private String worldName;
  private double x;
  private double y;
  private double z;
  private float  pitch;
  private float  yaw;

  public Location(org.bukkit.Location from) {
    this.worldName = from.getWorld().getName();
    this.x = from.getX();
    this.y = from.getY();
    this.z = from.getZ();
    this.pitch = from.getPitch();
    this.yaw = from.getYaw();
  }

  public Location() {
  }

  public static org.bukkit.Location fromLocation(Location location) {
    World world = Bukkit.getWorld(location.worldName);

    assert world != null;

    org.bukkit.Location bukkitLoc = new org.bukkit.Location(
        world,
        location.x,
        location.y,
        location.z,
        location.yaw,
        location.pitch
    );
    return bukkitLoc;
  }

  public String getWorldName() {
    return worldName;
  }

  public void setWorldName(String worldName) {
    this.worldName = worldName;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public double getZ() {
    return z;
  }

  public void setZ(double z) {
    this.z = z;
  }

  public float getPitch() {
    return pitch;
  }

  public void setPitch(float pitch) {
    this.pitch = pitch;
  }

  public float getYaw() {
    return yaw;
  }

  public void setYaw(float yaw) {
    this.yaw = yaw;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) { return true; }
    if (!( o instanceof Location )) { return false; }
    Location location = (Location) o;
    return Double.compare(location.x, x) == 0
        && Double.compare(location.y, y) == 0
        && Double.compare(location.z, z) == 0
        && Float.compare(location.pitch, pitch) == 0
        && Float.compare(location.yaw, yaw) == 0
        && worldName.equals(location.worldName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(worldName, x, y, z, pitch, yaw);
  }
}
