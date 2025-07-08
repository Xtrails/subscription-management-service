export interface IPaymentSystem {
  id?: string;
  name?: string;
}

export class PaymentSystem implements IPaymentSystem {
  constructor(
    public id?: string,
    public name?: string,
  ) {}
}
