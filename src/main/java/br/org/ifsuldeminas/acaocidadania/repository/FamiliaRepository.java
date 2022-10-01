package br.org.ifsuldeminas.acaocidadania.repository;

import br.org.ifsuldeminas.acaocidadania.domain.Familia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Familia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FamiliaRepository extends JpaRepository<Familia, Long> {}
