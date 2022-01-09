package com.camp.campr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.camp.campr.IntegrationTest;
import com.camp.campr.domain.WatchHistory;
import com.camp.campr.repository.WatchHistoryRepository;
import com.camp.campr.service.WatchHistoryService;
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
 * Integration tests for the {@link WatchHistoryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class WatchHistoryResourceIT {

    private static final Instant DEFAULT_DATE_WATCHED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_WATCHED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/watch-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WatchHistoryRepository watchHistoryRepository;

    @Mock
    private WatchHistoryRepository watchHistoryRepositoryMock;

    @Mock
    private WatchHistoryService watchHistoryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWatchHistoryMockMvc;

    private WatchHistory watchHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WatchHistory createEntity(EntityManager em) {
        WatchHistory watchHistory = new WatchHistory().dateWatched(DEFAULT_DATE_WATCHED);
        return watchHistory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WatchHistory createUpdatedEntity(EntityManager em) {
        WatchHistory watchHistory = new WatchHistory().dateWatched(UPDATED_DATE_WATCHED);
        return watchHistory;
    }

    @BeforeEach
    public void initTest() {
        watchHistory = createEntity(em);
    }

    @Test
    @Transactional
    void createWatchHistory() throws Exception {
        int databaseSizeBeforeCreate = watchHistoryRepository.findAll().size();
        // Create the WatchHistory
        restWatchHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(watchHistory)))
            .andExpect(status().isCreated());

        // Validate the WatchHistory in the database
        List<WatchHistory> watchHistoryList = watchHistoryRepository.findAll();
        assertThat(watchHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        WatchHistory testWatchHistory = watchHistoryList.get(watchHistoryList.size() - 1);
        assertThat(testWatchHistory.getDateWatched()).isEqualTo(DEFAULT_DATE_WATCHED);
    }

    @Test
    @Transactional
    void createWatchHistoryWithExistingId() throws Exception {
        // Create the WatchHistory with an existing ID
        watchHistory.setId(1L);

        int databaseSizeBeforeCreate = watchHistoryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWatchHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(watchHistory)))
            .andExpect(status().isBadRequest());

        // Validate the WatchHistory in the database
        List<WatchHistory> watchHistoryList = watchHistoryRepository.findAll();
        assertThat(watchHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWatchHistories() throws Exception {
        // Initialize the database
        watchHistoryRepository.saveAndFlush(watchHistory);

        // Get all the watchHistoryList
        restWatchHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(watchHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateWatched").value(hasItem(DEFAULT_DATE_WATCHED.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWatchHistoriesWithEagerRelationshipsIsEnabled() throws Exception {
        when(watchHistoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWatchHistoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(watchHistoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllWatchHistoriesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(watchHistoryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWatchHistoryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(watchHistoryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getWatchHistory() throws Exception {
        // Initialize the database
        watchHistoryRepository.saveAndFlush(watchHistory);

        // Get the watchHistory
        restWatchHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, watchHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(watchHistory.getId().intValue()))
            .andExpect(jsonPath("$.dateWatched").value(DEFAULT_DATE_WATCHED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingWatchHistory() throws Exception {
        // Get the watchHistory
        restWatchHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWatchHistory() throws Exception {
        // Initialize the database
        watchHistoryRepository.saveAndFlush(watchHistory);

        int databaseSizeBeforeUpdate = watchHistoryRepository.findAll().size();

        // Update the watchHistory
        WatchHistory updatedWatchHistory = watchHistoryRepository.findById(watchHistory.getId()).get();
        // Disconnect from session so that the updates on updatedWatchHistory are not directly saved in db
        em.detach(updatedWatchHistory);
        updatedWatchHistory.dateWatched(UPDATED_DATE_WATCHED);

        restWatchHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWatchHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWatchHistory))
            )
            .andExpect(status().isOk());

        // Validate the WatchHistory in the database
        List<WatchHistory> watchHistoryList = watchHistoryRepository.findAll();
        assertThat(watchHistoryList).hasSize(databaseSizeBeforeUpdate);
        WatchHistory testWatchHistory = watchHistoryList.get(watchHistoryList.size() - 1);
        assertThat(testWatchHistory.getDateWatched()).isEqualTo(UPDATED_DATE_WATCHED);
    }

    @Test
    @Transactional
    void putNonExistingWatchHistory() throws Exception {
        int databaseSizeBeforeUpdate = watchHistoryRepository.findAll().size();
        watchHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWatchHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, watchHistory.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(watchHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the WatchHistory in the database
        List<WatchHistory> watchHistoryList = watchHistoryRepository.findAll();
        assertThat(watchHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWatchHistory() throws Exception {
        int databaseSizeBeforeUpdate = watchHistoryRepository.findAll().size();
        watchHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWatchHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(watchHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the WatchHistory in the database
        List<WatchHistory> watchHistoryList = watchHistoryRepository.findAll();
        assertThat(watchHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWatchHistory() throws Exception {
        int databaseSizeBeforeUpdate = watchHistoryRepository.findAll().size();
        watchHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWatchHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(watchHistory)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WatchHistory in the database
        List<WatchHistory> watchHistoryList = watchHistoryRepository.findAll();
        assertThat(watchHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWatchHistoryWithPatch() throws Exception {
        // Initialize the database
        watchHistoryRepository.saveAndFlush(watchHistory);

        int databaseSizeBeforeUpdate = watchHistoryRepository.findAll().size();

        // Update the watchHistory using partial update
        WatchHistory partialUpdatedWatchHistory = new WatchHistory();
        partialUpdatedWatchHistory.setId(watchHistory.getId());

        restWatchHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWatchHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWatchHistory))
            )
            .andExpect(status().isOk());

        // Validate the WatchHistory in the database
        List<WatchHistory> watchHistoryList = watchHistoryRepository.findAll();
        assertThat(watchHistoryList).hasSize(databaseSizeBeforeUpdate);
        WatchHistory testWatchHistory = watchHistoryList.get(watchHistoryList.size() - 1);
        assertThat(testWatchHistory.getDateWatched()).isEqualTo(DEFAULT_DATE_WATCHED);
    }

    @Test
    @Transactional
    void fullUpdateWatchHistoryWithPatch() throws Exception {
        // Initialize the database
        watchHistoryRepository.saveAndFlush(watchHistory);

        int databaseSizeBeforeUpdate = watchHistoryRepository.findAll().size();

        // Update the watchHistory using partial update
        WatchHistory partialUpdatedWatchHistory = new WatchHistory();
        partialUpdatedWatchHistory.setId(watchHistory.getId());

        partialUpdatedWatchHistory.dateWatched(UPDATED_DATE_WATCHED);

        restWatchHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWatchHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWatchHistory))
            )
            .andExpect(status().isOk());

        // Validate the WatchHistory in the database
        List<WatchHistory> watchHistoryList = watchHistoryRepository.findAll();
        assertThat(watchHistoryList).hasSize(databaseSizeBeforeUpdate);
        WatchHistory testWatchHistory = watchHistoryList.get(watchHistoryList.size() - 1);
        assertThat(testWatchHistory.getDateWatched()).isEqualTo(UPDATED_DATE_WATCHED);
    }

    @Test
    @Transactional
    void patchNonExistingWatchHistory() throws Exception {
        int databaseSizeBeforeUpdate = watchHistoryRepository.findAll().size();
        watchHistory.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWatchHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, watchHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(watchHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the WatchHistory in the database
        List<WatchHistory> watchHistoryList = watchHistoryRepository.findAll();
        assertThat(watchHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWatchHistory() throws Exception {
        int databaseSizeBeforeUpdate = watchHistoryRepository.findAll().size();
        watchHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWatchHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(watchHistory))
            )
            .andExpect(status().isBadRequest());

        // Validate the WatchHistory in the database
        List<WatchHistory> watchHistoryList = watchHistoryRepository.findAll();
        assertThat(watchHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWatchHistory() throws Exception {
        int databaseSizeBeforeUpdate = watchHistoryRepository.findAll().size();
        watchHistory.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWatchHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(watchHistory))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WatchHistory in the database
        List<WatchHistory> watchHistoryList = watchHistoryRepository.findAll();
        assertThat(watchHistoryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWatchHistory() throws Exception {
        // Initialize the database
        watchHistoryRepository.saveAndFlush(watchHistory);

        int databaseSizeBeforeDelete = watchHistoryRepository.findAll().size();

        // Delete the watchHistory
        restWatchHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, watchHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WatchHistory> watchHistoryList = watchHistoryRepository.findAll();
        assertThat(watchHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
