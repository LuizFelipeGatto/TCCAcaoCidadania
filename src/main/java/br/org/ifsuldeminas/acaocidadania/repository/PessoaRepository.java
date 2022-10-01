package br.org.ifsuldeminas.acaocidadania.repository;

import br.org.ifsuldeminas.acaocidadania.domain.Pessoa;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pessoa entity.
 */
@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    default Optional<Pessoa> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Pessoa> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Pessoa> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct pessoa from Pessoa pessoa left join fetch pessoa.unidade left join fetch pessoa.familia",
        countQuery = "select count(distinct pessoa) from Pessoa pessoa"
    )
    Page<Pessoa> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct pessoa from Pessoa pessoa left join fetch pessoa.unidade left join fetch pessoa.familia")
    List<Pessoa> findAllWithToOneRelationships();

    @Query("select pessoa from Pessoa pessoa left join fetch pessoa.unidade left join fetch pessoa.familia where pessoa.id =:id")
    Optional<Pessoa> findOneWithToOneRelationships(@Param("id") Long id);
}
