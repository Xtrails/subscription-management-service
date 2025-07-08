import { type IExternalUser } from '@/shared/model/external-user.model';
import { type IClientSubscription } from '@/shared/model/client-subscription.model';
import { type IPaymentSystem } from '@/shared/model/payment-system.model';

import { type PaymentStatus } from '@/shared/model/enumerations/payment-status.model';
export interface IPayment {
  id?: string;
  amount?: number;
  status?: keyof typeof PaymentStatus;
  paymentDttm?: Date;
  hashSum?: string;
  user?: IExternalUser | null;
  clientSubscription?: IClientSubscription | null;
  paymentSystem?: IPaymentSystem | null;
}

export class Payment implements IPayment {
  constructor(
    public id?: string,
    public amount?: number,
    public status?: keyof typeof PaymentStatus,
    public paymentDttm?: Date,
    public hashSum?: string,
    public user?: IExternalUser | null,
    public clientSubscription?: IClientSubscription | null,
    public paymentSystem?: IPaymentSystem | null,
  ) {}
}
