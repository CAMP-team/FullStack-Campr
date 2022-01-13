import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale from './locale';
import authentication from './authentication';
import applicationProfile from './application-profile';

import administration from 'app/modules/administration/administration.reducer';
import userManagement from 'app/modules/administration/user-management/user-management.reducer';
import register from 'app/modules/account/register/register.reducer';
import activate from 'app/modules/account/activate/activate.reducer';
import password from 'app/modules/account/password/password.reducer';
import settings from 'app/modules/account/settings/settings.reducer';
import passwordReset from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import appUser from 'app/entities/app-user/app-user.reducer';
// prettier-ignore
import video from 'app/entities/video/video.reducer';
// prettier-ignore
import userComment from 'app/entities/user-comment/user-comment.reducer';
// prettier-ignore
import genre from 'app/entities/genre/genre.reducer';
// prettier-ignore
import userFavorites from 'app/entities/user-favorites/user-favorites.reducer';
// prettier-ignore
import userUpload from 'app/entities/user-upload/user-upload.reducer';
// prettier-ignore
import watchHistory from 'app/entities/watch-history/watch-history.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer = {
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  appUser,
  video,
  userComment,
  genre,
  userFavorites,
  userUpload,
  watchHistory,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
};

export default rootReducer;
