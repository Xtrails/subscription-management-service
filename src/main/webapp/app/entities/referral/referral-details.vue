<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="referral">
        <h2 class="jh-entity-heading" data-cy="referralDetailsHeading">
          <span v-text="t$('subscriptionManagementServiceApp.referral.detail.title')"></span> {{ referral.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.referral.referralCode')"></span>
          </dt>
          <dd>
            <span>{{ referral.referralCode }}</span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.referral.status')"></span>
          </dt>
          <dd>
            <span v-text="t$('subscriptionManagementServiceApp.ReferralStatus.' + referral.status)"></span>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.referral.referrer')"></span>
          </dt>
          <dd>
            <div v-if="referral.referrer">
              <router-link :to="{ name: 'ExternalUserView', params: { externalUserId: referral.referrer.id } }">{{
                referral.referrer.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.referral.referralProgram')"></span>
          </dt>
          <dd>
            <div v-if="referral.referralProgram">
              <router-link :to="{ name: 'ReferralProgramView', params: { referralProgramId: referral.referralProgram.id } }">{{
                referral.referralProgram.id
              }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="t$('subscriptionManagementServiceApp.referral.sourceApplication')"></span>
          </dt>
          <dd>
            <div v-if="referral.sourceApplication">
              <router-link :to="{ name: 'SourceApplicationView', params: { sourceApplicationId: referral.sourceApplication.id } }">{{
                referral.sourceApplication.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" @click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="referral.id" :to="{ name: 'ReferralEdit', params: { referralId: referral.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./referral-details.component.ts"></script>
