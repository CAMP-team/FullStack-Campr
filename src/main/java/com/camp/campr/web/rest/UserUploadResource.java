package com.camp.campr.web.rest;

import com.camp.campr.domain.UserUpload;
import com.camp.campr.repository.UserUploadRepository;
import com.camp.campr.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.camp.campr.domain.UserUpload}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UserUploadResource {

    private final Logger log = LoggerFactory.getLogger(UserUploadResource.class);

    private static final String ENTITY_NAME = "userUpload";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserUploadRepository userUploadRepository;

    public UserUploadResource(UserUploadRepository userUploadRepository) {
        this.userUploadRepository = userUploadRepository;
    }

    /**
     * {@code POST  /user-uploads} : Create a new userUpload.
     *
     * @param userUpload the userUpload to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userUpload, or with status {@code 400 (Bad Request)} if the userUpload has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-uploads")
    public ResponseEntity<UserUpload> createUserUpload(@RequestBody UserUpload userUpload) throws URISyntaxException {
        log.debug("REST request to save UserUpload : {}", userUpload);
        if (userUpload.getId() != null) {
            throw new BadRequestAlertException("A new userUpload cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserUpload result = userUploadRepository.save(userUpload);
        return ResponseEntity
            .created(new URI("/api/user-uploads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-uploads/:id} : Updates an existing userUpload.
     *
     * @param id the id of the userUpload to save.
     * @param userUpload the userUpload to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userUpload,
     * or with status {@code 400 (Bad Request)} if the userUpload is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userUpload couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-uploads/{id}")
    public ResponseEntity<UserUpload> updateUserUpload(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserUpload userUpload
    ) throws URISyntaxException {
        log.debug("REST request to update UserUpload : {}, {}", id, userUpload);
        if (userUpload.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userUpload.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userUploadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserUpload result = userUploadRepository.save(userUpload);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userUpload.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-uploads/:id} : Partial updates given fields of an existing userUpload, field will ignore if it is null
     *
     * @param id the id of the userUpload to save.
     * @param userUpload the userUpload to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userUpload,
     * or with status {@code 400 (Bad Request)} if the userUpload is not valid,
     * or with status {@code 404 (Not Found)} if the userUpload is not found,
     * or with status {@code 500 (Internal Server Error)} if the userUpload couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-uploads/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserUpload> partialUpdateUserUpload(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserUpload userUpload
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserUpload partially : {}, {}", id, userUpload);
        if (userUpload.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userUpload.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userUploadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserUpload> result = userUploadRepository
            .findById(userUpload.getId())
            .map(existingUserUpload -> {
                if (userUpload.getDateUploaded() != null) {
                    existingUserUpload.setDateUploaded(userUpload.getDateUploaded());
                }

                return existingUserUpload;
            })
            .map(userUploadRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userUpload.getId().toString())
        );
    }

    /**
     * {@code GET  /user-uploads} : get all the userUploads.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userUploads in body.
     */
    @GetMapping("/user-uploads")
    public List<UserUpload> getAllUserUploads() {
        log.debug("REST request to get all UserUploads");
        return userUploadRepository.findAll();
    }

    /**
     * {@code GET  /user-uploads/:id} : get the "id" userUpload.
     *
     * @param id the id of the userUpload to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userUpload, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-uploads/{id}")
    public ResponseEntity<UserUpload> getUserUpload(@PathVariable Long id) {
        log.debug("REST request to get UserUpload : {}", id);
        Optional<UserUpload> userUpload = userUploadRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userUpload);
    }

    /**
     * {@code DELETE  /user-uploads/:id} : delete the "id" userUpload.
     *
     * @param id the id of the userUpload to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-uploads/{id}")
    public ResponseEntity<Void> deleteUserUpload(@PathVariable Long id) {
        log.debug("REST request to delete UserUpload : {}", id);
        userUploadRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
