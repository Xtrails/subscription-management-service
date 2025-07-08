import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import SubscriptionAccessService from './subscription-access.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import SubscriptionDetailsService from '@/entities/subscription-details/subscription-details.service';
import { type ISubscriptionDetails } from '@/shared/model/subscription-details.model';
import { type ISubscriptionAccess, SubscriptionAccess } from '@/shared/model/subscription-access.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'SubscriptionAccessUpdate',
  setup() {
    const subscriptionAccessService = inject('subscriptionAccessService', () => new SubscriptionAccessService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const subscriptionAccess: Ref<ISubscriptionAccess> = ref(new SubscriptionAccess());

    const subscriptionDetailsService = inject('subscriptionDetailsService', () => new SubscriptionDetailsService());

    const subscriptionDetails: Ref<ISubscriptionDetails[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'ru'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveSubscriptionAccess = async subscriptionAccessId => {
      try {
        const res = await subscriptionAccessService().find(subscriptionAccessId);
        subscriptionAccess.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.subscriptionAccessId) {
      retrieveSubscriptionAccess(route.params.subscriptionAccessId);
    }

    const initRelationships = () => {
      subscriptionDetailsService()
        .retrieve()
        .then(res => {
          subscriptionDetails.value = res.data;
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
      order: {},
      role: {},
      roleGroup: {},
      active: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      subscriptionDetails: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
    };
    const v$ = useVuelidate(validationRules, subscriptionAccess as any);
    v$.value.$validate();

    return {
      subscriptionAccessService,
      alertService,
      subscriptionAccess,
      previousState,
      isSaving,
      currentLanguage,
      subscriptionDetails,
      v$,
      t$,
    };
  },
  created(): void {
    this.subscriptionAccess.subscriptionDetails = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.subscriptionAccess.id) {
        this.subscriptionAccessService()
          .update(this.subscriptionAccess)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('subscriptionManagementServiceApp.subscriptionAccess.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.subscriptionAccessService()
          .create(this.subscriptionAccess)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(
              this.t$('subscriptionManagementServiceApp.subscriptionAccess.created', { param: param.id }).toString(),
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
