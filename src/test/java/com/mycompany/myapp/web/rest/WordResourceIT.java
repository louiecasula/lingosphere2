package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Word;
import com.mycompany.myapp.repository.WordRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link WordResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WordResourceIT {

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    private static final String DEFAULT_WORD_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_WORD_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_PART_OF_SPEECH = "AAAAAAAAAA";
    private static final String UPDATED_PART_OF_SPEECH = "BBBBBBBBBB";

    private static final String DEFAULT_PRONUNCIATION = "AAAAAAAAAA";
    private static final String UPDATED_PRONUNCIATION = "BBBBBBBBBB";

    private static final String DEFAULT_AUDIO = "AAAAAAAAAA";
    private static final String UPDATED_AUDIO = "BBBBBBBBBB";

    private static final String DEFAULT_DEFINITION = "AAAAAAAAAA";
    private static final String UPDATED_DEFINITION = "BBBBBBBBBB";

    private static final String DEFAULT_EXAMPLE_SENTENCE = "AAAAAAAAAA";
    private static final String UPDATED_EXAMPLE_SENTENCE = "BBBBBBBBBB";

    private static final String DEFAULT_ETYMOLOGY = "AAAAAAAAAA";
    private static final String UPDATED_ETYMOLOGY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/words";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WordRepository wordRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWordMockMvc;

    private Word word;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Word createEntity(EntityManager em) {
        Word word = new Word()
            .language(DEFAULT_LANGUAGE)
            .wordText(DEFAULT_WORD_TEXT)
            .partOfSpeech(DEFAULT_PART_OF_SPEECH)
            .pronunciation(DEFAULT_PRONUNCIATION)
            .audio(DEFAULT_AUDIO)
            .definition(DEFAULT_DEFINITION)
            .exampleSentence(DEFAULT_EXAMPLE_SENTENCE)
            .etymology(DEFAULT_ETYMOLOGY);
        return word;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Word createUpdatedEntity(EntityManager em) {
        Word word = new Word()
            .language(UPDATED_LANGUAGE)
            .wordText(UPDATED_WORD_TEXT)
            .partOfSpeech(UPDATED_PART_OF_SPEECH)
            .pronunciation(UPDATED_PRONUNCIATION)
            .audio(UPDATED_AUDIO)
            .definition(UPDATED_DEFINITION)
            .exampleSentence(UPDATED_EXAMPLE_SENTENCE)
            .etymology(UPDATED_ETYMOLOGY);
        return word;
    }

    @BeforeEach
    public void initTest() {
        word = createEntity(em);
    }

    @Test
    @Transactional
    void createWord() throws Exception {
        int databaseSizeBeforeCreate = wordRepository.findAll().size();
        // Create the Word
        restWordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(word)))
            .andExpect(status().isCreated());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeCreate + 1);
        Word testWord = wordList.get(wordList.size() - 1);
        assertThat(testWord.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testWord.getWordText()).isEqualTo(DEFAULT_WORD_TEXT);
        assertThat(testWord.getPartOfSpeech()).isEqualTo(DEFAULT_PART_OF_SPEECH);
        assertThat(testWord.getPronunciation()).isEqualTo(DEFAULT_PRONUNCIATION);
        assertThat(testWord.getAudio()).isEqualTo(DEFAULT_AUDIO);
        assertThat(testWord.getDefinition()).isEqualTo(DEFAULT_DEFINITION);
        assertThat(testWord.getExampleSentence()).isEqualTo(DEFAULT_EXAMPLE_SENTENCE);
        assertThat(testWord.getEtymology()).isEqualTo(DEFAULT_ETYMOLOGY);
    }

    @Test
    @Transactional
    void createWordWithExistingId() throws Exception {
        // Create the Word with an existing ID
        word.setId(1L);

        int databaseSizeBeforeCreate = wordRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWordMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(word)))
            .andExpect(status().isBadRequest());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWords() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get all the wordList
        restWordMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(word.getId().intValue())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].wordText").value(hasItem(DEFAULT_WORD_TEXT)))
            .andExpect(jsonPath("$.[*].partOfSpeech").value(hasItem(DEFAULT_PART_OF_SPEECH)))
            .andExpect(jsonPath("$.[*].pronunciation").value(hasItem(DEFAULT_PRONUNCIATION)))
            .andExpect(jsonPath("$.[*].audio").value(hasItem(DEFAULT_AUDIO)))
            .andExpect(jsonPath("$.[*].definition").value(hasItem(DEFAULT_DEFINITION)))
            .andExpect(jsonPath("$.[*].exampleSentence").value(hasItem(DEFAULT_EXAMPLE_SENTENCE)))
            .andExpect(jsonPath("$.[*].etymology").value(hasItem(DEFAULT_ETYMOLOGY)));
    }

    @Test
    @Transactional
    void getWord() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        // Get the word
        restWordMockMvc
            .perform(get(ENTITY_API_URL_ID, word.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(word.getId().intValue()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE))
            .andExpect(jsonPath("$.wordText").value(DEFAULT_WORD_TEXT))
            .andExpect(jsonPath("$.partOfSpeech").value(DEFAULT_PART_OF_SPEECH))
            .andExpect(jsonPath("$.pronunciation").value(DEFAULT_PRONUNCIATION))
            .andExpect(jsonPath("$.audio").value(DEFAULT_AUDIO))
            .andExpect(jsonPath("$.definition").value(DEFAULT_DEFINITION))
            .andExpect(jsonPath("$.exampleSentence").value(DEFAULT_EXAMPLE_SENTENCE))
            .andExpect(jsonPath("$.etymology").value(DEFAULT_ETYMOLOGY));
    }

    @Test
    @Transactional
    void getNonExistingWord() throws Exception {
        // Get the word
        restWordMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWord() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        int databaseSizeBeforeUpdate = wordRepository.findAll().size();

        // Update the word
        Word updatedWord = wordRepository.findById(word.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWord are not directly saved in db
        em.detach(updatedWord);
        updatedWord
            .language(UPDATED_LANGUAGE)
            .wordText(UPDATED_WORD_TEXT)
            .partOfSpeech(UPDATED_PART_OF_SPEECH)
            .pronunciation(UPDATED_PRONUNCIATION)
            .audio(UPDATED_AUDIO)
            .definition(UPDATED_DEFINITION)
            .exampleSentence(UPDATED_EXAMPLE_SENTENCE)
            .etymology(UPDATED_ETYMOLOGY);

        restWordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWord.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWord))
            )
            .andExpect(status().isOk());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeUpdate);
        Word testWord = wordList.get(wordList.size() - 1);
        assertThat(testWord.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testWord.getWordText()).isEqualTo(UPDATED_WORD_TEXT);
        assertThat(testWord.getPartOfSpeech()).isEqualTo(UPDATED_PART_OF_SPEECH);
        assertThat(testWord.getPronunciation()).isEqualTo(UPDATED_PRONUNCIATION);
        assertThat(testWord.getAudio()).isEqualTo(UPDATED_AUDIO);
        assertThat(testWord.getDefinition()).isEqualTo(UPDATED_DEFINITION);
        assertThat(testWord.getExampleSentence()).isEqualTo(UPDATED_EXAMPLE_SENTENCE);
        assertThat(testWord.getEtymology()).isEqualTo(UPDATED_ETYMOLOGY);
    }

    @Test
    @Transactional
    void putNonExistingWord() throws Exception {
        int databaseSizeBeforeUpdate = wordRepository.findAll().size();
        word.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, word.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(word))
            )
            .andExpect(status().isBadRequest());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWord() throws Exception {
        int databaseSizeBeforeUpdate = wordRepository.findAll().size();
        word.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWordMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(word))
            )
            .andExpect(status().isBadRequest());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWord() throws Exception {
        int databaseSizeBeforeUpdate = wordRepository.findAll().size();
        word.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWordMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(word)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWordWithPatch() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        int databaseSizeBeforeUpdate = wordRepository.findAll().size();

        // Update the word using partial update
        Word partialUpdatedWord = new Word();
        partialUpdatedWord.setId(word.getId());

        partialUpdatedWord
            .language(UPDATED_LANGUAGE)
            .wordText(UPDATED_WORD_TEXT)
            .pronunciation(UPDATED_PRONUNCIATION)
            .definition(UPDATED_DEFINITION);

        restWordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWord))
            )
            .andExpect(status().isOk());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeUpdate);
        Word testWord = wordList.get(wordList.size() - 1);
        assertThat(testWord.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testWord.getWordText()).isEqualTo(UPDATED_WORD_TEXT);
        assertThat(testWord.getPartOfSpeech()).isEqualTo(DEFAULT_PART_OF_SPEECH);
        assertThat(testWord.getPronunciation()).isEqualTo(UPDATED_PRONUNCIATION);
        assertThat(testWord.getAudio()).isEqualTo(DEFAULT_AUDIO);
        assertThat(testWord.getDefinition()).isEqualTo(UPDATED_DEFINITION);
        assertThat(testWord.getExampleSentence()).isEqualTo(DEFAULT_EXAMPLE_SENTENCE);
        assertThat(testWord.getEtymology()).isEqualTo(DEFAULT_ETYMOLOGY);
    }

    @Test
    @Transactional
    void fullUpdateWordWithPatch() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        int databaseSizeBeforeUpdate = wordRepository.findAll().size();

        // Update the word using partial update
        Word partialUpdatedWord = new Word();
        partialUpdatedWord.setId(word.getId());

        partialUpdatedWord
            .language(UPDATED_LANGUAGE)
            .wordText(UPDATED_WORD_TEXT)
            .partOfSpeech(UPDATED_PART_OF_SPEECH)
            .pronunciation(UPDATED_PRONUNCIATION)
            .audio(UPDATED_AUDIO)
            .definition(UPDATED_DEFINITION)
            .exampleSentence(UPDATED_EXAMPLE_SENTENCE)
            .etymology(UPDATED_ETYMOLOGY);

        restWordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWord.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWord))
            )
            .andExpect(status().isOk());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeUpdate);
        Word testWord = wordList.get(wordList.size() - 1);
        assertThat(testWord.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testWord.getWordText()).isEqualTo(UPDATED_WORD_TEXT);
        assertThat(testWord.getPartOfSpeech()).isEqualTo(UPDATED_PART_OF_SPEECH);
        assertThat(testWord.getPronunciation()).isEqualTo(UPDATED_PRONUNCIATION);
        assertThat(testWord.getAudio()).isEqualTo(UPDATED_AUDIO);
        assertThat(testWord.getDefinition()).isEqualTo(UPDATED_DEFINITION);
        assertThat(testWord.getExampleSentence()).isEqualTo(UPDATED_EXAMPLE_SENTENCE);
        assertThat(testWord.getEtymology()).isEqualTo(UPDATED_ETYMOLOGY);
    }

    @Test
    @Transactional
    void patchNonExistingWord() throws Exception {
        int databaseSizeBeforeUpdate = wordRepository.findAll().size();
        word.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, word.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(word))
            )
            .andExpect(status().isBadRequest());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWord() throws Exception {
        int databaseSizeBeforeUpdate = wordRepository.findAll().size();
        word.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWordMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(word))
            )
            .andExpect(status().isBadRequest());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWord() throws Exception {
        int databaseSizeBeforeUpdate = wordRepository.findAll().size();
        word.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWordMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(word)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Word in the database
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWord() throws Exception {
        // Initialize the database
        wordRepository.saveAndFlush(word);

        int databaseSizeBeforeDelete = wordRepository.findAll().size();

        // Delete the word
        restWordMockMvc
            .perform(delete(ENTITY_API_URL_ID, word.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Word> wordList = wordRepository.findAll();
        assertThat(wordList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
