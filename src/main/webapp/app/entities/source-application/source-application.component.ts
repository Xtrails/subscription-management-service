import { type Ref, defineComponent, inject, onMounted, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import SourceApplicationService from './source-application.service';
import { type ISourceApplication } from '@/shared/model/source-application.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SourceApplication',
  setup() {
    const { t: t$ } = useI18n();
    const sourceApplicationService = inject('sourceApplicationService', () => new SourceApplicationService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const sourceApplications: Ref<ISourceApplication[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveSourceApplications = async () => {
      isFetching.value = true;
      try {
        const res = await sourceApplicationService().retrieve();
        sourceApplications.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveSourceApplications();
    };

    onMounted(async () => {
      await retrieveSourceApplications();
    });

    const removeId: Ref<string> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: ISourceApplication) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeSourceApplication = async () => {
      try {
        await sourceApplicationService().delete(removeId.value);
        const message = t$('subscriptionManagementServiceApp.sourceApplication.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveSourceApplications();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      sourceApplications,
      handleSyncList,
      isFetching,
      retrieveSourceApplications,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeSourceApplication,
      t$,
    };
  },
});
