package com.axonivy.demo.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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

  @Test
  void useQuery() {
    var orville = new Player();
    orville.setFirstName("Orville");
    orville.setLastName("Wright");
    orville.setBirthDate(new Date(1871, 8, 19));

    var wilbur = new Player();
    wilbur.setFirstName("Wilbur");
    wilbur.setLastName("Wright");
    wilbur.setBirthDate(new Date(1867, 4, 16));

    var louis = new Player();
    louis.setFirstName("Louis Charles Joseph");
    louis.setLastName("Blériot");
    louis.setBirthDate(new Date(1872, 7, 1));

    List<Player> pilots = List.of(orville, wilbur, louis);
    players.insertAll(pilots);

    List<Player> wrightBrothers = players.findByLastName("Wright");
    assertThat(wrightBrothers)
        .extracting(Player::getFirstName)
        .containsOnly("Orville", "Wilbur");

    assertThat(players.findByLastName("Blériot"))
        .extracting(Player::getFirstName)
        .containsOnly("Louis Charles Joseph");

    players.deleteAll(pilots);
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
