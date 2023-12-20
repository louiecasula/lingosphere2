import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Archive from './archive';
import ArchiveDetail from './archive-detail';
import ArchiveUpdate from './archive-update';
import ArchiveDeleteDialog from './archive-delete-dialog';

const ArchiveRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Archive />} />
    <Route path="new" element={<ArchiveUpdate />} />
    <Route path=":id">
      <Route index element={<ArchiveDetail />} />
      <Route path="edit" element={<ArchiveUpdate />} />
      <Route path="delete" element={<ArchiveDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ArchiveRoutes;
