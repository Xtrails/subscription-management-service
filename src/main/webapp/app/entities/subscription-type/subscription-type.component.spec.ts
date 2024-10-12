/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import SubscriptionType from './subscription-type.vue';
import SubscriptionTypeService from './subscription-type.service';
import AlertService from '@/shared/alert/alert.service';

type SubscriptionTypeComponentType = InstanceType<typeof SubscriptionType>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('SubscriptionType Management Component', () => {
    let subscriptionTypeServiceStub: SinonStubbedInstance<SubscriptionTypeService>;
    let mountOptions: MountingOptions<SubscriptionTypeComponentType>['global'];

    beforeEach(() => {
      subscriptionTypeServiceStub = sinon.createStubInstance<SubscriptionTypeService>(SubscriptionTypeService);
      subscriptionTypeServiceStub.retrieve.resolves({ headers: {} });

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
          subscriptionTypeService: () => subscriptionTypeServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        subscriptionTypeServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(SubscriptionType, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(subscriptionTypeServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.subscriptionTypes[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: SubscriptionTypeComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(SubscriptionType, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        subscriptionTypeServiceStub.retrieve.reset();
        subscriptionTypeServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        subscriptionTypeServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeSubscriptionType();
        await comp.$nextTick(); // clear components

        // THEN
        expect(subscriptionTypeServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(subscriptionTypeServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
