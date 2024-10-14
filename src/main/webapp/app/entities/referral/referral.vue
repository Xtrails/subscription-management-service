<template>
  <div>
    <h2 id="page-heading" data-cy="ReferralHeading">
      <span v-text="t$('subscriptionManagementServiceApp.referral.home.title')" id="referral-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('subscriptionManagementServiceApp.referral.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'ReferralCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-referral"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('subscriptionManagementServiceApp.referral.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && referrals && referrals.length === 0">
      <span v-text="t$('subscriptionManagementServiceApp.referral.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="referrals && referrals.length > 0">
      <table class="table table-striped" aria-describedby="referrals">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('referralCode')">
              <span v-text="t$('subscriptionManagementServiceApp.referral.referralCode')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'referralCode'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('status')">
              <span v-text="t$('subscriptionManagementServiceApp.referral.status')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'status'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('referrer.id')">
              <span v-text="t$('subscriptionManagementServiceApp.referral.referrer')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'referrer.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('referralProgram.id')">
              <span v-text="t$('subscriptionManagementServiceApp.referral.referralProgram')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'referralProgram.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('sourceApplication.id')">
              <span v-text="t$('subscriptionManagementServiceApp.referral.sourceApplication')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'sourceApplication.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="referral in referrals" :key="referral.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ReferralView', params: { referralId: referral.id } }">{{ referral.id }}</router-link>
            </td>
            <td>{{ referral.referralCode }}</td>
            <td v-text="t$('subscriptionManagementServiceApp.ReferralStatus.' + referral.status)"></td>
            <td>
              <div v-if="referral.referrer">
                <router-link :to="{ name: 'ExternalUserView', params: { externalUserId: referral.referrer.id } }">{{
                  referral.referrer.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="referral.referralProgram">
                <router-link :to="{ name: 'ReferralProgramView', params: { referralProgramId: referral.referralProgram.id } }">{{
                  referral.referralProgram.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="referral.sourceApplication">
                <router-link :to="{ name: 'SourceApplicationView', params: { sourceApplicationId: referral.sourceApplication.id } }">{{
                  referral.sourceApplication.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ReferralView', params: { referralId: referral.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ReferralEdit', params: { referralId: referral.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(referral)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
        <span ref="infiniteScrollEl"></span>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span
          id="subscriptionManagementServiceApp.referral.delete.question"
          data-cy="referralDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-referral-heading" v-text="t$('subscriptionManagementServiceApp.referral.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-referral"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeReferral()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./referral.component.ts"></script>
