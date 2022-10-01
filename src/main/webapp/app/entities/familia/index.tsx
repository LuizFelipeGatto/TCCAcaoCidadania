import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Familia from './familia';
import FamiliaDetail from './familia-detail';
import FamiliaUpdate from './familia-update';
import FamiliaDeleteDialog from './familia-delete-dialog';

const FamiliaRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Familia />} />
    <Route path="new" element={<FamiliaUpdate />} />
    <Route path=":id">
      <Route index element={<FamiliaDetail />} />
      <Route path="edit" element={<FamiliaUpdate />} />
      <Route path="delete" element={<FamiliaDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FamiliaRoutes;
