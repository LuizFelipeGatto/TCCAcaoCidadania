package br.org.ifsuldeminas.acaocidadania.repository;

import br.org.ifsuldeminas.acaocidadania.domain.CestaDescricao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CestaDescricao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CestaDescricaoRepository extends JpaRepository<CestaDescricao, Long> {}
