import { defineComponent, provide } from 'vue';

import ExternalUserService from './external-user/external-user.service';
import ClientSubscriptionService from './client-subscription/client-subscription.service';
import SubscriptionDetailsService from './subscription-details/subscription-details.service';
import ReferralProgramService from './referral-program/referral-program.service';
import PaymentService from './payment/payment.service';
import PaymentSystemService from './payment-system/payment-system.service';
import SourceApplicationService from './source-application/source-application.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('externalUserService', () => new ExternalUserService());
    provide('clientSubscriptionService', () => new ClientSubscriptionService());
    provide('subscriptionDetailsService', () => new SubscriptionDetailsService());
    provide('referralProgramService', () => new ReferralProgramService());
    provide('paymentService', () => new PaymentService());
    provide('paymentSystemService', () => new PaymentSystemService());
    provide('sourceApplicationService', () => new SourceApplicationService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
