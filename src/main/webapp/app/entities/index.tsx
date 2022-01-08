import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AppUser from './app-user';
import Video from './video';
import UserComment from './user-comment';
import Genre from './genre';
import UserFavorites from './user-favorites';
import UserUpload from './user-upload';
import WatchHistory from './watch-history';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}app-user`} component={AppUser} />
      <ErrorBoundaryRoute path={`${match.url}video`} component={Video} />
      <ErrorBoundaryRoute path={`${match.url}user-comment`} component={UserComment} />
      <ErrorBoundaryRoute path={`${match.url}genre`} component={Genre} />
      <ErrorBoundaryRoute path={`${match.url}user-favorites`} component={UserFavorites} />
      <ErrorBoundaryRoute path={`${match.url}user-upload`} component={UserUpload} />
      <ErrorBoundaryRoute path={`${match.url}watch-history`} component={WatchHistory} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
