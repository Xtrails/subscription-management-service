/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SubscriptionTypeDetails from './subscription-type-details.vue';
import SubscriptionTypeService from './subscription-type.service';
import AlertService from '@/shared/alert/alert.service';

type SubscriptionTypeDetailsComponentType = InstanceType<typeof SubscriptionTypeDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const subscriptionTypeSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('SubscriptionType Management Detail Component', () => {
    let subscriptionTypeServiceStub: SinonStubbedInstance<SubscriptionTypeService>;
    let mountOptions: MountingOptions<SubscriptionTypeDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      subscriptionTypeServiceStub = sinon.createStubInstance<SubscriptionTypeService>(SubscriptionTypeService);

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
          subscriptionTypeService: () => subscriptionTypeServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        subscriptionTypeServiceStub.find.resolves(subscriptionTypeSample);
        route = {
          params: {
            subscriptionTypeId: `${123}`,
          },
        };
        const wrapper = shallowMount(SubscriptionTypeDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.subscriptionType).toMatchObject(subscriptionTypeSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        subscriptionTypeServiceStub.find.resolves(subscriptionTypeSample);
        const wrapper = shallowMount(SubscriptionTypeDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
