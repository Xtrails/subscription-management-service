import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import SubscriptionDetailsService from './subscription-details.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import SourceApplicationService from '@/entities/source-application/source-application.service';
import { type ISourceApplication } from '@/shared/model/source-application.model';
import SubscriptionAccessService from '@/entities/subscription-access/subscription-access.service';
import { type ISubscriptionAccess } from '@/shared/model/subscription-access.model';
import { type ISubscriptionDetails, SubscriptionDetails } from '@/shared/model/subscription-details.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SubscriptionDetailsUpdate',
  setup() {
    const subscriptionDetailsService = inject('subscriptionDetailsService', () => new SubscriptionDetailsService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const subscriptionDetails: Ref<ISubscriptionDetails> = ref(new SubscriptionDetails());

    const sourceApplicationService = inject('sourceApplicationService', () => new SourceApplicationService());

    const sourceApplications: Ref<ISourceApplication[]> = ref([]);

    const subscriptionAccessService = inject('subscriptionAccessService', () => new SubscriptionAccessService());

    const subscriptionAccesses: Ref<ISubscriptionAccess[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'ru'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveSubscriptionDetails = async subscriptionDetailsId => {
      try {
        const res = await subscriptionDetailsService().find(subscriptionDetailsId);
        subscriptionDetails.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.subscriptionDetailsId) {
      retrieveSubscriptionDetails(route.params.subscriptionDetailsId);
    }

    const initRelationships = () => {
      sourceApplicationService()
        .retrieve()
        .then(res => {
          sourceApplications.value = res.data;
        });
      subscriptionAccessService()
        .retrieve()
        .then(res => {
          subscriptionAccesses.value = res.data;
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
      priceByMonth: {},
      duration: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      active: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      sourceApplication: {},
      subscriptionAccesses: {},
    };
    const v$ = useVuelidate(validationRules, subscriptionDetails as any);
    v$.value.$validate();

    return {
      subscriptionDetailsService,
      alertService,
      subscriptionDetails,
      previousState,
      isSaving,
      currentLanguage,
      sourceApplications,
      subscriptionAccesses,
      v$,
      t$,
    };
  },
  created(): void {
    this.subscriptionDetails.subscriptionAccesses = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.subscriptionDetails.id) {
        this.subscriptionDetailsService()
          .update(this.subscriptionDetails)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('subscriptionManagementServiceApp.subscriptionDetails.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.subscriptionDetailsService()
          .create(this.subscriptionDetails)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(
              this.t$('subscriptionManagementServiceApp.subscriptionDetails.created', { param: param.id }).toString(),
            );
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },

    getSelected(selectedVals, option, pkField = 'id'): any {
      if (selectedVals) {
        return selectedVals.find(value => option[pkField] === value[pkField]) ?? option;
      }
      return option;
    },
  },
});
