import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Video from './video';
import VideoDetail from './video-detail';
import VideoUpdate from './video-update';
import VideoDeleteDialog from './video-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={VideoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={VideoUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={VideoDetail} />
      <ErrorBoundaryRoute path={match.url} component={Video} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={VideoDeleteDialog} />
  </>
);

export default Routes;
