import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ExternalUserService from './external-user.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import SourceApplicationService from '@/entities/source-application/source-application.service';
import { type ISourceApplication } from '@/shared/model/source-application.model';
import { ExternalUser, type IExternalUser } from '@/shared/model/external-user.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ExternalUserUpdate',
  setup() {
    const externalUserService = inject('externalUserService', () => new ExternalUserService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const externalUser: Ref<IExternalUser> = ref(new ExternalUser());

    const sourceApplicationService = inject('sourceApplicationService', () => new SourceApplicationService());

    const sourceApplications: Ref<ISourceApplication[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'ru'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const initRelationships = () => {
      sourceApplicationService()
        .retrieve()
        .then(res => {
          sourceApplications.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      externalUserId: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      sourceApplication: {},
    };
    const v$ = useVuelidate(validationRules, externalUser as any);
    v$.value.$validate();

    return {
      externalUserService,
      alertService,
      externalUser,
      previousState,
      isSaving,
      currentLanguage,
      sourceApplications,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.externalUser.id) {
        this.externalUserService()
          .update(this.externalUser)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('subscriptionManagementServiceApp.externalUser.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.externalUserService()
          .create(this.externalUser)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('subscriptionManagementServiceApp.externalUser.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
