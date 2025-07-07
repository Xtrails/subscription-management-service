import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import ExternalUserDetails from './external-user-details.vue';
import ExternalUserService from './external-user.service';
import AlertService from '@/shared/alert/alert.service';

type ExternalUserDetailsComponentType = InstanceType<typeof ExternalUserDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const externalUserSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('ExternalUser Management Detail Component', () => {
    let externalUserServiceStub: SinonStubbedInstance<ExternalUserService>;
    let mountOptions: MountingOptions<ExternalUserDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      externalUserServiceStub = sinon.createStubInstance<ExternalUserService>(ExternalUserService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          externalUserService: () => externalUserServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        externalUserServiceStub.find.resolves(externalUserSample);
        route = {
          params: {
            externalUserId: `${123}`,
          },
        };
        const wrapper = shallowMount(ExternalUserDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.externalUser).toMatchObject(externalUserSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        externalUserServiceStub.find.resolves(externalUserSample);
        const wrapper = shallowMount(ExternalUserDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
