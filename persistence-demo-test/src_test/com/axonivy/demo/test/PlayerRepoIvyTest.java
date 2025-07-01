package com.axonivy.demo.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.hibernate.StatelessSession;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axonivy.demo.PlayerRepo;
import com.axonivy.demo.PlayerRepo_;
import com.axonivy.demo.persistence.demo.Player;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.environment.IvyTest;
import ch.ivyteam.ivy.scripting.objects.Date;

@IvyTest
class PlayerRepoIvyTest {

  private PlayerRepo players;

  @Test
  void useRepo() {
    var all = players.findAll().toList();
    assertThat(all).isEmpty();

    var james = new Player();
    james.setFirstName("James");
    james.setLastName("Bond");
    james.setBirthDate(new Date(1968, 4, 13));

    players.insert(james);
    assertThat(players.findAll().toList())
        .hasSize(1);
    var persisted = players.findAll()
        .filter(p -> "James".equals(p.getFirstName()))
        .findFirst();
    assertThat(persisted).isPresent();
    assertThat(persisted.get().getId())
        .isNotNull();

    players.delete(persisted.get());
    assertThat(players.findAll().toList())
        .isEmpty();
  }

  @BeforeEach
  void setUp() {
    this.players = new PlayerRepo_(session("league"));
  }

  private static StatelessSession session(String persistence) {
    var em = Ivy.persistence().get(persistence).createEntityManager();
    var sessionFactory = (SessionFactoryImpl) em.getEntityManagerFactory();
    return sessionFactory.openStatelessSession();
  }

}
