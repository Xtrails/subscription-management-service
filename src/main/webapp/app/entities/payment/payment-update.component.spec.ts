/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PaymentUpdate from './payment-update.vue';
import PaymentService from './payment.service';
import AlertService from '@/shared/alert/alert.service';

import ExternalUserService from '@/entities/external-user/external-user.service';
import ClientSubscriptionService from '@/entities/client-subscription/client-subscription.service';
import PaymentSystemService from '@/entities/payment-system/payment-system.service';

type PaymentUpdateComponentType = InstanceType<typeof PaymentUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const paymentSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PaymentUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('Payment Management Update Component', () => {
    let comp: PaymentUpdateComponentType;
    let paymentServiceStub: SinonStubbedInstance<PaymentService>;

    beforeEach(() => {
      route = {};
      paymentServiceStub = sinon.createStubInstance<PaymentService>(PaymentService);
      paymentServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          paymentService: () => paymentServiceStub,
          externalUserService: () =>
            sinon.createStubInstance<ExternalUserService>(ExternalUserService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          clientSubscriptionService: () =>
            sinon.createStubInstance<ClientSubscriptionService>(ClientSubscriptionService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          paymentSystemService: () =>
            sinon.createStubInstance<PaymentSystemService>(PaymentSystemService, {
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
        const wrapper = shallowMount(PaymentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.payment = paymentSample;
        paymentServiceStub.update.resolves(paymentSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(paymentServiceStub.update.calledWith(paymentSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        paymentServiceStub.create.resolves(entity);
        const wrapper = shallowMount(PaymentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.payment = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(paymentServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        paymentServiceStub.find.resolves(paymentSample);
        paymentServiceStub.retrieve.resolves([paymentSample]);

        // WHEN
        route = {
          params: {
            paymentId: `${paymentSample.id}`,
          },
        };
        const wrapper = shallowMount(PaymentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.payment).toMatchObject(paymentSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        paymentServiceStub.find.resolves(paymentSample);
        const wrapper = shallowMount(PaymentUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
