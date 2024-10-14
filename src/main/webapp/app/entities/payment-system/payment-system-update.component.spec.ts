/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import PaymentSystemUpdate from './payment-system-update.vue';
import PaymentSystemService from './payment-system.service';
import AlertService from '@/shared/alert/alert.service';

import SourceApplicationService from '@/entities/source-application/source-application.service';

type PaymentSystemUpdateComponentType = InstanceType<typeof PaymentSystemUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const paymentSystemSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<PaymentSystemUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('PaymentSystem Management Update Component', () => {
    let comp: PaymentSystemUpdateComponentType;
    let paymentSystemServiceStub: SinonStubbedInstance<PaymentSystemService>;

    beforeEach(() => {
      route = {};
      paymentSystemServiceStub = sinon.createStubInstance<PaymentSystemService>(PaymentSystemService);
      paymentSystemServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          paymentSystemService: () => paymentSystemServiceStub,
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
        const wrapper = shallowMount(PaymentSystemUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.paymentSystem = paymentSystemSample;
        paymentSystemServiceStub.update.resolves(paymentSystemSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(paymentSystemServiceStub.update.calledWith(paymentSystemSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        paymentSystemServiceStub.create.resolves(entity);
        const wrapper = shallowMount(PaymentSystemUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.paymentSystem = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(paymentSystemServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        paymentSystemServiceStub.find.resolves(paymentSystemSample);
        paymentSystemServiceStub.retrieve.resolves([paymentSystemSample]);

        // WHEN
        route = {
          params: {
            paymentSystemId: `${paymentSystemSample.id}`,
          },
        };
        const wrapper = shallowMount(PaymentSystemUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.paymentSystem).toMatchObject(paymentSystemSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        paymentSystemServiceStub.find.resolves(paymentSystemSample);
        const wrapper = shallowMount(PaymentSystemUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
