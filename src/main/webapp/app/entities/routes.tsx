import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Familia from './familia';
import CestaDescricao from './cesta-descricao';
import Unidade from './unidade';
import Doacao from './doacao';
import Pessoa from './pessoa';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="familia/*" element={<Familia />} />
        <Route path="cesta-descricao/*" element={<CestaDescricao />} />
        <Route path="unidade/*" element={<Unidade />} />
        <Route path="doacao/*" element={<Doacao />} />
        <Route path="pessoa/*" element={<Pessoa />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
