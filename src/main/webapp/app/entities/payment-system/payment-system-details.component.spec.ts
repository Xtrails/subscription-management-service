import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PaymentSystemDetails from './payment-system-details.vue';
import PaymentSystemService from './payment-system.service';
import AlertService from '@/shared/alert/alert.service';

type PaymentSystemDetailsComponentType = InstanceType<typeof PaymentSystemDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const paymentSystemSample = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('PaymentSystem Management Detail Component', () => {
    let paymentSystemServiceStub: SinonStubbedInstance<PaymentSystemService>;
    let mountOptions: MountingOptions<PaymentSystemDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      paymentSystemServiceStub = sinon.createStubInstance<PaymentSystemService>(PaymentSystemService);

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
          paymentSystemService: () => paymentSystemServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        paymentSystemServiceStub.find.resolves(paymentSystemSample);
        route = {
          params: {
            paymentSystemId: '' + '9fec3727-3421-4967-b213-ba36557ca194',
          },
        };
        const wrapper = shallowMount(PaymentSystemDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.paymentSystem).toMatchObject(paymentSystemSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        paymentSystemServiceStub.find.resolves(paymentSystemSample);
        const wrapper = shallowMount(PaymentSystemDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
