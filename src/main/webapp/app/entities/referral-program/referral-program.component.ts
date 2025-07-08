import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import ReferralProgramService from './referral-program.service';
import { type IReferralProgram } from '@/shared/model/referral-program.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ReferralProgram',
  setup() {
    const { t: t$ } = useI18n();
    const referralProgramService = inject('referralProgramService', () => new ReferralProgramService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const referralPrograms: Ref<IReferralProgram[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveReferralPrograms = async () => {
      isFetching.value = true;
      try {
        const res = await referralProgramService().retrieve();
        referralPrograms.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveReferralPrograms();
    };

    onMounted(async () => {
      await retrieveReferralPrograms();
    });

    const removeId: Ref<string> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IReferralProgram) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeReferralProgram = async () => {
      try {
        await referralProgramService().delete(removeId.value);
        const message = t$('subscriptionManagementServiceApp.referralProgram.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveReferralPrograms();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      referralPrograms,
      handleSyncList,
      isFetching,
      retrieveReferralPrograms,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeReferralProgram,
      t$,
    };
  },
});
