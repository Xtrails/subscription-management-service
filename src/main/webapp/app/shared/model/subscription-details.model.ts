import { type ISourceApplication } from '@/shared/model/source-application.model';
import { type ISubscriptionAccess } from '@/shared/model/subscription-access.model';

export interface ISubscriptionDetails {
  id?: string;
  name?: string;
  description?: string | null;
  price?: number;
  priceByMonth?: number | null;
  duration?: string;
  active?: boolean;
  sourceApplication?: ISourceApplication | null;
  subscriptionAccesses?: ISubscriptionAccess[] | null;
}

export class SubscriptionDetails implements ISubscriptionDetails {
  constructor(
    public id?: string,
    public name?: string,
    public description?: string | null,
    public price?: number,
    public priceByMonth?: number | null,
    public duration?: string,
    public active?: boolean,
    public sourceApplication?: ISourceApplication | null,
    public subscriptionAccesses?: ISubscriptionAccess[] | null,
  ) {
    this.active = this.active ?? false;
  }
}
