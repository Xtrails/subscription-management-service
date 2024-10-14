import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ReferralService from './referral.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import ExternalUserService from '@/entities/external-user/external-user.service';
import { type IExternalUser } from '@/shared/model/external-user.model';
import ReferralProgramService from '@/entities/referral-program/referral-program.service';
import { type IReferralProgram } from '@/shared/model/referral-program.model';
import SourceApplicationService from '@/entities/source-application/source-application.service';
import { type ISourceApplication } from '@/shared/model/source-application.model';
import { type IReferral, Referral } from '@/shared/model/referral.model';
import { ReferralStatus } from '@/shared/model/enumerations/referral-status.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ReferralUpdate',
  setup() {
    const referralService = inject('referralService', () => new ReferralService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const referral: Ref<IReferral> = ref(new Referral());

    const externalUserService = inject('externalUserService', () => new ExternalUserService());

    const externalUsers: Ref<IExternalUser[]> = ref([]);

    const referralProgramService = inject('referralProgramService', () => new ReferralProgramService());

    const referralPrograms: Ref<IReferralProgram[]> = ref([]);

    const sourceApplicationService = inject('sourceApplicationService', () => new SourceApplicationService());

    const sourceApplications: Ref<ISourceApplication[]> = ref([]);
    const referralStatusValues: Ref<string[]> = ref(Object.keys(ReferralStatus));
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'ru'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveReferral = async referralId => {
      try {
        const res = await referralService().find(referralId);
        referral.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.referralId) {
      retrieveReferral(route.params.referralId);
    }

    const initRelationships = () => {
      externalUserService()
        .retrieve()
        .then(res => {
          externalUsers.value = res.data;
        });
      referralProgramService()
        .retrieve()
        .then(res => {
          referralPrograms.value = res.data;
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
      referralCode: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      status: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      referrer: {},
      referralProgram: {},
      sourceApplication: {},
    };
    const v$ = useVuelidate(validationRules, referral as any);
    v$.value.$validate();

    return {
      referralService,
      alertService,
      referral,
      previousState,
      referralStatusValues,
      isSaving,
      currentLanguage,
      externalUsers,
      referralPrograms,
      sourceApplications,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.referral.id) {
        this.referralService()
          .update(this.referral)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('subscriptionManagementServiceApp.referral.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.referralService()
          .create(this.referral)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('subscriptionManagementServiceApp.referral.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
