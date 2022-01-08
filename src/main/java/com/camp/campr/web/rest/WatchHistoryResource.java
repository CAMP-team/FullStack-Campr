package com.camp.campr.web.rest;

import com.camp.campr.domain.WatchHistory;
import com.camp.campr.repository.WatchHistoryRepository;
import com.camp.campr.service.WatchHistoryService;
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
 * REST controller for managing {@link com.camp.campr.domain.WatchHistory}.
 */
@RestController
@RequestMapping("/api")
public class WatchHistoryResource {

    private final Logger log = LoggerFactory.getLogger(WatchHistoryResource.class);

    private static final String ENTITY_NAME = "watchHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WatchHistoryService watchHistoryService;

    private final WatchHistoryRepository watchHistoryRepository;

    public WatchHistoryResource(WatchHistoryService watchHistoryService, WatchHistoryRepository watchHistoryRepository) {
        this.watchHistoryService = watchHistoryService;
        this.watchHistoryRepository = watchHistoryRepository;
    }

    /**
     * {@code POST  /watch-histories} : Create a new watchHistory.
     *
     * @param watchHistory the watchHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new watchHistory, or with status {@code 400 (Bad Request)} if the watchHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/watch-histories")
    public ResponseEntity<WatchHistory> createWatchHistory(@RequestBody WatchHistory watchHistory) throws URISyntaxException {
        log.debug("REST request to save WatchHistory : {}", watchHistory);
        if (watchHistory.getId() != null) {
            throw new BadRequestAlertException("A new watchHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WatchHistory result = watchHistoryService.save(watchHistory);
        return ResponseEntity
            .created(new URI("/api/watch-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /watch-histories/:id} : Updates an existing watchHistory.
     *
     * @param id the id of the watchHistory to save.
     * @param watchHistory the watchHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated watchHistory,
     * or with status {@code 400 (Bad Request)} if the watchHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the watchHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/watch-histories/{id}")
    public ResponseEntity<WatchHistory> updateWatchHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WatchHistory watchHistory
    ) throws URISyntaxException {
        log.debug("REST request to update WatchHistory : {}, {}", id, watchHistory);
        if (watchHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, watchHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!watchHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WatchHistory result = watchHistoryService.save(watchHistory);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, watchHistory.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /watch-histories/:id} : Partial updates given fields of an existing watchHistory, field will ignore if it is null
     *
     * @param id the id of the watchHistory to save.
     * @param watchHistory the watchHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated watchHistory,
     * or with status {@code 400 (Bad Request)} if the watchHistory is not valid,
     * or with status {@code 404 (Not Found)} if the watchHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the watchHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/watch-histories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WatchHistory> partialUpdateWatchHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WatchHistory watchHistory
    ) throws URISyntaxException {
        log.debug("REST request to partial update WatchHistory partially : {}, {}", id, watchHistory);
        if (watchHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, watchHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!watchHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WatchHistory> result = watchHistoryService.partialUpdate(watchHistory);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, watchHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /watch-histories} : get all the watchHistories.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of watchHistories in body.
     */
    @GetMapping("/watch-histories")
    public ResponseEntity<List<WatchHistory>> getAllWatchHistories(
        Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of WatchHistories");
        Page<WatchHistory> page;
        if (eagerload) {
            page = watchHistoryService.findAllWithEagerRelationships(pageable);
        } else {
            page = watchHistoryService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /watch-histories/:id} : get the "id" watchHistory.
     *
     * @param id the id of the watchHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the watchHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/watch-histories/{id}")
    public ResponseEntity<WatchHistory> getWatchHistory(@PathVariable Long id) {
        log.debug("REST request to get WatchHistory : {}", id);
        Optional<WatchHistory> watchHistory = watchHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(watchHistory);
    }

    /**
     * {@code DELETE  /watch-histories/:id} : delete the "id" watchHistory.
     *
     * @param id the id of the watchHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/watch-histories/{id}")
    public ResponseEntity<Void> deleteWatchHistory(@PathVariable Long id) {
        log.debug("REST request to delete WatchHistory : {}", id);
        watchHistoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
