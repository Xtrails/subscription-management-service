import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import PaymentSystemService from './payment-system.service';
import { type IPaymentSystem } from '@/shared/model/payment-system.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PaymentSystemDetails',
  setup() {
    const paymentSystemService = inject('paymentSystemService', () => new PaymentSystemService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const paymentSystem: Ref<IPaymentSystem> = ref({});

    const retrievePaymentSystem = async paymentSystemId => {
      try {
        const res = await paymentSystemService().find(paymentSystemId);
        paymentSystem.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.paymentSystemId) {
      retrievePaymentSystem(route.params.paymentSystemId);
    }

    return {
      alertService,
      paymentSystem,

      previousState,
      t$: useI18n().t,
    };
  },
});
