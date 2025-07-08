import { type IReferralProgram } from '@/shared/model/referral-program.model';

export interface IExternalUser {
  id?: string;
  externalUserId?: string;
  referralCreator?: IReferralProgram | null;
  referralProgram?: IReferralProgram | null;
}

export class ExternalUser implements IExternalUser {
  constructor(
    public id?: string,
    public externalUserId?: string,
    public referralCreator?: IReferralProgram | null,
    public referralProgram?: IReferralProgram | null,
  ) {}
}
