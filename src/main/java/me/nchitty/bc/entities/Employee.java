package me.nchitty.bc.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

@Entity
public class Employee {
  private Long id;
  private String employeeName;
  private UUID uniqueId;

  @ManyToOne
  private Business business;
  private int completedJobs;
  private int jobID;

  public void setId(Long id) {
    this.id = id;
  }

  @Id
  @GeneratedValue
  public Long getId() {
    return id;
  }

  public String getEmployeeName() {
    return employeeName;
  }

  public void setEmployeeName(String employeeName) {
    this.employeeName = employeeName;
  }

  public UUID getUniqueId() {
    return uniqueId;
  }

  public void setUniqueId(UUID uniqueId) {
    this.uniqueId = uniqueId;
  }

  public Business getBusiness() {
    return business;
  }

  public void setBusiness(Business business) {
    this.business = business;
  }

  public int getCompletedJobs() {
    return completedJobs;
  }

  public void setCompletedJobs(int completedJobs) {
    this.completedJobs = completedJobs;
  }

  public int getJobID() {
    return jobID;
  }

  public void setJobID(int jobID) {
    this.jobID = jobID;
  }
}
