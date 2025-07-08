import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import SubscriptionAccessService from './subscription-access.service';
import { type ISubscriptionAccess } from '@/shared/model/subscription-access.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SubscriptionAccess',
  setup() {
    const { t: t$ } = useI18n();
    const subscriptionAccessService = inject('subscriptionAccessService', () => new SubscriptionAccessService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const subscriptionAccesses: Ref<ISubscriptionAccess[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveSubscriptionAccesss = async () => {
      isFetching.value = true;
      try {
        const res = await subscriptionAccessService().retrieve();
        subscriptionAccesses.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveSubscriptionAccesss();
    };

    onMounted(async () => {
      await retrieveSubscriptionAccesss();
    });

    const removeId: Ref<string> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: ISubscriptionAccess) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeSubscriptionAccess = async () => {
      try {
        await subscriptionAccessService().delete(removeId.value);
        const message = t$('subscriptionManagementServiceApp.subscriptionAccess.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveSubscriptionAccesss();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      subscriptionAccesses,
      handleSyncList,
      isFetching,
      retrieveSubscriptionAccesss,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeSubscriptionAccess,
      t$,
    };
  },
});
