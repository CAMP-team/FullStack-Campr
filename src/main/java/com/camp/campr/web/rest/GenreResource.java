package com.camp.campr.web.rest;

import com.camp.campr.domain.Genre;
import com.camp.campr.repository.GenreRepository;
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
 * REST controller for managing {@link com.camp.campr.domain.Genre}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GenreResource {

    private final Logger log = LoggerFactory.getLogger(GenreResource.class);

    private static final String ENTITY_NAME = "genre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GenreRepository genreRepository;

    public GenreResource(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    /**
     * {@code POST  /genres} : Create a new genre.
     *
     * @param genre the genre to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new genre, or with status {@code 400 (Bad Request)} if the genre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/genres")
    public ResponseEntity<Genre> createGenre(@RequestBody Genre genre) throws URISyntaxException {
        log.debug("REST request to save Genre : {}", genre);
        if (genre.getId() != null) {
            throw new BadRequestAlertException("A new genre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Genre result = genreRepository.save(genre);
        return ResponseEntity
            .created(new URI("/api/genres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /genres/:id} : Updates an existing genre.
     *
     * @param id the id of the genre to save.
     * @param genre the genre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated genre,
     * or with status {@code 400 (Bad Request)} if the genre is not valid,
     * or with status {@code 500 (Internal Server Error)} if the genre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/genres/{id}")
    public ResponseEntity<Genre> updateGenre(@PathVariable(value = "id", required = false) final Long id, @RequestBody Genre genre)
        throws URISyntaxException {
        log.debug("REST request to update Genre : {}, {}", id, genre);
        if (genre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, genre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!genreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Genre result = genreRepository.save(genre);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, genre.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /genres/:id} : Partial updates given fields of an existing genre, field will ignore if it is null
     *
     * @param id the id of the genre to save.
     * @param genre the genre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated genre,
     * or with status {@code 400 (Bad Request)} if the genre is not valid,
     * or with status {@code 404 (Not Found)} if the genre is not found,
     * or with status {@code 500 (Internal Server Error)} if the genre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/genres/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Genre> partialUpdateGenre(@PathVariable(value = "id", required = false) final Long id, @RequestBody Genre genre)
        throws URISyntaxException {
        log.debug("REST request to partial update Genre partially : {}, {}", id, genre);
        if (genre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, genre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!genreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Genre> result = genreRepository
            .findById(genre.getId())
            .map(existingGenre -> {
                if (genre.getApiId() != null) {
                    existingGenre.setApiId(genre.getApiId());
                }
                if (genre.getName() != null) {
                    existingGenre.setName(genre.getName());
                }

                return existingGenre;
            })
            .map(genreRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, genre.getId().toString())
        );
    }

    /**
     * {@code GET  /genres} : get all the genres.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of genres in body.
     */
    @GetMapping("/genres")
    public List<Genre> getAllGenres() {
        log.debug("REST request to get all Genres");
        return genreRepository.findAll();
    }

    /**
     * {@code GET  /genres/:id} : get the "id" genre.
     *
     * @param id the id of the genre to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the genre, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/genres/{id}")
    public ResponseEntity<Genre> getGenre(@PathVariable Long id) {
        log.debug("REST request to get Genre : {}", id);
        Optional<Genre> genre = genreRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(genre);
    }

    /**
     * {@code DELETE  /genres/:id} : delete the "id" genre.
     *
     * @param id the id of the genre to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/genres/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        log.debug("REST request to delete Genre : {}", id);
        genreRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
