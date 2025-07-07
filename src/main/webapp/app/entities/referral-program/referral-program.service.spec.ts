import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import ReferralProgramService from './referral-program.service';
import { DATE_FORMAT } from '@/shared/composables/date-format';
import { ReferralProgram } from '@/shared/model/referral-program.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('ReferralProgram Service', () => {
    let service: ReferralProgramService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new ReferralProgramService();
      currentDate = new Date();
      elemDefault = new ReferralProgram(123, 'AAAAAAA', 'AAAAAAA', 'AAAAAAA', currentDate, currentDate, 0, 'PENDING');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          startDttm: dayjs(currentDate).format(DATE_FORMAT),
          endDttm: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a ReferralProgram', async () => {
        const returnedFromService = {
          id: 123,
          startDttm: dayjs(currentDate).format(DATE_FORMAT),
          endDttm: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        const expected = { startDttm: currentDate, endDttm: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a ReferralProgram', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a ReferralProgram', async () => {
        const returnedFromService = {
          name: 'BBBBBB',
          referralCode: 'BBBBBB',
          description: 'BBBBBB',
          startDttm: dayjs(currentDate).format(DATE_FORMAT),
          endDttm: dayjs(currentDate).format(DATE_FORMAT),
          rewardAmount: 1,
          status: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { startDttm: currentDate, endDttm: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a ReferralProgram', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a ReferralProgram', async () => {
        const patchObject = {
          name: 'BBBBBB',
          referralCode: 'BBBBBB',
          description: 'BBBBBB',
          startDttm: dayjs(currentDate).format(DATE_FORMAT),
          endDttm: dayjs(currentDate).format(DATE_FORMAT),
          ...new ReferralProgram(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { startDttm: currentDate, endDttm: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a ReferralProgram', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of ReferralProgram', async () => {
        const returnedFromService = {
          name: 'BBBBBB',
          referralCode: 'BBBBBB',
          description: 'BBBBBB',
          startDttm: dayjs(currentDate).format(DATE_FORMAT),
          endDttm: dayjs(currentDate).format(DATE_FORMAT),
          rewardAmount: 1,
          status: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { startDttm: currentDate, endDttm: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve().then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of ReferralProgram', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a ReferralProgram', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a ReferralProgram', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
