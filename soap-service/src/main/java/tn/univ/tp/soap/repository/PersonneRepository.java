package tn.univ.tp.soap.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tn.univ.tp.soap.entity.Personne;

public interface PersonneRepository extends JpaRepository<Personne, Long> {
}
