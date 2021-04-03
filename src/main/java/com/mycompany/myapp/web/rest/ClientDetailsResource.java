package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ClientDetails;
import com.mycompany.myapp.repository.ClientDetailsRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ClientDetails}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ClientDetailsResource {

    private final Logger log = LoggerFactory.getLogger(ClientDetailsResource.class);

    private static final String ENTITY_NAME = "clientDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClientDetailsRepository clientDetailsRepository;

    public ClientDetailsResource(ClientDetailsRepository clientDetailsRepository) {
        this.clientDetailsRepository = clientDetailsRepository;
    }

    /**
     * {@code POST  /client-details} : Create a new clientDetails.
     *
     * @param clientDetails the clientDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clientDetails, or with status {@code 400 (Bad Request)} if the clientDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/client-details")
    public ResponseEntity<ClientDetails> createClientDetails(@RequestBody ClientDetails clientDetails) throws URISyntaxException {
        log.debug("REST request to save ClientDetails : {}", clientDetails);
        if (clientDetails.getId() != null) {
            throw new BadRequestAlertException("A new clientDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClientDetails result = clientDetailsRepository.save(clientDetails);
        return ResponseEntity
            .created(new URI("/api/client-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /client-details/:id} : Updates an existing clientDetails.
     *
     * @param id the id of the clientDetails to save.
     * @param clientDetails the clientDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientDetails,
     * or with status {@code 400 (Bad Request)} if the clientDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clientDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/client-details/{id}")
    public ResponseEntity<ClientDetails> updateClientDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ClientDetails clientDetails
    ) throws URISyntaxException {
        log.debug("REST request to update ClientDetails : {}, {}", id, clientDetails);
        if (clientDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clientDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ClientDetails result = clientDetailsRepository.save(clientDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, clientDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /client-details/:id} : Partial updates given fields of an existing clientDetails, field will ignore if it is null
     *
     * @param id the id of the clientDetails to save.
     * @param clientDetails the clientDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clientDetails,
     * or with status {@code 400 (Bad Request)} if the clientDetails is not valid,
     * or with status {@code 404 (Not Found)} if the clientDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the clientDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/client-details/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ClientDetails> partialUpdateClientDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ClientDetails clientDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update ClientDetails partially : {}, {}", id, clientDetails);
        if (clientDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clientDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clientDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ClientDetails> result = clientDetailsRepository
            .findById(clientDetails.getId())
            .map(
                existingClientDetails -> {
                    if (clientDetails.getKominyDymowe() != null) {
                        existingClientDetails.setKominyDymowe(clientDetails.getKominyDymowe());
                    }
                    if (clientDetails.getKominySpalinowe() != null) {
                        existingClientDetails.setKominySpalinowe(clientDetails.getKominySpalinowe());
                    }
                    if (clientDetails.getPrzewodyWentylacyjne() != null) {
                        existingClientDetails.setPrzewodyWentylacyjne(clientDetails.getPrzewodyWentylacyjne());
                    }
                    if (clientDetails.getRyczaltJednorazowy() != null) {
                        existingClientDetails.setRyczaltJednorazowy(clientDetails.getRyczaltJednorazowy());
                    }
                    if (clientDetails.getDataCzyszczenia() != null) {
                        existingClientDetails.setDataCzyszczenia(clientDetails.getDataCzyszczenia());
                    }
                    if (clientDetails.getDzienCzyszczenia() != null) {
                        existingClientDetails.setDzienCzyszczenia(clientDetails.getDzienCzyszczenia());
                    }
                    if (clientDetails.getZaplacono() != null) {
                        existingClientDetails.setZaplacono(clientDetails.getZaplacono());
                    }
                    if (clientDetails.getPodpis() != null) {
                        existingClientDetails.setPodpis(clientDetails.getPodpis());
                    }

                    return existingClientDetails;
                }
            )
            .map(clientDetailsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, clientDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /client-details} : get all the clientDetails.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clientDetails in body.
     */
    @GetMapping("/client-details")
    public List<ClientDetails> getAllClientDetails() {
        log.debug("REST request to get all ClientDetails");
        return clientDetailsRepository.findAll();
    }

    /**
     * {@code GET  /client-details/:id} : get the "id" clientDetails.
     *
     * @param id the id of the clientDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clientDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/client-details/{id}")
    public ResponseEntity<ClientDetails> getClientDetails(@PathVariable Long id) {
        log.debug("REST request to get ClientDetails : {}", id);
        Optional<ClientDetails> clientDetails = clientDetailsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(clientDetails);
    }

    /**
     * {@code DELETE  /client-details/:id} : delete the "id" clientDetails.
     *
     * @param id the id of the clientDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/client-details/{id}")
    public ResponseEntity<Void> deleteClientDetails(@PathVariable Long id) {
        log.debug("REST request to delete ClientDetails : {}", id);
        clientDetailsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
