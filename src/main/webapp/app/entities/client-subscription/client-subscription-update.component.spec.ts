import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ClientSubscriptionUpdate from './client-subscription-update.vue';
import ClientSubscriptionService from './client-subscription.service';
import AlertService from '@/shared/alert/alert.service';

import ExternalUserService from '@/entities/external-user/external-user.service';
import SubscriptionDetailsService from '@/entities/subscription-details/subscription-details.service';

type ClientSubscriptionUpdateComponentType = InstanceType<typeof ClientSubscriptionUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const clientSubscriptionSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ClientSubscriptionUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('ClientSubscription Management Update Component', () => {
    let comp: ClientSubscriptionUpdateComponentType;
    let clientSubscriptionServiceStub: SinonStubbedInstance<ClientSubscriptionService>;

    beforeEach(() => {
      route = {};
      clientSubscriptionServiceStub = sinon.createStubInstance<ClientSubscriptionService>(ClientSubscriptionService);
      clientSubscriptionServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          clientSubscriptionService: () => clientSubscriptionServiceStub,
          externalUserService: () =>
            sinon.createStubInstance<ExternalUserService>(ExternalUserService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
          subscriptionDetailsService: () =>
            sinon.createStubInstance<SubscriptionDetailsService>(SubscriptionDetailsService, {
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
        const wrapper = shallowMount(ClientSubscriptionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.clientSubscription = clientSubscriptionSample;
        clientSubscriptionServiceStub.update.resolves(clientSubscriptionSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(clientSubscriptionServiceStub.update.calledWith(clientSubscriptionSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        clientSubscriptionServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ClientSubscriptionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.clientSubscription = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(clientSubscriptionServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        clientSubscriptionServiceStub.find.resolves(clientSubscriptionSample);
        clientSubscriptionServiceStub.retrieve.resolves([clientSubscriptionSample]);

        // WHEN
        route = {
          params: {
            clientSubscriptionId: `${clientSubscriptionSample.id}`,
          },
        };
        const wrapper = shallowMount(ClientSubscriptionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.clientSubscription).toMatchObject(clientSubscriptionSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        clientSubscriptionServiceStub.find.resolves(clientSubscriptionSample);
        const wrapper = shallowMount(ClientSubscriptionUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
