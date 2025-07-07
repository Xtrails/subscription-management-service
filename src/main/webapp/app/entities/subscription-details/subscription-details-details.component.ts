import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import SubscriptionDetailsService from './subscription-details.service';
import { type ISubscriptionDetails } from '@/shared/model/subscription-details.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SubscriptionDetailsDetails',
  setup() {
    const subscriptionDetailsService = inject('subscriptionDetailsService', () => new SubscriptionDetailsService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const subscriptionDetails: Ref<ISubscriptionDetails> = ref({});

    const retrieveSubscriptionDetails = async subscriptionDetailsId => {
      try {
        const res = await subscriptionDetailsService().find(subscriptionDetailsId);
        subscriptionDetails.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.subscriptionDetailsId) {
      retrieveSubscriptionDetails(route.params.subscriptionDetailsId);
    }

    return {
      alertService,
      subscriptionDetails,

      previousState,
      t$: useI18n().t,
    };
  },
});
