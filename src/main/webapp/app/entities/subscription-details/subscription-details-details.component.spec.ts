import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SubscriptionDetailsDetails from './subscription-details-details.vue';
import SubscriptionDetailsService from './subscription-details.service';
import AlertService from '@/shared/alert/alert.service';

type SubscriptionDetailsDetailsComponentType = InstanceType<typeof SubscriptionDetailsDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const subscriptionDetailsSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('SubscriptionDetails Management Detail Component', () => {
    let subscriptionDetailsServiceStub: SinonStubbedInstance<SubscriptionDetailsService>;
    let mountOptions: MountingOptions<SubscriptionDetailsDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      subscriptionDetailsServiceStub = sinon.createStubInstance<SubscriptionDetailsService>(SubscriptionDetailsService);

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
          subscriptionDetailsService: () => subscriptionDetailsServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        subscriptionDetailsServiceStub.find.resolves(subscriptionDetailsSample);
        route = {
          params: {
            subscriptionDetailsId: `${123}`,
          },
        };
        const wrapper = shallowMount(SubscriptionDetailsDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.subscriptionDetails).toMatchObject(subscriptionDetailsSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        subscriptionDetailsServiceStub.find.resolves(subscriptionDetailsSample);
        const wrapper = shallowMount(SubscriptionDetailsDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
