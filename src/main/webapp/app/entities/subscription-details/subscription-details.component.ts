import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import SubscriptionDetailsService from './subscription-details.service';
import { type ISubscriptionDetails } from '@/shared/model/subscription-details.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SubscriptionDetails',
  setup() {
    const { t: t$ } = useI18n();
    const subscriptionDetailsService = inject('subscriptionDetailsService', () => new SubscriptionDetailsService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const subscriptionDetails: Ref<ISubscriptionDetails[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveSubscriptionDetailss = async () => {
      isFetching.value = true;
      try {
        const res = await subscriptionDetailsService().retrieve();
        subscriptionDetails.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveSubscriptionDetailss();
    };

    onMounted(async () => {
      await retrieveSubscriptionDetailss();
    });

    const removeId: Ref<string> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: ISubscriptionDetails) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeSubscriptionDetails = async () => {
      try {
        await subscriptionDetailsService().delete(removeId.value);
        const message = t$('subscriptionManagementServiceApp.subscriptionDetails.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveSubscriptionDetailss();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      subscriptionDetails,
      handleSyncList,
      isFetching,
      retrieveSubscriptionDetailss,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeSubscriptionDetails,
      t$,
    };
  },
});
