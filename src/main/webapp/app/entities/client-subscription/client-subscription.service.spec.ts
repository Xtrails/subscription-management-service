import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import ClientSubscriptionService from './client-subscription.service';
import { DATE_FORMAT } from '@/shared/composables/date-format';
import { ClientSubscription } from '@/shared/model/client-subscription.model';

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
  describe('ClientSubscription Service', () => {
    let service: ClientSubscriptionService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new ClientSubscriptionService();
      currentDate = new Date();
      elemDefault = new ClientSubscription('9fec3727-3421-4967-b213-ba36557ca194', currentDate, currentDate, 'ACTIVE');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = {
          startDttm: dayjs(currentDate).format(DATE_FORMAT),
          endDttm: dayjs(currentDate).format(DATE_FORMAT),
          ...elemDefault,
        };
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find('9fec3727-3421-4967-b213-ba36557ca194').then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find('9fec3727-3421-4967-b213-ba36557ca194')
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a ClientSubscription', async () => {
        const returnedFromService = {
          id: '9fec3727-3421-4967-b213-ba36557ca194',
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

      it('should not create a ClientSubscription', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a ClientSubscription', async () => {
        const returnedFromService = {
          startDttm: dayjs(currentDate).format(DATE_FORMAT),
          endDttm: dayjs(currentDate).format(DATE_FORMAT),
          status: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { startDttm: currentDate, endDttm: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a ClientSubscription', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a ClientSubscription', async () => {
        const patchObject = { startDttm: dayjs(currentDate).format(DATE_FORMAT), ...new ClientSubscription() };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { startDttm: currentDate, endDttm: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a ClientSubscription', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of ClientSubscription', async () => {
        const returnedFromService = {
          startDttm: dayjs(currentDate).format(DATE_FORMAT),
          endDttm: dayjs(currentDate).format(DATE_FORMAT),
          status: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { startDttm: currentDate, endDttm: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of ClientSubscription', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a ClientSubscription', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete('9fec3727-3421-4967-b213-ba36557ca194').then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a ClientSubscription', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete('9fec3727-3421-4967-b213-ba36557ca194')
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
