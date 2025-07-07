<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="sourceApplication">
        <h2 class="jh-entity-heading" data-cy="sourceApplicationDetailsHeading">
          <span v-text="t$('subscriptionManagementServiceApp.sourceApplication.detail.title')"></span> {{ sourceApplication.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.sourceApplication.applicationName')"></span>
          </dt>
          <dd>
            <span>{{ sourceApplication.applicationName }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.sourceApplication.user')"></span>
          </dt>
          <dd>
            <div v-if="sourceApplication.user">
              <router-link :to="{ name: 'ExternalUserView', params: { externalUserId: sourceApplication.user.id } }">{{
                sourceApplication.user.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="sourceApplication.id"
          :to="{ name: 'SourceApplicationEdit', params: { sourceApplicationId: sourceApplication.id } }"
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

<script lang="ts" src="./source-application-details.component.ts"></script>
