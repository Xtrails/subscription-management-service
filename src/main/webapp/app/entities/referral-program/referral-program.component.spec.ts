import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import ReferralProgram from './referral-program.vue';
import ReferralProgramService from './referral-program.service';
import AlertService from '@/shared/alert/alert.service';

type ReferralProgramComponentType = InstanceType<typeof ReferralProgram>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('ReferralProgram Management Component', () => {
    let referralProgramServiceStub: SinonStubbedInstance<ReferralProgramService>;
    let mountOptions: MountingOptions<ReferralProgramComponentType>['global'];

    beforeEach(() => {
      referralProgramServiceStub = sinon.createStubInstance<ReferralProgramService>(ReferralProgramService);
      referralProgramServiceStub.retrieve.resolves({ headers: {} });

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
          referralProgramService: () => referralProgramServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        referralProgramServiceStub.retrieve.resolves({ headers: {}, data: [{ id: '9fec3727-3421-4967-b213-ba36557ca194' }] });

        // WHEN
        const wrapper = shallowMount(ReferralProgram, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(referralProgramServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.referralPrograms[0]).toEqual(expect.objectContaining({ id: '9fec3727-3421-4967-b213-ba36557ca194' }));
      });
    });
    describe('Handles', () => {
      let comp: ReferralProgramComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(ReferralProgram, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        referralProgramServiceStub.retrieve.reset();
        referralProgramServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        referralProgramServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: '9fec3727-3421-4967-b213-ba36557ca194' });

        comp.removeReferralProgram();
        await comp.$nextTick(); // clear components

        // THEN
        expect(referralProgramServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(referralProgramServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
