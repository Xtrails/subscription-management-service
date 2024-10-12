import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import ReferralService from './referral.service';
import { type IReferral } from '@/shared/model/referral.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ReferralDetails',
  setup() {
    const referralService = inject('referralService', () => new ReferralService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const referral: Ref<IReferral> = ref({});

    const retrieveReferral = async referralId => {
      try {
        const res = await referralService().find(referralId);
        referral.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.referralId) {
      retrieveReferral(route.params.referralId);
    }

    return {
      alertService,
      referral,

      previousState,
      t$: useI18n().t,
    };
  },
});
