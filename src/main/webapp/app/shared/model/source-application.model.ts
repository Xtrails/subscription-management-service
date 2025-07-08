import { type IExternalUser } from '@/shared/model/external-user.model';

export interface ISourceApplication {
  id?: string;
  applicationName?: string;
  user?: IExternalUser | null;
}

export class SourceApplication implements ISourceApplication {
  constructor(
    public id?: string,
    public applicationName?: string,
    public user?: IExternalUser | null,
  ) {}
}
