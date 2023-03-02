package me.nchitty.bc.managers;

import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.contexts.ContextResolver;
import me.nchitty.bc.BusinessCore;
import me.nchitty.bc.entities.Employee;
import org.hibernate.SessionFactory;

public class EmployeeManager implements ContextResolver<Employee, BukkitCommandExecutionContext> {
  private final  SessionFactory  sessionFactory;
  private static EmployeeManager instance;

  private EmployeeManager() {
    assert BusinessCore.getInstance() != null;
    this.sessionFactory = BusinessCore.getInstance()
        .getSessionFactory();
  }

  public static EmployeeManager getInstance() {
    if (instance == null) { instance = new EmployeeManager(); }
    return instance;
  }

  @Override
  public Employee getContext(BukkitCommandExecutionContext executionContext)
      throws InvalidCommandArgument
  {
    return null;
  }
}
