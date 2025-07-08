import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import SubscriptionAccessService from './subscription-access.service';
import { type ISubscriptionAccess } from '@/shared/model/subscription-access.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SubscriptionAccessDetails',
  setup() {
    const subscriptionAccessService = inject('subscriptionAccessService', () => new SubscriptionAccessService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const subscriptionAccess: Ref<ISubscriptionAccess> = ref({});

    const retrieveSubscriptionAccess = async subscriptionAccessId => {
      try {
        const res = await subscriptionAccessService().find(subscriptionAccessId);
        subscriptionAccess.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.subscriptionAccessId) {
      retrieveSubscriptionAccess(route.params.subscriptionAccessId);
    }

    return {
      alertService,
      subscriptionAccess,

      previousState,
      t$: useI18n().t,
    };
  },
});
