package br.org.ifsuldeminas.acaocidadania.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.org.ifsuldeminas.acaocidadania.IntegrationTest;
import br.org.ifsuldeminas.acaocidadania.domain.Doacao;
import br.org.ifsuldeminas.acaocidadania.repository.DoacaoRepository;
import br.org.ifsuldeminas.acaocidadania.service.DoacaoService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DoacaoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DoacaoResourceIT {

    private static final LocalDate DEFAULT_DATA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/doacaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DoacaoRepository doacaoRepository;

    @Mock
    private DoacaoRepository doacaoRepositoryMock;

    @Mock
    private DoacaoService doacaoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDoacaoMockMvc;

    private Doacao doacao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doacao createEntity(EntityManager em) {
        Doacao doacao = new Doacao().data(DEFAULT_DATA);
        return doacao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doacao createUpdatedEntity(EntityManager em) {
        Doacao doacao = new Doacao().data(UPDATED_DATA);
        return doacao;
    }

    @BeforeEach
    public void initTest() {
        doacao = createEntity(em);
    }

    @Test
    @Transactional
    void createDoacao() throws Exception {
        int databaseSizeBeforeCreate = doacaoRepository.findAll().size();
        // Create the Doacao
        restDoacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doacao)))
            .andExpect(status().isCreated());

        // Validate the Doacao in the database
        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeCreate + 1);
        Doacao testDoacao = doacaoList.get(doacaoList.size() - 1);
        assertThat(testDoacao.getData()).isEqualTo(DEFAULT_DATA);
    }

    @Test
    @Transactional
    void createDoacaoWithExistingId() throws Exception {
        // Create the Doacao with an existing ID
        doacao.setId(1L);

        int databaseSizeBeforeCreate = doacaoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doacao)))
            .andExpect(status().isBadRequest());

        // Validate the Doacao in the database
        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = doacaoRepository.findAll().size();
        // set the field null
        doacao.setData(null);

        // Create the Doacao, which fails.

        restDoacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doacao)))
            .andExpect(status().isBadRequest());

        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDoacaos() throws Exception {
        // Initialize the database
        doacaoRepository.saveAndFlush(doacao);

        // Get all the doacaoList
        restDoacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDoacaosWithEagerRelationshipsIsEnabled() throws Exception {
        when(doacaoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDoacaoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(doacaoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDoacaosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(doacaoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDoacaoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(doacaoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDoacao() throws Exception {
        // Initialize the database
        doacaoRepository.saveAndFlush(doacao);

        // Get the doacao
        restDoacaoMockMvc
            .perform(get(ENTITY_API_URL_ID, doacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(doacao.getId().intValue()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDoacao() throws Exception {
        // Get the doacao
        restDoacaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDoacao() throws Exception {
        // Initialize the database
        doacaoRepository.saveAndFlush(doacao);

        int databaseSizeBeforeUpdate = doacaoRepository.findAll().size();

        // Update the doacao
        Doacao updatedDoacao = doacaoRepository.findById(doacao.getId()).get();
        // Disconnect from session so that the updates on updatedDoacao are not directly saved in db
        em.detach(updatedDoacao);
        updatedDoacao.data(UPDATED_DATA);

        restDoacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDoacao.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDoacao))
            )
            .andExpect(status().isOk());

        // Validate the Doacao in the database
        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeUpdate);
        Doacao testDoacao = doacaoList.get(doacaoList.size() - 1);
        assertThat(testDoacao.getData()).isEqualTo(UPDATED_DATA);
    }

    @Test
    @Transactional
    void putNonExistingDoacao() throws Exception {
        int databaseSizeBeforeUpdate = doacaoRepository.findAll().size();
        doacao.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, doacao.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(doacao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doacao in the database
        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDoacao() throws Exception {
        int databaseSizeBeforeUpdate = doacaoRepository.findAll().size();
        doacao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(doacao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doacao in the database
        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDoacao() throws Exception {
        int databaseSizeBeforeUpdate = doacaoRepository.findAll().size();
        doacao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoacaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(doacao)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Doacao in the database
        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDoacaoWithPatch() throws Exception {
        // Initialize the database
        doacaoRepository.saveAndFlush(doacao);

        int databaseSizeBeforeUpdate = doacaoRepository.findAll().size();

        // Update the doacao using partial update
        Doacao partialUpdatedDoacao = new Doacao();
        partialUpdatedDoacao.setId(doacao.getId());

        partialUpdatedDoacao.data(UPDATED_DATA);

        restDoacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDoacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDoacao))
            )
            .andExpect(status().isOk());

        // Validate the Doacao in the database
        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeUpdate);
        Doacao testDoacao = doacaoList.get(doacaoList.size() - 1);
        assertThat(testDoacao.getData()).isEqualTo(UPDATED_DATA);
    }

    @Test
    @Transactional
    void fullUpdateDoacaoWithPatch() throws Exception {
        // Initialize the database
        doacaoRepository.saveAndFlush(doacao);

        int databaseSizeBeforeUpdate = doacaoRepository.findAll().size();

        // Update the doacao using partial update
        Doacao partialUpdatedDoacao = new Doacao();
        partialUpdatedDoacao.setId(doacao.getId());

        partialUpdatedDoacao.data(UPDATED_DATA);

        restDoacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDoacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDoacao))
            )
            .andExpect(status().isOk());

        // Validate the Doacao in the database
        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeUpdate);
        Doacao testDoacao = doacaoList.get(doacaoList.size() - 1);
        assertThat(testDoacao.getData()).isEqualTo(UPDATED_DATA);
    }

    @Test
    @Transactional
    void patchNonExistingDoacao() throws Exception {
        int databaseSizeBeforeUpdate = doacaoRepository.findAll().size();
        doacao.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, doacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(doacao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doacao in the database
        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDoacao() throws Exception {
        int databaseSizeBeforeUpdate = doacaoRepository.findAll().size();
        doacao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(doacao))
            )
            .andExpect(status().isBadRequest());

        // Validate the Doacao in the database
        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDoacao() throws Exception {
        int databaseSizeBeforeUpdate = doacaoRepository.findAll().size();
        doacao.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDoacaoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(doacao)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Doacao in the database
        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDoacao() throws Exception {
        // Initialize the database
        doacaoRepository.saveAndFlush(doacao);

        int databaseSizeBeforeDelete = doacaoRepository.findAll().size();

        // Delete the doacao
        restDoacaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, doacao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Doacao> doacaoList = doacaoRepository.findAll();
        assertThat(doacaoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
