import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UserComment from './user-comment';
import UserCommentDetail from './user-comment-detail';
import UserCommentUpdate from './user-comment-update';
import UserCommentDeleteDialog from './user-comment-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UserCommentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UserCommentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UserCommentDetail} />
      <ErrorBoundaryRoute path={match.url} component={UserComment} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UserCommentDeleteDialog} />
  </>
);

export default Routes;
