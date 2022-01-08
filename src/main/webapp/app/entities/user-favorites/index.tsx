import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UserFavorites from './user-favorites';
import UserFavoritesDetail from './user-favorites-detail';
import UserFavoritesUpdate from './user-favorites-update';
import UserFavoritesDeleteDialog from './user-favorites-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UserFavoritesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UserFavoritesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UserFavoritesDetail} />
      <ErrorBoundaryRoute path={match.url} component={UserFavorites} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UserFavoritesDeleteDialog} />
  </>
);

export default Routes;
