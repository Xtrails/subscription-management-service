import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import PaymentSystemService from './payment-system.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IPaymentSystem, PaymentSystem } from '@/shared/model/payment-system.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PaymentSystemUpdate',
  setup() {
    const paymentSystemService = inject('paymentSystemService', () => new PaymentSystemService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const paymentSystem: Ref<IPaymentSystem> = ref(new PaymentSystem());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

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

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      name: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
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
      v$,
      t$,
    };
  },
  created(): void {},
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
  },
});
