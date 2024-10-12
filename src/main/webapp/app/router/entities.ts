import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

const ExternalUser = () => import('@/entities/external-user/external-user.vue');
const ExternalUserUpdate = () => import('@/entities/external-user/external-user-update.vue');
const ExternalUserDetails = () => import('@/entities/external-user/external-user-details.vue');

const ClientSubscription = () => import('@/entities/client-subscription/client-subscription.vue');
const ClientSubscriptionUpdate = () => import('@/entities/client-subscription/client-subscription-update.vue');
const ClientSubscriptionDetails = () => import('@/entities/client-subscription/client-subscription-details.vue');

const SubscriptionType = () => import('@/entities/subscription-type/subscription-type.vue');
const SubscriptionTypeUpdate = () => import('@/entities/subscription-type/subscription-type-update.vue');
const SubscriptionTypeDetails = () => import('@/entities/subscription-type/subscription-type-details.vue');

const ReferralProgram = () => import('@/entities/referral-program/referral-program.vue');
const ReferralProgramUpdate = () => import('@/entities/referral-program/referral-program-update.vue');
const ReferralProgramDetails = () => import('@/entities/referral-program/referral-program-details.vue');

const Referral = () => import('@/entities/referral/referral.vue');
const ReferralUpdate = () => import('@/entities/referral/referral-update.vue');
const ReferralDetails = () => import('@/entities/referral/referral-details.vue');

const Payment = () => import('@/entities/payment/payment.vue');
const PaymentUpdate = () => import('@/entities/payment/payment-update.vue');
const PaymentDetails = () => import('@/entities/payment/payment-details.vue');

const PaymentSystem = () => import('@/entities/payment-system/payment-system.vue');
const PaymentSystemUpdate = () => import('@/entities/payment-system/payment-system-update.vue');
const PaymentSystemDetails = () => import('@/entities/payment-system/payment-system-details.vue');

const SourceApplication = () => import('@/entities/source-application/source-application.vue');
const SourceApplicationUpdate = () => import('@/entities/source-application/source-application-update.vue');
const SourceApplicationDetails = () => import('@/entities/source-application/source-application-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'external-user',
      name: 'ExternalUser',
      component: ExternalUser,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'external-user/new',
      name: 'ExternalUserCreate',
      component: ExternalUserUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'external-user/:externalUserId/edit',
      name: 'ExternalUserEdit',
      component: ExternalUserUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'external-user/:externalUserId/view',
      name: 'ExternalUserView',
      component: ExternalUserDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'client-subscription',
      name: 'ClientSubscription',
      component: ClientSubscription,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'client-subscription/new',
      name: 'ClientSubscriptionCreate',
      component: ClientSubscriptionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'client-subscription/:clientSubscriptionId/edit',
      name: 'ClientSubscriptionEdit',
      component: ClientSubscriptionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'client-subscription/:clientSubscriptionId/view',
      name: 'ClientSubscriptionView',
      component: ClientSubscriptionDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'subscription-type',
      name: 'SubscriptionType',
      component: SubscriptionType,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'subscription-type/new',
      name: 'SubscriptionTypeCreate',
      component: SubscriptionTypeUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'subscription-type/:subscriptionTypeId/edit',
      name: 'SubscriptionTypeEdit',
      component: SubscriptionTypeUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'subscription-type/:subscriptionTypeId/view',
      name: 'SubscriptionTypeView',
      component: SubscriptionTypeDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'referral-program',
      name: 'ReferralProgram',
      component: ReferralProgram,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'referral-program/new',
      name: 'ReferralProgramCreate',
      component: ReferralProgramUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'referral-program/:referralProgramId/edit',
      name: 'ReferralProgramEdit',
      component: ReferralProgramUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'referral-program/:referralProgramId/view',
      name: 'ReferralProgramView',
      component: ReferralProgramDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'referral',
      name: 'Referral',
      component: Referral,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'referral/new',
      name: 'ReferralCreate',
      component: ReferralUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'referral/:referralId/edit',
      name: 'ReferralEdit',
      component: ReferralUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'referral/:referralId/view',
      name: 'ReferralView',
      component: ReferralDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'payment',
      name: 'Payment',
      component: Payment,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'payment/new',
      name: 'PaymentCreate',
      component: PaymentUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'payment/:paymentId/edit',
      name: 'PaymentEdit',
      component: PaymentUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'payment/:paymentId/view',
      name: 'PaymentView',
      component: PaymentDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'payment-system',
      name: 'PaymentSystem',
      component: PaymentSystem,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'payment-system/new',
      name: 'PaymentSystemCreate',
      component: PaymentSystemUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'payment-system/:paymentSystemId/edit',
      name: 'PaymentSystemEdit',
      component: PaymentSystemUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'payment-system/:paymentSystemId/view',
      name: 'PaymentSystemView',
      component: PaymentSystemDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'source-application',
      name: 'SourceApplication',
      component: SourceApplication,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'source-application/new',
      name: 'SourceApplicationCreate',
      component: SourceApplicationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'source-application/:sourceApplicationId/edit',
      name: 'SourceApplicationEdit',
      component: SourceApplicationUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'source-application/:sourceApplicationId/view',
      name: 'SourceApplicationView',
      component: SourceApplicationDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
