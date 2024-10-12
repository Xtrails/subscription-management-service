import { type Ref, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import ExternalUserService from './external-user.service';
import { type IExternalUser } from '@/shared/model/external-user.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ExternalUserDetails',
  setup() {
    const externalUserService = inject('externalUserService', () => new ExternalUserService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const externalUser: Ref<IExternalUser> = ref({});

    const retrieveExternalUser = async externalUserId => {
      try {
        const res = await externalUserService().find(externalUserId);
        externalUser.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.externalUserId) {
      retrieveExternalUser(route.params.externalUserId);
    }

    return {
      alertService,
      externalUser,

      previousState,
      t$: useI18n().t,
    };
  },
});
