import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import ExternalUserService from './external-user.service';
import { type IExternalUser } from '@/shared/model/external-user.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ExternalUser',
  setup() {
    const { t: t$ } = useI18n();
    const externalUserService = inject('externalUserService', () => new ExternalUserService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const externalUsers: Ref<IExternalUser[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveExternalUsers = async () => {
      isFetching.value = true;
      try {
        const res = await externalUserService().retrieve();
        externalUsers.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveExternalUsers();
    };

    onMounted(async () => {
      await retrieveExternalUsers();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IExternalUser) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeExternalUser = async () => {
      try {
        await externalUserService().delete(removeId.value);
        const message = t$('subscriptionManagementServiceApp.externalUser.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveExternalUsers();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      externalUsers,
      handleSyncList,
      isFetching,
      retrieveExternalUsers,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeExternalUser,
      t$,
    };
  },
});
