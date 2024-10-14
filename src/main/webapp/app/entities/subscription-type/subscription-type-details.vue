<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="subscriptionType">
        <h2 class="jh-entity-heading" data-cy="subscriptionTypeDetailsHeading">
          <span v-text="t$('subscriptionManagementServiceApp.subscriptionType.detail.title')"></span> {{ subscriptionType.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.subscriptionType.name')"></span>
          </dt>
          <dd>
            <span>{{ subscriptionType.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.subscriptionType.description')"></span>
          </dt>
          <dd>
            <span>{{ subscriptionType.description }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.subscriptionType.price')"></span>
          </dt>
          <dd>
            <span>{{ subscriptionType.price }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.subscriptionType.duration')"></span>
          </dt>
          <dd>
            <span>{{ subscriptionType.duration }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.subscriptionType.visible')"></span>
          </dt>
          <dd>
            <span>{{ subscriptionType.visible }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.subscriptionType.sourceApplication')"></span>
          </dt>
          <dd>
            <div v-if="subscriptionType.sourceApplication">
              <router-link
                :to="{ name: 'SourceApplicationView', params: { sourceApplicationId: subscriptionType.sourceApplication.id } }"
                >{{ subscriptionType.sourceApplication.id }}</router-link
              >
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="subscriptionType.id"
          :to="{ name: 'SubscriptionTypeEdit', params: { subscriptionTypeId: subscriptionType.id } }"
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

<script lang="ts" src="./subscription-type-details.component.ts"></script>
