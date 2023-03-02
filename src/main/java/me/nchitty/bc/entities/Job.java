package me.nchitty.bc.entities;

import jakarta.persistence.*;
import me.nchitty.bc.entities.converters.PlayerConverter;
import me.nchitty.bc.entities.embeddable.Location;
import me.nchitty.bc.util.PlaceholderPattern;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Job {
  private Long id;

  @Convert(converter = PlayerConverter.class)
  private Player creator;
  private String description;

  @Embedded
  private Location   loc;
  private BigDecimal pay;

  @Nullable
  @OneToOne
  private Employee worker;
  private boolean  claimed = false;

  public void setId(Long id) {
    this.id = id;
  }

  @Id
  @GeneratedValue
  @PlaceholderPattern(pattern = "<job_id>")
  public Long getId() {
    return id;
  }

  public Player getCreator() {
    return creator;
  }

  @PlaceholderPattern(pattern = "<job_creator>")
  public void setCreator(Player creator) {
    this.creator = creator;
  }

  @PlaceholderPattern(pattern = "<job_description>")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @PlaceholderPattern(pattern = "<job_location>")
  public Location getLoc() {
    return loc;
  }

  public void setLoc(Location loc) {
    this.loc = loc;
  }

  @PlaceholderPattern(pattern = "<job_payment>")
  public BigDecimal getPay() {
    return pay;
  }

  public void setPay(BigDecimal pay) {
    this.pay = pay;
  }

  @Nullable
  @PlaceholderPattern(pattern = "<job_worker>")
  public Employee getWorker() {
    return worker;
  }

  public void setWorker(@Nullable Employee worker) {
    this.worker = worker;
  }

  public boolean isClaimed() {
    return claimed;
  }

  public void setClaimed(boolean claimed) {
    this.claimed = claimed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) { return true; }
    if (!( o instanceof Job )) { return false; }
    Job job = (Job) o;
    return id.equals(job.id) && creator.equals(job.creator);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, creator);
  }
}
