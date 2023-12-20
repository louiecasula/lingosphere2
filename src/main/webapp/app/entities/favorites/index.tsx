import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Favorites from './favorites';
import FavoritesDetail from './favorites-detail';
import FavoritesUpdate from './favorites-update';
import FavoritesDeleteDialog from './favorites-delete-dialog';

const FavoritesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Favorites />} />
    <Route path="new" element={<FavoritesUpdate />} />
    <Route path=":id">
      <Route index element={<FavoritesDetail />} />
      <Route path="edit" element={<FavoritesUpdate />} />
      <Route path="delete" element={<FavoritesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FavoritesRoutes;
