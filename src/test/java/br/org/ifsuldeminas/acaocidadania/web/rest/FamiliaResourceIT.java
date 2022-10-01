package br.org.ifsuldeminas.acaocidadania.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.org.ifsuldeminas.acaocidadania.IntegrationTest;
import br.org.ifsuldeminas.acaocidadania.domain.Familia;
import br.org.ifsuldeminas.acaocidadania.repository.FamiliaRepository;
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
 * Integration tests for the {@link FamiliaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FamiliaResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVA = false;
    private static final Boolean UPDATED_ATIVA = true;

    private static final Double DEFAULT_RENDA = 7000D;
    private static final Double UPDATED_RENDA = 6999D;

    private static final String ENTITY_API_URL = "/api/familias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FamiliaRepository familiaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFamiliaMockMvc;

    private Familia familia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Familia createEntity(EntityManager em) {
        Familia familia = new Familia().nome(DEFAULT_NOME).ativa(DEFAULT_ATIVA).renda(DEFAULT_RENDA);
        return familia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Familia createUpdatedEntity(EntityManager em) {
        Familia familia = new Familia().nome(UPDATED_NOME).ativa(UPDATED_ATIVA).renda(UPDATED_RENDA);
        return familia;
    }

    @BeforeEach
    public void initTest() {
        familia = createEntity(em);
    }

    @Test
    @Transactional
    void createFamilia() throws Exception {
        int databaseSizeBeforeCreate = familiaRepository.findAll().size();
        // Create the Familia
        restFamiliaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familia)))
            .andExpect(status().isCreated());

        // Validate the Familia in the database
        List<Familia> familiaList = familiaRepository.findAll();
        assertThat(familiaList).hasSize(databaseSizeBeforeCreate + 1);
        Familia testFamilia = familiaList.get(familiaList.size() - 1);
        assertThat(testFamilia.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testFamilia.getAtiva()).isEqualTo(DEFAULT_ATIVA);
        assertThat(testFamilia.getRenda()).isEqualTo(DEFAULT_RENDA);
    }

    @Test
    @Transactional
    void createFamiliaWithExistingId() throws Exception {
        // Create the Familia with an existing ID
        familia.setId(1L);

        int databaseSizeBeforeCreate = familiaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFamiliaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familia)))
            .andExpect(status().isBadRequest());

        // Validate the Familia in the database
        List<Familia> familiaList = familiaRepository.findAll();
        assertThat(familiaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = familiaRepository.findAll().size();
        // set the field null
        familia.setNome(null);

        // Create the Familia, which fails.

        restFamiliaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familia)))
            .andExpect(status().isBadRequest());

        List<Familia> familiaList = familiaRepository.findAll();
        assertThat(familiaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAtivaIsRequired() throws Exception {
        int databaseSizeBeforeTest = familiaRepository.findAll().size();
        // set the field null
        familia.setAtiva(null);

        // Create the Familia, which fails.

        restFamiliaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familia)))
            .andExpect(status().isBadRequest());

        List<Familia> familiaList = familiaRepository.findAll();
        assertThat(familiaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRendaIsRequired() throws Exception {
        int databaseSizeBeforeTest = familiaRepository.findAll().size();
        // set the field null
        familia.setRenda(null);

        // Create the Familia, which fails.

        restFamiliaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familia)))
            .andExpect(status().isBadRequest());

        List<Familia> familiaList = familiaRepository.findAll();
        assertThat(familiaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFamilias() throws Exception {
        // Initialize the database
        familiaRepository.saveAndFlush(familia);

        // Get all the familiaList
        restFamiliaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(familia.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].ativa").value(hasItem(DEFAULT_ATIVA.booleanValue())))
            .andExpect(jsonPath("$.[*].renda").value(hasItem(DEFAULT_RENDA.doubleValue())));
    }

    @Test
    @Transactional
    void getFamilia() throws Exception {
        // Initialize the database
        familiaRepository.saveAndFlush(familia);

        // Get the familia
        restFamiliaMockMvc
            .perform(get(ENTITY_API_URL_ID, familia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(familia.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.ativa").value(DEFAULT_ATIVA.booleanValue()))
            .andExpect(jsonPath("$.renda").value(DEFAULT_RENDA.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingFamilia() throws Exception {
        // Get the familia
        restFamiliaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFamilia() throws Exception {
        // Initialize the database
        familiaRepository.saveAndFlush(familia);

        int databaseSizeBeforeUpdate = familiaRepository.findAll().size();

        // Update the familia
        Familia updatedFamilia = familiaRepository.findById(familia.getId()).get();
        // Disconnect from session so that the updates on updatedFamilia are not directly saved in db
        em.detach(updatedFamilia);
        updatedFamilia.nome(UPDATED_NOME).ativa(UPDATED_ATIVA).renda(UPDATED_RENDA);

        restFamiliaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFamilia.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFamilia))
            )
            .andExpect(status().isOk());

        // Validate the Familia in the database
        List<Familia> familiaList = familiaRepository.findAll();
        assertThat(familiaList).hasSize(databaseSizeBeforeUpdate);
        Familia testFamilia = familiaList.get(familiaList.size() - 1);
        assertThat(testFamilia.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testFamilia.getAtiva()).isEqualTo(UPDATED_ATIVA);
        assertThat(testFamilia.getRenda()).isEqualTo(UPDATED_RENDA);
    }

    @Test
    @Transactional
    void putNonExistingFamilia() throws Exception {
        int databaseSizeBeforeUpdate = familiaRepository.findAll().size();
        familia.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamiliaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, familia.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(familia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Familia in the database
        List<Familia> familiaList = familiaRepository.findAll();
        assertThat(familiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFamilia() throws Exception {
        int databaseSizeBeforeUpdate = familiaRepository.findAll().size();
        familia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamiliaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(familia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Familia in the database
        List<Familia> familiaList = familiaRepository.findAll();
        assertThat(familiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFamilia() throws Exception {
        int databaseSizeBeforeUpdate = familiaRepository.findAll().size();
        familia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamiliaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(familia)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Familia in the database
        List<Familia> familiaList = familiaRepository.findAll();
        assertThat(familiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFamiliaWithPatch() throws Exception {
        // Initialize the database
        familiaRepository.saveAndFlush(familia);

        int databaseSizeBeforeUpdate = familiaRepository.findAll().size();

        // Update the familia using partial update
        Familia partialUpdatedFamilia = new Familia();
        partialUpdatedFamilia.setId(familia.getId());

        partialUpdatedFamilia.ativa(UPDATED_ATIVA).renda(UPDATED_RENDA);

        restFamiliaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamilia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFamilia))
            )
            .andExpect(status().isOk());

        // Validate the Familia in the database
        List<Familia> familiaList = familiaRepository.findAll();
        assertThat(familiaList).hasSize(databaseSizeBeforeUpdate);
        Familia testFamilia = familiaList.get(familiaList.size() - 1);
        assertThat(testFamilia.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testFamilia.getAtiva()).isEqualTo(UPDATED_ATIVA);
        assertThat(testFamilia.getRenda()).isEqualTo(UPDATED_RENDA);
    }

    @Test
    @Transactional
    void fullUpdateFamiliaWithPatch() throws Exception {
        // Initialize the database
        familiaRepository.saveAndFlush(familia);

        int databaseSizeBeforeUpdate = familiaRepository.findAll().size();

        // Update the familia using partial update
        Familia partialUpdatedFamilia = new Familia();
        partialUpdatedFamilia.setId(familia.getId());

        partialUpdatedFamilia.nome(UPDATED_NOME).ativa(UPDATED_ATIVA).renda(UPDATED_RENDA);

        restFamiliaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFamilia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFamilia))
            )
            .andExpect(status().isOk());

        // Validate the Familia in the database
        List<Familia> familiaList = familiaRepository.findAll();
        assertThat(familiaList).hasSize(databaseSizeBeforeUpdate);
        Familia testFamilia = familiaList.get(familiaList.size() - 1);
        assertThat(testFamilia.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testFamilia.getAtiva()).isEqualTo(UPDATED_ATIVA);
        assertThat(testFamilia.getRenda()).isEqualTo(UPDATED_RENDA);
    }

    @Test
    @Transactional
    void patchNonExistingFamilia() throws Exception {
        int databaseSizeBeforeUpdate = familiaRepository.findAll().size();
        familia.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFamiliaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, familia.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(familia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Familia in the database
        List<Familia> familiaList = familiaRepository.findAll();
        assertThat(familiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFamilia() throws Exception {
        int databaseSizeBeforeUpdate = familiaRepository.findAll().size();
        familia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamiliaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(familia))
            )
            .andExpect(status().isBadRequest());

        // Validate the Familia in the database
        List<Familia> familiaList = familiaRepository.findAll();
        assertThat(familiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFamilia() throws Exception {
        int databaseSizeBeforeUpdate = familiaRepository.findAll().size();
        familia.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFamiliaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(familia)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Familia in the database
        List<Familia> familiaList = familiaRepository.findAll();
        assertThat(familiaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFamilia() throws Exception {
        // Initialize the database
        familiaRepository.saveAndFlush(familia);

        int databaseSizeBeforeDelete = familiaRepository.findAll().size();

        // Delete the familia
        restFamiliaMockMvc
            .perform(delete(ENTITY_API_URL_ID, familia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Familia> familiaList = familiaRepository.findAll();
        assertThat(familiaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
