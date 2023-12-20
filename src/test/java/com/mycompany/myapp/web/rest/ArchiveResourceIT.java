package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Archive;
import com.mycompany.myapp.repository.ArchiveRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ArchiveResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArchiveResourceIT {

    private static final ZonedDateTime DEFAULT_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_PROFICIENCY_LVL = 1;
    private static final Integer UPDATED_PROFICIENCY_LVL = 2;

    private static final String ENTITY_API_URL = "/api/archives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ArchiveRepository archiveRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArchiveMockMvc;

    private Archive archive;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Archive createEntity(EntityManager em) {
        Archive archive = new Archive().timestamp(DEFAULT_TIMESTAMP).proficiencyLvl(DEFAULT_PROFICIENCY_LVL);
        return archive;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Archive createUpdatedEntity(EntityManager em) {
        Archive archive = new Archive().timestamp(UPDATED_TIMESTAMP).proficiencyLvl(UPDATED_PROFICIENCY_LVL);
        return archive;
    }

    @BeforeEach
    public void initTest() {
        archive = createEntity(em);
    }

    @Test
    @Transactional
    void createArchive() throws Exception {
        int databaseSizeBeforeCreate = archiveRepository.findAll().size();
        // Create the Archive
        restArchiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(archive)))
            .andExpect(status().isCreated());

        // Validate the Archive in the database
        List<Archive> archiveList = archiveRepository.findAll();
        assertThat(archiveList).hasSize(databaseSizeBeforeCreate + 1);
        Archive testArchive = archiveList.get(archiveList.size() - 1);
        assertThat(testArchive.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testArchive.getProficiencyLvl()).isEqualTo(DEFAULT_PROFICIENCY_LVL);
    }

    @Test
    @Transactional
    void createArchiveWithExistingId() throws Exception {
        // Create the Archive with an existing ID
        archive.setId(1L);

        int databaseSizeBeforeCreate = archiveRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArchiveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(archive)))
            .andExpect(status().isBadRequest());

        // Validate the Archive in the database
        List<Archive> archiveList = archiveRepository.findAll();
        assertThat(archiveList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllArchives() throws Exception {
        // Initialize the database
        archiveRepository.saveAndFlush(archive);

        // Get all the archiveList
        restArchiveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(archive.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(sameInstant(DEFAULT_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].proficiencyLvl").value(hasItem(DEFAULT_PROFICIENCY_LVL)));
    }

    @Test
    @Transactional
    void getArchive() throws Exception {
        // Initialize the database
        archiveRepository.saveAndFlush(archive);

        // Get the archive
        restArchiveMockMvc
            .perform(get(ENTITY_API_URL_ID, archive.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(archive.getId().intValue()))
            .andExpect(jsonPath("$.timestamp").value(sameInstant(DEFAULT_TIMESTAMP)))
            .andExpect(jsonPath("$.proficiencyLvl").value(DEFAULT_PROFICIENCY_LVL));
    }

    @Test
    @Transactional
    void getNonExistingArchive() throws Exception {
        // Get the archive
        restArchiveMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArchive() throws Exception {
        // Initialize the database
        archiveRepository.saveAndFlush(archive);

        int databaseSizeBeforeUpdate = archiveRepository.findAll().size();

        // Update the archive
        Archive updatedArchive = archiveRepository.findById(archive.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedArchive are not directly saved in db
        em.detach(updatedArchive);
        updatedArchive.timestamp(UPDATED_TIMESTAMP).proficiencyLvl(UPDATED_PROFICIENCY_LVL);

        restArchiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedArchive.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedArchive))
            )
            .andExpect(status().isOk());

        // Validate the Archive in the database
        List<Archive> archiveList = archiveRepository.findAll();
        assertThat(archiveList).hasSize(databaseSizeBeforeUpdate);
        Archive testArchive = archiveList.get(archiveList.size() - 1);
        assertThat(testArchive.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testArchive.getProficiencyLvl()).isEqualTo(UPDATED_PROFICIENCY_LVL);
    }

    @Test
    @Transactional
    void putNonExistingArchive() throws Exception {
        int databaseSizeBeforeUpdate = archiveRepository.findAll().size();
        archive.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArchiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, archive.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(archive))
            )
            .andExpect(status().isBadRequest());

        // Validate the Archive in the database
        List<Archive> archiveList = archiveRepository.findAll();
        assertThat(archiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArchive() throws Exception {
        int databaseSizeBeforeUpdate = archiveRepository.findAll().size();
        archive.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArchiveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(archive))
            )
            .andExpect(status().isBadRequest());

        // Validate the Archive in the database
        List<Archive> archiveList = archiveRepository.findAll();
        assertThat(archiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArchive() throws Exception {
        int databaseSizeBeforeUpdate = archiveRepository.findAll().size();
        archive.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArchiveMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(archive)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Archive in the database
        List<Archive> archiveList = archiveRepository.findAll();
        assertThat(archiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArchiveWithPatch() throws Exception {
        // Initialize the database
        archiveRepository.saveAndFlush(archive);

        int databaseSizeBeforeUpdate = archiveRepository.findAll().size();

        // Update the archive using partial update
        Archive partialUpdatedArchive = new Archive();
        partialUpdatedArchive.setId(archive.getId());

        partialUpdatedArchive.timestamp(UPDATED_TIMESTAMP);

        restArchiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArchive.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArchive))
            )
            .andExpect(status().isOk());

        // Validate the Archive in the database
        List<Archive> archiveList = archiveRepository.findAll();
        assertThat(archiveList).hasSize(databaseSizeBeforeUpdate);
        Archive testArchive = archiveList.get(archiveList.size() - 1);
        assertThat(testArchive.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testArchive.getProficiencyLvl()).isEqualTo(DEFAULT_PROFICIENCY_LVL);
    }

    @Test
    @Transactional
    void fullUpdateArchiveWithPatch() throws Exception {
        // Initialize the database
        archiveRepository.saveAndFlush(archive);

        int databaseSizeBeforeUpdate = archiveRepository.findAll().size();

        // Update the archive using partial update
        Archive partialUpdatedArchive = new Archive();
        partialUpdatedArchive.setId(archive.getId());

        partialUpdatedArchive.timestamp(UPDATED_TIMESTAMP).proficiencyLvl(UPDATED_PROFICIENCY_LVL);

        restArchiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArchive.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArchive))
            )
            .andExpect(status().isOk());

        // Validate the Archive in the database
        List<Archive> archiveList = archiveRepository.findAll();
        assertThat(archiveList).hasSize(databaseSizeBeforeUpdate);
        Archive testArchive = archiveList.get(archiveList.size() - 1);
        assertThat(testArchive.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testArchive.getProficiencyLvl()).isEqualTo(UPDATED_PROFICIENCY_LVL);
    }

    @Test
    @Transactional
    void patchNonExistingArchive() throws Exception {
        int databaseSizeBeforeUpdate = archiveRepository.findAll().size();
        archive.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArchiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, archive.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(archive))
            )
            .andExpect(status().isBadRequest());

        // Validate the Archive in the database
        List<Archive> archiveList = archiveRepository.findAll();
        assertThat(archiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArchive() throws Exception {
        int databaseSizeBeforeUpdate = archiveRepository.findAll().size();
        archive.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArchiveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(archive))
            )
            .andExpect(status().isBadRequest());

        // Validate the Archive in the database
        List<Archive> archiveList = archiveRepository.findAll();
        assertThat(archiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArchive() throws Exception {
        int databaseSizeBeforeUpdate = archiveRepository.findAll().size();
        archive.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArchiveMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(archive)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Archive in the database
        List<Archive> archiveList = archiveRepository.findAll();
        assertThat(archiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArchive() throws Exception {
        // Initialize the database
        archiveRepository.saveAndFlush(archive);

        int databaseSizeBeforeDelete = archiveRepository.findAll().size();

        // Delete the archive
        restArchiveMockMvc
            .perform(delete(ENTITY_API_URL_ID, archive.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Archive> archiveList = archiveRepository.findAll();
        assertThat(archiveList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
