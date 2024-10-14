import { type ISourceApplication } from '@/shared/model/source-application.model';

export interface IPaymentSystem {
  id?: number;
  name?: string;
  sourceApplications?: ISourceApplication[] | null;
}

export class PaymentSystem implements IPaymentSystem {
  constructor(
    public id?: number,
    public name?: string,
    public sourceApplications?: ISourceApplication[] | null,
  ) {}
}
