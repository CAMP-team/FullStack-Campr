package com.camp.campr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.camp.campr.IntegrationTest;
import com.camp.campr.domain.UserComment;
import com.camp.campr.repository.UserCommentRepository;
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
 * Integration tests for the {@link UserCommentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserCommentResourceIT {

    private static final String DEFAULT_COMMENT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT_BODY = "BBBBBBBBBB";

    private static final Instant DEFAULT_COMMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-comments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserCommentRepository userCommentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserCommentMockMvc;

    private UserComment userComment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserComment createEntity(EntityManager em) {
        UserComment userComment = new UserComment().commentBody(DEFAULT_COMMENT_BODY).commentDate(DEFAULT_COMMENT_DATE);
        return userComment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserComment createUpdatedEntity(EntityManager em) {
        UserComment userComment = new UserComment().commentBody(UPDATED_COMMENT_BODY).commentDate(UPDATED_COMMENT_DATE);
        return userComment;
    }

    @BeforeEach
    public void initTest() {
        userComment = createEntity(em);
    }

    @Test
    @Transactional
    void createUserComment() throws Exception {
        int databaseSizeBeforeCreate = userCommentRepository.findAll().size();
        // Create the UserComment
        restUserCommentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userComment)))
            .andExpect(status().isCreated());

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeCreate + 1);
        UserComment testUserComment = userCommentList.get(userCommentList.size() - 1);
        assertThat(testUserComment.getCommentBody()).isEqualTo(DEFAULT_COMMENT_BODY);
        assertThat(testUserComment.getCommentDate()).isEqualTo(DEFAULT_COMMENT_DATE);
    }

    @Test
    @Transactional
    void createUserCommentWithExistingId() throws Exception {
        // Create the UserComment with an existing ID
        userComment.setId(1L);

        int databaseSizeBeforeCreate = userCommentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserCommentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userComment)))
            .andExpect(status().isBadRequest());

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserComments() throws Exception {
        // Initialize the database
        userCommentRepository.saveAndFlush(userComment);

        // Get all the userCommentList
        restUserCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
        //            .andExpect(jsonPath("$.[*].id").value(hasItem(userComment.getId().intValue())))
        //            .andExpect(jsonPath("$.[*].commentBody").value(hasItem(DEFAULT_COMMENT_BODY)))
        //            .andExpect(jsonPath("$.[*].commentDate").value(hasItem(DEFAULT_COMMENT_DATE.toString())));
    }

    @Test
    @Transactional
    void getUserComment() throws Exception {
        // Initialize the database
        userCommentRepository.saveAndFlush(userComment);

        // Get the userComment
        restUserCommentMockMvc
            .perform(get(ENTITY_API_URL_ID, userComment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userComment.getId().intValue()))
            .andExpect(jsonPath("$.commentBody").value(DEFAULT_COMMENT_BODY))
            .andExpect(jsonPath("$.commentDate").value(DEFAULT_COMMENT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserComment() throws Exception {
        // Get the userComment
        restUserCommentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUserComment() throws Exception {
        // Initialize the database
        userCommentRepository.saveAndFlush(userComment);

        int databaseSizeBeforeUpdate = userCommentRepository.findAll().size();

        // Update the userComment
        UserComment updatedUserComment = userCommentRepository.findById(userComment.getId()).get();
        // Disconnect from session so that the updates on updatedUserComment are not directly saved in db
        em.detach(updatedUserComment);
        updatedUserComment.commentBody(UPDATED_COMMENT_BODY).commentDate(UPDATED_COMMENT_DATE);

        restUserCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserComment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserComment))
            )
            .andExpect(status().isOk());

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
        UserComment testUserComment = userCommentList.get(userCommentList.size() - 1);
        assertThat(testUserComment.getCommentBody()).isEqualTo(UPDATED_COMMENT_BODY);
        assertThat(testUserComment.getCommentDate()).isEqualTo(UPDATED_COMMENT_DATE);
    }

    @Test
    @Transactional
    void putNonExistingUserComment() throws Exception {
        int databaseSizeBeforeUpdate = userCommentRepository.findAll().size();
        userComment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userComment.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userComment))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserComment() throws Exception {
        int databaseSizeBeforeUpdate = userCommentRepository.findAll().size();
        userComment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userComment))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserComment() throws Exception {
        int databaseSizeBeforeUpdate = userCommentRepository.findAll().size();
        userComment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCommentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userComment)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserCommentWithPatch() throws Exception {
        // Initialize the database
        userCommentRepository.saveAndFlush(userComment);

        int databaseSizeBeforeUpdate = userCommentRepository.findAll().size();

        // Update the userComment using partial update
        UserComment partialUpdatedUserComment = new UserComment();
        partialUpdatedUserComment.setId(userComment.getId());

        restUserCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserComment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserComment))
            )
            .andExpect(status().isOk());

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
        UserComment testUserComment = userCommentList.get(userCommentList.size() - 1);
        assertThat(testUserComment.getCommentBody()).isEqualTo(DEFAULT_COMMENT_BODY);
        assertThat(testUserComment.getCommentDate()).isEqualTo(DEFAULT_COMMENT_DATE);
    }

    @Test
    @Transactional
    void fullUpdateUserCommentWithPatch() throws Exception {
        // Initialize the database
        userCommentRepository.saveAndFlush(userComment);

        int databaseSizeBeforeUpdate = userCommentRepository.findAll().size();

        // Update the userComment using partial update
        UserComment partialUpdatedUserComment = new UserComment();
        partialUpdatedUserComment.setId(userComment.getId());

        partialUpdatedUserComment.commentBody(UPDATED_COMMENT_BODY).commentDate(UPDATED_COMMENT_DATE);

        restUserCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserComment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserComment))
            )
            .andExpect(status().isOk());

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
        UserComment testUserComment = userCommentList.get(userCommentList.size() - 1);
        assertThat(testUserComment.getCommentBody()).isEqualTo(UPDATED_COMMENT_BODY);
        assertThat(testUserComment.getCommentDate()).isEqualTo(UPDATED_COMMENT_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingUserComment() throws Exception {
        int databaseSizeBeforeUpdate = userCommentRepository.findAll().size();
        userComment.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userComment.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userComment))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserComment() throws Exception {
        int databaseSizeBeforeUpdate = userCommentRepository.findAll().size();
        userComment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userComment))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserComment() throws Exception {
        int databaseSizeBeforeUpdate = userCommentRepository.findAll().size();
        userComment.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserCommentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userComment))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserComment in the database
        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserComment() throws Exception {
        // Initialize the database
        userCommentRepository.saveAndFlush(userComment);

        int databaseSizeBeforeDelete = userCommentRepository.findAll().size();

        // Delete the userComment
        restUserCommentMockMvc
            .perform(delete(ENTITY_API_URL_ID, userComment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserComment> userCommentList = userCommentRepository.findAll();
        assertThat(userCommentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
