import { type IExternalUser } from '@/shared/model/external-user.model';
import { type ISourceApplication } from '@/shared/model/source-application.model';

import { type ReferralStatus } from '@/shared/model/enumerations/referral-status.model';
export interface IReferralProgram {
  id?: string;
  name?: string;
  referralCode?: string;
  description?: string | null;
  startDttm?: Date;
  endDttm?: Date;
  rewardAmount?: number;
  status?: keyof typeof ReferralStatus;
  referralCreator?: IExternalUser | null;
  sourceApplication?: ISourceApplication | null;
}

export class ReferralProgram implements IReferralProgram {
  constructor(
    public id?: string,
    public name?: string,
    public referralCode?: string,
    public description?: string | null,
    public startDttm?: Date,
    public endDttm?: Date,
    public rewardAmount?: number,
    public status?: keyof typeof ReferralStatus,
    public referralCreator?: IExternalUser | null,
    public sourceApplication?: ISourceApplication | null,
  ) {}
}
