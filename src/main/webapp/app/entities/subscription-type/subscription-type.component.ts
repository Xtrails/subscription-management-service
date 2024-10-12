import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import SubscriptionTypeService from './subscription-type.service';
import { type ISubscriptionType } from '@/shared/model/subscription-type.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SubscriptionType',
  setup() {
    const { t: t$ } = useI18n();
    const subscriptionTypeService = inject('subscriptionTypeService', () => new SubscriptionTypeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const subscriptionTypes: Ref<ISubscriptionType[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveSubscriptionTypes = async () => {
      isFetching.value = true;
      try {
        const res = await subscriptionTypeService().retrieve();
        subscriptionTypes.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveSubscriptionTypes();
    };

    onMounted(async () => {
      await retrieveSubscriptionTypes();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: ISubscriptionType) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeSubscriptionType = async () => {
      try {
        await subscriptionTypeService().delete(removeId.value);
        const message = t$('subscriptionManagementServiceApp.subscriptionType.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveSubscriptionTypes();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      subscriptionTypes,
      handleSyncList,
      isFetching,
      retrieveSubscriptionTypes,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeSubscriptionType,
      t$,
    };
  },
});
