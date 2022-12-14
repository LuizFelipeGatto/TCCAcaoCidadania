package br.org.ifsuldeminas.acaocidadania.web.rest;

import br.org.ifsuldeminas.acaocidadania.domain.Doacao;
import br.org.ifsuldeminas.acaocidadania.repository.DoacaoRepository;
import br.org.ifsuldeminas.acaocidadania.service.DoacaoService;
import br.org.ifsuldeminas.acaocidadania.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
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
 * REST controller for managing {@link br.org.ifsuldeminas.acaocidadania.domain.Doacao}.
 */
@RestController
@RequestMapping("/api")
public class DoacaoResource {

    private final Logger log = LoggerFactory.getLogger(DoacaoResource.class);

    private static final String ENTITY_NAME = "doacao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DoacaoService doacaoService;

    private final DoacaoRepository doacaoRepository;

    public DoacaoResource(DoacaoService doacaoService, DoacaoRepository doacaoRepository) {
        this.doacaoService = doacaoService;
        this.doacaoRepository = doacaoRepository;
    }

    /**
     * {@code POST  /doacaos} : Create a new doacao.
     *
     * @param doacao the doacao to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new doacao, or with status {@code 400 (Bad Request)} if the doacao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/doacaos")
    public ResponseEntity<Doacao> createDoacao(@Valid @RequestBody Doacao doacao) throws URISyntaxException {
        log.debug("REST request to save Doacao : {}", doacao);
        if (doacao.getId() != null) {
            throw new BadRequestAlertException("A new doacao cannot already have an ID", ENTITY_NAME, "idexists");
        }

        if (doacaoRepository.existsDoacao(LocalDate.now().minusDays(29), doacao.getFamilia().getId())) {
            throw new BadRequestAlertException(
                "Essa família não pode retirar cesta básica no momento, pois faz menos de 30 dias que retirou.",
                ENTITY_NAME,
                "notdoar"
            );
        }

        doacao.setData(LocalDate.now());

        //  if (doacao.getId() == null) {
        //     throw new BadRequestAlertException("Essa família não pode retirar cesta básica no momento, pois faz menos de 30 dias que retirou.", ENTITY_NAME, "notdoar");
        // }
        Doacao result = doacaoService.save(doacao);
        return ResponseEntity
            .created(new URI("/api/doacaos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /doacaos/:id} : Updates an existing doacao.
     *
     * @param id the id of the doacao to save.
     * @param doacao the doacao to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated doacao,
     * or with status {@code 400 (Bad Request)} if the doacao is not valid,
     * or with status {@code 500 (Internal Server Error)} if the doacao couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/doacaos/{id}")
    public ResponseEntity<Doacao> updateDoacao(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Doacao doacao
    ) throws URISyntaxException {
        log.debug("REST request to update Doacao : {}, {}", id, doacao);
        if (doacao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, doacao.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!doacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Doacao result = doacaoService.update(doacao);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, doacao.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /doacaos/:id} : Partial updates given fields of an existing doacao, field will ignore if it is null
     *
     * @param id the id of the doacao to save.
     * @param doacao the doacao to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated doacao,
     * or with status {@code 400 (Bad Request)} if the doacao is not valid,
     * or with status {@code 404 (Not Found)} if the doacao is not found,
     * or with status {@code 500 (Internal Server Error)} if the doacao couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/doacaos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Doacao> partialUpdateDoacao(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Doacao doacao
    ) throws URISyntaxException {
        log.debug("REST request to partial update Doacao partially : {}, {}", id, doacao);
        if (doacao.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, doacao.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!doacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Doacao> result = doacaoService.partialUpdate(doacao);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, doacao.getId().toString())
        );
    }

    /**
     * {@code GET  /doacaos} : get all the doacaos.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of doacaos in body.
     */
    @GetMapping("/doacaos")
    public ResponseEntity<List<Doacao>> getAllDoacaos(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Doacaos");
        Page<Doacao> page;
        if (eagerload) {
            page = doacaoService.findAllWithEagerRelationships(pageable);
        } else {
            page = doacaoService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /doacaos/:id} : get the "id" doacao.
     *
     * @param id the id of the doacao to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the doacao, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/doacaos/{id}")
    public ResponseEntity<Doacao> getDoacao(@PathVariable Long id) {
        log.debug("REST request to get Doacao : {}", id);
        Optional<Doacao> doacao = doacaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(doacao);
    }

    /**
     * {@code DELETE  /doacaos/:id} : delete the "id" doacao.
     *
     * @param id the id of the doacao to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/doacaos/{id}")
    public ResponseEntity<Void> deleteDoacao(@PathVariable Long id) {
        log.debug("REST request to delete Doacao : {}", id);
        doacaoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
