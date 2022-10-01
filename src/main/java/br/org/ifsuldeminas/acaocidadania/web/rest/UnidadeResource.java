package br.org.ifsuldeminas.acaocidadania.web.rest;

import br.org.ifsuldeminas.acaocidadania.domain.Unidade;
import br.org.ifsuldeminas.acaocidadania.repository.UnidadeRepository;
import br.org.ifsuldeminas.acaocidadania.service.UnidadeService;
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
 * REST controller for managing {@link br.org.ifsuldeminas.acaocidadania.domain.Unidade}.
 */
@RestController
@RequestMapping("/api")
public class UnidadeResource {

    private final Logger log = LoggerFactory.getLogger(UnidadeResource.class);

    private static final String ENTITY_NAME = "unidade";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UnidadeService unidadeService;

    private final UnidadeRepository unidadeRepository;

    public UnidadeResource(UnidadeService unidadeService, UnidadeRepository unidadeRepository) {
        this.unidadeService = unidadeService;
        this.unidadeRepository = unidadeRepository;
    }

    /**
     * {@code POST  /unidades} : Create a new unidade.
     *
     * @param unidade the unidade to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new unidade, or with status {@code 400 (Bad Request)} if the unidade has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/unidades")
    public ResponseEntity<Unidade> createUnidade(@Valid @RequestBody Unidade unidade) throws URISyntaxException {
        log.debug("REST request to save Unidade : {}", unidade);
        if (unidade.getId() != null) {
            throw new BadRequestAlertException("A new unidade cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Unidade result = unidadeService.save(unidade);
        return ResponseEntity
            .created(new URI("/api/unidades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /unidades/:id} : Updates an existing unidade.
     *
     * @param id the id of the unidade to save.
     * @param unidade the unidade to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unidade,
     * or with status {@code 400 (Bad Request)} if the unidade is not valid,
     * or with status {@code 500 (Internal Server Error)} if the unidade couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/unidades/{id}")
    public ResponseEntity<Unidade> updateUnidade(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Unidade unidade
    ) throws URISyntaxException {
        log.debug("REST request to update Unidade : {}, {}", id, unidade);
        if (unidade.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unidade.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unidadeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Unidade result = unidadeService.update(unidade);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, unidade.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /unidades/:id} : Partial updates given fields of an existing unidade, field will ignore if it is null
     *
     * @param id the id of the unidade to save.
     * @param unidade the unidade to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unidade,
     * or with status {@code 400 (Bad Request)} if the unidade is not valid,
     * or with status {@code 404 (Not Found)} if the unidade is not found,
     * or with status {@code 500 (Internal Server Error)} if the unidade couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/unidades/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Unidade> partialUpdateUnidade(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Unidade unidade
    ) throws URISyntaxException {
        log.debug("REST request to partial update Unidade partially : {}, {}", id, unidade);
        if (unidade.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unidade.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!unidadeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Unidade> result = unidadeService.partialUpdate(unidade);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, unidade.getId().toString())
        );
    }

    /**
     * {@code GET  /unidades} : get all the unidades.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of unidades in body.
     */
    @GetMapping("/unidades")
    public ResponseEntity<List<Unidade>> getAllUnidades(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Unidades");
        Page<Unidade> page = unidadeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /unidades/:id} : get the "id" unidade.
     *
     * @param id the id of the unidade to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the unidade, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/unidades/{id}")
    public ResponseEntity<Unidade> getUnidade(@PathVariable Long id) {
        log.debug("REST request to get Unidade : {}", id);
        Optional<Unidade> unidade = unidadeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(unidade);
    }

    /**
     * {@code DELETE  /unidades/:id} : delete the "id" unidade.
     *
     * @param id the id of the unidade to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/unidades/{id}")
    public ResponseEntity<Void> deleteUnidade(@PathVariable Long id) {
        log.debug("REST request to delete Unidade : {}", id);
        unidadeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
