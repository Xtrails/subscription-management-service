import { type ISourceApplication } from '@/shared/model/source-application.model';

export interface ISubscriptionDetails {
  id?: number;
  name?: string;
  description?: string | null;
  price?: number;
  duration?: number;
  sourceApplication?: ISourceApplication | null;
}

export class SubscriptionDetails implements ISubscriptionDetails {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public price?: number,
    public duration?: number,
    public sourceApplication?: ISourceApplication | null,
  ) {}
}
