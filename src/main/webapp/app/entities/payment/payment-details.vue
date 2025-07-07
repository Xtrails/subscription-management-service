<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="payment">
        <h2 class="jh-entity-heading" data-cy="paymentDetailsHeading">
          <span v-text="t$('subscriptionManagementServiceApp.payment.detail.title')"></span> {{ payment.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.payment.amount')"></span>
          </dt>
          <dd>
            <span>{{ payment.amount }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.payment.status')"></span>
          </dt>
          <dd>
            <span v-text="t$('subscriptionManagementServiceApp.PaymentStatus.' + payment.status)"></span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.payment.paymentDttm')"></span>
          </dt>
          <dd>
            <span>{{ payment.paymentDttm }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.payment.hashSum')"></span>
          </dt>
          <dd>
            <span>{{ payment.hashSum }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.payment.clientSubscription')"></span>
          </dt>
          <dd>
            <div v-if="payment.clientSubscription">
              <router-link :to="{ name: 'ClientSubscriptionView', params: { clientSubscriptionId: payment.clientSubscription.id } }">{{
                payment.clientSubscription.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.payment.user')"></span>
          </dt>
          <dd>
            <div v-if="payment.user">
              <router-link :to="{ name: 'ExternalUserView', params: { externalUserId: payment.user.id } }">{{
                payment.user.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.payment.clietntSubscription')"></span>
          </dt>
          <dd>
            <div v-if="payment.clietntSubscription">
              <router-link :to="{ name: 'ClientSubscriptionView', params: { clientSubscriptionId: payment.clietntSubscription.id } }">{{
                payment.clietntSubscription.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.payment.paymentSystem')"></span>
          </dt>
          <dd>
            <div v-if="payment.paymentSystem">
              <router-link :to="{ name: 'PaymentSystemView', params: { paymentSystemId: payment.paymentSystem.id } }">{{
                payment.paymentSystem.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="payment.id" :to="{ name: 'PaymentEdit', params: { paymentId: payment.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./payment-details.component.ts"></script>
