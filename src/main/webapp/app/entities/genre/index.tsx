import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Genre from './genre';
import GenreDetail from './genre-detail';
import GenreUpdate from './genre-update';
import GenreDeleteDialog from './genre-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GenreUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GenreUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GenreDetail} />
      <ErrorBoundaryRoute path={match.url} component={Genre} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GenreDeleteDialog} />
  </>
);

export default Routes;
