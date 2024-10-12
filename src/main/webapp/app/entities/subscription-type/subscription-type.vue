<template>
  <div>
    <h2 id="page-heading" data-cy="SubscriptionTypeHeading">
      <span v-text="t$('subscriptionManagementServiceApp.subscriptionType.home.title')" id="subscription-type-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" @click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('subscriptionManagementServiceApp.subscriptionType.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'SubscriptionTypeCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-subscription-type"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('subscriptionManagementServiceApp.subscriptionType.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && subscriptionTypes && subscriptionTypes.length === 0">
      <span v-text="t$('subscriptionManagementServiceApp.subscriptionType.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="subscriptionTypes && subscriptionTypes.length > 0">
      <table class="table table-striped" aria-describedby="subscriptionTypes">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.subscriptionType.name')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.subscriptionType.description')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.subscriptionType.price')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.subscriptionType.duration')"></span></th>
            <th scope="row"><span v-text="t$('subscriptionManagementServiceApp.subscriptionType.sourceApplication')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="subscriptionType in subscriptionTypes" :key="subscriptionType.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'SubscriptionTypeView', params: { subscriptionTypeId: subscriptionType.id } }">{{
                subscriptionType.id
              }}</router-link>
            </td>
            <td>{{ subscriptionType.name }}</td>
            <td>{{ subscriptionType.description }}</td>
            <td>{{ subscriptionType.price }}</td>
            <td>{{ subscriptionType.duration }}</td>
            <td>
              <div v-if="subscriptionType.sourceApplication">
                <router-link
                  :to="{ name: 'SourceApplicationView', params: { sourceApplicationId: subscriptionType.sourceApplication.id } }"
                  >{{ subscriptionType.sourceApplication.id }}</router-link
                >
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'SubscriptionTypeView', params: { subscriptionTypeId: subscriptionType.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'SubscriptionTypeEdit', params: { subscriptionTypeId: subscriptionType.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  @click="prepareRemove(subscriptionType)"
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
          id="subscriptionManagementServiceApp.subscriptionType.delete.question"
          data-cy="subscriptionTypeDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-subscriptionType-heading"
          v-text="t$('subscriptionManagementServiceApp.subscriptionType.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" @click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-subscriptionType"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            @click="removeSubscriptionType()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./subscription-type.component.ts"></script>
