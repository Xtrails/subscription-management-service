import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import SubscriptionTypeService from './subscription-type.service';
import { type ISubscriptionType } from '@/shared/model/subscription-type.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SubscriptionTypeDetails',
  setup() {
    const subscriptionTypeService = inject('subscriptionTypeService', () => new SubscriptionTypeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const subscriptionType: Ref<ISubscriptionType> = ref({});

    const retrieveSubscriptionType = async subscriptionTypeId => {
      try {
        const res = await subscriptionTypeService().find(subscriptionTypeId);
        subscriptionType.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.subscriptionTypeId) {
      retrieveSubscriptionType(route.params.subscriptionTypeId);
    }

    return {
      alertService,
      subscriptionType,

      previousState,
      t$: useI18n().t,
    };
  },
});
