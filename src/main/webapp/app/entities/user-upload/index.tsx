import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UserUpload from './user-upload';
import UserUploadDetail from './user-upload-detail';
import UserUploadUpdate from './user-upload-update';
import UserUploadDeleteDialog from './user-upload-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UserUploadUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UserUploadUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UserUploadDetail} />
      <ErrorBoundaryRoute path={match.url} component={UserUpload} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UserUploadDeleteDialog} />
  </>
);

export default Routes;
