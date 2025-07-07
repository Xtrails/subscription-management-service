<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="clientSubscription">
        <h2 class="jh-entity-heading" data-cy="clientSubscriptionDetailsHeading">
          <span v-text="t$('subscriptionManagementServiceApp.clientSubscription.detail.title')"></span> {{ clientSubscription.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.clientSubscription.startDttm')"></span>
          </dt>
          <dd>
            <span>{{ clientSubscription.startDttm }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.clientSubscription.endDttm')"></span>
          </dt>
          <dd>
            <span>{{ clientSubscription.endDttm }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.clientSubscription.status')"></span>
          </dt>
          <dd>
            <span v-text="t$('subscriptionManagementServiceApp.SubscriptionStatus.' + clientSubscription.status)"></span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.clientSubscription.user')"></span>
          </dt>
          <dd>
            <div v-if="clientSubscription.user">
              <router-link :to="{ name: 'ExternalUserView', params: { externalUserId: clientSubscription.user.id } }">{{
                clientSubscription.user.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.clientSubscription.subscriptionDetails')"></span>
          </dt>
          <dd>
            <div v-if="clientSubscription.subscriptionDetails">
              <router-link
                :to="{ name: 'SubscriptionDetailsView', params: { subscriptionDetailsId: clientSubscription.subscriptionDetails.id } }"
                >{{ clientSubscription.subscriptionDetails.id }}</router-link
              >
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="clientSubscription.id"
          :to="{ name: 'ClientSubscriptionEdit', params: { clientSubscriptionId: clientSubscription.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./client-subscription-details.component.ts"></script>
