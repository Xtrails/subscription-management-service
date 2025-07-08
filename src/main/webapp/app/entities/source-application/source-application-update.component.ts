import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import SourceApplicationService from './source-application.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ExternalUserService from '@/entities/external-user/external-user.service';
import { type IExternalUser } from '@/shared/model/external-user.model';
import { type ISourceApplication, SourceApplication } from '@/shared/model/source-application.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SourceApplicationUpdate',
  setup() {
    const sourceApplicationService = inject('sourceApplicationService', () => new SourceApplicationService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const sourceApplication: Ref<ISourceApplication> = ref(new SourceApplication());

    const externalUserService = inject('externalUserService', () => new ExternalUserService());

    const externalUsers: Ref<IExternalUser[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'ru'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

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

    const initRelationships = () => {
      externalUserService()
        .retrieve()
        .then(res => {
          externalUsers.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      applicationName: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      referralPrograms: {},
      subscriptionDetails: {},
      user: {},
    };
    const v$ = useVuelidate(validationRules, sourceApplication as any);
    v$.value.$validate();

    return {
      sourceApplicationService,
      alertService,
      sourceApplication,
      previousState,
      isSaving,
      currentLanguage,
      externalUsers,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.sourceApplication.id) {
        this.sourceApplicationService()
          .update(this.sourceApplication)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('subscriptionManagementServiceApp.sourceApplication.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.sourceApplicationService()
          .create(this.sourceApplication)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(
              this.t$('subscriptionManagementServiceApp.sourceApplication.created', { param: param.id }).toString(),
            );
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
