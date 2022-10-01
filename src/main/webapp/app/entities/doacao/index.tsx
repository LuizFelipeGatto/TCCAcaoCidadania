import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Doacao from './doacao';
import DoacaoDetail from './doacao-detail';
import DoacaoUpdate from './doacao-update';
import DoacaoDeleteDialog from './doacao-delete-dialog';

const DoacaoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Doacao />} />
    <Route path="new" element={<DoacaoUpdate />} />
    <Route path=":id">
      <Route index element={<DoacaoDetail />} />
      <Route path="edit" element={<DoacaoUpdate />} />
      <Route path="delete" element={<DoacaoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default DoacaoRoutes;
