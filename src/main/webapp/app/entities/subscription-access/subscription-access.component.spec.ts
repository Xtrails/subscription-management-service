import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import SubscriptionAccess from './subscription-access.vue';
import SubscriptionAccessService from './subscription-access.service';
import AlertService from '@/shared/alert/alert.service';

type SubscriptionAccessComponentType = InstanceType<typeof SubscriptionAccess>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('SubscriptionAccess Management Component', () => {
    let subscriptionAccessServiceStub: SinonStubbedInstance<SubscriptionAccessService>;
    let mountOptions: MountingOptions<SubscriptionAccessComponentType>['global'];

    beforeEach(() => {
      subscriptionAccessServiceStub = sinon.createStubInstance<SubscriptionAccessService>(SubscriptionAccessService);
      subscriptionAccessServiceStub.retrieve.resolves({ headers: {} });

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
          subscriptionAccessService: () => subscriptionAccessServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        subscriptionAccessServiceStub.retrieve.resolves({ headers: {}, data: [{ id: '9fec3727-3421-4967-b213-ba36557ca194' }] });

        // WHEN
        const wrapper = shallowMount(SubscriptionAccess, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(subscriptionAccessServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.subscriptionAccesses[0]).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
      });
    });
    describe('Handles', () => {
      let comp: SubscriptionAccessComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(SubscriptionAccess, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        subscriptionAccessServiceStub.retrieve.reset();
        subscriptionAccessServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        subscriptionAccessServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: '9fec3727-3421-4967-b213-ba36557ca194' });

        comp.removeSubscriptionAccess();
        await comp.$nextTick(); // clear components

        // THEN
        expect(subscriptionAccessServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(subscriptionAccessServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
