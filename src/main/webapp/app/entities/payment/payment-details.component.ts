import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import PaymentService from './payment.service';
import { type IPayment } from '@/shared/model/payment.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PaymentDetails',
  setup() {
    const paymentService = inject('paymentService', () => new PaymentService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const payment: Ref<IPayment> = ref({});

    const retrievePayment = async paymentId => {
      try {
        const res = await paymentService().find(paymentId);
        payment.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.paymentId) {
      retrievePayment(route.params.paymentId);
    }

    return {
      alertService,
      payment,

      previousState,
      t$: useI18n().t,
    };
  },
});
