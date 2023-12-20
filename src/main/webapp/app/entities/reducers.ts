import userProfile from 'app/entities/user-profile/user-profile.reducer';
import archive from 'app/entities/archive/archive.reducer';
import favorites from 'app/entities/favorites/favorites.reducer';
import word from 'app/entities/word/word.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  userProfile,
  archive,
  favorites,
  word,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
