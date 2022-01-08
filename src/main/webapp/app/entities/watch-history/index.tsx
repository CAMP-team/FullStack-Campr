import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import WatchHistory from './watch-history';
import WatchHistoryDetail from './watch-history-detail';
import WatchHistoryUpdate from './watch-history-update';
import WatchHistoryDeleteDialog from './watch-history-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={WatchHistoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={WatchHistoryUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={WatchHistoryDetail} />
      <ErrorBoundaryRoute path={match.url} component={WatchHistory} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={WatchHistoryDeleteDialog} />
  </>
);

export default Routes;
