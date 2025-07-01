package com.axonivy.demo;

import com.axonivy.demo.persistence.demo.Player;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;

@Repository
public interface PlayerRepo extends CrudRepository<Player, Long> {

}
