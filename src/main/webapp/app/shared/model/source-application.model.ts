import { type IExternalUser } from '@/shared/model/external-user.model';

export interface ISourceApplication {
  id?: number;
  applicationName?: string;
  user?: IExternalUser | null;
}

export class SourceApplication implements ISourceApplication {
  constructor(
    public id?: number,
    public applicationName?: string,
    public user?: IExternalUser | null,
  ) {}
}
