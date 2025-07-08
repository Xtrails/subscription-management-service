import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SubscriptionDetailsUpdate from './subscription-details-update.vue';
import SubscriptionDetailsService from './subscription-details.service';
import AlertService from '@/shared/alert/alert.service';

import SourceApplicationService from '@/entities/source-application/source-application.service';

type SubscriptionDetailsUpdateComponentType = InstanceType<typeof SubscriptionDetailsUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const subscriptionDetailsSample = { id: '9fec3727-3421-4967-b213-ba36557ca194' };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<SubscriptionDetailsUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('SubscriptionDetails Management Update Component', () => {
    let comp: SubscriptionDetailsUpdateComponentType;
    let subscriptionDetailsServiceStub: SinonStubbedInstance<SubscriptionDetailsService>;

    beforeEach(() => {
      route = {};
      subscriptionDetailsServiceStub = sinon.createStubInstance<SubscriptionDetailsService>(SubscriptionDetailsService);
      subscriptionDetailsServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          subscriptionDetailsService: () => subscriptionDetailsServiceStub,
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
        const wrapper = shallowMount(SubscriptionDetailsUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.subscriptionDetails = subscriptionDetailsSample;
        subscriptionDetailsServiceStub.update.resolves(subscriptionDetailsSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(subscriptionDetailsServiceStub.update.calledWith(subscriptionDetailsSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        subscriptionDetailsServiceStub.create.resolves(entity);
        const wrapper = shallowMount(SubscriptionDetailsUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.subscriptionDetails = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(subscriptionDetailsServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        subscriptionDetailsServiceStub.find.resolves(subscriptionDetailsSample);
        subscriptionDetailsServiceStub.retrieve.resolves([subscriptionDetailsSample]);

        // WHEN
        route = {
          params: {
            subscriptionDetailsId: `${subscriptionDetailsSample.id}`,
          },
        };
        const wrapper = shallowMount(SubscriptionDetailsUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.subscriptionDetails).toMatchObject(subscriptionDetailsSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        subscriptionDetailsServiceStub.find.resolves(subscriptionDetailsSample);
        const wrapper = shallowMount(SubscriptionDetailsUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
