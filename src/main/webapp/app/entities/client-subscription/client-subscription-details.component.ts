import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import ClientSubscriptionService from './client-subscription.service';
import { type IClientSubscription } from '@/shared/model/client-subscription.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ClientSubscriptionDetails',
  setup() {
    const clientSubscriptionService = inject('clientSubscriptionService', () => new ClientSubscriptionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const clientSubscription: Ref<IClientSubscription> = ref({});

    const retrieveClientSubscription = async clientSubscriptionId => {
      try {
        const res = await clientSubscriptionService().find(clientSubscriptionId);
        clientSubscription.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.clientSubscriptionId) {
      retrieveClientSubscription(route.params.clientSubscriptionId);
    }

    return {
      alertService,
      clientSubscription,

      previousState,
      t$: useI18n().t,
    };
  },
});
