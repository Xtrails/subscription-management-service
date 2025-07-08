import { type IExternalUser } from '@/shared/model/external-user.model';
import { type ISubscriptionDetails } from '@/shared/model/subscription-details.model';

import { type SubscriptionStatus } from '@/shared/model/enumerations/subscription-status.model';
export interface IClientSubscription {
  id?: string;
  startDttm?: Date;
  endDttm?: Date;
  status?: keyof typeof SubscriptionStatus;
  user?: IExternalUser | null;
  subscriptionDetails?: ISubscriptionDetails | null;
}

export class ClientSubscription implements IClientSubscription {
  constructor(
    public id?: string,
    public startDttm?: Date,
    public endDttm?: Date,
    public status?: keyof typeof SubscriptionStatus,
    public user?: IExternalUser | null,
    public subscriptionDetails?: ISubscriptionDetails | null,
  ) {}
}
