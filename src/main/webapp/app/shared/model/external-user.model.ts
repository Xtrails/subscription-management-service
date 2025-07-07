import { type IReferralProgram } from '@/shared/model/referral-program.model';

export interface IExternalUser {
  id?: number;
  externalUserId?: string;
  referralCreator?: IReferralProgram | null;
  referralProgram?: IReferralProgram | null;
}

export class ExternalUser implements IExternalUser {
  constructor(
    public id?: number,
    public externalUserId?: string,
    public referralCreator?: IReferralProgram | null,
    public referralProgram?: IReferralProgram | null,
  ) {}
}
