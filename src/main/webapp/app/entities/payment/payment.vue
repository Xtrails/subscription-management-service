<template>
  <div>
    <h2 id="page-heading" data-cy="PaymentHeading">
      <span v-text="t$('subscriptionManagementServiceApp.payment.home.title')" id="payment-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('subscriptionManagementServiceApp.payment.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'PaymentCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-payment"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('subscriptionManagementServiceApp.payment.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && payments && payments.length === 0">
      <span v-text="t$('subscriptionManagementServiceApp.payment.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="payments && payments.length > 0">
      <table class="table table-striped" aria-describedby="payments">
        <thead>
          <tr>
            <th scope="row" @click="changeOrder('id')">
              <span v-text="t$('global.field.id')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('amount')">
              <span v-text="t$('subscriptionManagementServiceApp.payment.amount')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'amount'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('status')">
              <span v-text="t$('subscriptionManagementServiceApp.payment.status')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'status'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('paymentDate')">
              <span v-text="t$('subscriptionManagementServiceApp.payment.paymentDate')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'paymentDate'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('user.id')">
              <span v-text="t$('subscriptionManagementServiceApp.payment.user')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'user.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('clientSubscription.id')">
              <span v-text="t$('subscriptionManagementServiceApp.payment.clientSubscription')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'clientSubscription.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('paymentSystem.id')">
              <span v-text="t$('subscriptionManagementServiceApp.payment.paymentSystem')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'paymentSystem.id'"></jhi-sort-indicator>
            </th>
            <th scope="row" @click="changeOrder('sourceApplication.id')">
              <span v-text="t$('subscriptionManagementServiceApp.payment.sourceApplication')"></span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'sourceApplication.id'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="payment in payments" :key="payment.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PaymentView', params: { paymentId: payment.id } }">{{ payment.id }}</router-link>
            </td>
            <td>{{ payment.amount }}</td>
            <td v-text="t$('subscriptionManagementServiceApp.PaymentStatus.' + payment.status)"></td>
            <td>{{ payment.paymentDate }}</td>
            <td>
              <div v-if="payment.user">
                <router-link :to="{ name: 'ExternalUserView', params: { externalUserId: payment.user.id } }">{{
                  payment.user.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="payment.clientSubscription">
                <router-link :to="{ name: 'ClientSubscriptionView', params: { clientSubscriptionId: payment.clientSubscription.id } }">{{
                  payment.clientSubscription.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="payment.paymentSystem">
                <router-link :to="{ name: 'PaymentSystemView', params: { paymentSystemId: payment.paymentSystem.id } }">{{
                  payment.paymentSystem.id
                }}</router-link>
              </div>
            </td>
            <td>
              <div v-if="payment.sourceApplication">
                <router-link :to="{ name: 'SourceApplicationView', params: { sourceApplicationId: payment.sourceApplication.id } }">{{
                  payment.sourceApplication.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'PaymentView', params: { paymentId: payment.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'PaymentEdit', params: { paymentId: payment.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(payment)"
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
          id="subscriptionManagementServiceApp.payment.delete.question"
          data-cy="paymentDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p id="jhi-delete-payment-heading" v-text="t$('subscriptionManagementServiceApp.payment.delete.question', { id: removeId })"></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-payment"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removePayment()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./payment.component.ts"></script>
