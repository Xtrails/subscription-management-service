<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="externalUser">
        <h2 class="jh-entity-heading" data-cy="externalUserDetailsHeading">
          <span v-text="t$('subscriptionManagementServiceApp.externalUser.detail.title')"></span> {{ externalUser.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.externalUser.externalUserId')"></span>
          </dt>
          <dd>
            <span>{{ externalUser.externalUserId }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.externalUser.sourceApplication')"></span>
          </dt>
          <dd>
            <div v-if="externalUser.sourceApplication">
              <router-link :to="{ name: 'SourceApplicationView', params: { sourceApplicationId: externalUser.sourceApplication.id } }">{{
                externalUser.sourceApplication.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="externalUser.id"
          :to="{ name: 'ExternalUserEdit', params: { externalUserId: externalUser.id } }"
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

<script lang="ts" src="./external-user-details.component.ts"></script>
