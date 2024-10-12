/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SubscriptionTypeUpdate from './subscription-type-update.vue';
import SubscriptionTypeService from './subscription-type.service';
import AlertService from '@/shared/alert/alert.service';

import SourceApplicationService from '@/entities/source-application/source-application.service';

type SubscriptionTypeUpdateComponentType = InstanceType<typeof SubscriptionTypeUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const subscriptionTypeSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<SubscriptionTypeUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('SubscriptionType Management Update Component', () => {
    let comp: SubscriptionTypeUpdateComponentType;
    let subscriptionTypeServiceStub: SinonStubbedInstance<SubscriptionTypeService>;

    beforeEach(() => {
      route = {};
      subscriptionTypeServiceStub = sinon.createStubInstance<SubscriptionTypeService>(SubscriptionTypeService);
      subscriptionTypeServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          subscriptionTypeService: () => subscriptionTypeServiceStub,
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
        const wrapper = shallowMount(SubscriptionTypeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.subscriptionType = subscriptionTypeSample;
        subscriptionTypeServiceStub.update.resolves(subscriptionTypeSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(subscriptionTypeServiceStub.update.calledWith(subscriptionTypeSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        subscriptionTypeServiceStub.create.resolves(entity);
        const wrapper = shallowMount(SubscriptionTypeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.subscriptionType = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(subscriptionTypeServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        subscriptionTypeServiceStub.find.resolves(subscriptionTypeSample);
        subscriptionTypeServiceStub.retrieve.resolves([subscriptionTypeSample]);

        // WHEN
        route = {
          params: {
            subscriptionTypeId: `${subscriptionTypeSample.id}`,
          },
        };
        const wrapper = shallowMount(SubscriptionTypeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.subscriptionType).toMatchObject(subscriptionTypeSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        subscriptionTypeServiceStub.find.resolves(subscriptionTypeSample);
        const wrapper = shallowMount(SubscriptionTypeUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
