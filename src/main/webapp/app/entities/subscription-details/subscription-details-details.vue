<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="subscriptionDetails">
        <h2 class="jh-entity-heading" data-cy="subscriptionDetailsDetailsHeading">
          <span v-text="t$('subscriptionManagementServiceApp.subscriptionDetails.detail.title')"></span> {{ subscriptionDetails.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.subscriptionDetails.name')"></span>
          </dt>
          <dd>
            <span>{{ subscriptionDetails.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.subscriptionDetails.description')"></span>
          </dt>
          <dd>
            <span>{{ subscriptionDetails.description }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.subscriptionDetails.price')"></span>
          </dt>
          <dd>
            <span>{{ subscriptionDetails.price }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.subscriptionDetails.duration')"></span>
          </dt>
          <dd>
            <span>{{ subscriptionDetails.duration }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.subscriptionDetails.sourceApplication')"></span>
          </dt>
          <dd>
            <div v-if="subscriptionDetails.sourceApplication">
              <router-link
                :to="{ name: 'SourceApplicationView', params: { sourceApplicationId: subscriptionDetails.sourceApplication.id } }"
                >{{ subscriptionDetails.sourceApplication.id }}</router-link
              >
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="subscriptionDetails.id"
          :to="{ name: 'SubscriptionDetailsEdit', params: { subscriptionDetailsId: subscriptionDetails.id } }"
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

<script lang="ts" src="./subscription-details-details.component.ts"></script>
