import { type ISourceApplication } from '@/shared/model/source-application.model';

export interface IExternalUser {
  id?: number;
  externalUserId?: string;
  sourceApplication?: ISourceApplication | null;
}

export class ExternalUser implements IExternalUser {
  constructor(
    public id?: number,
    public externalUserId?: string,
    public sourceApplication?: ISourceApplication | null,
  ) {}
}
