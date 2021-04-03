package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ClientDetails;
import com.mycompany.myapp.repository.ClientDetailsRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ClientDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClientDetailsResourceIT {

    private static final String DEFAULT_KOMINY_DYMOWE = "AAAAAAAAAA";
    private static final String UPDATED_KOMINY_DYMOWE = "BBBBBBBBBB";

    private static final String DEFAULT_KOMINY_SPALINOWE = "AAAAAAAAAA";
    private static final String UPDATED_KOMINY_SPALINOWE = "BBBBBBBBBB";

    private static final String DEFAULT_PRZEWODY_WENTYLACYJNE = "AAAAAAAAAA";
    private static final String UPDATED_PRZEWODY_WENTYLACYJNE = "BBBBBBBBBB";

    private static final String DEFAULT_RYCZALT_JEDNORAZOWY = "AAAAAAAAAA";
    private static final String UPDATED_RYCZALT_JEDNORAZOWY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_CZYSZCZENIA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_CZYSZCZENIA = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_DZIEN_CZYSZCZENIA = 1;
    private static final Integer UPDATED_DZIEN_CZYSZCZENIA = 2;

    private static final String DEFAULT_ZAPLACONO = "AAAAAAAAAA";
    private static final String UPDATED_ZAPLACONO = "BBBBBBBBBB";

    private static final String DEFAULT_PODPIS = "AAAAAAAAAA";
    private static final String UPDATED_PODPIS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/client-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClientDetailsRepository clientDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClientDetailsMockMvc;

    private ClientDetails clientDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientDetails createEntity(EntityManager em) {
        ClientDetails clientDetails = new ClientDetails()
            .kominyDymowe(DEFAULT_KOMINY_DYMOWE)
            .kominySpalinowe(DEFAULT_KOMINY_SPALINOWE)
            .przewodyWentylacyjne(DEFAULT_PRZEWODY_WENTYLACYJNE)
            .ryczaltJednorazowy(DEFAULT_RYCZALT_JEDNORAZOWY)
            .dataCzyszczenia(DEFAULT_DATA_CZYSZCZENIA)
            .dzienCzyszczenia(DEFAULT_DZIEN_CZYSZCZENIA)
            .zaplacono(DEFAULT_ZAPLACONO)
            .podpis(DEFAULT_PODPIS);
        return clientDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClientDetails createUpdatedEntity(EntityManager em) {
        ClientDetails clientDetails = new ClientDetails()
            .kominyDymowe(UPDATED_KOMINY_DYMOWE)
            .kominySpalinowe(UPDATED_KOMINY_SPALINOWE)
            .przewodyWentylacyjne(UPDATED_PRZEWODY_WENTYLACYJNE)
            .ryczaltJednorazowy(UPDATED_RYCZALT_JEDNORAZOWY)
            .dataCzyszczenia(UPDATED_DATA_CZYSZCZENIA)
            .dzienCzyszczenia(UPDATED_DZIEN_CZYSZCZENIA)
            .zaplacono(UPDATED_ZAPLACONO)
            .podpis(UPDATED_PODPIS);
        return clientDetails;
    }

    @BeforeEach
    public void initTest() {
        clientDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createClientDetails() throws Exception {
        int databaseSizeBeforeCreate = clientDetailsRepository.findAll().size();
        // Create the ClientDetails
        restClientDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDetails))
            )
            .andExpect(status().isCreated());

        // Validate the ClientDetails in the database
        List<ClientDetails> clientDetailsList = clientDetailsRepository.findAll();
        assertThat(clientDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        ClientDetails testClientDetails = clientDetailsList.get(clientDetailsList.size() - 1);
        assertThat(testClientDetails.getKominyDymowe()).isEqualTo(DEFAULT_KOMINY_DYMOWE);
        assertThat(testClientDetails.getKominySpalinowe()).isEqualTo(DEFAULT_KOMINY_SPALINOWE);
        assertThat(testClientDetails.getPrzewodyWentylacyjne()).isEqualTo(DEFAULT_PRZEWODY_WENTYLACYJNE);
        assertThat(testClientDetails.getRyczaltJednorazowy()).isEqualTo(DEFAULT_RYCZALT_JEDNORAZOWY);
        assertThat(testClientDetails.getDataCzyszczenia()).isEqualTo(DEFAULT_DATA_CZYSZCZENIA);
        assertThat(testClientDetails.getDzienCzyszczenia()).isEqualTo(DEFAULT_DZIEN_CZYSZCZENIA);
        assertThat(testClientDetails.getZaplacono()).isEqualTo(DEFAULT_ZAPLACONO);
        assertThat(testClientDetails.getPodpis()).isEqualTo(DEFAULT_PODPIS);
    }

    @Test
    @Transactional
    void createClientDetailsWithExistingId() throws Exception {
        // Create the ClientDetails with an existing ID
        clientDetails.setId(1L);

        int databaseSizeBeforeCreate = clientDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientDetails in the database
        List<ClientDetails> clientDetailsList = clientDetailsRepository.findAll();
        assertThat(clientDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllClientDetails() throws Exception {
        // Initialize the database
        clientDetailsRepository.saveAndFlush(clientDetails);

        // Get all the clientDetailsList
        restClientDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].kominyDymowe").value(hasItem(DEFAULT_KOMINY_DYMOWE)))
            .andExpect(jsonPath("$.[*].kominySpalinowe").value(hasItem(DEFAULT_KOMINY_SPALINOWE)))
            .andExpect(jsonPath("$.[*].przewodyWentylacyjne").value(hasItem(DEFAULT_PRZEWODY_WENTYLACYJNE)))
            .andExpect(jsonPath("$.[*].ryczaltJednorazowy").value(hasItem(DEFAULT_RYCZALT_JEDNORAZOWY)))
            .andExpect(jsonPath("$.[*].dataCzyszczenia").value(hasItem(DEFAULT_DATA_CZYSZCZENIA.toString())))
            .andExpect(jsonPath("$.[*].dzienCzyszczenia").value(hasItem(DEFAULT_DZIEN_CZYSZCZENIA)))
            .andExpect(jsonPath("$.[*].zaplacono").value(hasItem(DEFAULT_ZAPLACONO)))
            .andExpect(jsonPath("$.[*].podpis").value(hasItem(DEFAULT_PODPIS)));
    }

    @Test
    @Transactional
    void getClientDetails() throws Exception {
        // Initialize the database
        clientDetailsRepository.saveAndFlush(clientDetails);

        // Get the clientDetails
        restClientDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, clientDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clientDetails.getId().intValue()))
            .andExpect(jsonPath("$.kominyDymowe").value(DEFAULT_KOMINY_DYMOWE))
            .andExpect(jsonPath("$.kominySpalinowe").value(DEFAULT_KOMINY_SPALINOWE))
            .andExpect(jsonPath("$.przewodyWentylacyjne").value(DEFAULT_PRZEWODY_WENTYLACYJNE))
            .andExpect(jsonPath("$.ryczaltJednorazowy").value(DEFAULT_RYCZALT_JEDNORAZOWY))
            .andExpect(jsonPath("$.dataCzyszczenia").value(DEFAULT_DATA_CZYSZCZENIA.toString()))
            .andExpect(jsonPath("$.dzienCzyszczenia").value(DEFAULT_DZIEN_CZYSZCZENIA))
            .andExpect(jsonPath("$.zaplacono").value(DEFAULT_ZAPLACONO))
            .andExpect(jsonPath("$.podpis").value(DEFAULT_PODPIS));
    }

    @Test
    @Transactional
    void getNonExistingClientDetails() throws Exception {
        // Get the clientDetails
        restClientDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClientDetails() throws Exception {
        // Initialize the database
        clientDetailsRepository.saveAndFlush(clientDetails);

        int databaseSizeBeforeUpdate = clientDetailsRepository.findAll().size();

        // Update the clientDetails
        ClientDetails updatedClientDetails = clientDetailsRepository.findById(clientDetails.getId()).get();
        // Disconnect from session so that the updates on updatedClientDetails are not directly saved in db
        em.detach(updatedClientDetails);
        updatedClientDetails
            .kominyDymowe(UPDATED_KOMINY_DYMOWE)
            .kominySpalinowe(UPDATED_KOMINY_SPALINOWE)
            .przewodyWentylacyjne(UPDATED_PRZEWODY_WENTYLACYJNE)
            .ryczaltJednorazowy(UPDATED_RYCZALT_JEDNORAZOWY)
            .dataCzyszczenia(UPDATED_DATA_CZYSZCZENIA)
            .dzienCzyszczenia(UPDATED_DZIEN_CZYSZCZENIA)
            .zaplacono(UPDATED_ZAPLACONO)
            .podpis(UPDATED_PODPIS);

        restClientDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClientDetails.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClientDetails))
            )
            .andExpect(status().isOk());

        // Validate the ClientDetails in the database
        List<ClientDetails> clientDetailsList = clientDetailsRepository.findAll();
        assertThat(clientDetailsList).hasSize(databaseSizeBeforeUpdate);
        ClientDetails testClientDetails = clientDetailsList.get(clientDetailsList.size() - 1);
        assertThat(testClientDetails.getKominyDymowe()).isEqualTo(UPDATED_KOMINY_DYMOWE);
        assertThat(testClientDetails.getKominySpalinowe()).isEqualTo(UPDATED_KOMINY_SPALINOWE);
        assertThat(testClientDetails.getPrzewodyWentylacyjne()).isEqualTo(UPDATED_PRZEWODY_WENTYLACYJNE);
        assertThat(testClientDetails.getRyczaltJednorazowy()).isEqualTo(UPDATED_RYCZALT_JEDNORAZOWY);
        assertThat(testClientDetails.getDataCzyszczenia()).isEqualTo(UPDATED_DATA_CZYSZCZENIA);
        assertThat(testClientDetails.getDzienCzyszczenia()).isEqualTo(UPDATED_DZIEN_CZYSZCZENIA);
        assertThat(testClientDetails.getZaplacono()).isEqualTo(UPDATED_ZAPLACONO);
        assertThat(testClientDetails.getPodpis()).isEqualTo(UPDATED_PODPIS);
    }

    @Test
    @Transactional
    void putNonExistingClientDetails() throws Exception {
        int databaseSizeBeforeUpdate = clientDetailsRepository.findAll().size();
        clientDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clientDetails.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientDetails in the database
        List<ClientDetails> clientDetailsList = clientDetailsRepository.findAll();
        assertThat(clientDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClientDetails() throws Exception {
        int databaseSizeBeforeUpdate = clientDetailsRepository.findAll().size();
        clientDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientDetails in the database
        List<ClientDetails> clientDetailsList = clientDetailsRepository.findAll();
        assertThat(clientDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClientDetails() throws Exception {
        int databaseSizeBeforeUpdate = clientDetailsRepository.findAll().size();
        clientDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientDetailsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clientDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClientDetails in the database
        List<ClientDetails> clientDetailsList = clientDetailsRepository.findAll();
        assertThat(clientDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClientDetailsWithPatch() throws Exception {
        // Initialize the database
        clientDetailsRepository.saveAndFlush(clientDetails);

        int databaseSizeBeforeUpdate = clientDetailsRepository.findAll().size();

        // Update the clientDetails using partial update
        ClientDetails partialUpdatedClientDetails = new ClientDetails();
        partialUpdatedClientDetails.setId(clientDetails.getId());

        partialUpdatedClientDetails
            .kominySpalinowe(UPDATED_KOMINY_SPALINOWE)
            .ryczaltJednorazowy(UPDATED_RYCZALT_JEDNORAZOWY)
            .zaplacono(UPDATED_ZAPLACONO)
            .podpis(UPDATED_PODPIS);

        restClientDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClientDetails.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClientDetails))
            )
            .andExpect(status().isOk());

        // Validate the ClientDetails in the database
        List<ClientDetails> clientDetailsList = clientDetailsRepository.findAll();
        assertThat(clientDetailsList).hasSize(databaseSizeBeforeUpdate);
        ClientDetails testClientDetails = clientDetailsList.get(clientDetailsList.size() - 1);
        assertThat(testClientDetails.getKominyDymowe()).isEqualTo(DEFAULT_KOMINY_DYMOWE);
        assertThat(testClientDetails.getKominySpalinowe()).isEqualTo(UPDATED_KOMINY_SPALINOWE);
        assertThat(testClientDetails.getPrzewodyWentylacyjne()).isEqualTo(DEFAULT_PRZEWODY_WENTYLACYJNE);
        assertThat(testClientDetails.getRyczaltJednorazowy()).isEqualTo(UPDATED_RYCZALT_JEDNORAZOWY);
        assertThat(testClientDetails.getDataCzyszczenia()).isEqualTo(DEFAULT_DATA_CZYSZCZENIA);
        assertThat(testClientDetails.getDzienCzyszczenia()).isEqualTo(DEFAULT_DZIEN_CZYSZCZENIA);
        assertThat(testClientDetails.getZaplacono()).isEqualTo(UPDATED_ZAPLACONO);
        assertThat(testClientDetails.getPodpis()).isEqualTo(UPDATED_PODPIS);
    }

    @Test
    @Transactional
    void fullUpdateClientDetailsWithPatch() throws Exception {
        // Initialize the database
        clientDetailsRepository.saveAndFlush(clientDetails);

        int databaseSizeBeforeUpdate = clientDetailsRepository.findAll().size();

        // Update the clientDetails using partial update
        ClientDetails partialUpdatedClientDetails = new ClientDetails();
        partialUpdatedClientDetails.setId(clientDetails.getId());

        partialUpdatedClientDetails
            .kominyDymowe(UPDATED_KOMINY_DYMOWE)
            .kominySpalinowe(UPDATED_KOMINY_SPALINOWE)
            .przewodyWentylacyjne(UPDATED_PRZEWODY_WENTYLACYJNE)
            .ryczaltJednorazowy(UPDATED_RYCZALT_JEDNORAZOWY)
            .dataCzyszczenia(UPDATED_DATA_CZYSZCZENIA)
            .dzienCzyszczenia(UPDATED_DZIEN_CZYSZCZENIA)
            .zaplacono(UPDATED_ZAPLACONO)
            .podpis(UPDATED_PODPIS);

        restClientDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClientDetails.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClientDetails))
            )
            .andExpect(status().isOk());

        // Validate the ClientDetails in the database
        List<ClientDetails> clientDetailsList = clientDetailsRepository.findAll();
        assertThat(clientDetailsList).hasSize(databaseSizeBeforeUpdate);
        ClientDetails testClientDetails = clientDetailsList.get(clientDetailsList.size() - 1);
        assertThat(testClientDetails.getKominyDymowe()).isEqualTo(UPDATED_KOMINY_DYMOWE);
        assertThat(testClientDetails.getKominySpalinowe()).isEqualTo(UPDATED_KOMINY_SPALINOWE);
        assertThat(testClientDetails.getPrzewodyWentylacyjne()).isEqualTo(UPDATED_PRZEWODY_WENTYLACYJNE);
        assertThat(testClientDetails.getRyczaltJednorazowy()).isEqualTo(UPDATED_RYCZALT_JEDNORAZOWY);
        assertThat(testClientDetails.getDataCzyszczenia()).isEqualTo(UPDATED_DATA_CZYSZCZENIA);
        assertThat(testClientDetails.getDzienCzyszczenia()).isEqualTo(UPDATED_DZIEN_CZYSZCZENIA);
        assertThat(testClientDetails.getZaplacono()).isEqualTo(UPDATED_ZAPLACONO);
        assertThat(testClientDetails.getPodpis()).isEqualTo(UPDATED_PODPIS);
    }

    @Test
    @Transactional
    void patchNonExistingClientDetails() throws Exception {
        int databaseSizeBeforeUpdate = clientDetailsRepository.findAll().size();
        clientDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clientDetails.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clientDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientDetails in the database
        List<ClientDetails> clientDetailsList = clientDetailsRepository.findAll();
        assertThat(clientDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClientDetails() throws Exception {
        int databaseSizeBeforeUpdate = clientDetailsRepository.findAll().size();
        clientDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clientDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the ClientDetails in the database
        List<ClientDetails> clientDetailsList = clientDetailsRepository.findAll();
        assertThat(clientDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClientDetails() throws Exception {
        int databaseSizeBeforeUpdate = clientDetailsRepository.findAll().size();
        clientDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clientDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ClientDetails in the database
        List<ClientDetails> clientDetailsList = clientDetailsRepository.findAll();
        assertThat(clientDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClientDetails() throws Exception {
        // Initialize the database
        clientDetailsRepository.saveAndFlush(clientDetails);

        int databaseSizeBeforeDelete = clientDetailsRepository.findAll().size();

        // Delete the clientDetails
        restClientDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, clientDetails.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ClientDetails> clientDetailsList = clientDetailsRepository.findAll();
        assertThat(clientDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
