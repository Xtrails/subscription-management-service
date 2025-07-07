<template>
  <div>
    <h2 id="page-heading" data-cy="PaymentSystemHeading">
      <span v-text="t$('subscriptionManagementServiceApp.paymentSystem.home.title')" id="payment-system-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('subscriptionManagementServiceApp.paymentSystem.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'PaymentSystemCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-payment-system"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('subscriptionManagementServiceApp.paymentSystem.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && paymentSystems && paymentSystems.length === 0">
      <span v-text="t$('subscriptionManagementServiceApp.paymentSystem.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="paymentSystems && paymentSystems.length > 0">
      <table class="table table-striped" aria-describedby="paymentSystems">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.paymentSystem.name')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="paymentSystem in paymentSystems" :key="paymentSystem.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PaymentSystemView', params: { paymentSystemId: paymentSystem.id } }">{{
                paymentSystem.id
              }}</router-link>
            </td>
            <td>{{ paymentSystem.name }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'PaymentSystemView', params: { paymentSystemId: paymentSystem.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'PaymentSystemEdit', params: { paymentSystemId: paymentSystem.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(paymentSystem)"
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
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span
          id="subscriptionManagementServiceApp.paymentSystem.delete.question"
          data-cy="paymentSystemDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-paymentSystem-heading"
          v-text="t$('subscriptionManagementServiceApp.paymentSystem.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-paymentSystem"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removePaymentSystem()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./payment-system.component.ts"></script>
