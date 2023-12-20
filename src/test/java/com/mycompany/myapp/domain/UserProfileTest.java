package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ArchiveTestSamples.*;
import static com.mycompany.myapp.domain.FavoritesTestSamples.*;
import static com.mycompany.myapp.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfile.class);
        UserProfile userProfile1 = getUserProfileSample1();
        UserProfile userProfile2 = new UserProfile();
        assertThat(userProfile1).isNotEqualTo(userProfile2);

        userProfile2.setId(userProfile1.getId());
        assertThat(userProfile1).isEqualTo(userProfile2);

        userProfile2 = getUserProfileSample2();
        assertThat(userProfile1).isNotEqualTo(userProfile2);
    }

    @Test
    void archiveTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        Archive archiveBack = getArchiveRandomSampleGenerator();

        userProfile.setArchive(archiveBack);
        assertThat(userProfile.getArchive()).isEqualTo(archiveBack);

        userProfile.archive(null);
        assertThat(userProfile.getArchive()).isNull();
    }

    @Test
    void favoritesTest() throws Exception {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        Favorites favoritesBack = getFavoritesRandomSampleGenerator();

        userProfile.setFavorites(favoritesBack);
        assertThat(userProfile.getFavorites()).isEqualTo(favoritesBack);

        userProfile.favorites(null);
        assertThat(userProfile.getFavorites()).isNull();
    }
}
