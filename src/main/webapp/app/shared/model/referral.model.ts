import { type IExternalUser } from '@/shared/model/external-user.model';
import { type IReferralProgram } from '@/shared/model/referral-program.model';

import { type ReferralStatus } from '@/shared/model/enumerations/referral-status.model';
export interface IReferral {
  id?: number;
  referralCode?: string;
  status?: keyof typeof ReferralStatus;
  referrer?: IExternalUser | null;
  referralProgram?: IReferralProgram | null;
}

export class Referral implements IReferral {
  constructor(
    public id?: number,
    public referralCode?: string,
    public status?: keyof typeof ReferralStatus,
    public referrer?: IExternalUser | null,
    public referralProgram?: IReferralProgram | null,
  ) {}
}
