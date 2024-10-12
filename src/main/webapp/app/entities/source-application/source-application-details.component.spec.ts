/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import SourceApplicationDetails from './source-application-details.vue';
import SourceApplicationService from './source-application.service';
import AlertService from '@/shared/alert/alert.service';

type SourceApplicationDetailsComponentType = InstanceType<typeof SourceApplicationDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const sourceApplicationSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('SourceApplication Management Detail Component', () => {
    let sourceApplicationServiceStub: SinonStubbedInstance<SourceApplicationService>;
    let mountOptions: MountingOptions<SourceApplicationDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      sourceApplicationServiceStub = sinon.createStubInstance<SourceApplicationService>(SourceApplicationService);

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
          sourceApplicationService: () => sourceApplicationServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        sourceApplicationServiceStub.find.resolves(sourceApplicationSample);
        route = {
          params: {
            sourceApplicationId: `${123}`,
          },
        };
        const wrapper = shallowMount(SourceApplicationDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.sourceApplication).toMatchObject(sourceApplicationSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        sourceApplicationServiceStub.find.resolves(sourceApplicationSample);
        const wrapper = shallowMount(SourceApplicationDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
