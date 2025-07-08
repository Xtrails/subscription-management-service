import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import PaymentSystemService from './payment-system.service';
import { type IPaymentSystem } from '@/shared/model/payment-system.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PaymentSystem',
  setup() {
    const { t: t$ } = useI18n();
    const paymentSystemService = inject('paymentSystemService', () => new PaymentSystemService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const paymentSystems: Ref<IPaymentSystem[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrievePaymentSystems = async () => {
      isFetching.value = true;
      try {
        const res = await paymentSystemService().retrieve();
        paymentSystems.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrievePaymentSystems();
    };

    onMounted(async () => {
      await retrievePaymentSystems();
    });

    const removeId: Ref<string> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IPaymentSystem) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removePaymentSystem = async () => {
      try {
        await paymentSystemService().delete(removeId.value);
        const message = t$('subscriptionManagementServiceApp.paymentSystem.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrievePaymentSystems();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      paymentSystems,
      handleSyncList,
      isFetching,
      retrievePaymentSystems,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removePaymentSystem,
      t$,
    };
  },
});
