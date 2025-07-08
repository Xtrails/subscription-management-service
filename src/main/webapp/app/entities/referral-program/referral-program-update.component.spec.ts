import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ReferralProgramUpdate from './referral-program-update.vue';
import ReferralProgramService from './referral-program.service';
import AlertService from '@/shared/alert/alert.service';

import ExternalUserService from '@/entities/external-user/external-user.service';
import SourceApplicationService from '@/entities/source-application/source-application.service';

type ReferralProgramUpdateComponentType = InstanceType<typeof ReferralProgramUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const referralProgramSample = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ReferralProgramUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('ReferralProgram Management Update Component', () => {
    let comp: ReferralProgramUpdateComponentType;
    let referralProgramServiceStub: SinonStubbedInstance<ReferralProgramService>;

    beforeEach(() => {
      route = {};
      referralProgramServiceStub = sinon.createStubInstance<ReferralProgramService>(ReferralProgramService);
      referralProgramServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          referralProgramService: () => referralProgramServiceStub,
          externalUserService: () =>
            sinon.createStubInstance<ExternalUserService>(ExternalUserService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          sourceApplicationService: () =>
            sinon.createStubInstance<SourceApplicationService>(SourceApplicationService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ReferralProgramUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.referralProgram = referralProgramSample;
        referralProgramServiceStub.update.resolves(referralProgramSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(referralProgramServiceStub.update.calledWith(referralProgramSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        referralProgramServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ReferralProgramUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.referralProgram = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(referralProgramServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        referralProgramServiceStub.find.resolves(referralProgramSample);
        referralProgramServiceStub.retrieve.resolves([referralProgramSample]);

        // WHEN
        route = {
          params: {
            referralProgramId: `${referralProgramSample.id}`,
          },
        };
        const wrapper = shallowMount(ReferralProgramUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.referralProgram).toMatchObject(referralProgramSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        referralProgramServiceStub.find.resolves(referralProgramSample);
        const wrapper = shallowMount(ReferralProgramUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
