import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/user-profile">
        <Translate contentKey="global.menu.entities.userProfile" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/archive">
        <Translate contentKey="global.menu.entities.archive" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/favorites">
        <Translate contentKey="global.menu.entities.favorites" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/word">
        <Translate contentKey="global.menu.entities.word" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
