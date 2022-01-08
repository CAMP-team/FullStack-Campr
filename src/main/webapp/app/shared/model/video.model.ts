import { IUserUpload } from 'app/shared/model/user-upload.model';
import { IGenre } from 'app/shared/model/genre.model';
import { IUserComment } from 'app/shared/model/user-comment.model';
import { IWatchHistory } from 'app/shared/model/watch-history.model';
import { IUserFavorites } from 'app/shared/model/user-favorites.model';

export interface IVideo {
  id?: number;
  title?: string | null;
  imageUrl?: string | null;
  videoUrl?: string | null;
  trailerId?: string | null;
  description?: string | null;
  userUpload?: IUserUpload | null;
  genres?: IGenre[] | null;
  userComments?: IUserComment[] | null;
  watchhistories?: IWatchHistory[] | null;
  userfavorites?: IUserFavorites[] | null;
}

export const defaultValue: Readonly<IVideo> = {};
