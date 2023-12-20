import { IArchive } from 'app/shared/model/archive.model';
import { IFavorites } from 'app/shared/model/favorites.model';

export interface IWord {
  id?: number;
  language?: string | null;
  wordText?: string | null;
  partOfSpeech?: string | null;
  pronunciation?: string | null;
  audio?: string | null;
  definition?: string | null;
  exampleSentence?: string | null;
  etymology?: string | null;
  archive?: IArchive | null;
  favorites?: IFavorites | null;
}

export const defaultValue: Readonly<IWord> = {};
