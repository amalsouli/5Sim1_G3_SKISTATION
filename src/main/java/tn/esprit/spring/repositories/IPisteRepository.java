package tn.esprit.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import tn.esprit.spring.entities.Piste;

import java.util.Optional;

public interface IPisteRepository extends JpaRepository<Piste, Long> {
    Optional<Piste> findByNamePiste(String namePiste);

}
