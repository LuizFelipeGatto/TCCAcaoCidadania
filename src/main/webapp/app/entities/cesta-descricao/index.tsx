import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CestaDescricao from './cesta-descricao';
import CestaDescricaoDetail from './cesta-descricao-detail';
import CestaDescricaoUpdate from './cesta-descricao-update';
import CestaDescricaoDeleteDialog from './cesta-descricao-delete-dialog';

const CestaDescricaoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CestaDescricao />} />
    <Route path="new" element={<CestaDescricaoUpdate />} />
    <Route path=":id">
      <Route index element={<CestaDescricaoDetail />} />
      <Route path="edit" element={<CestaDescricaoUpdate />} />
      <Route path="delete" element={<CestaDescricaoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CestaDescricaoRoutes;
