import { type ISubscriptionDetails } from '@/shared/model/subscription-details.model';

export interface ISubscriptionAccess {
  id?: string;
  name?: string;
  description?: string | null;
  order?: number | null;
  role?: string | null;
  roleGroup?: string | null;
  active?: boolean;
  subscriptionDetails?: ISubscriptionDetails[];
}

export class SubscriptionAccess implements ISubscriptionAccess {
  constructor(
    public id?: string,
    public name?: string,
    public description?: string | null,
    public order?: number | null,
    public role?: string | null,
    public roleGroup?: string | null,
    public active?: boolean,
    public subscriptionDetails?: ISubscriptionDetails[],
  ) {
    this.active = this.active ?? false;
  }
}
