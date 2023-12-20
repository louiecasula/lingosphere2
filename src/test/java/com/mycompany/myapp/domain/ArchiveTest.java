package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ArchiveTestSamples.*;
import static com.mycompany.myapp.domain.UserProfileTestSamples.*;
import static com.mycompany.myapp.domain.WordTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ArchiveTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Archive.class);
        Archive archive1 = getArchiveSample1();
        Archive archive2 = new Archive();
        assertThat(archive1).isNotEqualTo(archive2);

        archive2.setId(archive1.getId());
        assertThat(archive1).isEqualTo(archive2);

        archive2 = getArchiveSample2();
        assertThat(archive1).isNotEqualTo(archive2);
    }

    @Test
    void wordTest() throws Exception {
        Archive archive = getArchiveRandomSampleGenerator();
        Word wordBack = getWordRandomSampleGenerator();

        archive.addWord(wordBack);
        assertThat(archive.getWords()).containsOnly(wordBack);
        assertThat(wordBack.getArchive()).isEqualTo(archive);

        archive.removeWord(wordBack);
        assertThat(archive.getWords()).doesNotContain(wordBack);
        assertThat(wordBack.getArchive()).isNull();

        archive.words(new HashSet<>(Set.of(wordBack)));
        assertThat(archive.getWords()).containsOnly(wordBack);
        assertThat(wordBack.getArchive()).isEqualTo(archive);

        archive.setWords(new HashSet<>());
        assertThat(archive.getWords()).doesNotContain(wordBack);
        assertThat(wordBack.getArchive()).isNull();
    }

    @Test
    void userProfileTest() throws Exception {
        Archive archive = getArchiveRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        archive.setUserProfile(userProfileBack);
        assertThat(archive.getUserProfile()).isEqualTo(userProfileBack);
        assertThat(userProfileBack.getArchive()).isEqualTo(archive);

        archive.userProfile(null);
        assertThat(archive.getUserProfile()).isNull();
        assertThat(userProfileBack.getArchive()).isNull();
    }
}
