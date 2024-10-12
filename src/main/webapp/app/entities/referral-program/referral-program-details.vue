<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="referralProgram">
        <h2 class="jh-entity-heading" data-cy="referralProgramDetailsHeading">
          <span v-text="t$('subscriptionManagementServiceApp.referralProgram.detail.title')"></span> {{ referralProgram.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.referralProgram.name')"></span>
          </dt>
          <dd>
            <span>{{ referralProgram.name }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.referralProgram.description')"></span>
          </dt>
          <dd>
            <span>{{ referralProgram.description }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.referralProgram.rewardAmount')"></span>
          </dt>
          <dd>
            <span>{{ referralProgram.rewardAmount }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.referralProgram.sourceApplication')"></span>
          </dt>
          <dd>
            <div v-if="referralProgram.sourceApplication">
              <router-link :to="{ name: 'SourceApplicationView', params: { sourceApplicationId: referralProgram.sourceApplication.id } }">{{
                referralProgram.sourceApplication.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link
          v-if="referralProgram.id"
          :to="{ name: 'ReferralProgramEdit', params: { referralProgramId: referralProgram.id } }"
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

<script lang="ts" src="./referral-program-details.component.ts"></script>
