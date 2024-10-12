/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ReferralProgramDetails from './referral-program-details.vue';
import ReferralProgramService from './referral-program.service';
import AlertService from '@/shared/alert/alert.service';

type ReferralProgramDetailsComponentType = InstanceType<typeof ReferralProgramDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const referralProgramSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('ReferralProgram Management Detail Component', () => {
    let referralProgramServiceStub: SinonStubbedInstance<ReferralProgramService>;
    let mountOptions: MountingOptions<ReferralProgramDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      referralProgramServiceStub = sinon.createStubInstance<ReferralProgramService>(ReferralProgramService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          referralProgramService: () => referralProgramServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        referralProgramServiceStub.find.resolves(referralProgramSample);
        route = {
          params: {
            referralProgramId: `${123}`,
          },
        };
        const wrapper = shallowMount(ReferralProgramDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.referralProgram).toMatchObject(referralProgramSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        referralProgramServiceStub.find.resolves(referralProgramSample);
        const wrapper = shallowMount(ReferralProgramDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
