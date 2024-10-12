import { type Ref, computed, defineComponent, inject, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import ReferralProgramService from './referral-program.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import SourceApplicationService from '@/entities/source-application/source-application.service';
import { type ISourceApplication } from '@/shared/model/source-application.model';
import { type IReferralProgram, ReferralProgram } from '@/shared/model/referral-program.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'ReferralProgramUpdate',
  setup() {
    const referralProgramService = inject('referralProgramService', () => new ReferralProgramService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const referralProgram: Ref<IReferralProgram> = ref(new ReferralProgram());

    const sourceApplicationService = inject('sourceApplicationService', () => new SourceApplicationService());

    const sourceApplications: Ref<ISourceApplication[]> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'ru'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveReferralProgram = async referralProgramId => {
      try {
        const res = await referralProgramService().find(referralProgramId);
        referralProgram.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.referralProgramId) {
      retrieveReferralProgram(route.params.referralProgramId);
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
      rewardAmount: {
        required: validations.required(t$('entity.validation.required').toString()),
      },
      sourceApplication: {},
    };
    const v$ = useVuelidate(validationRules, referralProgram as any);
    v$.value.$validate();

    return {
      referralProgramService,
      alertService,
      referralProgram,
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
      if (this.referralProgram.id) {
        this.referralProgramService()
          .update(this.referralProgram)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('subscriptionManagementServiceApp.referralProgram.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.referralProgramService()
          .create(this.referralProgram)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(
              this.t$('subscriptionManagementServiceApp.referralProgram.created', { param: param.id }).toString(),
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
