package br.org.ifsuldeminas.acaocidadania.service;

import br.org.ifsuldeminas.acaocidadania.domain.Doacao;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Doacao}.
 */
public interface DoacaoService {
    /**
     * Save a doacao.
     *
     * @param doacao the entity to save.
     * @return the persisted entity.
     */
    Doacao save(Doacao doacao);

    /**
     * Updates a doacao.
     *
     * @param doacao the entity to update.
     * @return the persisted entity.
     */
    Doacao update(Doacao doacao);

    /**
     * Partially updates a doacao.
     *
     * @param doacao the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Doacao> partialUpdate(Doacao doacao);

    /**
     * Get all the doacaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Doacao> findAll(Pageable pageable);

    /**
     * Get all the doacaos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Doacao> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" doacao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Doacao> findOne(Long id);

    /**
     * Delete the "id" doacao.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
