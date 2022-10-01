package br.org.ifsuldeminas.acaocidadania.web.rest;

import br.org.ifsuldeminas.acaocidadania.domain.CestaDescricao;
import br.org.ifsuldeminas.acaocidadania.repository.CestaDescricaoRepository;
import br.org.ifsuldeminas.acaocidadania.service.CestaDescricaoService;
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
 * REST controller for managing {@link br.org.ifsuldeminas.acaocidadania.domain.CestaDescricao}.
 */
@RestController
@RequestMapping("/api")
public class CestaDescricaoResource {

    private final Logger log = LoggerFactory.getLogger(CestaDescricaoResource.class);

    private static final String ENTITY_NAME = "cestaDescricao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CestaDescricaoService cestaDescricaoService;

    private final CestaDescricaoRepository cestaDescricaoRepository;

    public CestaDescricaoResource(CestaDescricaoService cestaDescricaoService, CestaDescricaoRepository cestaDescricaoRepository) {
        this.cestaDescricaoService = cestaDescricaoService;
        this.cestaDescricaoRepository = cestaDescricaoRepository;
    }

    /**
     * {@code POST  /cesta-descricaos} : Create a new cestaDescricao.
     *
     * @param cestaDescricao the cestaDescricao to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cestaDescricao, or with status {@code 400 (Bad Request)} if the cestaDescricao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cesta-descricaos")
    public ResponseEntity<CestaDescricao> createCestaDescricao(@Valid @RequestBody CestaDescricao cestaDescricao)
        throws URISyntaxException {
        log.debug("REST request to save CestaDescricao : {}", cestaDescricao);
        if (cestaDescricao.getId() != null) {
            throw new BadRequestAlertException("A new cestaDescricao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CestaDescricao result = cestaDescricaoService.save(cestaDescricao);
        return ResponseEntity
            .created(new URI("/api/cesta-descricaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cesta-descricaos/:id} : Updates an existing cestaDescricao.
     *
     * @param id the id of the cestaDescricao to save.
     * @param cestaDescricao the cestaDescricao to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cestaDescricao,
     * or with status {@code 400 (Bad Request)} if the cestaDescricao is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cestaDescricao couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cesta-descricaos/{id}")
    public ResponseEntity<CestaDescricao> updateCestaDescricao(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CestaDescricao cestaDescricao
    ) throws URISyntaxException {
        log.debug("REST request to update CestaDescricao : {}, {}", id, cestaDescricao);
        if (cestaDescricao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cestaDescricao.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cestaDescricaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CestaDescricao result = cestaDescricaoService.update(cestaDescricao);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cestaDescricao.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cesta-descricaos/:id} : Partial updates given fields of an existing cestaDescricao, field will ignore if it is null
     *
     * @param id the id of the cestaDescricao to save.
     * @param cestaDescricao the cestaDescricao to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cestaDescricao,
     * or with status {@code 400 (Bad Request)} if the cestaDescricao is not valid,
     * or with status {@code 404 (Not Found)} if the cestaDescricao is not found,
     * or with status {@code 500 (Internal Server Error)} if the cestaDescricao couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cesta-descricaos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CestaDescricao> partialUpdateCestaDescricao(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CestaDescricao cestaDescricao
    ) throws URISyntaxException {
        log.debug("REST request to partial update CestaDescricao partially : {}, {}", id, cestaDescricao);
        if (cestaDescricao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cestaDescricao.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cestaDescricaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CestaDescricao> result = cestaDescricaoService.partialUpdate(cestaDescricao);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cestaDescricao.getId().toString())
        );
    }

    /**
     * {@code GET  /cesta-descricaos} : get all the cestaDescricaos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cestaDescricaos in body.
     */
    @GetMapping("/cesta-descricaos")
    public ResponseEntity<List<CestaDescricao>> getAllCestaDescricaos(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of CestaDescricaos");
        Page<CestaDescricao> page = cestaDescricaoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cesta-descricaos/:id} : get the "id" cestaDescricao.
     *
     * @param id the id of the cestaDescricao to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cestaDescricao, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cesta-descricaos/{id}")
    public ResponseEntity<CestaDescricao> getCestaDescricao(@PathVariable Long id) {
        log.debug("REST request to get CestaDescricao : {}", id);
        Optional<CestaDescricao> cestaDescricao = cestaDescricaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cestaDescricao);
    }

    /**
     * {@code DELETE  /cesta-descricaos/:id} : delete the "id" cestaDescricao.
     *
     * @param id the id of the cestaDescricao to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cesta-descricaos/{id}")
    public ResponseEntity<Void> deleteCestaDescricao(@PathVariable Long id) {
        log.debug("REST request to delete CestaDescricao : {}", id);
        cestaDescricaoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
