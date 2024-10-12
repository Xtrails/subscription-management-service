import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import SubscriptionTypeService from './subscription-type.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import SourceApplicationService from '@/entities/source-application/source-application.service';
import { type ISourceApplication } from '@/shared/model/source-application.model';
import { type ISubscriptionType, SubscriptionType } from '@/shared/model/subscription-type.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SubscriptionTypeUpdate',
  setup() {
    const subscriptionTypeService = inject('subscriptionTypeService', () => new SubscriptionTypeService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const subscriptionType: Ref<ISubscriptionType> = ref(new SubscriptionType());

    const sourceApplicationService = inject('sourceApplicationService', () => new SourceApplicationService());

    const sourceApplications: Ref<ISourceApplication[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'ru'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveSubscriptionType = async subscriptionTypeId => {
      try {
        const res = await subscriptionTypeService().find(subscriptionTypeId);
        subscriptionType.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.subscriptionTypeId) {
      retrieveSubscriptionType(route.params.subscriptionTypeId);
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
      name: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      description: {},
      price: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      duration: {
        required: validations.required(t$('entity.validation.required').toString()),
        integer: validations.integer(t$('entity.validation.number').toString()),
      },
      sourceApplication: {},
    };
    const v$ = useVuelidate(validationRules, subscriptionType as any);
    v$.value.$validate();

    return {
      subscriptionTypeService,
      alertService,
      subscriptionType,
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
      if (this.subscriptionType.id) {
        this.subscriptionTypeService()
          .update(this.subscriptionType)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('subscriptionManagementServiceApp.subscriptionType.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.subscriptionTypeService()
          .create(this.subscriptionType)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(
              this.t$('subscriptionManagementServiceApp.subscriptionType.created', { param: param.id }).toString(),
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
