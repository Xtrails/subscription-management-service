import { type IExternalUser } from '@/shared/model/external-user.model';
import { type IClientSubscription } from '@/shared/model/client-subscription.model';
import { type IPaymentSystem } from '@/shared/model/payment-system.model';

import { type PaymentStatus } from '@/shared/model/enumerations/payment-status.model';
export interface IPayment {
  id?: number;
  amount?: number;
  status?: keyof typeof PaymentStatus;
  paymentDate?: Date;
  user?: IExternalUser | null;
  clietntSubscription?: IClientSubscription | null;
  paymentSystem?: IPaymentSystem | null;
}

export class Payment implements IPayment {
  constructor(
    public id?: number,
    public amount?: number,
    public status?: keyof typeof PaymentStatus,
    public paymentDate?: Date,
    public user?: IExternalUser | null,
    public clietntSubscription?: IClientSubscription | null,
    public paymentSystem?: IPaymentSystem | null,
  ) {}
}
