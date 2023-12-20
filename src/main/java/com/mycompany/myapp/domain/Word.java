package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Word.
 */
@Entity
@Table(name = "word")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Word implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "language")
    private String language;

    @Column(name = "word_text")
    private String wordText;

    @Column(name = "part_of_speech")
    private String partOfSpeech;

    @Column(name = "pronunciation")
    private String pronunciation;

    @Column(name = "audio")
    private String audio;

    @Column(name = "definition")
    private String definition;

    @Column(name = "example_sentence")
    private String exampleSentence;

    @Column(name = "etymology")
    private String etymology;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "words", "userProfile" }, allowSetters = true)
    private Archive archive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "words", "userProfile" }, allowSetters = true)
    private Favorites favorites;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Word id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLanguage() {
        return this.language;
    }

    public Word language(String language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getWordText() {
        return this.wordText;
    }

    public Word wordText(String wordText) {
        this.setWordText(wordText);
        return this;
    }

    public void setWordText(String wordText) {
        this.wordText = wordText;
    }

    public String getPartOfSpeech() {
        return this.partOfSpeech;
    }

    public Word partOfSpeech(String partOfSpeech) {
        this.setPartOfSpeech(partOfSpeech);
        return this;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getPronunciation() {
        return this.pronunciation;
    }

    public Word pronunciation(String pronunciation) {
        this.setPronunciation(pronunciation);
        return this;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getAudio() {
        return this.audio;
    }

    public Word audio(String audio) {
        this.setAudio(audio);
        return this;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getDefinition() {
        return this.definition;
    }

    public Word definition(String definition) {
        this.setDefinition(definition);
        return this;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getExampleSentence() {
        return this.exampleSentence;
    }

    public Word exampleSentence(String exampleSentence) {
        this.setExampleSentence(exampleSentence);
        return this;
    }

    public void setExampleSentence(String exampleSentence) {
        this.exampleSentence = exampleSentence;
    }

    public String getEtymology() {
        return this.etymology;
    }

    public Word etymology(String etymology) {
        this.setEtymology(etymology);
        return this;
    }

    public void setEtymology(String etymology) {
        this.etymology = etymology;
    }

    public Archive getArchive() {
        return this.archive;
    }

    public void setArchive(Archive archive) {
        this.archive = archive;
    }

    public Word archive(Archive archive) {
        this.setArchive(archive);
        return this;
    }

    public Favorites getFavorites() {
        return this.favorites;
    }

    public void setFavorites(Favorites favorites) {
        this.favorites = favorites;
    }

    public Word favorites(Favorites favorites) {
        this.setFavorites(favorites);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Word)) {
            return false;
        }
        return getId() != null && getId().equals(((Word) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Word{" +
            "id=" + getId() +
            ", language='" + getLanguage() + "'" +
            ", wordText='" + getWordText() + "'" +
            ", partOfSpeech='" + getPartOfSpeech() + "'" +
            ", pronunciation='" + getPronunciation() + "'" +
            ", audio='" + getAudio() + "'" +
            ", definition='" + getDefinition() + "'" +
            ", exampleSentence='" + getExampleSentence() + "'" +
            ", etymology='" + getEtymology() + "'" +
            "}";
    }
}
