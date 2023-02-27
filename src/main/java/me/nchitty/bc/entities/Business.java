package me.nchitty.bc.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
public class Business {
  private Long id;
  private String name;
  private UUID ownerUUID;
  private BigDecimal balance;

  @OneToMany(targetEntity = Employee.class)
  private List<Employee> employees;

  public void setId(Long id) {
    this.id = id;
  }

  @Id
  @GeneratedValue
  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public UUID getOwnerUUID() {
    return ownerUUID;
  }

  public void setOwnerUUID(UUID ownerUUID) {
    this.ownerUUID = ownerUUID;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public List<Employee> getEmployees() {
    return employees;
  }

  public void setEmployees(List<Employee> employees) {
    this.employees = employees;
  }
}
