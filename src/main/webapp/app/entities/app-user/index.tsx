import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AppUser from './app-user';
import AppUserDetail from './app-user-detail';
import AppUserUpdate from './app-user-update';
import AppUserDeleteDialog from './app-user-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AppUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AppUserUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AppUserDetail} />
      <ErrorBoundaryRoute path={match.url} component={AppUser} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AppUserDeleteDialog} />
  </>
);

export default Routes;
