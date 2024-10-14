import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ClientSubscriptionService from './client-subscription.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ExternalUserService from '@/entities/external-user/external-user.service';
import { type IExternalUser } from '@/shared/model/external-user.model';
import SubscriptionTypeService from '@/entities/subscription-type/subscription-type.service';
import { type ISubscriptionType } from '@/shared/model/subscription-type.model';
import SourceApplicationService from '@/entities/source-application/source-application.service';
import { type ISourceApplication } from '@/shared/model/source-application.model';
import { ClientSubscription, type IClientSubscription } from '@/shared/model/client-subscription.model';
import { SubscriptionStatus } from '@/shared/model/enumerations/subscription-status.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ClientSubscriptionUpdate',
  setup() {
    const clientSubscriptionService = inject('clientSubscriptionService', () => new ClientSubscriptionService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const clientSubscription: Ref<IClientSubscription> = ref(new ClientSubscription());

    const externalUserService = inject('externalUserService', () => new ExternalUserService());

    const externalUsers: Ref<IExternalUser[]> = ref([]);

    const subscriptionTypeService = inject('subscriptionTypeService', () => new SubscriptionTypeService());

    const subscriptionTypes: Ref<ISubscriptionType[]> = ref([]);

    const sourceApplicationService = inject('sourceApplicationService', () => new SourceApplicationService());

    const sourceApplications: Ref<ISourceApplication[]> = ref([]);
    const subscriptionStatusValues: Ref<string[]> = ref(Object.keys(SubscriptionStatus));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'ru'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveClientSubscription = async clientSubscriptionId => {
      try {
        const res = await clientSubscriptionService().find(clientSubscriptionId);
        clientSubscription.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.clientSubscriptionId) {
      retrieveClientSubscription(route.params.clientSubscriptionId);
    }

    const initRelationships = () => {
      externalUserService()
        .retrieve()
        .then(res => {
          externalUsers.value = res.data;
        });
      subscriptionTypeService()
        .retrieve()
        .then(res => {
          subscriptionTypes.value = res.data;
        });
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
      startDate: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      endDate: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      status: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      user: {},
      subscriptionType: {},
      sourceApplication: {},
    };
    const v$ = useVuelidate(validationRules, clientSubscription as any);
    v$.value.$validate();

    return {
      clientSubscriptionService,
      alertService,
      clientSubscription,
      previousState,
      subscriptionStatusValues,
      isSaving,
      currentLanguage,
      externalUsers,
      subscriptionTypes,
      sourceApplications,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.clientSubscription.id) {
        this.clientSubscriptionService()
          .update(this.clientSubscription)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('subscriptionManagementServiceApp.clientSubscription.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.clientSubscriptionService()
          .create(this.clientSubscription)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(
              this.t$('subscriptionManagementServiceApp.clientSubscription.created', { param: param.id }).toString(),
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
