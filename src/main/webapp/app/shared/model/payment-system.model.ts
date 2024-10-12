export interface IPaymentSystem {
  id?: number;
  name?: string;
}

export class PaymentSystem implements IPaymentSystem {
  constructor(
    public id?: number,
    public name?: string,
  ) {}
}
