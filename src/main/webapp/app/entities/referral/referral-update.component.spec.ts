/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ReferralUpdate from './referral-update.vue';
import ReferralService from './referral.service';
import AlertService from '@/shared/alert/alert.service';

import ExternalUserService from '@/entities/external-user/external-user.service';
import ReferralProgramService from '@/entities/referral-program/referral-program.service';

type ReferralUpdateComponentType = InstanceType<typeof ReferralUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const referralSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ReferralUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Referral Management Update Component', () => {
    let comp: ReferralUpdateComponentType;
    let referralServiceStub: SinonStubbedInstance<ReferralService>;

    beforeEach(() => {
      route = {};
      referralServiceStub = sinon.createStubInstance<ReferralService>(ReferralService);
      referralServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          referralService: () => referralServiceStub,
          externalUserService: () =>
            sinon.createStubInstance<ExternalUserService>(ExternalUserService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          referralProgramService: () =>
            sinon.createStubInstance<ReferralProgramService>(ReferralProgramService, {
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
        const wrapper = shallowMount(ReferralUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.referral = referralSample;
        referralServiceStub.update.resolves(referralSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(referralServiceStub.update.calledWith(referralSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        referralServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ReferralUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.referral = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(referralServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        referralServiceStub.find.resolves(referralSample);
        referralServiceStub.retrieve.resolves([referralSample]);

        // WHEN
        route = {
          params: {
            referralId: `${referralSample.id}`,
          },
        };
        const wrapper = shallowMount(ReferralUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.referral).toMatchObject(referralSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        referralServiceStub.find.resolves(referralSample);
        const wrapper = shallowMount(ReferralUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
