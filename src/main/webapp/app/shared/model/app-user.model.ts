import { IUser } from 'app/shared/model/user.model';
import { IUserUpload } from 'app/shared/model/user-upload.model';
import { IWatchHistory } from 'app/shared/model/watch-history.model';
import { IUserFavorites } from 'app/shared/model/user-favorites.model';
import { IUserComment } from 'app/shared/model/user-comment.model';

export interface IAppUser {
  id?: number;
  internalUser?: IUser | null;
  userUpload?: IUserUpload | null;
  watchHistory?: IWatchHistory | null;
  userFavorites?: IUserFavorites | null;
  userComments?: IUserComment[] | null;
}

export const defaultValue: Readonly<IAppUser> = {};
