import { type ISourceApplication } from '@/shared/model/source-application.model';

export interface IReferralProgram {
  id?: number;
  name?: string;
  description?: string | null;
  rewardAmount?: number;
  sourceApplication?: ISourceApplication | null;
}

export class ReferralProgram implements IReferralProgram {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string | null,
    public rewardAmount?: number,
    public sourceApplication?: ISourceApplication | null,
  ) {}
}
