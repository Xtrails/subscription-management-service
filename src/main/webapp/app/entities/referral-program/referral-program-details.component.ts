import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import ReferralProgramService from './referral-program.service';
import { type IReferralProgram } from '@/shared/model/referral-program.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ReferralProgramDetails',
  setup() {
    const referralProgramService = inject('referralProgramService', () => new ReferralProgramService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const referralProgram: Ref<IReferralProgram> = ref({});

    const retrieveReferralProgram = async referralProgramId => {
      try {
        const res = await referralProgramService().find(referralProgramId);
        referralProgram.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.referralProgramId) {
      retrieveReferralProgram(route.params.referralProgramId);
    }

    return {
      alertService,
      referralProgram,

      previousState,
      t$: useI18n().t,
    };
  },
});
