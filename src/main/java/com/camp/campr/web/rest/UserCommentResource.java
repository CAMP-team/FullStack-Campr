package com.camp.campr.web.rest;

import com.camp.campr.domain.UserComment;
import com.camp.campr.repository.UserCommentRepository;
import com.camp.campr.security.SecurityUtils;
import com.camp.campr.service.UserCommentService;
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
 * REST controller for managing {@link com.camp.campr.domain.UserComment}.
 */
@RestController
@RequestMapping("/api")
public class UserCommentResource {

    private final Logger log = LoggerFactory.getLogger(UserCommentResource.class);

    private static final String ENTITY_NAME = "userComment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserCommentService userCommentService;

    private final UserCommentRepository userCommentRepository;

    public UserCommentResource(UserCommentService userCommentService, UserCommentRepository userCommentRepository) {
        this.userCommentService = userCommentService;
        this.userCommentRepository = userCommentRepository;
    }

    /**
     * {@code POST  /user-comments} : Create a new userComment.
     *
     * @param userComment the userComment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userComment, or with status {@code 400 (Bad Request)} if the userComment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user-comments")
    public ResponseEntity<UserComment> createUserComment(@RequestBody UserComment userComment) throws URISyntaxException {
        log.debug("REST request to save UserComment : {}", userComment);
        if (userComment.getId() != null) {
            throw new BadRequestAlertException("A new userComment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserComment result = userCommentService.save(userComment);
        return ResponseEntity
            .created(new URI("/api/user-comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-comments/:id} : Updates an existing userComment.
     *
     * @param id the id of the userComment to save.
     * @param userComment the userComment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userComment,
     * or with status {@code 400 (Bad Request)} if the userComment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userComment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/user-comments/{id}")
    public ResponseEntity<UserComment> updateUserComment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserComment userComment
    ) throws URISyntaxException {
        log.debug("REST request to update UserComment : {}, {}", id, userComment);
        if (userComment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userComment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userCommentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserComment result = userCommentService.save(userComment);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userComment.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-comments/:id} : Partial updates given fields of an existing userComment, field will ignore if it is null
     *
     * @param id the id of the userComment to save.
     * @param userComment the userComment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userComment,
     * or with status {@code 400 (Bad Request)} if the userComment is not valid,
     * or with status {@code 404 (Not Found)} if the userComment is not found,
     * or with status {@code 500 (Internal Server Error)} if the userComment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/user-comments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserComment> partialUpdateUserComment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserComment userComment
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserComment partially : {}, {}", id, userComment);
        if (userComment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userComment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userCommentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserComment> result = userCommentService.partialUpdate(userComment);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userComment.getId().toString())
        );
    }

    /**
     * {@code GET  /user-comments} : get all the userComments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userComments in body.
     */
    @GetMapping("/user-comments")
    public ResponseEntity<List<UserComment>> getAllUserComments(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of UserComments");
        // Page<UserComment> page = userCommentService.findAll(pageable);
        Page<UserComment> page = userCommentService.findByAppUser(SecurityUtils.getCurrentUserLogin().orElse(null), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-comments/:id} : get the "id" userComment.
     *
     * @param id the id of the userComment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userComment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/user-comments/{id}")
    public ResponseEntity<UserComment> getUserComment(@PathVariable Long id) {
        log.debug("REST request to get UserComment : {}", id);
        Optional<UserComment> userComment = userCommentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userComment);
    }

    /**
     * {@code DELETE  /user-comments/:id} : delete the "id" userComment.
     *
     * @param id the id of the userComment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/user-comments/{id}")
    public ResponseEntity<Void> deleteUserComment(@PathVariable Long id) {
        log.debug("REST request to delete UserComment : {}", id);
        userCommentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
