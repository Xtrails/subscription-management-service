import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import ClientSubscription from './client-subscription.vue';
import ClientSubscriptionService from './client-subscription.service';
import AlertService from '@/shared/alert/alert.service';

type ClientSubscriptionComponentType = InstanceType<typeof ClientSubscription>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('ClientSubscription Management Component', () => {
    let clientSubscriptionServiceStub: SinonStubbedInstance<ClientSubscriptionService>;
    let mountOptions: MountingOptions<ClientSubscriptionComponentType>['global'];

    beforeEach(() => {
      clientSubscriptionServiceStub = sinon.createStubInstance<ClientSubscriptionService>(ClientSubscriptionService);
      clientSubscriptionServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          jhiItemCount: true,
          bPagination: true,
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'jhi-sort-indicator': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          clientSubscriptionService: () => clientSubscriptionServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        clientSubscriptionServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(ClientSubscription, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(clientSubscriptionServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.clientSubscriptions[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for an id', async () => {
        // WHEN
        const wrapper = shallowMount(ClientSubscription, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(clientSubscriptionServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['id,asc'],
        });
      });
    });
    describe('Handles', () => {
      let comp: ClientSubscriptionComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(ClientSubscription, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        clientSubscriptionServiceStub.retrieve.reset();
        clientSubscriptionServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('should load a page', async () => {
        // GIVEN
        clientSubscriptionServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.page = 2;
        await comp.$nextTick();

        // THEN
        expect(clientSubscriptionServiceStub.retrieve.called).toBeTruthy();
        expect(comp.clientSubscriptions[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should re-initialize the page', async () => {
        // GIVEN
        comp.page = 2;
        await comp.$nextTick();
        clientSubscriptionServiceStub.retrieve.reset();
        clientSubscriptionServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        comp.clear();
        await comp.$nextTick();

        // THEN
        expect(comp.page).toEqual(1);
        expect(clientSubscriptionServiceStub.retrieve.callCount).toEqual(1);
        expect(comp.clientSubscriptions[0]).toEqual(expect.objectContaining({ id: 123 }));
      });

      it('should calculate the sort attribute for a non-id attribute', async () => {
        // WHEN
        comp.propOrder = 'name';
        await comp.$nextTick();

        // THEN
        expect(clientSubscriptionServiceStub.retrieve.lastCall.firstArg).toMatchObject({
          sort: ['name,asc', 'id'],
        });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        clientSubscriptionServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeClientSubscription();
        await comp.$nextTick(); // clear components

        // THEN
        expect(clientSubscriptionServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(clientSubscriptionServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
