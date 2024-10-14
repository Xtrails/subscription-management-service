import { type IExternalUser } from '@/shared/model/external-user.model';
import { type IReferralProgram } from '@/shared/model/referral-program.model';
import { type ISourceApplication } from '@/shared/model/source-application.model';

import { type ReferralStatus } from '@/shared/model/enumerations/referral-status.model';
export interface IReferral {
  id?: number;
  referralCode?: string;
  status?: keyof typeof ReferralStatus;
  referrer?: IExternalUser | null;
  referralProgram?: IReferralProgram | null;
  sourceApplication?: ISourceApplication | null;
}

export class Referral implements IReferral {
  constructor(
    public id?: number,
    public referralCode?: string,
    public status?: keyof typeof ReferralStatus,
    public referrer?: IExternalUser | null,
    public referralProgram?: IReferralProgram | null,
    public sourceApplication?: ISourceApplication | null,
  ) {}
}
