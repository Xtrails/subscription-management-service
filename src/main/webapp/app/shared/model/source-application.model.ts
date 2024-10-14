import { type IPaymentSystem } from '@/shared/model/payment-system.model';

export interface ISourceApplication {
  id?: number;
  applicationName?: string;
  paymentSystems?: IPaymentSystem[] | null;
}

export class SourceApplication implements ISourceApplication {
  constructor(
    public id?: number,
    public applicationName?: string,
    public paymentSystems?: IPaymentSystem[] | null,
  ) {}
}
