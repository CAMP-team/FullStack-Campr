import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IVideo } from 'app/shared/model/video.model';

export interface IUserFavorites {
  id?: number;
  dateAdded?: string | null;
  user?: IUser | null;
  videos?: IVideo[] | null;
}

export const defaultValue: Readonly<IUserFavorites> = {};
