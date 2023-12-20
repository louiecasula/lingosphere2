package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.FavoritesTestSamples.*;
import static com.mycompany.myapp.domain.UserProfileTestSamples.*;
import static com.mycompany.myapp.domain.WordTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FavoritesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Favorites.class);
        Favorites favorites1 = getFavoritesSample1();
        Favorites favorites2 = new Favorites();
        assertThat(favorites1).isNotEqualTo(favorites2);

        favorites2.setId(favorites1.getId());
        assertThat(favorites1).isEqualTo(favorites2);

        favorites2 = getFavoritesSample2();
        assertThat(favorites1).isNotEqualTo(favorites2);
    }

    @Test
    void wordTest() throws Exception {
        Favorites favorites = getFavoritesRandomSampleGenerator();
        Word wordBack = getWordRandomSampleGenerator();

        favorites.addWord(wordBack);
        assertThat(favorites.getWords()).containsOnly(wordBack);
        assertThat(wordBack.getFavorites()).isEqualTo(favorites);

        favorites.removeWord(wordBack);
        assertThat(favorites.getWords()).doesNotContain(wordBack);
        assertThat(wordBack.getFavorites()).isNull();

        favorites.words(new HashSet<>(Set.of(wordBack)));
        assertThat(favorites.getWords()).containsOnly(wordBack);
        assertThat(wordBack.getFavorites()).isEqualTo(favorites);

        favorites.setWords(new HashSet<>());
        assertThat(favorites.getWords()).doesNotContain(wordBack);
        assertThat(wordBack.getFavorites()).isNull();
    }

    @Test
    void userProfileTest() throws Exception {
        Favorites favorites = getFavoritesRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        favorites.setUserProfile(userProfileBack);
        assertThat(favorites.getUserProfile()).isEqualTo(userProfileBack);
        assertThat(userProfileBack.getFavorites()).isEqualTo(favorites);

        favorites.userProfile(null);
        assertThat(favorites.getUserProfile()).isNull();
        assertThat(userProfileBack.getFavorites()).isNull();
    }
}
