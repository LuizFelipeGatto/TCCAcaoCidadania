package br.org.ifsuldeminas.acaocidadania.service;

import br.org.ifsuldeminas.acaocidadania.domain.Familia;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Familia}.
 */
public interface FamiliaService {
    /**
     * Save a familia.
     *
     * @param familia the entity to save.
     * @return the persisted entity.
     */
    Familia save(Familia familia);

    /**
     * Updates a familia.
     *
     * @param familia the entity to update.
     * @return the persisted entity.
     */
    Familia update(Familia familia);

    /**
     * Partially updates a familia.
     *
     * @param familia the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Familia> partialUpdate(Familia familia);

    /**
     * Get all the familias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Familia> findAll(Pageable pageable);

    /**
     * Get the "id" familia.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Familia> findOne(Long id);

    /**
     * Delete the "id" familia.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
