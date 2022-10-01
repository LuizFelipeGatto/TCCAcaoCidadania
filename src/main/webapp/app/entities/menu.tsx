import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/familia">
        <Translate contentKey="global.menu.entities.familia" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/cesta-descricao">
        <Translate contentKey="global.menu.entities.cestaDescricao" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/unidade">
        <Translate contentKey="global.menu.entities.unidade" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/doacao">
        <Translate contentKey="global.menu.entities.doacao" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/pessoa">
        <Translate contentKey="global.menu.entities.pessoa" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
