import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import SourceApplication from './source-application.vue';
import SourceApplicationService from './source-application.service';
import AlertService from '@/shared/alert/alert.service';

type SourceApplicationComponentType = InstanceType<typeof SourceApplication>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('SourceApplication Management Component', () => {
    let sourceApplicationServiceStub: SinonStubbedInstance<SourceApplicationService>;
    let mountOptions: MountingOptions<SourceApplicationComponentType>['global'];

    beforeEach(() => {
      sourceApplicationServiceStub = sinon.createStubInstance<SourceApplicationService>(SourceApplicationService);
      sourceApplicationServiceStub.retrieve.resolves({ headers: {} });

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
          sourceApplicationService: () => sourceApplicationServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        sourceApplicationServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(SourceApplication, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(sourceApplicationServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.sourceApplications[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: SourceApplicationComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(SourceApplication, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        sourceApplicationServiceStub.retrieve.reset();
        sourceApplicationServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        sourceApplicationServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeSourceApplication();
        await comp.$nextTick(); // clear components

        // THEN
        expect(sourceApplicationServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(sourceApplicationServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
