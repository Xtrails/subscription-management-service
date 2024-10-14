import { type ISourceApplication } from '@/shared/model/source-application.model';

export interface ISubscriptionType {
  id?: number;
  name?: string;
  description?: string | null;
  price?: number;
  duration?: string;
  visible?: boolean;
  sourceApplication?: ISourceApplication | null;
}

export class SubscriptionType implements ISubscriptionType {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public price?: number,
    public duration?: string,
    public visible?: boolean,
    public sourceApplication?: ISourceApplication | null,
  ) {
    this.visible = this.visible ?? false;
  }
}
