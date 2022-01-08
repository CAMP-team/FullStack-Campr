import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/app-user">
      <Translate contentKey="global.menu.entities.appUser" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/video">
      <Translate contentKey="global.menu.entities.video" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/user-comment">
      <Translate contentKey="global.menu.entities.userComment" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/genre">
      <Translate contentKey="global.menu.entities.genre" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/user-favorites">
      <Translate contentKey="global.menu.entities.userFavorites" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/user-upload">
      <Translate contentKey="global.menu.entities.userUpload" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/watch-history">
      <Translate contentKey="global.menu.entities.watchHistory" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
