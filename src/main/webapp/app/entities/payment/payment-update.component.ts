import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import PaymentService from './payment.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ExternalUserService from '@/entities/external-user/external-user.service';
import { type IExternalUser } from '@/shared/model/external-user.model';
import ClientSubscriptionService from '@/entities/client-subscription/client-subscription.service';
import { type IClientSubscription } from '@/shared/model/client-subscription.model';
import PaymentSystemService from '@/entities/payment-system/payment-system.service';
import { type IPaymentSystem } from '@/shared/model/payment-system.model';
import { type IPayment, Payment } from '@/shared/model/payment.model';
import { PaymentStatus } from '@/shared/model/enumerations/payment-status.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'PaymentUpdate',
  setup() {
    const paymentService = inject('paymentService', () => new PaymentService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const payment: Ref<IPayment> = ref(new Payment());

    const externalUserService = inject('externalUserService', () => new ExternalUserService());

    const externalUsers: Ref<IExternalUser[]> = ref([]);

    const clientSubscriptionService = inject('clientSubscriptionService', () => new ClientSubscriptionService());

    const clientSubscriptions: Ref<IClientSubscription[]> = ref([]);

    const paymentSystemService = inject('paymentSystemService', () => new PaymentSystemService());

    const paymentSystems: Ref<IPaymentSystem[]> = ref([]);
    const paymentStatusValues: Ref<string[]> = ref(Object.keys(PaymentStatus));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'ru'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrievePayment = async paymentId => {
      try {
        const res = await paymentService().find(paymentId);
        payment.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.paymentId) {
      retrievePayment(route.params.paymentId);
    }

    const initRelationships = () => {
      externalUserService()
        .retrieve()
        .then(res => {
          externalUsers.value = res.data;
        });
      clientSubscriptionService()
        .retrieve()
        .then(res => {
          clientSubscriptions.value = res.data;
        });
      paymentSystemService()
        .retrieve()
        .then(res => {
          paymentSystems.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      amount: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      status: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      paymentDttm: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      hashSum: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      user: {},
      clientSubscription: {},
      paymentSystem: {},
    };
    const v$ = useVuelidate(validationRules, payment as any);
    v$.value.$validate();

    return {
      paymentService,
      alertService,
      payment,
      previousState,
      paymentStatusValues,
      isSaving,
      currentLanguage,
      externalUsers,
      clientSubscriptions,
      paymentSystems,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.payment.id) {
        this.paymentService()
          .update(this.payment)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('subscriptionManagementServiceApp.payment.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.paymentService()
          .create(this.payment)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('subscriptionManagementServiceApp.payment.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
