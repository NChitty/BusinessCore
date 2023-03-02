package me.nchitty.bc.managers;

import co.aikar.commands.BukkitCommandExecutionContext;
import co.aikar.commands.ConditionFailedException;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.contexts.ContextResolver;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import me.nchitty.bc.BusinessCore;
import me.nchitty.bc.entities.Business;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class BusinessManager implements ContextResolver<Business, BukkitCommandExecutionContext> {
  private final  SessionFactory  sessionFactory;
  private static BusinessManager instance;

  private BusinessManager() {
    this.sessionFactory = BusinessCore.getInstance().getSessionFactory();
  }

  public static BusinessManager getInstance() {
    if (instance == null) { instance = new BusinessManager(); }
    return instance;
  }

  public static void notOwnerCondition(Player sender) {
    if (this.isOwner(sender)) {
      throw new ConditionFailedException(
          "You already own a business." // TODO handle customizable messages
      );
    }
  }

  public static void isOwnerCondition(Player sender) {
    if (!this.isOwner(sender)) {
      throw new ConditionFailedException(
          "You do not own a business." // TODO handle customizable messages
      );
    }
  }

  public Business findByOwner(Player player) {
    Session                 session      = sessionFactory.getCurrentSession();
    Business                business     = null;
    CriteriaBuilder         cb           = session.getCriteriaBuilder();
    CriteriaQuery<Business> cq           = cb.createQuery(Business.class);
    Root<Business>          baseBusiness = cq.from(Business.class);
    Predicate playerOwner = cb.equal(
        baseBusiness.get("owner"),
        player.getUniqueId()
    );

    return getBusiness(session, cq, playerOwner);
  }

  public Business findById(Long id) {
    Session  session  = sessionFactory.getCurrentSession();
    Business business = null;

    session.beginTransaction();
    business = session.get(Business.class, id);
    session.getTransaction().commit();
    session.close();

    if (business == null) {
      throw new InvalidCommandArgument("Could not find a business with the given ID");
    }

    return business;
  }

  public Business findByName(String name) {
    Session                 session      = sessionFactory.getCurrentSession();
    Business                business     = null;
    CriteriaBuilder         cb           = session.getCriteriaBuilder();
    CriteriaQuery<Business> cq           = cb.createQuery(Business.class);
    Root<Business>          baseBusiness = cq.from(Business.class);
    Predicate namePredicate = cb.like(
        baseBusiness.get("name"),
        name
    );

    return getBusiness(session, cq, namePredicate);
  }

  private Business getBusiness(
      Session session,
      CriteriaQuery<Business> cq,
      Predicate predicate
  )
  {
    Business business;
    cq.where(predicate);

    TypedQuery<Business> query = session.createQuery(cq);

    try {
      business = query.getSingleResult();
    } catch (NoResultException nre) {
      throw new InvalidCommandArgument(
          "Unable to find a corresponding business based on the user and provided arguments.");
    } finally {
      session.close();
    }

    return business;
  }

  @Override
  public Business getContext(BukkitCommandExecutionContext executionContext)
      throws InvalidCommandArgument
  {
    if (executionContext.getSender() instanceof Player && !executionContext.getCmd()
        .getCommand()
        .equals("business admin"))
    {
      return this.findByOwner(executionContext.getPlayer());
    }
    if (executionContext.getNumParams() == 1
        && StringUtils.isNumeric(executionContext.getFirstArg()))
    {
      return this.findById(Long.parseLong(executionContext.getFirstArg()));
    } else {
      return this.findByName(executionContext.joinArgs());
    }
  }

  public Business createBusiness(Player sender, String businessName) {
    Session session = sessionFactory.getCurrentSession();

    Business b = new Business(sender, businessName);

    session.beginTransaction();
    session.persist(b);
    session.getTransaction().commit();
    session.close();

    return b;
  }
}
