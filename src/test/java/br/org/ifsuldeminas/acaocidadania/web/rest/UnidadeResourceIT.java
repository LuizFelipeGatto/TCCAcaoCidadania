package br.org.ifsuldeminas.acaocidadania.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.org.ifsuldeminas.acaocidadania.IntegrationTest;
import br.org.ifsuldeminas.acaocidadania.domain.Unidade;
import br.org.ifsuldeminas.acaocidadania.repository.UnidadeRepository;
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
 * Integration tests for the {@link UnidadeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UnidadeResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_CEP = "AAAAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBBBB";

    private static final String DEFAULT_CIDADE = "AAAAAAAAAA";
    private static final String UPDATED_CIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_LOGRADOURO = "AAAAAAAAAA";
    private static final String UPDATED_LOGRADOURO = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_IBGE = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_IBGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/unidades";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUnidadeMockMvc;

    private Unidade unidade;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Unidade createEntity(EntityManager em) {
        Unidade unidade = new Unidade()
            .nome(DEFAULT_NOME)
            .cep(DEFAULT_CEP)
            .cidade(DEFAULT_CIDADE)
            .logradouro(DEFAULT_LOGRADOURO)
            .codigoIbge(DEFAULT_CODIGO_IBGE);
        return unidade;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Unidade createUpdatedEntity(EntityManager em) {
        Unidade unidade = new Unidade()
            .nome(UPDATED_NOME)
            .cep(UPDATED_CEP)
            .cidade(UPDATED_CIDADE)
            .logradouro(UPDATED_LOGRADOURO)
            .codigoIbge(UPDATED_CODIGO_IBGE);
        return unidade;
    }

    @BeforeEach
    public void initTest() {
        unidade = createEntity(em);
    }

    @Test
    @Transactional
    void createUnidade() throws Exception {
        int databaseSizeBeforeCreate = unidadeRepository.findAll().size();
        // Create the Unidade
        restUnidadeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unidade)))
            .andExpect(status().isCreated());

        // Validate the Unidade in the database
        List<Unidade> unidadeList = unidadeRepository.findAll();
        assertThat(unidadeList).hasSize(databaseSizeBeforeCreate + 1);
        Unidade testUnidade = unidadeList.get(unidadeList.size() - 1);
        assertThat(testUnidade.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testUnidade.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testUnidade.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testUnidade.getLogradouro()).isEqualTo(DEFAULT_LOGRADOURO);
        assertThat(testUnidade.getCodigoIbge()).isEqualTo(DEFAULT_CODIGO_IBGE);
    }

    @Test
    @Transactional
    void createUnidadeWithExistingId() throws Exception {
        // Create the Unidade with an existing ID
        unidade.setId(1L);

        int databaseSizeBeforeCreate = unidadeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUnidadeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unidade)))
            .andExpect(status().isBadRequest());

        // Validate the Unidade in the database
        List<Unidade> unidadeList = unidadeRepository.findAll();
        assertThat(unidadeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = unidadeRepository.findAll().size();
        // set the field null
        unidade.setNome(null);

        // Create the Unidade, which fails.

        restUnidadeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unidade)))
            .andExpect(status().isBadRequest());

        List<Unidade> unidadeList = unidadeRepository.findAll();
        assertThat(unidadeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCepIsRequired() throws Exception {
        int databaseSizeBeforeTest = unidadeRepository.findAll().size();
        // set the field null
        unidade.setCep(null);

        // Create the Unidade, which fails.

        restUnidadeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unidade)))
            .andExpect(status().isBadRequest());

        List<Unidade> unidadeList = unidadeRepository.findAll();
        assertThat(unidadeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCidadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = unidadeRepository.findAll().size();
        // set the field null
        unidade.setCidade(null);

        // Create the Unidade, which fails.

        restUnidadeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unidade)))
            .andExpect(status().isBadRequest());

        List<Unidade> unidadeList = unidadeRepository.findAll();
        assertThat(unidadeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLogradouroIsRequired() throws Exception {
        int databaseSizeBeforeTest = unidadeRepository.findAll().size();
        // set the field null
        unidade.setLogradouro(null);

        // Create the Unidade, which fails.

        restUnidadeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unidade)))
            .andExpect(status().isBadRequest());

        List<Unidade> unidadeList = unidadeRepository.findAll();
        assertThat(unidadeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCodigoIbgeIsRequired() throws Exception {
        int databaseSizeBeforeTest = unidadeRepository.findAll().size();
        // set the field null
        unidade.setCodigoIbge(null);

        // Create the Unidade, which fails.

        restUnidadeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unidade)))
            .andExpect(status().isBadRequest());

        List<Unidade> unidadeList = unidadeRepository.findAll();
        assertThat(unidadeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUnidades() throws Exception {
        // Initialize the database
        unidadeRepository.saveAndFlush(unidade);

        // Get all the unidadeList
        restUnidadeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(unidade.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE)))
            .andExpect(jsonPath("$.[*].logradouro").value(hasItem(DEFAULT_LOGRADOURO)))
            .andExpect(jsonPath("$.[*].codigoIbge").value(hasItem(DEFAULT_CODIGO_IBGE)));
    }

    @Test
    @Transactional
    void getUnidade() throws Exception {
        // Initialize the database
        unidadeRepository.saveAndFlush(unidade);

        // Get the unidade
        restUnidadeMockMvc
            .perform(get(ENTITY_API_URL_ID, unidade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(unidade.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE))
            .andExpect(jsonPath("$.logradouro").value(DEFAULT_LOGRADOURO))
            .andExpect(jsonPath("$.codigoIbge").value(DEFAULT_CODIGO_IBGE));
    }

    @Test
    @Transactional
    void getNonExistingUnidade() throws Exception {
        // Get the unidade
        restUnidadeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUnidade() throws Exception {
        // Initialize the database
        unidadeRepository.saveAndFlush(unidade);

        int databaseSizeBeforeUpdate = unidadeRepository.findAll().size();

        // Update the unidade
        Unidade updatedUnidade = unidadeRepository.findById(unidade.getId()).get();
        // Disconnect from session so that the updates on updatedUnidade are not directly saved in db
        em.detach(updatedUnidade);
        updatedUnidade
            .nome(UPDATED_NOME)
            .cep(UPDATED_CEP)
            .cidade(UPDATED_CIDADE)
            .logradouro(UPDATED_LOGRADOURO)
            .codigoIbge(UPDATED_CODIGO_IBGE);

        restUnidadeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUnidade.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUnidade))
            )
            .andExpect(status().isOk());

        // Validate the Unidade in the database
        List<Unidade> unidadeList = unidadeRepository.findAll();
        assertThat(unidadeList).hasSize(databaseSizeBeforeUpdate);
        Unidade testUnidade = unidadeList.get(unidadeList.size() - 1);
        assertThat(testUnidade.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testUnidade.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testUnidade.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testUnidade.getLogradouro()).isEqualTo(UPDATED_LOGRADOURO);
        assertThat(testUnidade.getCodigoIbge()).isEqualTo(UPDATED_CODIGO_IBGE);
    }

    @Test
    @Transactional
    void putNonExistingUnidade() throws Exception {
        int databaseSizeBeforeUpdate = unidadeRepository.findAll().size();
        unidade.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnidadeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, unidade.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unidade))
            )
            .andExpect(status().isBadRequest());

        // Validate the Unidade in the database
        List<Unidade> unidadeList = unidadeRepository.findAll();
        assertThat(unidadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUnidade() throws Exception {
        int databaseSizeBeforeUpdate = unidadeRepository.findAll().size();
        unidade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnidadeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(unidade))
            )
            .andExpect(status().isBadRequest());

        // Validate the Unidade in the database
        List<Unidade> unidadeList = unidadeRepository.findAll();
        assertThat(unidadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUnidade() throws Exception {
        int databaseSizeBeforeUpdate = unidadeRepository.findAll().size();
        unidade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnidadeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(unidade)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Unidade in the database
        List<Unidade> unidadeList = unidadeRepository.findAll();
        assertThat(unidadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUnidadeWithPatch() throws Exception {
        // Initialize the database
        unidadeRepository.saveAndFlush(unidade);

        int databaseSizeBeforeUpdate = unidadeRepository.findAll().size();

        // Update the unidade using partial update
        Unidade partialUpdatedUnidade = new Unidade();
        partialUpdatedUnidade.setId(unidade.getId());

        partialUpdatedUnidade.nome(UPDATED_NOME).cep(UPDATED_CEP).logradouro(UPDATED_LOGRADOURO);

        restUnidadeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnidade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUnidade))
            )
            .andExpect(status().isOk());

        // Validate the Unidade in the database
        List<Unidade> unidadeList = unidadeRepository.findAll();
        assertThat(unidadeList).hasSize(databaseSizeBeforeUpdate);
        Unidade testUnidade = unidadeList.get(unidadeList.size() - 1);
        assertThat(testUnidade.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testUnidade.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testUnidade.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testUnidade.getLogradouro()).isEqualTo(UPDATED_LOGRADOURO);
        assertThat(testUnidade.getCodigoIbge()).isEqualTo(DEFAULT_CODIGO_IBGE);
    }

    @Test
    @Transactional
    void fullUpdateUnidadeWithPatch() throws Exception {
        // Initialize the database
        unidadeRepository.saveAndFlush(unidade);

        int databaseSizeBeforeUpdate = unidadeRepository.findAll().size();

        // Update the unidade using partial update
        Unidade partialUpdatedUnidade = new Unidade();
        partialUpdatedUnidade.setId(unidade.getId());

        partialUpdatedUnidade
            .nome(UPDATED_NOME)
            .cep(UPDATED_CEP)
            .cidade(UPDATED_CIDADE)
            .logradouro(UPDATED_LOGRADOURO)
            .codigoIbge(UPDATED_CODIGO_IBGE);

        restUnidadeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUnidade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUnidade))
            )
            .andExpect(status().isOk());

        // Validate the Unidade in the database
        List<Unidade> unidadeList = unidadeRepository.findAll();
        assertThat(unidadeList).hasSize(databaseSizeBeforeUpdate);
        Unidade testUnidade = unidadeList.get(unidadeList.size() - 1);
        assertThat(testUnidade.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testUnidade.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testUnidade.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testUnidade.getLogradouro()).isEqualTo(UPDATED_LOGRADOURO);
        assertThat(testUnidade.getCodigoIbge()).isEqualTo(UPDATED_CODIGO_IBGE);
    }

    @Test
    @Transactional
    void patchNonExistingUnidade() throws Exception {
        int databaseSizeBeforeUpdate = unidadeRepository.findAll().size();
        unidade.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUnidadeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, unidade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unidade))
            )
            .andExpect(status().isBadRequest());

        // Validate the Unidade in the database
        List<Unidade> unidadeList = unidadeRepository.findAll();
        assertThat(unidadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUnidade() throws Exception {
        int databaseSizeBeforeUpdate = unidadeRepository.findAll().size();
        unidade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnidadeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(unidade))
            )
            .andExpect(status().isBadRequest());

        // Validate the Unidade in the database
        List<Unidade> unidadeList = unidadeRepository.findAll();
        assertThat(unidadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUnidade() throws Exception {
        int databaseSizeBeforeUpdate = unidadeRepository.findAll().size();
        unidade.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUnidadeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(unidade)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Unidade in the database
        List<Unidade> unidadeList = unidadeRepository.findAll();
        assertThat(unidadeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUnidade() throws Exception {
        // Initialize the database
        unidadeRepository.saveAndFlush(unidade);

        int databaseSizeBeforeDelete = unidadeRepository.findAll().size();

        // Delete the unidade
        restUnidadeMockMvc
            .perform(delete(ENTITY_API_URL_ID, unidade.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Unidade> unidadeList = unidadeRepository.findAll();
        assertThat(unidadeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
