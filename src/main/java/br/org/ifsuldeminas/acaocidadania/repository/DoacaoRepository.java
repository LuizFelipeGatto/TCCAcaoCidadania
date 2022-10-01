package br.org.ifsuldeminas.acaocidadania.repository;

import br.org.ifsuldeminas.acaocidadania.domain.Doacao;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Doacao entity.
 */
@Repository
public interface DoacaoRepository extends JpaRepository<Doacao, Long> {
    default Optional<Doacao> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Doacao> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Doacao> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct doacao from Doacao doacao left join fetch doacao.cesta left join fetch doacao.familia",
        countQuery = "select count(distinct doacao) from Doacao doacao"
    )
    Page<Doacao> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct doacao from Doacao doacao left join fetch doacao.cesta left join fetch doacao.familia")
    List<Doacao> findAllWithToOneRelationships();

    @Query("select doacao from Doacao doacao left join fetch doacao.cesta left join fetch doacao.familia where doacao.id =:id")
    Optional<Doacao> findOneWithToOneRelationships(@Param("id") Long id);
}
