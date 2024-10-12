import { defineComponent, provide } from 'vue';

import ExternalUserService from './external-user/external-user.service';
import ClientSubscriptionService from './client-subscription/client-subscription.service';
import SubscriptionTypeService from './subscription-type/subscription-type.service';
import ReferralProgramService from './referral-program/referral-program.service';
import ReferralService from './referral/referral.service';
import PaymentService from './payment/payment.service';
import PaymentSystemService from './payment-system/payment-system.service';
import SourceApplicationService from './source-application/source-application.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('externalUserService', () => new ExternalUserService());
    provide('clientSubscriptionService', () => new ClientSubscriptionService());
    provide('subscriptionTypeService', () => new SubscriptionTypeService());
    provide('referralProgramService', () => new ReferralProgramService());
    provide('referralService', () => new ReferralService());
    provide('paymentService', () => new PaymentService());
    provide('paymentSystemService', () => new PaymentSystemService());
    provide('sourceApplicationService', () => new SourceApplicationService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
