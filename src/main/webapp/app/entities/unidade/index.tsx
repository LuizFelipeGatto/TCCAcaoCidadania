import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Unidade from './unidade';
import UnidadeDetail from './unidade-detail';
import UnidadeUpdate from './unidade-update';
import UnidadeDeleteDialog from './unidade-delete-dialog';

const UnidadeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Unidade />} />
    <Route path="new" element={<UnidadeUpdate />} />
    <Route path=":id">
      <Route index element={<UnidadeDetail />} />
      <Route path="edit" element={<UnidadeUpdate />} />
      <Route path="delete" element={<UnidadeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UnidadeRoutes;
