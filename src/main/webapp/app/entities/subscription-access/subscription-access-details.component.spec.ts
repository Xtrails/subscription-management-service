import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SubscriptionAccessDetails from './subscription-access-details.vue';
import SubscriptionAccessService from './subscription-access.service';
import AlertService from '@/shared/alert/alert.service';

type SubscriptionAccessDetailsComponentType = InstanceType<typeof SubscriptionAccessDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const subscriptionAccessSample = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('SubscriptionAccess Management Detail Component', () => {
    let subscriptionAccessServiceStub: SinonStubbedInstance<SubscriptionAccessService>;
    let mountOptions: MountingOptions<SubscriptionAccessDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      subscriptionAccessServiceStub = sinon.createStubInstance<SubscriptionAccessService>(SubscriptionAccessService);

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
          subscriptionAccessService: () => subscriptionAccessServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        subscriptionAccessServiceStub.find.resolves(subscriptionAccessSample);
        route = {
          params: {
            subscriptionAccessId: '' + '9fec3727-3421-4967-b213-ba36557ca194',
          },
        };
        const wrapper = shallowMount(SubscriptionAccessDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.subscriptionAccess).toMatchObject(subscriptionAccessSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        subscriptionAccessServiceStub.find.resolves(subscriptionAccessSample);
        const wrapper = shallowMount(SubscriptionAccessDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
