import dayjs from 'dayjs';
import { IAppUser } from 'app/shared/model/app-user.model';
import { IVideo } from 'app/shared/model/video.model';

export interface IWatchHistory {
  id?: number;
  dateWatched?: string | null;
  appUser?: IAppUser | null;
  videos?: IVideo[] | null;
}

export const defaultValue: Readonly<IWatchHistory> = {};
