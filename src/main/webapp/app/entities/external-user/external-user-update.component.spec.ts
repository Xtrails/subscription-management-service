/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ExternalUserUpdate from './external-user-update.vue';
import ExternalUserService from './external-user.service';
import AlertService from '@/shared/alert/alert.service';

type ExternalUserUpdateComponentType = InstanceType<typeof ExternalUserUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const externalUserSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<ExternalUserUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('ExternalUser Management Update Component', () => {
    let comp: ExternalUserUpdateComponentType;
    let externalUserServiceStub: SinonStubbedInstance<ExternalUserService>;

    beforeEach(() => {
      route = {};
      externalUserServiceStub = sinon.createStubInstance<ExternalUserService>(ExternalUserService);
      externalUserServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

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
          externalUserService: () => externalUserServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(ExternalUserUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.externalUser = externalUserSample;
        externalUserServiceStub.update.resolves(externalUserSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(externalUserServiceStub.update.calledWith(externalUserSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        externalUserServiceStub.create.resolves(entity);
        const wrapper = shallowMount(ExternalUserUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.externalUser = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(externalUserServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        externalUserServiceStub.find.resolves(externalUserSample);
        externalUserServiceStub.retrieve.resolves([externalUserSample]);

        // WHEN
        route = {
          params: {
            externalUserId: `${externalUserSample.id}`,
          },
        };
        const wrapper = shallowMount(ExternalUserUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.externalUser).toMatchObject(externalUserSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        externalUserServiceStub.find.resolves(externalUserSample);
        const wrapper = shallowMount(ExternalUserUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
