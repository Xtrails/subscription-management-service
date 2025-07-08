<template>
  <div>
    <h2 id="page-heading" data-cy="SubscriptionAccessHeading">
      <span v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.home.title')" id="subscription-access-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'SubscriptionAccessCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-subscription-access"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && subscriptionAccesses && subscriptionAccesses.length === 0">
      <span v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="subscriptionAccesses && subscriptionAccesses.length > 0">
      <table class="table table-striped" aria-describedby="subscriptionAccesses">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.name')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.description')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.order')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.role')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.roleGroup')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.active')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.subscriptionDetails')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="subscriptionAccess in subscriptionAccesses" :key="subscriptionAccess.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'SubscriptionAccessView', params: { subscriptionAccessId: subscriptionAccess.id } }">{{
                subscriptionAccess.id
              }}</router-link>
            </td>
            <td>{{ subscriptionAccess.name }}</td>
            <td>{{ subscriptionAccess.description }}</td>
            <td>{{ subscriptionAccess.order }}</td>
            <td>{{ subscriptionAccess.role }}</td>
            <td>{{ subscriptionAccess.roleGroup }}</td>
            <td>{{ subscriptionAccess.active }}</td>
            <td>
              <span v-for="(subscriptionDetails, i) in subscriptionAccess.subscriptionDetails" :key="subscriptionDetails.id"
                >{{ i > 0 ? ', ' : '' }}
                <router-link
                  class="form-control-static"
                  :to="{ name: 'SubscriptionDetailsView', params: { subscriptionDetailsId: subscriptionDetails.id } }"
                  >{{ subscriptionDetails.id }}</router-link
                >
              </span>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'SubscriptionAccessView', params: { subscriptionAccessId: subscriptionAccess.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'SubscriptionAccessEdit', params: { subscriptionAccessId: subscriptionAccess.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(subscriptionAccess)"
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
          id="subscriptionManagementServiceApp.subscriptionAccess.delete.question"
          data-cy="subscriptionAccessDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-subscriptionAccess-heading"
          v-text="t$('subscriptionManagementServiceApp.subscriptionAccess.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-subscriptionAccess"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeSubscriptionAccess()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./subscription-access.component.ts"></script>
