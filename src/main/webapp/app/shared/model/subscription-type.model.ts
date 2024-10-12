import { type ISourceApplication } from '@/shared/model/source-application.model';

export interface ISubscriptionType {
  id?: number;
  name?: string;
  description?: string | null;
  price?: number;
  duration?: number;
  sourceApplication?: ISourceApplication | null;
}

export class SubscriptionType implements ISubscriptionType {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public price?: number,
    public duration?: number,
    public sourceApplication?: ISourceApplication | null,
  ) {}
}
