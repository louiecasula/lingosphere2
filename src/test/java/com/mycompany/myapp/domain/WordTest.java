package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ArchiveTestSamples.*;
import static com.mycompany.myapp.domain.FavoritesTestSamples.*;
import static com.mycompany.myapp.domain.WordTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Word.class);
        Word word1 = getWordSample1();
        Word word2 = new Word();
        assertThat(word1).isNotEqualTo(word2);

        word2.setId(word1.getId());
        assertThat(word1).isEqualTo(word2);

        word2 = getWordSample2();
        assertThat(word1).isNotEqualTo(word2);
    }

    @Test
    void archiveTest() throws Exception {
        Word word = getWordRandomSampleGenerator();
        Archive archiveBack = getArchiveRandomSampleGenerator();

        word.setArchive(archiveBack);
        assertThat(word.getArchive()).isEqualTo(archiveBack);

        word.archive(null);
        assertThat(word.getArchive()).isNull();
    }

    @Test
    void favoritesTest() throws Exception {
        Word word = getWordRandomSampleGenerator();
        Favorites favoritesBack = getFavoritesRandomSampleGenerator();

        word.setFavorites(favoritesBack);
        assertThat(word.getFavorites()).isEqualTo(favoritesBack);

        word.favorites(null);
        assertThat(word.getFavorites()).isNull();
    }
}
