package com.axonivy.demo;

import java.util.List;

import com.axonivy.demo.persistence.demo.Player;

import jakarta.data.repository.By;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Find;
import jakarta.data.repository.Repository;

@Repository
public interface PlayerRepo extends CrudRepository<Player, Long> {

  @Find
  List<Player> findByLastName(@By("lastName") String lastName);

}
