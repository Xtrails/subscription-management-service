import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import PaymentSystemService from './payment-system.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import SourceApplicationService from '@/entities/source-application/source-application.service';
import { type ISourceApplication } from '@/shared/model/source-application.model';
import { type IPaymentSystem, PaymentSystem } from '@/shared/model/payment-system.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PaymentSystemUpdate',
  setup() {
    const paymentSystemService = inject('paymentSystemService', () => new PaymentSystemService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const paymentSystem: Ref<IPaymentSystem> = ref(new PaymentSystem());

    const sourceApplicationService = inject('sourceApplicationService', () => new SourceApplicationService());

    const sourceApplications: Ref<ISourceApplication[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'ru'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePaymentSystem = async paymentSystemId => {
      try {
        const res = await paymentSystemService().find(paymentSystemId);
        paymentSystem.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.paymentSystemId) {
      retrievePaymentSystem(route.params.paymentSystemId);
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
      sourceApplications: {},
    };
    const v$ = useVuelidate(validationRules, paymentSystem as any);
    v$.value.$validate();

    return {
      paymentSystemService,
      alertService,
      paymentSystem,
      previousState,
      isSaving,
      currentLanguage,
      sourceApplications,
      v$,
      t$,
    };
  },
  created(): void {
    this.paymentSystem.sourceApplications = [];
  },
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.paymentSystem.id) {
        this.paymentSystemService()
          .update(this.paymentSystem)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('subscriptionManagementServiceApp.paymentSystem.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.paymentSystemService()
          .create(this.paymentSystem)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(
              this.t$('subscriptionManagementServiceApp.paymentSystem.created', { param: param.id }).toString(),
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
