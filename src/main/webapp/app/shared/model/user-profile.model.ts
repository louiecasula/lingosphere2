import { IArchive } from 'app/shared/model/archive.model';
import { IFavorites } from 'app/shared/model/favorites.model';

export interface IUserProfile {
  id?: number;
  name?: string | null;
  email?: string | null;
  password?: string | null;
  nativeLanguage?: string | null;
  targetLanguage?: string | null;
  archive?: IArchive | null;
  favorites?: IFavorites | null;
}

export const defaultValue: Readonly<IUserProfile> = {};
