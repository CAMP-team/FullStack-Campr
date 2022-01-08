package com.camp.campr.web.rest;

import com.camp.campr.domain.UserFavorites;
import com.camp.campr.repository.UserFavoritesRepository;
import com.camp.campr.service.UserFavoritesService;
import com.camp.campr.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.camp.campr.domain.UserFavorites}.
 */
@RestController
@RequestMapping("/api")
public class UserFavoritesResource {

    private final Logger log = LoggerFactory.getLogger(UserFavoritesResource.class);

    private static final String ENTITY_NAME = "userFavorites";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserFavoritesService userFavoritesService;

    private final UserFavoritesRepository userFavoritesRepository;

    public UserFavoritesResource(UserFavoritesService userFavoritesService, UserFavoritesRepository userFavoritesRepository) {
        this.userFavoritesService = userFavoritesService;
        this.userFavoritesRepository = userFavoritesRepository;
    }

    /**
     * {@code POST  /user-favorites} : Create a new userFavorites.
     *
     * @param userFavorites the userFavorites to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userFavorites, or with status {@code 400 (Bad Request)} if the userFavorites has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-favorites")
    public ResponseEntity<UserFavorites> createUserFavorites(@RequestBody UserFavorites userFavorites) throws URISyntaxException {
        log.debug("REST request to save UserFavorites : {}", userFavorites);
        if (userFavorites.getId() != null) {
            throw new BadRequestAlertException("A new userFavorites cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserFavorites result = userFavoritesService.save(userFavorites);
        return ResponseEntity
            .created(new URI("/api/user-favorites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-favorites/:id} : Updates an existing userFavorites.
     *
     * @param id the id of the userFavorites to save.
     * @param userFavorites the userFavorites to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userFavorites,
     * or with status {@code 400 (Bad Request)} if the userFavorites is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userFavorites couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-favorites/{id}")
    public ResponseEntity<UserFavorites> updateUserFavorites(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserFavorites userFavorites
    ) throws URISyntaxException {
        log.debug("REST request to update UserFavorites : {}, {}", id, userFavorites);
        if (userFavorites.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userFavorites.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userFavoritesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserFavorites result = userFavoritesService.save(userFavorites);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userFavorites.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-favorites/:id} : Partial updates given fields of an existing userFavorites, field will ignore if it is null
     *
     * @param id the id of the userFavorites to save.
     * @param userFavorites the userFavorites to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userFavorites,
     * or with status {@code 400 (Bad Request)} if the userFavorites is not valid,
     * or with status {@code 404 (Not Found)} if the userFavorites is not found,
     * or with status {@code 500 (Internal Server Error)} if the userFavorites couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-favorites/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserFavorites> partialUpdateUserFavorites(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserFavorites userFavorites
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserFavorites partially : {}, {}", id, userFavorites);
        if (userFavorites.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userFavorites.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userFavoritesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserFavorites> result = userFavoritesService.partialUpdate(userFavorites);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userFavorites.getId().toString())
        );
    }

    /**
     * {@code GET  /user-favorites} : get all the userFavorites.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userFavorites in body.
     */
    @GetMapping("/user-favorites")
    public ResponseEntity<List<UserFavorites>> getAllUserFavorites(
        Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of UserFavorites");
        Page<UserFavorites> page;
        if (eagerload) {
            page = userFavoritesService.findAllWithEagerRelationships(pageable);
        } else {
            page = userFavoritesService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-favorites/:id} : get the "id" userFavorites.
     *
     * @param id the id of the userFavorites to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userFavorites, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-favorites/{id}")
    public ResponseEntity<UserFavorites> getUserFavorites(@PathVariable Long id) {
        log.debug("REST request to get UserFavorites : {}", id);
        Optional<UserFavorites> userFavorites = userFavoritesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userFavorites);
    }

    /**
     * {@code DELETE  /user-favorites/:id} : delete the "id" userFavorites.
     *
     * @param id the id of the userFavorites to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-favorites/{id}")
    public ResponseEntity<Void> deleteUserFavorites(@PathVariable Long id) {
        log.debug("REST request to delete UserFavorites : {}", id);
        userFavoritesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
