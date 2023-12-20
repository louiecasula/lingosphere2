package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Favorites.
 */
@Entity
@Table(name = "favorites")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Favorites implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "timestamp")
    private ZonedDateTime timestamp;

    @Column(name = "proficiency_lvl")
    private Integer proficiencyLvl;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "favorites")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "archive", "favorites" }, allowSetters = true)
    private Set<Word> words = new HashSet<>();

    @JsonIgnoreProperties(value = { "archive", "favorites" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "favorites")
    private UserProfile userProfile;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Favorites id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getTimestamp() {
        return this.timestamp;
    }

    public Favorites timestamp(ZonedDateTime timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getProficiencyLvl() {
        return this.proficiencyLvl;
    }

    public Favorites proficiencyLvl(Integer proficiencyLvl) {
        this.setProficiencyLvl(proficiencyLvl);
        return this;
    }

    public void setProficiencyLvl(Integer proficiencyLvl) {
        this.proficiencyLvl = proficiencyLvl;
    }

    public Set<Word> getWords() {
        return this.words;
    }

    public void setWords(Set<Word> words) {
        if (this.words != null) {
            this.words.forEach(i -> i.setFavorites(null));
        }
        if (words != null) {
            words.forEach(i -> i.setFavorites(this));
        }
        this.words = words;
    }

    public Favorites words(Set<Word> words) {
        this.setWords(words);
        return this;
    }

    public Favorites addWord(Word word) {
        this.words.add(word);
        word.setFavorites(this);
        return this;
    }

    public Favorites removeWord(Word word) {
        this.words.remove(word);
        word.setFavorites(null);
        return this;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        if (this.userProfile != null) {
            this.userProfile.setFavorites(null);
        }
        if (userProfile != null) {
            userProfile.setFavorites(this);
        }
        this.userProfile = userProfile;
    }

    public Favorites userProfile(UserProfile userProfile) {
        this.setUserProfile(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Favorites)) {
            return false;
        }
        return getId() != null && getId().equals(((Favorites) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Favorites{" +
            "id=" + getId() +
            ", timestamp='" + getTimestamp() + "'" +
            ", proficiencyLvl=" + getProficiencyLvl() +
            "}";
    }
}
