<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="subscriptionAccess">
        <h2 class="jh-entity-heading" data-cy="subscriptionAccessDetailsHeading">
          <span v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.detail.title')"></span> {{ subscriptionAccess.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.name')"></span>
          </dt>
          <dd>
            <span>{{ subscriptionAccess.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.description')"></span>
          </dt>
          <dd>
            <span>{{ subscriptionAccess.description }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.order')"></span>
          </dt>
          <dd>
            <span>{{ subscriptionAccess.order }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.role')"></span>
          </dt>
          <dd>
            <span>{{ subscriptionAccess.role }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.roleGroup')"></span>
          </dt>
          <dd>
            <span>{{ subscriptionAccess.roleGroup }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.active')"></span>
          </dt>
          <dd>
            <span>{{ subscriptionAccess.active }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.subscriptionDetails')"></span>
          </dt>
          <dd>
            <span v-for="(subscriptionDetails, i) in subscriptionAccess.subscriptionDetails" :key="subscriptionDetails.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'SubscriptionDetailsView', params: { subscriptionDetailsId: subscriptionDetails.id } }">{{
                subscriptionDetails.id
              }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="subscriptionAccess.id"
          :to="{ name: 'SubscriptionAccessEdit', params: { subscriptionAccessId: subscriptionAccess.id } }"
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

<script lang="ts" src="./subscription-access-details.component.ts"></script>
