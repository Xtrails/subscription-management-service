export interface IExternalUser {
  id?: string;
  externalUserId?: string;
}

export class ExternalUser implements IExternalUser {
  constructor(
    public id?: string,
    public externalUserId?: string,
  ) {}
}
