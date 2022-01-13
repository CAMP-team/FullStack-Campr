package com.camp.campr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.camp.campr.IntegrationTest;
import com.camp.campr.domain.Genre;
import com.camp.campr.repository.GenreRepository;
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
 * Integration tests for the {@link GenreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GenreResourceIT {

    private static final Integer DEFAULT_API_ID = 1;
    private static final Integer UPDATED_API_ID = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/genres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGenreMockMvc;

    private Genre genre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Genre createEntity(EntityManager em) {
        Genre genre = new Genre().apiId(DEFAULT_API_ID).name(DEFAULT_NAME);
        return genre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Genre createUpdatedEntity(EntityManager em) {
        Genre genre = new Genre().apiId(UPDATED_API_ID).name(UPDATED_NAME);
        return genre;
    }

    @BeforeEach
    public void initTest() {
        genre = createEntity(em);
    }

    @Test
    @Transactional
    void createGenre() throws Exception {
        int databaseSizeBeforeCreate = genreRepository.findAll().size();
        // Create the Genre
        restGenreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genre)))
            .andExpect(status().isCreated());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeCreate + 1);
        Genre testGenre = genreList.get(genreList.size() - 1);
        assertThat(testGenre.getApiId()).isEqualTo(DEFAULT_API_ID);
        assertThat(testGenre.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createGenreWithExistingId() throws Exception {
        // Create the Genre with an existing ID
        genre.setId(1L);

        int databaseSizeBeforeCreate = genreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGenreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genre)))
            .andExpect(status().isBadRequest());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGenres() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get all the genreList
        restGenreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(genre.getId().intValue())))
            .andExpect(jsonPath("$.[*].apiId").value(hasItem(DEFAULT_API_ID)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getGenre() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        // Get the genre
        restGenreMockMvc
            .perform(get(ENTITY_API_URL_ID, genre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(genre.getId().intValue()))
            .andExpect(jsonPath("$.apiId").value(DEFAULT_API_ID))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingGenre() throws Exception {
        // Get the genre
        restGenreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGenre() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        int databaseSizeBeforeUpdate = genreRepository.findAll().size();

        // Update the genre
        Genre updatedGenre = genreRepository.findById(genre.getId()).get();
        // Disconnect from session so that the updates on updatedGenre are not directly saved in db
        em.detach(updatedGenre);
        updatedGenre.apiId(UPDATED_API_ID).name(UPDATED_NAME);

        restGenreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGenre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGenre))
            )
            .andExpect(status().isOk());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
        Genre testGenre = genreList.get(genreList.size() - 1);
        assertThat(testGenre.getApiId()).isEqualTo(UPDATED_API_ID);
        assertThat(testGenre.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingGenre() throws Exception {
        int databaseSizeBeforeUpdate = genreRepository.findAll().size();
        genre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGenreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, genre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGenre() throws Exception {
        int databaseSizeBeforeUpdate = genreRepository.findAll().size();
        genre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(genre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGenre() throws Exception {
        int databaseSizeBeforeUpdate = genreRepository.findAll().size();
        genre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(genre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGenreWithPatch() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        int databaseSizeBeforeUpdate = genreRepository.findAll().size();

        // Update the genre using partial update
        Genre partialUpdatedGenre = new Genre();
        partialUpdatedGenre.setId(genre.getId());

        partialUpdatedGenre.apiId(UPDATED_API_ID).name(UPDATED_NAME);

        restGenreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenre))
            )
            .andExpect(status().isOk());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
        Genre testGenre = genreList.get(genreList.size() - 1);
        assertThat(testGenre.getApiId()).isEqualTo(UPDATED_API_ID);
        assertThat(testGenre.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateGenreWithPatch() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        int databaseSizeBeforeUpdate = genreRepository.findAll().size();

        // Update the genre using partial update
        Genre partialUpdatedGenre = new Genre();
        partialUpdatedGenre.setId(genre.getId());

        partialUpdatedGenre.apiId(UPDATED_API_ID).name(UPDATED_NAME);

        restGenreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGenre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGenre))
            )
            .andExpect(status().isOk());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
        Genre testGenre = genreList.get(genreList.size() - 1);
        assertThat(testGenre.getApiId()).isEqualTo(UPDATED_API_ID);
        assertThat(testGenre.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingGenre() throws Exception {
        int databaseSizeBeforeUpdate = genreRepository.findAll().size();
        genre.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGenreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, genre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(genre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGenre() throws Exception {
        int databaseSizeBeforeUpdate = genreRepository.findAll().size();
        genre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(genre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGenre() throws Exception {
        int databaseSizeBeforeUpdate = genreRepository.findAll().size();
        genre.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGenreMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(genre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Genre in the database
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGenre() throws Exception {
        // Initialize the database
        genreRepository.saveAndFlush(genre);

        int databaseSizeBeforeDelete = genreRepository.findAll().size();

        // Delete the genre
        restGenreMockMvc
            .perform(delete(ENTITY_API_URL_ID, genre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Genre> genreList = genreRepository.findAll();
        assertThat(genreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
