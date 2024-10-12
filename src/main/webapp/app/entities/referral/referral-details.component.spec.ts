/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ReferralDetails from './referral-details.vue';
import ReferralService from './referral.service';
import AlertService from '@/shared/alert/alert.service';

type ReferralDetailsComponentType = InstanceType<typeof ReferralDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const referralSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('Referral Management Detail Component', () => {
    let referralServiceStub: SinonStubbedInstance<ReferralService>;
    let mountOptions: MountingOptions<ReferralDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      referralServiceStub = sinon.createStubInstance<ReferralService>(ReferralService);

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
          referralService: () => referralServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        referralServiceStub.find.resolves(referralSample);
        route = {
          params: {
            referralId: `${123}`,
          },
        };
        const wrapper = shallowMount(ReferralDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.referral).toMatchObject(referralSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        referralServiceStub.find.resolves(referralSample);
        const wrapper = shallowMount(ReferralDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
