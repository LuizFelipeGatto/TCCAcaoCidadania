package br.org.ifsuldeminas.acaocidadania.service;

import br.org.ifsuldeminas.acaocidadania.domain.CestaDescricao;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link CestaDescricao}.
 */
public interface CestaDescricaoService {
    /**
     * Save a cestaDescricao.
     *
     * @param cestaDescricao the entity to save.
     * @return the persisted entity.
     */
    CestaDescricao save(CestaDescricao cestaDescricao);

    /**
     * Updates a cestaDescricao.
     *
     * @param cestaDescricao the entity to update.
     * @return the persisted entity.
     */
    CestaDescricao update(CestaDescricao cestaDescricao);

    /**
     * Partially updates a cestaDescricao.
     *
     * @param cestaDescricao the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CestaDescricao> partialUpdate(CestaDescricao cestaDescricao);

    /**
     * Get all the cestaDescricaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CestaDescricao> findAll(Pageable pageable);

    /**
     * Get the "id" cestaDescricao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CestaDescricao> findOne(Long id);

    /**
     * Delete the "id" cestaDescricao.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
