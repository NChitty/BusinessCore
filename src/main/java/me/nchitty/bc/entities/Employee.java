package me.nchitty.bc.entities;

import jakarta.persistence.*;
import me.nchitty.bc.entities.converters.PlayerConverter;
import me.nchitty.bc.util.PlaceholderPattern;
import org.bukkit.entity.Player;

import java.util.Objects;

@Entity
public class Employee {
  @Id
  private Long id;

  @Convert(converter = PlayerConverter.class)
  private Player player;

  @ManyToOne
  private Business business;
  private int      completedJobs;

  @OneToOne
  private Job job;

  public void setId(Long id) { this.id = id; }

  @Id
  @GeneratedValue
  public Long getId() { return id;}

  @PlaceholderPattern(pattern = "<employee_business>")
  public Business getBusiness() { return business; }

  public void setBusiness(Business business) { this.business = business; }

  @PlaceholderPattern(pattern = "<employee_completed_jobs>")
  public int getCompletedJobs() { return completedJobs; }

  public void setCompletedJobs(int completedJobs) { this.completedJobs = completedJobs; }

  @PlaceholderPattern(pattern = "<employee_current_job>")
  public Job getJob() { return job; }

  public void setJob(Job job) { this.job = job; }

  @Override
  public boolean equals(Object o) {
    if (this == o) { return true; }
    if (!( o instanceof Employee )) { return false; }
    Employee employee = (Employee) o;
    return id.equals(employee.id) && player.equals(employee.player);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, player);
  }
}
