package me.nchitty.bc.entities;

import jakarta.persistence.*;
import me.nchitty.bc.entities.converters.PlayerConverter;
import me.nchitty.bc.util.PlaceholderPattern;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Business {
  private Long   id;
  private String name;

  @Convert(converter = PlayerConverter.class)
  private Player     owner;
  private BigDecimal balance;

  @OneToMany(targetEntity = Employee.class)
  private List<Employee> employees;

  public void setId(Long id) { this.id = id; }

  @Id
  @GeneratedValue
  @PlaceholderPattern(pattern = "<business_id>")
  public Long getId() { return id; }

  @PlaceholderPattern(pattern = "<business_name>")
  public String getName() { return name; }

  public void setName(String name) { this.name = name; }

  public Player getOwner() { return owner; }

  public void setOwner(Player owner) { this.owner = owner; }

  @PlaceholderPattern(pattern = "<business_balance>")
  public BigDecimal getBalance() { return balance; }

  public void setBalance(BigDecimal balance) { this.balance = balance; }

  public List<Employee> getEmployees() { return employees; }

  public void setEmployees(List<Employee> employees) { this.employees = employees; }

  @Override
  public boolean equals(Object o) {
    if (this == o) { return true; }
    if (!( o instanceof Business )) { return false; }
    Business business = (Business) o;
    return id.equals(business.id)
        && name.equals(business.name)
        && owner.getUniqueId().equals(business.getOwner().getUniqueId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, owner.getUniqueId());
  }
}
