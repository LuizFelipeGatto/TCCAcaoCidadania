package br.org.ifsuldeminas.acaocidadania.web.rest;

import br.org.ifsuldeminas.acaocidadania.domain.Familia;
import br.org.ifsuldeminas.acaocidadania.repository.FamiliaRepository;
import br.org.ifsuldeminas.acaocidadania.service.FamiliaService;
import br.org.ifsuldeminas.acaocidadania.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link br.org.ifsuldeminas.acaocidadania.domain.Familia}.
 */
@RestController
@RequestMapping("/api")
public class FamiliaResource {

    private final Logger log = LoggerFactory.getLogger(FamiliaResource.class);

    private static final String ENTITY_NAME = "familia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FamiliaService familiaService;

    private final FamiliaRepository familiaRepository;

    public FamiliaResource(FamiliaService familiaService, FamiliaRepository familiaRepository) {
        this.familiaService = familiaService;
        this.familiaRepository = familiaRepository;
    }

    /**
     * {@code POST  /familias} : Create a new familia.
     *
     * @param familia the familia to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new familia, or with status {@code 400 (Bad Request)} if the familia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/familias")
    public ResponseEntity<Familia> createFamilia(@Valid @RequestBody Familia familia) throws URISyntaxException {
        log.debug("REST request to save Familia : {}", familia);
        if (familia.getId() != null) {
            throw new BadRequestAlertException("A new familia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Familia result = familiaService.save(familia);
        return ResponseEntity
            .created(new URI("/api/familias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /familias/:id} : Updates an existing familia.
     *
     * @param id the id of the familia to save.
     * @param familia the familia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated familia,
     * or with status {@code 400 (Bad Request)} if the familia is not valid,
     * or with status {@code 500 (Internal Server Error)} if the familia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/familias/{id}")
    public ResponseEntity<Familia> updateFamilia(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Familia familia
    ) throws URISyntaxException {
        log.debug("REST request to update Familia : {}, {}", id, familia);
        if (familia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, familia.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familiaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Familia result = familiaService.update(familia);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, familia.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /familias/:id} : Partial updates given fields of an existing familia, field will ignore if it is null
     *
     * @param id the id of the familia to save.
     * @param familia the familia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated familia,
     * or with status {@code 400 (Bad Request)} if the familia is not valid,
     * or with status {@code 404 (Not Found)} if the familia is not found,
     * or with status {@code 500 (Internal Server Error)} if the familia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/familias/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Familia> partialUpdateFamilia(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Familia familia
    ) throws URISyntaxException {
        log.debug("REST request to partial update Familia partially : {}, {}", id, familia);
        if (familia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, familia.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!familiaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Familia> result = familiaService.partialUpdate(familia);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, familia.getId().toString())
        );
    }

    /**
     * {@code GET  /familias} : get all the familias.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of familias in body.
     */
    @GetMapping("/familias")
    public ResponseEntity<List<Familia>> getAllFamilias(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Familias");
        Page<Familia> page = familiaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /familias/:id} : get the "id" familia.
     *
     * @param id the id of the familia to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the familia, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/familias/{id}")
    public ResponseEntity<Familia> getFamilia(@PathVariable Long id) {
        log.debug("REST request to get Familia : {}", id);
        Optional<Familia> familia = familiaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(familia);
    }

    /**
     * {@code DELETE  /familias/:id} : delete the "id" familia.
     *
     * @param id the id of the familia to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/familias/{id}")
    public ResponseEntity<Void> deleteFamilia(@PathVariable Long id) {
        log.debug("REST request to delete Familia : {}", id);
        familiaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
