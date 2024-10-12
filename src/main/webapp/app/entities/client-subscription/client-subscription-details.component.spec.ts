/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ClientSubscriptionDetails from './client-subscription-details.vue';
import ClientSubscriptionService from './client-subscription.service';
import AlertService from '@/shared/alert/alert.service';

type ClientSubscriptionDetailsComponentType = InstanceType<typeof ClientSubscriptionDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const clientSubscriptionSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('ClientSubscription Management Detail Component', () => {
    let clientSubscriptionServiceStub: SinonStubbedInstance<ClientSubscriptionService>;
    let mountOptions: MountingOptions<ClientSubscriptionDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      clientSubscriptionServiceStub = sinon.createStubInstance<ClientSubscriptionService>(ClientSubscriptionService);

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
          clientSubscriptionService: () => clientSubscriptionServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        clientSubscriptionServiceStub.find.resolves(clientSubscriptionSample);
        route = {
          params: {
            clientSubscriptionId: `${123}`,
          },
        };
        const wrapper = shallowMount(ClientSubscriptionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.clientSubscription).toMatchObject(clientSubscriptionSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        clientSubscriptionServiceStub.find.resolves(clientSubscriptionSample);
        const wrapper = shallowMount(ClientSubscriptionDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
