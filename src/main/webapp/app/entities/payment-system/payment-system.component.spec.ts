import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import PaymentSystem from './payment-system.vue';
import PaymentSystemService from './payment-system.service';
import AlertService from '@/shared/alert/alert.service';

type PaymentSystemComponentType = InstanceType<typeof PaymentSystem>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('PaymentSystem Management Component', () => {
    let paymentSystemServiceStub: SinonStubbedInstance<PaymentSystemService>;
    let mountOptions: MountingOptions<PaymentSystemComponentType>['global'];

    beforeEach(() => {
      paymentSystemServiceStub = sinon.createStubInstance<PaymentSystemService>(PaymentSystemService);
      paymentSystemServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          paymentSystemService: () => paymentSystemServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        paymentSystemServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(PaymentSystem, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(paymentSystemServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.paymentSystems[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: PaymentSystemComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(PaymentSystem, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        paymentSystemServiceStub.retrieve.reset();
        paymentSystemServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        paymentSystemServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removePaymentSystem();
        await comp.$nextTick(); // clear components

        // THEN
        expect(paymentSystemServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(paymentSystemServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
