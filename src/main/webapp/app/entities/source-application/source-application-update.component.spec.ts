import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SourceApplicationUpdate from './source-application-update.vue';
import SourceApplicationService from './source-application.service';
import AlertService from '@/shared/alert/alert.service';

import ExternalUserService from '@/entities/external-user/external-user.service';

type SourceApplicationUpdateComponentType = InstanceType<typeof SourceApplicationUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const sourceApplicationSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<SourceApplicationUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('SourceApplication Management Update Component', () => {
    let comp: SourceApplicationUpdateComponentType;
    let sourceApplicationServiceStub: SinonStubbedInstance<SourceApplicationService>;

    beforeEach(() => {
      route = {};
      sourceApplicationServiceStub = sinon.createStubInstance<SourceApplicationService>(SourceApplicationService);
      sourceApplicationServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          sourceApplicationService: () => sourceApplicationServiceStub,
          externalUserService: () =>
            sinon.createStubInstance<ExternalUserService>(ExternalUserService, {
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
        const wrapper = shallowMount(SourceApplicationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.sourceApplication = sourceApplicationSample;
        sourceApplicationServiceStub.update.resolves(sourceApplicationSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(sourceApplicationServiceStub.update.calledWith(sourceApplicationSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        sourceApplicationServiceStub.create.resolves(entity);
        const wrapper = shallowMount(SourceApplicationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.sourceApplication = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(sourceApplicationServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        sourceApplicationServiceStub.find.resolves(sourceApplicationSample);
        sourceApplicationServiceStub.retrieve.resolves([sourceApplicationSample]);

        // WHEN
        route = {
          params: {
            sourceApplicationId: `${sourceApplicationSample.id}`,
          },
        };
        const wrapper = shallowMount(SourceApplicationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.sourceApplication).toMatchObject(sourceApplicationSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        sourceApplicationServiceStub.find.resolves(sourceApplicationSample);
        const wrapper = shallowMount(SourceApplicationUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
