<template>
  <div>
    <h2 id="page-heading" data-cy="ClientSubscriptionHeading">
      <span v-text="t$('subscriptionManagementServiceApp.clientSubscription.home.title')" id="client-subscription-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('subscriptionManagementServiceApp.clientSubscription.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'ClientSubscriptionCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-client-subscription"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('subscriptionManagementServiceApp.clientSubscription.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && clientSubscriptions && clientSubscriptions.length === 0">
      <span v-text="t$('subscriptionManagementServiceApp.clientSubscription.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="clientSubscriptions && clientSubscriptions.length > 0">
      <table class="table table-striped" aria-describedby="clientSubscriptions">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('startDate')">
              <span v-text="t$('subscriptionManagementServiceApp.clientSubscription.startDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'startDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('endDate')">
              <span v-text="t$('subscriptionManagementServiceApp.clientSubscription.endDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'endDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('status')">
              <span v-text="t$('subscriptionManagementServiceApp.clientSubscription.status')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'status'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('user.id')">
              <span v-text="t$('subscriptionManagementServiceApp.clientSubscription.user')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'user.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('subscriptionType.id')">
              <span v-text="t$('subscriptionManagementServiceApp.clientSubscription.subscriptionType')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'subscriptionType.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="clientSubscription in clientSubscriptions" :key="clientSubscription.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ClientSubscriptionView', params: { clientSubscriptionId: clientSubscription.id } }">{{
                clientSubscription.id
              }}</router-link>
            </td>
            <td>{{ clientSubscription.startDate }}</td>
            <td>{{ clientSubscription.endDate }}</td>
            <td v-text="t$('subscriptionManagementServiceApp.SubscriptionStatus.' + clientSubscription.status)"></td>
            <td>
              <div v-if="clientSubscription.user">
                <router-link :to="{ name: 'ExternalUserView', params: { externalUserId: clientSubscription.user.id } }">{{
                  clientSubscription.user.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="clientSubscription.subscriptionType">
                <router-link
                  :to="{ name: 'SubscriptionTypeView', params: { subscriptionTypeId: clientSubscription.subscriptionType.id } }"
                  >{{ clientSubscription.subscriptionType.id }}</router-link
                >
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'ClientSubscriptionView', params: { clientSubscriptionId: clientSubscription.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'ClientSubscriptionEdit', params: { clientSubscriptionId: clientSubscription.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(clientSubscription)"
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
          id="subscriptionManagementServiceApp.clientSubscription.delete.question"
          data-cy="clientSubscriptionDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-clientSubscription-heading"
          v-text="t$('subscriptionManagementServiceApp.clientSubscription.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-clientSubscription"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeClientSubscription()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./client-subscription.component.ts"></script>
