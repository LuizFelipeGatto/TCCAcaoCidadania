package br.org.ifsuldeminas.acaocidadania.service;

import br.org.ifsuldeminas.acaocidadania.domain.Unidade;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Unidade}.
 */
public interface UnidadeService {
    /**
     * Save a unidade.
     *
     * @param unidade the entity to save.
     * @return the persisted entity.
     */
    Unidade save(Unidade unidade);

    /**
     * Updates a unidade.
     *
     * @param unidade the entity to update.
     * @return the persisted entity.
     */
    Unidade update(Unidade unidade);

    /**
     * Partially updates a unidade.
     *
     * @param unidade the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Unidade> partialUpdate(Unidade unidade);

    /**
     * Get all the unidades.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Unidade> findAll(Pageable pageable);

    /**
     * Get the "id" unidade.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Unidade> findOne(Long id);

    /**
     * Delete the "id" unidade.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
