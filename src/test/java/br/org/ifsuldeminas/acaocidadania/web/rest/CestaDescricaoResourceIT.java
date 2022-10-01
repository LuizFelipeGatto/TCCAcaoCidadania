package br.org.ifsuldeminas.acaocidadania.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.org.ifsuldeminas.acaocidadania.IntegrationTest;
import br.org.ifsuldeminas.acaocidadania.domain.CestaDescricao;
import br.org.ifsuldeminas.acaocidadania.repository.CestaDescricaoRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CestaDescricaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CestaDescricaoResourceIT {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cesta-descricaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CestaDescricaoRepository cestaDescricaoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCestaDescricaoMockMvc;

    private CestaDescricao cestaDescricao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CestaDescricao createEntity(EntityManager em) {
        CestaDescricao cestaDescricao = new CestaDescricao().descricao(DEFAULT_DESCRICAO);
        return cestaDescricao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CestaDescricao createUpdatedEntity(EntityManager em) {
        CestaDescricao cestaDescricao = new CestaDescricao().descricao(UPDATED_DESCRICAO);
        return cestaDescricao;
    }

    @BeforeEach
    public void initTest() {
        cestaDescricao = createEntity(em);
    }

    @Test
    @Transactional
    void createCestaDescricao() throws Exception {
        int databaseSizeBeforeCreate = cestaDescricaoRepository.findAll().size();
        // Create the CestaDescricao
        restCestaDescricaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cestaDescricao))
            )
            .andExpect(status().isCreated());

        // Validate the CestaDescricao in the database
        List<CestaDescricao> cestaDescricaoList = cestaDescricaoRepository.findAll();
        assertThat(cestaDescricaoList).hasSize(databaseSizeBeforeCreate + 1);
        CestaDescricao testCestaDescricao = cestaDescricaoList.get(cestaDescricaoList.size() - 1);
        assertThat(testCestaDescricao.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void createCestaDescricaoWithExistingId() throws Exception {
        // Create the CestaDescricao with an existing ID
        cestaDescricao.setId(1L);

        int databaseSizeBeforeCreate = cestaDescricaoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCestaDescricaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cestaDescricao))
            )
            .andExpect(status().isBadRequest());

        // Validate the CestaDescricao in the database
        List<CestaDescricao> cestaDescricaoList = cestaDescricaoRepository.findAll();
        assertThat(cestaDescricaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDescricaoIsRequired() throws Exception {
        int databaseSizeBeforeTest = cestaDescricaoRepository.findAll().size();
        // set the field null
        cestaDescricao.setDescricao(null);

        // Create the CestaDescricao, which fails.

        restCestaDescricaoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cestaDescricao))
            )
            .andExpect(status().isBadRequest());

        List<CestaDescricao> cestaDescricaoList = cestaDescricaoRepository.findAll();
        assertThat(cestaDescricaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCestaDescricaos() throws Exception {
        // Initialize the database
        cestaDescricaoRepository.saveAndFlush(cestaDescricao);

        // Get all the cestaDescricaoList
        restCestaDescricaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cestaDescricao.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)));
    }

    @Test
    @Transactional
    void getCestaDescricao() throws Exception {
        // Initialize the database
        cestaDescricaoRepository.saveAndFlush(cestaDescricao);

        // Get the cestaDescricao
        restCestaDescricaoMockMvc
            .perform(get(ENTITY_API_URL_ID, cestaDescricao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cestaDescricao.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO));
    }

    @Test
    @Transactional
    void getNonExistingCestaDescricao() throws Exception {
        // Get the cestaDescricao
        restCestaDescricaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCestaDescricao() throws Exception {
        // Initialize the database
        cestaDescricaoRepository.saveAndFlush(cestaDescricao);

        int databaseSizeBeforeUpdate = cestaDescricaoRepository.findAll().size();

        // Update the cestaDescricao
        CestaDescricao updatedCestaDescricao = cestaDescricaoRepository.findById(cestaDescricao.getId()).get();
        // Disconnect from session so that the updates on updatedCestaDescricao are not directly saved in db
        em.detach(updatedCestaDescricao);
        updatedCestaDescricao.descricao(UPDATED_DESCRICAO);

        restCestaDescricaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCestaDescricao.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCestaDescricao))
            )
            .andExpect(status().isOk());

        // Validate the CestaDescricao in the database
        List<CestaDescricao> cestaDescricaoList = cestaDescricaoRepository.findAll();
        assertThat(cestaDescricaoList).hasSize(databaseSizeBeforeUpdate);
        CestaDescricao testCestaDescricao = cestaDescricaoList.get(cestaDescricaoList.size() - 1);
        assertThat(testCestaDescricao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void putNonExistingCestaDescricao() throws Exception {
        int databaseSizeBeforeUpdate = cestaDescricaoRepository.findAll().size();
        cestaDescricao.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCestaDescricaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cestaDescricao.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cestaDescricao))
            )
            .andExpect(status().isBadRequest());

        // Validate the CestaDescricao in the database
        List<CestaDescricao> cestaDescricaoList = cestaDescricaoRepository.findAll();
        assertThat(cestaDescricaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCestaDescricao() throws Exception {
        int databaseSizeBeforeUpdate = cestaDescricaoRepository.findAll().size();
        cestaDescricao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCestaDescricaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cestaDescricao))
            )
            .andExpect(status().isBadRequest());

        // Validate the CestaDescricao in the database
        List<CestaDescricao> cestaDescricaoList = cestaDescricaoRepository.findAll();
        assertThat(cestaDescricaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCestaDescricao() throws Exception {
        int databaseSizeBeforeUpdate = cestaDescricaoRepository.findAll().size();
        cestaDescricao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCestaDescricaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cestaDescricao)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CestaDescricao in the database
        List<CestaDescricao> cestaDescricaoList = cestaDescricaoRepository.findAll();
        assertThat(cestaDescricaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCestaDescricaoWithPatch() throws Exception {
        // Initialize the database
        cestaDescricaoRepository.saveAndFlush(cestaDescricao);

        int databaseSizeBeforeUpdate = cestaDescricaoRepository.findAll().size();

        // Update the cestaDescricao using partial update
        CestaDescricao partialUpdatedCestaDescricao = new CestaDescricao();
        partialUpdatedCestaDescricao.setId(cestaDescricao.getId());

        partialUpdatedCestaDescricao.descricao(UPDATED_DESCRICAO);

        restCestaDescricaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCestaDescricao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCestaDescricao))
            )
            .andExpect(status().isOk());

        // Validate the CestaDescricao in the database
        List<CestaDescricao> cestaDescricaoList = cestaDescricaoRepository.findAll();
        assertThat(cestaDescricaoList).hasSize(databaseSizeBeforeUpdate);
        CestaDescricao testCestaDescricao = cestaDescricaoList.get(cestaDescricaoList.size() - 1);
        assertThat(testCestaDescricao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void fullUpdateCestaDescricaoWithPatch() throws Exception {
        // Initialize the database
        cestaDescricaoRepository.saveAndFlush(cestaDescricao);

        int databaseSizeBeforeUpdate = cestaDescricaoRepository.findAll().size();

        // Update the cestaDescricao using partial update
        CestaDescricao partialUpdatedCestaDescricao = new CestaDescricao();
        partialUpdatedCestaDescricao.setId(cestaDescricao.getId());

        partialUpdatedCestaDescricao.descricao(UPDATED_DESCRICAO);

        restCestaDescricaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCestaDescricao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCestaDescricao))
            )
            .andExpect(status().isOk());

        // Validate the CestaDescricao in the database
        List<CestaDescricao> cestaDescricaoList = cestaDescricaoRepository.findAll();
        assertThat(cestaDescricaoList).hasSize(databaseSizeBeforeUpdate);
        CestaDescricao testCestaDescricao = cestaDescricaoList.get(cestaDescricaoList.size() - 1);
        assertThat(testCestaDescricao.getDescricao()).isEqualTo(UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void patchNonExistingCestaDescricao() throws Exception {
        int databaseSizeBeforeUpdate = cestaDescricaoRepository.findAll().size();
        cestaDescricao.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCestaDescricaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cestaDescricao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cestaDescricao))
            )
            .andExpect(status().isBadRequest());

        // Validate the CestaDescricao in the database
        List<CestaDescricao> cestaDescricaoList = cestaDescricaoRepository.findAll();
        assertThat(cestaDescricaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCestaDescricao() throws Exception {
        int databaseSizeBeforeUpdate = cestaDescricaoRepository.findAll().size();
        cestaDescricao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCestaDescricaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cestaDescricao))
            )
            .andExpect(status().isBadRequest());

        // Validate the CestaDescricao in the database
        List<CestaDescricao> cestaDescricaoList = cestaDescricaoRepository.findAll();
        assertThat(cestaDescricaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCestaDescricao() throws Exception {
        int databaseSizeBeforeUpdate = cestaDescricaoRepository.findAll().size();
        cestaDescricao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCestaDescricaoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cestaDescricao))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CestaDescricao in the database
        List<CestaDescricao> cestaDescricaoList = cestaDescricaoRepository.findAll();
        assertThat(cestaDescricaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCestaDescricao() throws Exception {
        // Initialize the database
        cestaDescricaoRepository.saveAndFlush(cestaDescricao);

        int databaseSizeBeforeDelete = cestaDescricaoRepository.findAll().size();

        // Delete the cestaDescricao
        restCestaDescricaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, cestaDescricao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CestaDescricao> cestaDescricaoList = cestaDescricaoRepository.findAll();
        assertThat(cestaDescricaoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
