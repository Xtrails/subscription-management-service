export interface IExternalUser {
  id?: number;
  externalUserId?: string;
}

export class ExternalUser implements IExternalUser {
  constructor(
    public id?: number,
    public externalUserId?: string,
  ) {}
}
