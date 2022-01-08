package com.camp.campr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.camp.campr.IntegrationTest;
import com.camp.campr.domain.UserFavorites;
import com.camp.campr.repository.UserFavoritesRepository;
import com.camp.campr.service.UserFavoritesService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserFavoritesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserFavoritesResourceIT {

    private static final Instant DEFAULT_DATE_ADDED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_ADDED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-favorites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserFavoritesRepository userFavoritesRepository;

    @Mock
    private UserFavoritesRepository userFavoritesRepositoryMock;

    @Mock
    private UserFavoritesService userFavoritesServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserFavoritesMockMvc;

    private UserFavorites userFavorites;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserFavorites createEntity(EntityManager em) {
        UserFavorites userFavorites = new UserFavorites().dateAdded(DEFAULT_DATE_ADDED);
        return userFavorites;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserFavorites createUpdatedEntity(EntityManager em) {
        UserFavorites userFavorites = new UserFavorites().dateAdded(UPDATED_DATE_ADDED);
        return userFavorites;
    }

    @BeforeEach
    public void initTest() {
        userFavorites = createEntity(em);
    }

    @Test
    @Transactional
    void createUserFavorites() throws Exception {
        int databaseSizeBeforeCreate = userFavoritesRepository.findAll().size();
        // Create the UserFavorites
        restUserFavoritesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userFavorites)))
            .andExpect(status().isCreated());

        // Validate the UserFavorites in the database
        List<UserFavorites> userFavoritesList = userFavoritesRepository.findAll();
        assertThat(userFavoritesList).hasSize(databaseSizeBeforeCreate + 1);
        UserFavorites testUserFavorites = userFavoritesList.get(userFavoritesList.size() - 1);
        assertThat(testUserFavorites.getDateAdded()).isEqualTo(DEFAULT_DATE_ADDED);
    }

    @Test
    @Transactional
    void createUserFavoritesWithExistingId() throws Exception {
        // Create the UserFavorites with an existing ID
        userFavorites.setId(1L);

        int databaseSizeBeforeCreate = userFavoritesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserFavoritesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userFavorites)))
            .andExpect(status().isBadRequest());

        // Validate the UserFavorites in the database
        List<UserFavorites> userFavoritesList = userFavoritesRepository.findAll();
        assertThat(userFavoritesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserFavorites() throws Exception {
        // Initialize the database
        userFavoritesRepository.saveAndFlush(userFavorites);

        // Get all the userFavoritesList
        restUserFavoritesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userFavorites.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateAdded").value(hasItem(DEFAULT_DATE_ADDED.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserFavoritesWithEagerRelationshipsIsEnabled() throws Exception {
        when(userFavoritesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserFavoritesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userFavoritesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserFavoritesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userFavoritesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserFavoritesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userFavoritesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getUserFavorites() throws Exception {
        // Initialize the database
        userFavoritesRepository.saveAndFlush(userFavorites);

        // Get the userFavorites
        restUserFavoritesMockMvc
            .perform(get(ENTITY_API_URL_ID, userFavorites.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userFavorites.getId().intValue()))
            .andExpect(jsonPath("$.dateAdded").value(DEFAULT_DATE_ADDED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserFavorites() throws Exception {
        // Get the userFavorites
        restUserFavoritesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserFavorites() throws Exception {
        // Initialize the database
        userFavoritesRepository.saveAndFlush(userFavorites);

        int databaseSizeBeforeUpdate = userFavoritesRepository.findAll().size();

        // Update the userFavorites
        UserFavorites updatedUserFavorites = userFavoritesRepository.findById(userFavorites.getId()).get();
        // Disconnect from session so that the updates on updatedUserFavorites are not directly saved in db
        em.detach(updatedUserFavorites);
        updatedUserFavorites.dateAdded(UPDATED_DATE_ADDED);

        restUserFavoritesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserFavorites.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserFavorites))
            )
            .andExpect(status().isOk());

        // Validate the UserFavorites in the database
        List<UserFavorites> userFavoritesList = userFavoritesRepository.findAll();
        assertThat(userFavoritesList).hasSize(databaseSizeBeforeUpdate);
        UserFavorites testUserFavorites = userFavoritesList.get(userFavoritesList.size() - 1);
        assertThat(testUserFavorites.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
    }

    @Test
    @Transactional
    void putNonExistingUserFavorites() throws Exception {
        int databaseSizeBeforeUpdate = userFavoritesRepository.findAll().size();
        userFavorites.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserFavoritesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userFavorites.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userFavorites))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserFavorites in the database
        List<UserFavorites> userFavoritesList = userFavoritesRepository.findAll();
        assertThat(userFavoritesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserFavorites() throws Exception {
        int databaseSizeBeforeUpdate = userFavoritesRepository.findAll().size();
        userFavorites.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserFavoritesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userFavorites))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserFavorites in the database
        List<UserFavorites> userFavoritesList = userFavoritesRepository.findAll();
        assertThat(userFavoritesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserFavorites() throws Exception {
        int databaseSizeBeforeUpdate = userFavoritesRepository.findAll().size();
        userFavorites.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserFavoritesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userFavorites)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserFavorites in the database
        List<UserFavorites> userFavoritesList = userFavoritesRepository.findAll();
        assertThat(userFavoritesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserFavoritesWithPatch() throws Exception {
        // Initialize the database
        userFavoritesRepository.saveAndFlush(userFavorites);

        int databaseSizeBeforeUpdate = userFavoritesRepository.findAll().size();

        // Update the userFavorites using partial update
        UserFavorites partialUpdatedUserFavorites = new UserFavorites();
        partialUpdatedUserFavorites.setId(userFavorites.getId());

        partialUpdatedUserFavorites.dateAdded(UPDATED_DATE_ADDED);

        restUserFavoritesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserFavorites.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserFavorites))
            )
            .andExpect(status().isOk());

        // Validate the UserFavorites in the database
        List<UserFavorites> userFavoritesList = userFavoritesRepository.findAll();
        assertThat(userFavoritesList).hasSize(databaseSizeBeforeUpdate);
        UserFavorites testUserFavorites = userFavoritesList.get(userFavoritesList.size() - 1);
        assertThat(testUserFavorites.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
    }

    @Test
    @Transactional
    void fullUpdateUserFavoritesWithPatch() throws Exception {
        // Initialize the database
        userFavoritesRepository.saveAndFlush(userFavorites);

        int databaseSizeBeforeUpdate = userFavoritesRepository.findAll().size();

        // Update the userFavorites using partial update
        UserFavorites partialUpdatedUserFavorites = new UserFavorites();
        partialUpdatedUserFavorites.setId(userFavorites.getId());

        partialUpdatedUserFavorites.dateAdded(UPDATED_DATE_ADDED);

        restUserFavoritesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserFavorites.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserFavorites))
            )
            .andExpect(status().isOk());

        // Validate the UserFavorites in the database
        List<UserFavorites> userFavoritesList = userFavoritesRepository.findAll();
        assertThat(userFavoritesList).hasSize(databaseSizeBeforeUpdate);
        UserFavorites testUserFavorites = userFavoritesList.get(userFavoritesList.size() - 1);
        assertThat(testUserFavorites.getDateAdded()).isEqualTo(UPDATED_DATE_ADDED);
    }

    @Test
    @Transactional
    void patchNonExistingUserFavorites() throws Exception {
        int databaseSizeBeforeUpdate = userFavoritesRepository.findAll().size();
        userFavorites.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserFavoritesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userFavorites.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userFavorites))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserFavorites in the database
        List<UserFavorites> userFavoritesList = userFavoritesRepository.findAll();
        assertThat(userFavoritesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserFavorites() throws Exception {
        int databaseSizeBeforeUpdate = userFavoritesRepository.findAll().size();
        userFavorites.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserFavoritesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userFavorites))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserFavorites in the database
        List<UserFavorites> userFavoritesList = userFavoritesRepository.findAll();
        assertThat(userFavoritesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserFavorites() throws Exception {
        int databaseSizeBeforeUpdate = userFavoritesRepository.findAll().size();
        userFavorites.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserFavoritesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userFavorites))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserFavorites in the database
        List<UserFavorites> userFavoritesList = userFavoritesRepository.findAll();
        assertThat(userFavoritesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserFavorites() throws Exception {
        // Initialize the database
        userFavoritesRepository.saveAndFlush(userFavorites);

        int databaseSizeBeforeDelete = userFavoritesRepository.findAll().size();

        // Delete the userFavorites
        restUserFavoritesMockMvc
            .perform(delete(ENTITY_API_URL_ID, userFavorites.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserFavorites> userFavoritesList = userFavoritesRepository.findAll();
        assertThat(userFavoritesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
