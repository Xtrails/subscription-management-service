import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SubscriptionAccessUpdate from './subscription-access-update.vue';
import SubscriptionAccessService from './subscription-access.service';
import AlertService from '@/shared/alert/alert.service';

import SubscriptionDetailsService from '@/entities/subscription-details/subscription-details.service';

type SubscriptionAccessUpdateComponentType = InstanceType<typeof SubscriptionAccessUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const subscriptionAccessSample = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<SubscriptionAccessUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('SubscriptionAccess Management Update Component', () => {
    let comp: SubscriptionAccessUpdateComponentType;
    let subscriptionAccessServiceStub: SinonStubbedInstance<SubscriptionAccessService>;

    beforeEach(() => {
      route = {};
      subscriptionAccessServiceStub = sinon.createStubInstance<SubscriptionAccessService>(SubscriptionAccessService);
      subscriptionAccessServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          subscriptionAccessService: () => subscriptionAccessServiceStub,
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
        const wrapper = shallowMount(SubscriptionAccessUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.subscriptionAccess = subscriptionAccessSample;
        subscriptionAccessServiceStub.update.resolves(subscriptionAccessSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(subscriptionAccessServiceStub.update.calledWith(subscriptionAccessSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        subscriptionAccessServiceStub.create.resolves(entity);
        const wrapper = shallowMount(SubscriptionAccessUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.subscriptionAccess = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(subscriptionAccessServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        subscriptionAccessServiceStub.find.resolves(subscriptionAccessSample);
        subscriptionAccessServiceStub.retrieve.resolves([subscriptionAccessSample]);

        // WHEN
        route = {
          params: {
            subscriptionAccessId: `${subscriptionAccessSample.id}`,
          },
        };
        const wrapper = shallowMount(SubscriptionAccessUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.subscriptionAccess).toMatchObject(subscriptionAccessSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        subscriptionAccessServiceStub.find.resolves(subscriptionAccessSample);
        const wrapper = shallowMount(SubscriptionAccessUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
