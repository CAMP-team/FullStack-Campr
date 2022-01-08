package com.camp.campr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.camp.campr.IntegrationTest;
import com.camp.campr.domain.UserUpload;
import com.camp.campr.repository.UserUploadRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link UserUploadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserUploadResourceIT {

    private static final Instant DEFAULT_DATE_UPLOADED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_UPLOADED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-uploads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserUploadRepository userUploadRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserUploadMockMvc;

    private UserUpload userUpload;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserUpload createEntity(EntityManager em) {
        UserUpload userUpload = new UserUpload().dateUploaded(DEFAULT_DATE_UPLOADED);
        return userUpload;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserUpload createUpdatedEntity(EntityManager em) {
        UserUpload userUpload = new UserUpload().dateUploaded(UPDATED_DATE_UPLOADED);
        return userUpload;
    }

    @BeforeEach
    public void initTest() {
        userUpload = createEntity(em);
    }

    @Test
    @Transactional
    void createUserUpload() throws Exception {
        int databaseSizeBeforeCreate = userUploadRepository.findAll().size();
        // Create the UserUpload
        restUserUploadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userUpload)))
            .andExpect(status().isCreated());

        // Validate the UserUpload in the database
        List<UserUpload> userUploadList = userUploadRepository.findAll();
        assertThat(userUploadList).hasSize(databaseSizeBeforeCreate + 1);
        UserUpload testUserUpload = userUploadList.get(userUploadList.size() - 1);
        assertThat(testUserUpload.getDateUploaded()).isEqualTo(DEFAULT_DATE_UPLOADED);
    }

    @Test
    @Transactional
    void createUserUploadWithExistingId() throws Exception {
        // Create the UserUpload with an existing ID
        userUpload.setId(1L);

        int databaseSizeBeforeCreate = userUploadRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserUploadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userUpload)))
            .andExpect(status().isBadRequest());

        // Validate the UserUpload in the database
        List<UserUpload> userUploadList = userUploadRepository.findAll();
        assertThat(userUploadList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserUploads() throws Exception {
        // Initialize the database
        userUploadRepository.saveAndFlush(userUpload);

        // Get all the userUploadList
        restUserUploadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userUpload.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateUploaded").value(hasItem(DEFAULT_DATE_UPLOADED.toString())));
    }

    @Test
    @Transactional
    void getUserUpload() throws Exception {
        // Initialize the database
        userUploadRepository.saveAndFlush(userUpload);

        // Get the userUpload
        restUserUploadMockMvc
            .perform(get(ENTITY_API_URL_ID, userUpload.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userUpload.getId().intValue()))
            .andExpect(jsonPath("$.dateUploaded").value(DEFAULT_DATE_UPLOADED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserUpload() throws Exception {
        // Get the userUpload
        restUserUploadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserUpload() throws Exception {
        // Initialize the database
        userUploadRepository.saveAndFlush(userUpload);

        int databaseSizeBeforeUpdate = userUploadRepository.findAll().size();

        // Update the userUpload
        UserUpload updatedUserUpload = userUploadRepository.findById(userUpload.getId()).get();
        // Disconnect from session so that the updates on updatedUserUpload are not directly saved in db
        em.detach(updatedUserUpload);
        updatedUserUpload.dateUploaded(UPDATED_DATE_UPLOADED);

        restUserUploadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserUpload.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserUpload))
            )
            .andExpect(status().isOk());

        // Validate the UserUpload in the database
        List<UserUpload> userUploadList = userUploadRepository.findAll();
        assertThat(userUploadList).hasSize(databaseSizeBeforeUpdate);
        UserUpload testUserUpload = userUploadList.get(userUploadList.size() - 1);
        assertThat(testUserUpload.getDateUploaded()).isEqualTo(UPDATED_DATE_UPLOADED);
    }

    @Test
    @Transactional
    void putNonExistingUserUpload() throws Exception {
        int databaseSizeBeforeUpdate = userUploadRepository.findAll().size();
        userUpload.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserUploadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userUpload.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userUpload))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserUpload in the database
        List<UserUpload> userUploadList = userUploadRepository.findAll();
        assertThat(userUploadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserUpload() throws Exception {
        int databaseSizeBeforeUpdate = userUploadRepository.findAll().size();
        userUpload.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserUploadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userUpload))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserUpload in the database
        List<UserUpload> userUploadList = userUploadRepository.findAll();
        assertThat(userUploadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserUpload() throws Exception {
        int databaseSizeBeforeUpdate = userUploadRepository.findAll().size();
        userUpload.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserUploadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userUpload)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserUpload in the database
        List<UserUpload> userUploadList = userUploadRepository.findAll();
        assertThat(userUploadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserUploadWithPatch() throws Exception {
        // Initialize the database
        userUploadRepository.saveAndFlush(userUpload);

        int databaseSizeBeforeUpdate = userUploadRepository.findAll().size();

        // Update the userUpload using partial update
        UserUpload partialUpdatedUserUpload = new UserUpload();
        partialUpdatedUserUpload.setId(userUpload.getId());

        partialUpdatedUserUpload.dateUploaded(UPDATED_DATE_UPLOADED);

        restUserUploadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserUpload.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserUpload))
            )
            .andExpect(status().isOk());

        // Validate the UserUpload in the database
        List<UserUpload> userUploadList = userUploadRepository.findAll();
        assertThat(userUploadList).hasSize(databaseSizeBeforeUpdate);
        UserUpload testUserUpload = userUploadList.get(userUploadList.size() - 1);
        assertThat(testUserUpload.getDateUploaded()).isEqualTo(UPDATED_DATE_UPLOADED);
    }

    @Test
    @Transactional
    void fullUpdateUserUploadWithPatch() throws Exception {
        // Initialize the database
        userUploadRepository.saveAndFlush(userUpload);

        int databaseSizeBeforeUpdate = userUploadRepository.findAll().size();

        // Update the userUpload using partial update
        UserUpload partialUpdatedUserUpload = new UserUpload();
        partialUpdatedUserUpload.setId(userUpload.getId());

        partialUpdatedUserUpload.dateUploaded(UPDATED_DATE_UPLOADED);

        restUserUploadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserUpload.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserUpload))
            )
            .andExpect(status().isOk());

        // Validate the UserUpload in the database
        List<UserUpload> userUploadList = userUploadRepository.findAll();
        assertThat(userUploadList).hasSize(databaseSizeBeforeUpdate);
        UserUpload testUserUpload = userUploadList.get(userUploadList.size() - 1);
        assertThat(testUserUpload.getDateUploaded()).isEqualTo(UPDATED_DATE_UPLOADED);
    }

    @Test
    @Transactional
    void patchNonExistingUserUpload() throws Exception {
        int databaseSizeBeforeUpdate = userUploadRepository.findAll().size();
        userUpload.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserUploadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userUpload.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userUpload))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserUpload in the database
        List<UserUpload> userUploadList = userUploadRepository.findAll();
        assertThat(userUploadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserUpload() throws Exception {
        int databaseSizeBeforeUpdate = userUploadRepository.findAll().size();
        userUpload.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserUploadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userUpload))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserUpload in the database
        List<UserUpload> userUploadList = userUploadRepository.findAll();
        assertThat(userUploadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserUpload() throws Exception {
        int databaseSizeBeforeUpdate = userUploadRepository.findAll().size();
        userUpload.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserUploadMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userUpload))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserUpload in the database
        List<UserUpload> userUploadList = userUploadRepository.findAll();
        assertThat(userUploadList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserUpload() throws Exception {
        // Initialize the database
        userUploadRepository.saveAndFlush(userUpload);

        int databaseSizeBeforeDelete = userUploadRepository.findAll().size();

        // Delete the userUpload
        restUserUploadMockMvc
            .perform(delete(ENTITY_API_URL_ID, userUpload.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserUpload> userUploadList = userUploadRepository.findAll();
        assertThat(userUploadList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
