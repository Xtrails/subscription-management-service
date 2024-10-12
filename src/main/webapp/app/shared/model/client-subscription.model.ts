import { type IExternalUser } from '@/shared/model/external-user.model';
import { type ISubscriptionType } from '@/shared/model/subscription-type.model';

import { type SubscriptionStatus } from '@/shared/model/enumerations/subscription-status.model';
export interface IClientSubscription {
  id?: number;
  startDate?: Date;
  endDate?: Date;
  status?: keyof typeof SubscriptionStatus;
  user?: IExternalUser | null;
  subscriptionType?: ISubscriptionType | null;
}

export class ClientSubscription implements IClientSubscription {
  constructor(
    public id?: number,
    public startDate?: Date,
    public endDate?: Date,
    public status?: keyof typeof SubscriptionStatus,
    public user?: IExternalUser | null,
    public subscriptionType?: ISubscriptionType | null,
  ) {}
}
