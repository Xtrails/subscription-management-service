<template>
  <div>
    <h2 id="page-heading" data-cy="SubscriptionDetailsHeading">
      <span v-text="t$('subscriptionManagementServiceApp.subscriptionDetails.home.title')" id="subscription-details-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('subscriptionManagementServiceApp.subscriptionDetails.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'SubscriptionDetailsCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-subscription-details"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('subscriptionManagementServiceApp.subscriptionDetails.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && subscriptionDetails && subscriptionDetails.length === 0">
      <span v-text="t$('subscriptionManagementServiceApp.subscriptionDetails.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="subscriptionDetails && subscriptionDetails.length > 0">
      <table class="table table-striped" aria-describedby="subscriptionDetails">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.subscriptionDetails.name')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.subscriptionDetails.description')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.subscriptionDetails.price')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.subscriptionDetails.priceByMonth')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.subscriptionDetails.duration')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.subscriptionDetails.active')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.subscriptionDetails.sourceApplication')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.subscriptionDetails.subscriptionAccess')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="subscriptionDetails in subscriptionDetails" :key="subscriptionDetails.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'SubscriptionDetailsView', params: { subscriptionDetailsId: subscriptionDetails.id } }">{{
                subscriptionDetails.id
              }}</router-link>
            </td>
            <td>{{ subscriptionDetails.name }}</td>
            <td>{{ subscriptionDetails.description }}</td>
            <td>{{ subscriptionDetails.price }}</td>
            <td>{{ subscriptionDetails.priceByMonth }}</td>
            <td>{{ subscriptionDetails.duration }}</td>
            <td>{{ subscriptionDetails.active }}</td>
            <td>
              <div v-if="subscriptionDetails.sourceApplication">
                <router-link
                  :to="{ name: 'SourceApplicationView', params: { sourceApplicationId: subscriptionDetails.sourceApplication.id } }"
                  >{{ subscriptionDetails.sourceApplication.id }}</router-link
                >
              </div>
            </td>
            <td>
              <span v-for="(subscriptionAccess, i) in subscriptionDetails.subscriptionAccesses" :key="subscriptionAccess.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link
                  class="form-control-static"
                  :to="{ name: 'SubscriptionAccessView', params: { subscriptionAccessId: subscriptionAccess.id } }"
                  >{{ subscriptionAccess.id }}</router-link
                >
              </span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'SubscriptionDetailsView', params: { subscriptionDetailsId: subscriptionDetails.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'SubscriptionDetailsEdit', params: { subscriptionDetailsId: subscriptionDetails.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(subscriptionDetails)"
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
          id="subscriptionManagementServiceApp.subscriptionDetails.delete.question"
          data-cy="subscriptionDetailsDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-subscriptionDetails-heading"
          v-text="t$('subscriptionManagementServiceApp.subscriptionDetails.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-subscriptionDetails"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeSubscriptionDetails()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./subscription-details.component.ts"></script>
