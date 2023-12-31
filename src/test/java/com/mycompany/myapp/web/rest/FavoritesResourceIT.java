package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Favorites;
import com.mycompany.myapp.repository.FavoritesRepository;
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
 * Integration tests for the {@link FavoritesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FavoritesResourceIT {

    private static final ZonedDateTime DEFAULT_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_PROFICIENCY_LVL = 1;
    private static final Integer UPDATED_PROFICIENCY_LVL = 2;

    private static final String ENTITY_API_URL = "/api/favorites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FavoritesRepository favoritesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFavoritesMockMvc;

    private Favorites favorites;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Favorites createEntity(EntityManager em) {
        Favorites favorites = new Favorites().timestamp(DEFAULT_TIMESTAMP).proficiencyLvl(DEFAULT_PROFICIENCY_LVL);
        return favorites;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Favorites createUpdatedEntity(EntityManager em) {
        Favorites favorites = new Favorites().timestamp(UPDATED_TIMESTAMP).proficiencyLvl(UPDATED_PROFICIENCY_LVL);
        return favorites;
    }

    @BeforeEach
    public void initTest() {
        favorites = createEntity(em);
    }

    @Test
    @Transactional
    void createFavorites() throws Exception {
        int databaseSizeBeforeCreate = favoritesRepository.findAll().size();
        // Create the Favorites
        restFavoritesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(favorites)))
            .andExpect(status().isCreated());

        // Validate the Favorites in the database
        List<Favorites> favoritesList = favoritesRepository.findAll();
        assertThat(favoritesList).hasSize(databaseSizeBeforeCreate + 1);
        Favorites testFavorites = favoritesList.get(favoritesList.size() - 1);
        assertThat(testFavorites.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testFavorites.getProficiencyLvl()).isEqualTo(DEFAULT_PROFICIENCY_LVL);
    }

    @Test
    @Transactional
    void createFavoritesWithExistingId() throws Exception {
        // Create the Favorites with an existing ID
        favorites.setId(1L);

        int databaseSizeBeforeCreate = favoritesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFavoritesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(favorites)))
            .andExpect(status().isBadRequest());

        // Validate the Favorites in the database
        List<Favorites> favoritesList = favoritesRepository.findAll();
        assertThat(favoritesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFavorites() throws Exception {
        // Initialize the database
        favoritesRepository.saveAndFlush(favorites);

        // Get all the favoritesList
        restFavoritesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(favorites.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(sameInstant(DEFAULT_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].proficiencyLvl").value(hasItem(DEFAULT_PROFICIENCY_LVL)));
    }

    @Test
    @Transactional
    void getFavorites() throws Exception {
        // Initialize the database
        favoritesRepository.saveAndFlush(favorites);

        // Get the favorites
        restFavoritesMockMvc
            .perform(get(ENTITY_API_URL_ID, favorites.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(favorites.getId().intValue()))
            .andExpect(jsonPath("$.timestamp").value(sameInstant(DEFAULT_TIMESTAMP)))
            .andExpect(jsonPath("$.proficiencyLvl").value(DEFAULT_PROFICIENCY_LVL));
    }

    @Test
    @Transactional
    void getNonExistingFavorites() throws Exception {
        // Get the favorites
        restFavoritesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFavorites() throws Exception {
        // Initialize the database
        favoritesRepository.saveAndFlush(favorites);

        int databaseSizeBeforeUpdate = favoritesRepository.findAll().size();

        // Update the favorites
        Favorites updatedFavorites = favoritesRepository.findById(favorites.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFavorites are not directly saved in db
        em.detach(updatedFavorites);
        updatedFavorites.timestamp(UPDATED_TIMESTAMP).proficiencyLvl(UPDATED_PROFICIENCY_LVL);

        restFavoritesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFavorites.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFavorites))
            )
            .andExpect(status().isOk());

        // Validate the Favorites in the database
        List<Favorites> favoritesList = favoritesRepository.findAll();
        assertThat(favoritesList).hasSize(databaseSizeBeforeUpdate);
        Favorites testFavorites = favoritesList.get(favoritesList.size() - 1);
        assertThat(testFavorites.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testFavorites.getProficiencyLvl()).isEqualTo(UPDATED_PROFICIENCY_LVL);
    }

    @Test
    @Transactional
    void putNonExistingFavorites() throws Exception {
        int databaseSizeBeforeUpdate = favoritesRepository.findAll().size();
        favorites.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFavoritesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, favorites.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(favorites))
            )
            .andExpect(status().isBadRequest());

        // Validate the Favorites in the database
        List<Favorites> favoritesList = favoritesRepository.findAll();
        assertThat(favoritesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFavorites() throws Exception {
        int databaseSizeBeforeUpdate = favoritesRepository.findAll().size();
        favorites.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFavoritesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(favorites))
            )
            .andExpect(status().isBadRequest());

        // Validate the Favorites in the database
        List<Favorites> favoritesList = favoritesRepository.findAll();
        assertThat(favoritesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFavorites() throws Exception {
        int databaseSizeBeforeUpdate = favoritesRepository.findAll().size();
        favorites.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFavoritesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(favorites)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Favorites in the database
        List<Favorites> favoritesList = favoritesRepository.findAll();
        assertThat(favoritesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFavoritesWithPatch() throws Exception {
        // Initialize the database
        favoritesRepository.saveAndFlush(favorites);

        int databaseSizeBeforeUpdate = favoritesRepository.findAll().size();

        // Update the favorites using partial update
        Favorites partialUpdatedFavorites = new Favorites();
        partialUpdatedFavorites.setId(favorites.getId());

        partialUpdatedFavorites.timestamp(UPDATED_TIMESTAMP).proficiencyLvl(UPDATED_PROFICIENCY_LVL);

        restFavoritesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFavorites.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFavorites))
            )
            .andExpect(status().isOk());

        // Validate the Favorites in the database
        List<Favorites> favoritesList = favoritesRepository.findAll();
        assertThat(favoritesList).hasSize(databaseSizeBeforeUpdate);
        Favorites testFavorites = favoritesList.get(favoritesList.size() - 1);
        assertThat(testFavorites.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testFavorites.getProficiencyLvl()).isEqualTo(UPDATED_PROFICIENCY_LVL);
    }

    @Test
    @Transactional
    void fullUpdateFavoritesWithPatch() throws Exception {
        // Initialize the database
        favoritesRepository.saveAndFlush(favorites);

        int databaseSizeBeforeUpdate = favoritesRepository.findAll().size();

        // Update the favorites using partial update
        Favorites partialUpdatedFavorites = new Favorites();
        partialUpdatedFavorites.setId(favorites.getId());

        partialUpdatedFavorites.timestamp(UPDATED_TIMESTAMP).proficiencyLvl(UPDATED_PROFICIENCY_LVL);

        restFavoritesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFavorites.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFavorites))
            )
            .andExpect(status().isOk());

        // Validate the Favorites in the database
        List<Favorites> favoritesList = favoritesRepository.findAll();
        assertThat(favoritesList).hasSize(databaseSizeBeforeUpdate);
        Favorites testFavorites = favoritesList.get(favoritesList.size() - 1);
        assertThat(testFavorites.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testFavorites.getProficiencyLvl()).isEqualTo(UPDATED_PROFICIENCY_LVL);
    }

    @Test
    @Transactional
    void patchNonExistingFavorites() throws Exception {
        int databaseSizeBeforeUpdate = favoritesRepository.findAll().size();
        favorites.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFavoritesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, favorites.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(favorites))
            )
            .andExpect(status().isBadRequest());

        // Validate the Favorites in the database
        List<Favorites> favoritesList = favoritesRepository.findAll();
        assertThat(favoritesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFavorites() throws Exception {
        int databaseSizeBeforeUpdate = favoritesRepository.findAll().size();
        favorites.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFavoritesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(favorites))
            )
            .andExpect(status().isBadRequest());

        // Validate the Favorites in the database
        List<Favorites> favoritesList = favoritesRepository.findAll();
        assertThat(favoritesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFavorites() throws Exception {
        int databaseSizeBeforeUpdate = favoritesRepository.findAll().size();
        favorites.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFavoritesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(favorites))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Favorites in the database
        List<Favorites> favoritesList = favoritesRepository.findAll();
        assertThat(favoritesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFavorites() throws Exception {
        // Initialize the database
        favoritesRepository.saveAndFlush(favorites);

        int databaseSizeBeforeDelete = favoritesRepository.findAll().size();

        // Delete the favorites
        restFavoritesMockMvc
            .perform(delete(ENTITY_API_URL_ID, favorites.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Favorites> favoritesList = favoritesRepository.findAll();
        assertThat(favoritesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
