import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import SourceApplicationService from './source-application.service';
import { type ISourceApplication } from '@/shared/model/source-application.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SourceApplicationDetails',
  setup() {
    const sourceApplicationService = inject('sourceApplicationService', () => new SourceApplicationService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const sourceApplication: Ref<ISourceApplication> = ref({});

    const retrieveSourceApplication = async sourceApplicationId => {
      try {
        const res = await sourceApplicationService().find(sourceApplicationId);
        sourceApplication.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.sourceApplicationId) {
      retrieveSourceApplication(route.params.sourceApplicationId);
    }

    return {
      alertService,
      sourceApplication,

      previousState,
      t$: useI18n().t,
    };
  },
});
