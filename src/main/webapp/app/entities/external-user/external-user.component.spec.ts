import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import ExternalUser from './external-user.vue';
import ExternalUserService from './external-user.service';
import AlertService from '@/shared/alert/alert.service';

type ExternalUserComponentType = InstanceType<typeof ExternalUser>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('ExternalUser Management Component', () => {
    let externalUserServiceStub: SinonStubbedInstance<ExternalUserService>;
    let mountOptions: MountingOptions<ExternalUserComponentType>['global'];

    beforeEach(() => {
      externalUserServiceStub = sinon.createStubInstance<ExternalUserService>(ExternalUserService);
      externalUserServiceStub.retrieve.resolves({ headers: {} });

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
          externalUserService: () => externalUserServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        externalUserServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(ExternalUser, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(externalUserServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.externalUsers[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: ExternalUserComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(ExternalUser, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        externalUserServiceStub.retrieve.reset();
        externalUserServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        externalUserServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeExternalUser();
        await comp.$nextTick(); // clear components

        // THEN
        expect(externalUserServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(externalUserServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
